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
import android.support.v7.view.menu.MenuView;
import android.text.TextPaint;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

import static calculinc.google.httpssites.skol_app.R.drawable.rainbowcolor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    long id_number;
    int day = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK);
    int week = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    String pdfFileName = "Nova.pdf";
    File pdfFile;
    String loginFileName = "login.txt";
    File loginFile;

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
        kek.setText(String.valueOf(week));

        pdfFile = new File(getFilesDir() + "/" + pdfFileName);

        if (pdfFile.exists()) {
            pdfFile.delete();
        }

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

    private Menu menu;

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

        if (id == R.id.nav_nyheter) {

            toolbar.setTitle(R.string.Tab_1);
            vf.setDisplayedChild(0);

        } else if (id == R.id.nav_schema) {

            toolbar.setTitle(R.string.Tab_2);
            vf.setDisplayedChild(1);
            new DownloadFile().execute("http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=pdf&schoolid=81530/sv-se&type=0&id=" + id_number  + "&period=&week=" + 20 + "&mode=0&printer=0&colors=32&head=5&clock=7&foot=1&day=" + 0 + "&width=400&height=640");

        } else if (id == R.id.nav_matsedel) {

            toolbar.setTitle(R.string.Tab_3);
            vf.setDisplayedChild(2);
            ggfunktion();

        } else if (id == R.id.nav_laxor) {

            toolbar.setTitle(R.string.Tab_4);
            vf.setDisplayedChild(3);

        } else if (id == R.id.nav_login) {

            toolbar.setTitle(R.string.Tab_5);
            vf.setDisplayedChild(4);

        } else if (id == R.id.nav_send) {

            toolbar.setTitle(R.string.Tab_6);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            //String fileName = strings[1];

            final int  MEGABYTE = 1024 * 1024;

            String lel = "finns";
            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                lel = "hej";
                if (pdfFile.exists()) {
                    pdfFile.delete();
                    lel = "borta";
                }
                FileOutputStream fileOutputStream = openFileOutput(pdfFileName,MODE_PRIVATE);
                int totalSize = urlConnection.getContentLength();
                lel = "finns";

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // The following is purely for testing in the debugger
            String kek;

            if (pdfFile.exists()) {
                kek = "pdfFile exists";
            } else {
                kek = "pdfFile doesn't exist";
            }

            if (pdfFile.isDirectory()) {
                kek = "pdfFile is a directory";
            } else {
                kek = "pdfFile ain't a directory";
            }

            if (pdfFile.isFile()) {
                kek = "pdfFile is a file";
            } else {
                kek = "pdfFile ain't a file";
            }

            if (pdfFile.length() > 1000) {
                kek = "pdfFile is loooong " + pdfFile.length();
            } else {
                kek = "pdfFile is short " + pdfFile.length();
            }
            //delete when not needed

            return null;
        }
    }

}