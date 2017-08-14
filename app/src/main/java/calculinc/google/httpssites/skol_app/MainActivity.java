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
import android.text.Html;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.Line;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

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
import static calculinc.google.httpssites.skol_app.R.drawable.ya_blew_it;

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
    boolean DayPref;
    File DayPrefFile;

    Document docpsdgh = Jsoup.parse("ll");

    String[] mat;
    String realDeal = "";
    String title;
    String simple;
    Document doc1 = Jsoup.parse("<html><head>\n" +
            "\t<meta charset=\"utf-8\">\n" +
            "\t<title>Skolmaten - Östra Real</title>\n" +
            "\t<meta name=\"apple-itunes-app\" content=\"app-id=416550379, app-argument=schoolmeal://ostra-real/\">\n" +
            "\t<link rel=\"apple-touch-icon\" href=\"/apple-touch-icon.png\">\n" +
            "\t<link rel=\"icon\" href=\"/favicon.ico\">\n" +
            "\t<link rel=\"mask-icon\" href=\"/img/me/pin-icon.svg\" color=\"#2194F9\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\"><link rel=\"stylesheet\" type=\"text/css\" href=\"/css/me/base.css?4-0-3\">\n" +
            "\t\t<link rel=\"stylesheet\" type=\"text/css\" href=\"/css/theme.css?4-0-3\"><link rel=\"stylesheet\" type=\"text/css\" href=\"/css/me/viewer.css?4-0-3\"><script async=\"\" src=\"https://www.google-analytics.com/analytics.js\"></script><script type=\"text/javascript\" src=\"/environment.js?4-0-3\"></script>\n" +
            "\t\t<script type=\"text/javascript\" src=\"/js/me/base.js?4-0-3\"></script><script type=\"text/javascript\" src=\"/js/me/viewer.js?4-0-3\"></script><script>\n" +
            "\t\t(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
            "\t\t(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
            "\t\tm=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
            "\t\t})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');ga('create', 'UA-68155234-2', 'auto');\n" +
            "\t\t\tga('send', 'pageview');</script>\n" +
            "</head>\n" +
            "<body><header>\n" +
            "\t<ul>\n" +
            "\t\t<li><a href=\"/admin/authorize/\">Logga in</a></li>\n" +
            "\t</ul>\n" +
            "</header><main class=\"tiny\"><a href=\"/d/stockholms-stad/\" class=\"back\">Stockholms stad</a>\n" +
            "\t<h2><img src=\"https://lh3.googleusercontent.com/sNDdvgfu7HQL_I7XxzEXJgA3WHkmqBEvsTaWD25faQrXdTzpSjTbWA42ozKSRuiSg3vRDDdS5r1DVKpMW7k\" alt=\"Östra Real\">Östra Real\n" +
            "\t</h2>\n" +
            "\t\n" +
            "\t<div id=\"menu\" data-id=\"5562614612492288\">\n" +
            "\t\t<div id=\"weeks\">\n" +
            "\t\t\t<div class=\"week  \" id=\"week-2017-32\" data-year=\"2017\" data-week-of-year=\"32\">\n" +
            "\t\t\t\t<h3>Vecka  <span>32</span></h3><span class=\"week-reason missing\">Menyn saknas.<br>Kolla med ditt skolkök.</span></div>\n" +
            "\t\t<div id=\"week-2017-33\" class=\"week  visible\" data-year=\"2017\" data-week-of-year=\"33\"><h3>Vecka <span>33</span></h3><div class=\"row\"><div class=\"date\"><span class=\"weekday\">Måndag</span><span class=\"weekday min\">Mån</span><span class=\"date\">2017-08-14</span></div><div class=\"items\"><p class=\"missing\">Menyn saknas.</p></div></div><div class=\"row\"><div class=\"date\"><span class=\"weekday\">Tisdag</span><span class=\"weekday min\">Tis</span><span class=\"date\">2017-08-15</span></div><div class=\"items\"><p class=\"missing\">Menyn saknas.</p></div></div><div class=\"row\"><div class=\"date\"><span class=\"weekday\">Onsdag</span><span class=\"weekday min\">Ons</span><span class=\"date\">2017-08-16</span></div><div class=\"items\"><p class=\"missing\">Menyn saknas.</p></div></div><div class=\"row\"><div class=\"date\"><span class=\"weekday\">Torsdag</span><span class=\"weekday min\">Tors</span><span class=\"date\">2017-08-17</span></div><div class=\"items\"><p><span>Spagetti med sojafärssås</span></p><p><span>Spagetti med köttfärssås</span></p></div></div><div class=\"row\"><div class=\"date\"><span class=\"weekday\">Fredag</span><span class=\"weekday min\">Fre</span><span class=\"date\">2017-08-18</span></div><div class=\"items\"><p class=\"missing\">Menyn saknas.</p></div></div></div></div>\n" +
            "\t\t<div id=\"bulletins\"></div>\n" +
            "\t</div>\n" +
            "\t\n" +
            "\t<div id=\"controls\">\n" +
            "\t\t<a href=\"javascript:$viewer.fetchNextWeek();\" class=\"next\"><span>Nästa vecka</span></a>\n" +
            "\t\t<a href=\"javascript:$viewer.fetchPreviousWeek();\" class=\"previous\"><span>Föregående vecka</span></a>\n" +
            "\t</div>\n" +
            "\t\n" +
            "\t<ul class=\"links\">\n" +
            "\t\t<li>\n" +
            "\t\t\t<ul>\n" +
            "\t\t\t\t<li><a href=\"print/\">Skriv ut</a></li>\n" +
            "\t\t\t\t<li><a href=\"/about/calendar/ostra-real/\">Kalender</a></li>\n" +
            "\t\t\t\t<li><a href=\"/about/rss/ostra-real/\">RSS flöde</a></li>\n" +
            "\t\t\t</ul>\n" +
            "\t\t</li>\n" +
            "\t</ul></main><footer>\n" +
            "\t<div><ul>\n" +
            "\t\t<li><h2>Skolmaten</h2></li>\n" +
            "\t\t<li><p>v.4-0-3</p></li>\n" +
            "\t</ul></div>\n" +
            "\t<div><ul>\n" +
            "\t\t<li>Om Skolmaten</li>\n" +
            "\t\t<li><a href=\"http://www.dinskolmat.se/\">Information</a></li>\n" +
            "\t\t<li><a href=\"/about/cookies/\">Cookies</a></li>\n" +
            "\t\t<li><a href=\"mailto:info@dinskolmat.se\">Kontakta oss</a></li></ul></div>\n" +
            "\t<div><ul>\n" +
            "\t\t<li>Appar</li>\n" +
            "\t\t<li><a href=\"https://itunes.apple.com/se/app/skolmaten/id416550379\" target=\"_blank\">iOS</a></li>\n" +
            "\t\t<li><a href=\"https://play.google.com/store/apps/details?id=se.yo.android.skolmat&amp;hl=sv\" target=\"_blank\">Android</a></li>\n" +
            "\t</ul></div><div><ul>\n" +
            "\t\t<li>Administration</li>\n" +
            "\t\t<li><a href=\"/admin/authorize/\">Logga in</a></li>\n" +
            "\t</ul></div></footer>\n" +
            "\n" +
            "</body></html>");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView kek = (TextView) findViewById(R.id.öpö);
        kek.setText(String.valueOf(currentMinute));

        if (currentDay == 7) {
            currentDay = 1;
        }
        if (currentDay != 1) {
            currentDay--;
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
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);
        TextView textView1 = (TextView) findViewById(R.id.test_text1);
        TextView textView2 = (TextView) findViewById(R.id.test_text2);
        TextView textView3 = (TextView) findViewById(R.id.test_text3);
        TextView textView4 = (TextView) findViewById(R.id.test_text4);
        TextView textView5 = (TextView) findViewById(R.id.test_text5);

        mat = realDeal.split("/maq1/");


        textView1.setText(mat[1].substring(11));
        textView2.setText(mat[2].substring(11));
        textView3.setText(mat[3].substring(11));
        textView4.setText(mat[4].substring(11));
        textView5.setText(mat[5].substring(11));

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

            schemaSelect öpö = new schemaSelect();
            öpö.start();
            theSwtich();
            fidget_spinner();

        } else if (id == R.id.nav_matsedel) {

            toolbar.setTitle(R.string.Tab_3);
            vf.setDisplayedChild(2);
            drawer.closeDrawer(GravityCompat.START);
            htmldownloader test = new htmldownloader();
            test.start();

        } else if (id == R.id.nav_laxor) {

            toolbar.setTitle(R.string.Tab_4);
            vf.setDisplayedChild(3);
            drawer.closeDrawer(GravityCompat.START);
            schemaArrayFixer lloldawd = new schemaArrayFixer();
            lloldawd.start();

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

    private class schemaSelect extends Thread {
        public void run() {

            downloadWeek = 34;
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

    private class schemaRefresh extends Thread {

    }

    public void drawSchema() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

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
                    RelativeLayout schema_space = (RelativeLayout) findViewById(R.id.relativt_schema1);
                    for (int i = 0; i <= 4; i++) {
                        String[] desert;
                        if (druvor.length > 4){
                            if (!druvor[i].equals("")) {
                                desert = druvor[i].split("%maq1ekax%");
                            } else {
                                desert = druvor;
                            }
                        } else {
                            desert = druvor;
                        }

                        if (i == 0){
                            schema_space = (RelativeLayout) findViewById(R.id.relativt_schema1);
                        }
                        if (i == 1){
                            schema_space = (RelativeLayout) findViewById(R.id.relativt_schema2);
                        }
                        if (i == 2){
                            schema_space = (RelativeLayout) findViewById(R.id.relativt_schema3);
                        }
                        if (i == 3){
                            schema_space = (RelativeLayout) findViewById(R.id.relativt_schema4);
                        }
                        if (i == 4){
                            schema_space = (RelativeLayout) findViewById(R.id.relativt_schema5);
                        }

                        int status = 0;

                        double starttid = 0;
                        double sluttid;
                        String öpö = "ö";

                        for (int j = 0; j < desert.length; j++) {
                            status++;
                            try {
                                if (status == 1) {
                                    //lite matte här
                                    String[] time = desert[j].split(":");
                                    starttid = Double.parseDouble(time[0]) - 8 + (Double.parseDouble(time[1])) / 60;

                                }
                                if (status == 2) {
                                    //hämta lektionstext
                                    öpö = desert[j];
                                }
                                if (status == 3) {
                                    //lite mer matte
                                    String[] time = desert[j].split(":");
                                    sluttid = Double.parseDouble(time[0]) - 8 + Double.parseDouble(time[1]) / 60;

                                    LinearLayout linear = new LinearLayout(getBaseContext());
                                    linear.setWeightSum(11);
                                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                    linear.setOrientation(LinearLayout.VERTICAL);
                                    linear.setLayoutParams(params2);

                                    TextView blank1 = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams paramsBlank1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                    paramsBlank1.weight = (float)(starttid+1);
                                    blank1.setLayoutParams(paramsBlank1);
                                    blank1.setTextSize(0);
                                    linear.addView(blank1);

                                    TextView lektion = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams paramsLektion = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                                    paramsLektion.weight = (float)(sluttid - starttid);
                                    lektion.setLayoutParams(paramsLektion);
                                    lektion.setBackgroundColor(Color.parseColor("#70ff5a36"));
                                    lektion.setTextSize(0);
                                    //lektion.setBackgroundResource(ya_blew_it);
                                    //lektion.setText(öpö);
                                    linear.addView(lektion);

                                    TextView blank2 = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams paramsBlank2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                                    paramsBlank2.weight = (float)(10 - sluttid);
                                    blank2.setLayoutParams(paramsBlank2);
                                    blank2.setTextSize(0);
                                    linear.addView(blank2);

                                    schema_space.addView(linear);
                                    status = 0;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

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

    public void theSwtich() {

        final String DayPrefFileName = "DayPref.txt";
        DayPrefFile = new File(getFilesDir() + "/" + DayPrefFileName);
        DayPref = DayPrefFile.exists();

        Switch the_switch = (Switch) findViewById(R.id.the_switch);
        the_switch.setChecked(!DayPref);
        if (DayPref) {
            makeSchemaDag(currentDay);
        }

        the_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeSchemaVecka();
                    DayPref = false;
                    DayPrefFile.delete();
                } else {
                    makeSchemaDag(currentDay);
                    DayPref = true;
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(DayPrefFileName,MODE_PRIVATE);
                        outputStream.write(DayPrefFileName.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void makeSchemaDag(int dag) {

        final RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativt_schema1);
        final RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativt_schema2);
        final RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.relativt_schema3);
        final RelativeLayout relativeLayout4 = (RelativeLayout) findViewById(R.id.relativt_schema4);
        final RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.relativt_schema5);
        final LinearLayout vänsterPOOSH = (LinearLayout) findViewById(R.id.vänsterPOOSH);
        final LinearLayout högerPOOSH = (LinearLayout) findViewById(R.id.högerPOOSH);
        final LinearLayout schemaVecka = (LinearLayout) findViewById(R.id.schema_vecka);
        final RelativeLayout relativtsep1 = (RelativeLayout) findViewById(R.id.relativt_sep1);
        final RelativeLayout relativtsep2 = (RelativeLayout) findViewById(R.id.relativt_sep2);
        final RelativeLayout relativtsep3 = (RelativeLayout) findViewById(R.id.relativt_sep3);
        final RelativeLayout relativtsep4 = (RelativeLayout) findViewById(R.id.relativt_sep4);
        final LinearLayout schemaVeckaLayout = (LinearLayout) findViewById(R.id.schema_vecka_layout);
        final LinearLayout schemaDagLayout = (LinearLayout) findViewById(R.id.schema_dag_layout);
        final TextView dagensDag = (TextView) findViewById(R.id.dag_text);

        String[] veckodagar = {"Calculinc", "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag", "Söndag"};

        LinearLayout.LayoutParams paramsBlank2 = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 0);

        if (dag != 1) {
            relativeLayout1.setLayoutParams(paramsBlank2);
        } if (dag != 2) {
            relativeLayout2.setLayoutParams(paramsBlank2);
        } if (dag != 3) {
            relativeLayout3.setLayoutParams(paramsBlank2);
        } if (dag != 4) {
            relativeLayout4.setLayoutParams(paramsBlank2);
        } if (dag != 5) {
            relativeLayout5.setLayoutParams(paramsBlank2);
        }

        LinearLayout.LayoutParams paramsExpand2 = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.6f);
        LinearLayout.LayoutParams paramsExpand3 = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 5.4f);

        vänsterPOOSH.setLayoutParams(paramsExpand2);
        högerPOOSH.setLayoutParams(paramsExpand2);
        schemaVecka.setLayoutParams(paramsExpand3);

        relativtsep1.setVisibility(View.GONE);
        relativtsep2.setVisibility(View.GONE);
        relativtsep3.setVisibility(View.GONE);
        relativtsep4.setVisibility(View.GONE);

        schemaVeckaLayout.setVisibility(View.GONE);
        schemaDagLayout.setVisibility(View.VISIBLE);

        dagensDag.setText(veckodagar[dag]);
    }

    public void makeSchemaVecka() {

        final RelativeLayout relativeLayout1 = (RelativeLayout) findViewById(R.id.relativt_schema1);
        final RelativeLayout relativeLayout2 = (RelativeLayout) findViewById(R.id.relativt_schema2);
        final RelativeLayout relativeLayout3 = (RelativeLayout) findViewById(R.id.relativt_schema3);
        final RelativeLayout relativeLayout4 = (RelativeLayout) findViewById(R.id.relativt_schema4);
        final RelativeLayout relativeLayout5 = (RelativeLayout) findViewById(R.id.relativt_schema5);
        final LinearLayout vänsterPOOSH = (LinearLayout) findViewById(R.id.vänsterPOOSH);
        final LinearLayout högerPOOSH = (LinearLayout) findViewById(R.id.högerPOOSH);
        final LinearLayout schemaVecka = (LinearLayout) findViewById(R.id.schema_vecka);
        final RelativeLayout relativtsep1 = (RelativeLayout) findViewById(R.id.relativt_sep1);
        final RelativeLayout relativtsep2 = (RelativeLayout) findViewById(R.id.relativt_sep2);
        final RelativeLayout relativtsep3 = (RelativeLayout) findViewById(R.id.relativt_sep3);
        final RelativeLayout relativtsep4 = (RelativeLayout) findViewById(R.id.relativt_sep4);
        final LinearLayout schemaVeckaLayout = (LinearLayout) findViewById(R.id.schema_vecka_layout);
        final LinearLayout schemaDagLayout = (LinearLayout) findViewById(R.id.schema_dag_layout);

        LinearLayout.LayoutParams paramsBlank = new LinearLayout.LayoutParams( 0, LayoutParams.MATCH_PARENT, 8);
        LinearLayout.LayoutParams paramsBlank2 = new LinearLayout.LayoutParams( 0, LayoutParams.MATCH_PARENT, 2);
        LinearLayout.LayoutParams paramsBlank3 = new LinearLayout.LayoutParams( 0, LayoutParams.MATCH_PARENT, 0);
        relativeLayout1.setLayoutParams(paramsBlank2);
        relativeLayout2.setLayoutParams(paramsBlank2);
        relativeLayout3.setLayoutParams(paramsBlank2);
        relativeLayout4.setLayoutParams(paramsBlank2);
        relativeLayout5.setLayoutParams(paramsBlank2);

        vänsterPOOSH.setLayoutParams(paramsBlank3);
        högerPOOSH.setLayoutParams(paramsBlank3);
        schemaVecka.setLayoutParams(paramsBlank);

        relativtsep1.setVisibility(View.VISIBLE);
        relativtsep2.setVisibility(View.VISIBLE);
        relativtsep3.setVisibility(View.VISIBLE);
        relativtsep4.setVisibility(View.VISIBLE);

        schemaDagLayout.setVisibility(View.GONE);
        schemaVeckaLayout.setVisibility(View.VISIBLE);
    }

    private class schemaArrayFixer extends Thread{
        public void run() {
            try {
                Document doc = Jsoup.connect("http://192.168.1.70").get();
                String llol = doc.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class htmldownloader extends Thread {
        public void run() {
            Document doc;
            String stringtemp;
            String[] veckodagar = {"MåndagMån", "TisdagTis", "OnsdagOns", "TorsdagTors", "FredagFre"};
            String[] stringarraytemp;
            int size;

            try {
                doc = Jsoup.connect("https://skolmaten.se/norra-real/").get();
                title = doc1.select("div[class=row]").text();
                size = doc1.select("div[class=row]").size();
                stringtemp = title;
                int status = 0;

                for (int i = 0; i < 5 ; i++) {

                    stringarraytemp = stringtemp.split(veckodagar[i]);

                    if (stringarraytemp.length == 2 ) {

                        status ++;

                        if ( status == size ) {

                            realDeal = realDeal + "/maq1/" + stringarraytemp[0] + "/maq1/" + stringarraytemp[1];
                        } else {

                            if ( status == 1 ) {

                                stringtemp = stringarraytemp[1];
                            } else {

                                realDeal = realDeal + "/maq1/" + stringarraytemp[0];
                                stringtemp = stringarraytemp[1];
                            }

                        }

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void fidget_spinner(){

        Spinner spinner = (Spinner) findViewById(R.id.fidget_spinner);
        Spinner spinnerSchemaType = (Spinner) findViewById(R.id.typ_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.spinner_layout);
        spinner.setAdapter(adapter);

        ArrayAdapter adapterTyp = ArrayAdapter.createFromResource(this, R.array.typ_array, R.layout.spinner_layout);
        spinnerSchemaType.setAdapter(adapterTyp);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        adapterTyp.setDropDownViewResource(R.layout.spinner_dropdown_layout);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinnerSchemaType.setAdapter(adapterTyp);

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
                    //URL url = new URL("http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=pdf&schoolid=81530/sv-se&type=0&id=" + fileid  + "&period=&week=" + fileweek + "&mode=0&printer=0&colors=32&head=5&clock=7&foot=1&day=" + Math.round(Math.pow(2,i)) + "&width=400&height=640");
                    URL url = new URL("http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=pdf&schoolid=81660/sv-se&type=0&id=" + "na16b" + "&period=&week=" + fileweek + "&mode=0&printer=0&colors=32&head=5&clock=7&foot=1&day=" + Math.round(Math.pow(2,i)) + "&width=400&height=640");
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
