package calculinc.google.httpssites.skol_app;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.view.menu.MenuView;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static calculinc.google.httpssites.skol_app.R.drawable.hexagon_bottom;
import static calculinc.google.httpssites.skol_app.R.drawable.hexagon_top;
import static calculinc.google.httpssites.skol_app.R.drawable.rainbowcolor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    long id_number;
    int currentWeek = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    int currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
    int currentHour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int currentMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE);

    int downloadWeek = currentWeek;

    String schemaFileName = "Nova.txt";
    File schemaFile;
    String loginFileName = "login.txt";
    File loginFile;
    boolean downloadSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView kek = (TextView) findViewById(R.id.öpö);
        kek.setText(String.valueOf(currentMinute));

        loginFile = new File(getFilesDir() + "/" + loginFileName);

        try {
            if (loginFile.exists()) {
                FileInputStream fis = openFileInput(loginFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                String loginString = sb.toString();

                id_number = Long.parseLong(loginString);
                EditText personal_id = (EditText) findViewById(R.id.editText);
                if (id_number == 0) {
                    personal_id.setText("");
                } else {
                    personal_id.setText(String.valueOf(id_number));
                }

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginSubmit (View view) {
        final EditText personal_id = (EditText) findViewById(R.id.editText);
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        view.startAnimation(anim_button_click);

        if (personal_id.getText().toString().equals("")) {
            id_number = 0;
        } else {
            id_number = Long.parseLong(personal_id.getText().toString());
        }
        String login_content = String.valueOf(id_number);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(loginFileName,MODE_PRIVATE);
            outputStream.write(login_content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_nyheter) {

            toolbar.setTitle(R.string.Tab_1);
            vf.setDisplayedChild(0);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_schema) {

            toolbar.setTitle(R.string.Tab_2);
            vf.setDisplayedChild(1);
            drawer.closeDrawer(GravityCompat.START);
            //start loading animation here (spinning circle and "laddar..." text

            schemaRefresh öpö = new schemaRefresh();
            öpö.start();

        } else if (id == R.id.nav_matsedel) {

            toolbar.setTitle(R.string.Tab_3);
            vf.setDisplayedChild(2);
            drawer.closeDrawer(GravityCompat.START);
            ggfunktion();

        } else if (id == R.id.nav_laxor) {

            toolbar.setTitle(R.string.Tab_4);
            vf.setDisplayedChild(3);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.nav_login) {

            toolbar.setTitle(R.string.Tab_5);
            vf.setDisplayedChild(4);
            drawer.closeDrawer(GravityCompat.START);
            fidget_spinner();

        } else if (id == R.id.nav_send) {

            toolbar.setTitle(R.string.Tab_6);
            drawer.closeDrawer(GravityCompat.START);

        }

        return true;
    }

    public void schemaTimeRefresh() {

        currentWeek = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
        currentHour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
        currentMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE);

    }

    private class schemaRefresh extends Thread {
        public void run() {

            downloadWeek = 20;
            String nameOfFile = "id:" + id_number + "week:" + downloadWeek + "_" + schemaFileName;
            schemaFile = new File(getFilesDir() + "/" + nameOfFile);
            String downloadID = String.valueOf(id_number);

            DownloadFile M = new DownloadFile(downloadID,downloadWeek,nameOfFile);
            M.start();
            try {
                M.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            drawSchema();
        }
    }

    public void drawSchema() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout schema_space = (LinearLayout) findViewById(R.id.schema_layout);

                StringBuilder sb = new StringBuilder();
                try {
                    String nameOfFile = "id:" + id_number + "week:" + downloadWeek + "_" + schemaFileName;
                    FileInputStream fis = openFileInput(nameOfFile);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (schemaFile.exists()) {
                    String[] druvor = sb.toString().split("%day%");
                    for (int i = 0; i <= 4; i++) {
                        String[] desert = druvor[i].split("%maq1ekax%");

                        int status = 1;
                        for (int j = 0; j < desert.length; j++) {

                            TextView textView = new TextView(getBaseContext());
                            textView.setTextColor(Color.parseColor("#ffffff"));
                            textView.setText(desert[j]);
                            textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                            if (status == 1) {

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 30, 0, 0);
                                textView.setBackgroundResource(hexagon_top);
                                textView.setLayoutParams(params);

                            } else if (status == 2) {

                                textView.setBackgroundColor(Color.parseColor("#ff219c"));
                                textView.setWidth(Math.round(getResources().getDimension(R.dimen.image_width)));
                                textView.setGravity(Gravity.CENTER);

                            } else if (status == 3) {

                                textView.setBackgroundResource(hexagon_bottom);
                                textView.setGravity(Gravity.RIGHT);
                            }

                            if (status == 3) {

                                status = 1;

                            } else {

                                status++;

                            }

                            schema_space.addView(textView);
                        }
                    }
                } else {
                    //no file to draw
                    String fail = "fail";
                    String megafail = "fail";
                }
            }
        });
    }

    public void drawing()  {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Paint plebpaint = new Paint();
        plebpaint.setColor(Color.parseColor("#ffffff"));
        plebpaint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#61b0ae"));
        TextPaint textpaint = new TextPaint();
        textpaint.setColor(Color.parseColor("#ffffff"));
        textpaint.setAntiAlias(true);

        Bitmap bg = Bitmap.createBitmap(480,800, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        canvas.drawRect(12,120,468,250,paint);
        canvas.drawRect(17,125,463,245,plebpaint);
        textpaint.setTextSize(32);
        canvas.drawText("( ͡° ͜ʖ ͡°)",120,200,textpaint);
        ImageView föfan = (ImageView) findViewById(R.id.föfan);
        föfan.setImageBitmap(bg);
    }

    public void ggfunktion() {

        Button button = (Button) findViewById(R.id.plebknapp);
        final LinearLayout linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        final TextView ggboistext = (TextView) findViewById(R.id.gg_bois);
        final ImageView hexa = (ImageView) findViewById(R.id.hexagon);
        final TextView clock = (TextView) findViewById(R.id.klockan);
        final LinearLayout midbox = (LinearLayout) findViewById(R.id.mid_box);
        final ImageView hexa2 = (ImageView) findViewById(R.id.hexagon2);

        button.setOnClickListener(new View.OnClickListener() {
            int status = 1;
            int test = 1;
            @Override
            public void onClick(View view) {

                ggboistext.setText("GG BOIS");
                linearlayout.setBackgroundResource(rainbowcolor);
                hexa.setColorFilter(Color.parseColor("#16aba0"), PorterDuff.Mode.SRC_ATOP);
                hexa2.setColorFilter(Color.parseColor("#16aba0"), PorterDuff.Mode.SRC_ATOP);
                clock.setText(String.valueOf(GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(GregorianCalendar.getInstance().get(Calendar.MINUTE)));
                midbox.setBackgroundColor(Color.parseColor("#16aba0"));
                midbox.getLayoutParams().height = ((int) getResources().getDimension(R.dimen.dp_unit))*180;
                midbox.requestLayout();
            }
        });
    }

    public void fidget_spinner(){

        Spinner spinner = (Spinner) findViewById(R.id.fidget_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.spinner_layout);
        spinner.setAdapter(adapter);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    private class DownloadFile extends Thread{

        String fileid;
        String fileweek;
        String filename;
        DownloadFile(String fileid, int fileweek, String filename){
            this.fileid = fileid;
            this.fileweek = String.valueOf(fileweek);
            this.filename = filename;
        }

        public void run() {

            final int  MEGABYTE = 1024 * 1024;

            StringBuilder förädlat = new StringBuilder();
            String päron = "";
            downloadSuccess = true;

            for (int i = 0; i <= 4; i++) {
                String tempFileName = "temppdffile" + i;
                File tempFile = new File(getFilesDir() + "/" + tempFileName);
                try {
                    URL url = new URL("http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=pdf&schoolid=81530/sv-se&type=0&id=" + fileid  + "&period=&week=" + fileweek + "&mode=0&printer=0&colors=32&head=5&clock=7&foot=1&day=" + Math.round(Math.pow(2,i)) + "&width=400&height=640");
                    HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    FileOutputStream fileOutputStream = openFileOutput(tempFileName,MODE_PRIVATE);
                    int totalSize = urlConnection.getContentLength();

                    byte[] buffer = new byte[MEGABYTE];
                    int bufferLength = 0;
                    while((bufferLength = inputStream.read(buffer))>0 ){
                        fileOutputStream.write(buffer, 0, bufferLength);
                    }
                    fileOutputStream.close();

                    try {
                        PdfReader reader = new PdfReader(tempFile.getPath());
                        päron = PdfTextExtractor.getTextFromPage(reader, 1);
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        downloadSuccess = false;
                    }

                    String[] single = päron.split("\n");
                    for (int j = 1; j < single.length; j++) {
                        String[] testing = single[j].split(":");
                        if (testing.length <= 2) {
                            förädlat.append(single[j]);
                        } else {
                            String[] tempArray = single[j].split(" ");
                            förädlat.append(tempArray[0]);
                            förädlat.append("%maq1ekax%");
                            förädlat.append(tempArray[1]);
                        }
                        if (j != single.length - 1) {
                            förädlat.append("%maq1ekax%");
                        }
                    }

                    if (i != 4) {
                        förädlat.append("%day%");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    downloadSuccess = false;
                }

                tempFile.delete();
            }
            if (downloadSuccess) {
                FileOutputStream outputStream;
                try {
                    schemaFile.delete();

                    outputStream = openFileOutput(filename,MODE_PRIVATE);
                    outputStream.write(förädlat.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    downloadSuccess = false;
                }
            }
        }
    }

}