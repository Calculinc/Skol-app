package calculinc.google.httpssites.skol_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout.LayoutParams;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import calculinc.google.httpssites.skol_app.Matsedel.FirstFragment;
import calculinc.google.httpssites.skol_app.Matsedel.SecondFragment;
import calculinc.google.httpssites.skol_app.days.Friday;
import calculinc.google.httpssites.skol_app.days.Monday;
import calculinc.google.httpssites.skol_app.days.Thursday;
import calculinc.google.httpssites.skol_app.days.Tuesday;
import calculinc.google.httpssites.skol_app.days.Wednesday;

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

    public class FixedTabsPagerAdapter extends FragmentPagerAdapter {

        public FixedTabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {

                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                default:
                    return null;
            }
        }

    }

    public class FixedDayPagerAdapter extends FragmentPagerAdapter {

        public FixedDayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {

                case 0:
                    return new Monday();
                case 1:
                    return new Tuesday();
                case 2:
                    return new Wednesday();
                case 3:
                    return new Thursday();
                case 4:
                    return new Friday();
                default:
                    return null;
            }
        }

    }

    public void viewPager() {

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager2);
        PagerAdapter pagerAdapter = new FixedDayPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem( currentDay -1 );

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout2);
        tabLayout.setupWithViewPager(pager);
    }

    public void viewFuckingPager() {

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new FixedTabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }

    public void loginSubmit (View view) {
        final EditText personal_id = (EditText) findViewById(R.id.editText);
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        matsedel();

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
            viewPager();

        } else if (id == R.id.nav_matsedel) {

            toolbar.setTitle(R.string.Tab_3);
            vf.setDisplayedChild(2);
            drawer.closeDrawer(GravityCompat.START);
            getMat test = new getMat();
            test.start();
            viewFuckingPager();

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

                    final int[] relWeekIDS = {
                            R.id.relativt_schema1,
                            R.id.relativt_schema2,
                            R.id.relativt_schema3,
                            R.id.relativt_schema4,
                            R.id.relativt_schema5
                    };
                    final int[] relDayIDS = {
                            R.id.relative_layout_monday,
                            R.id.relativt_schema2,
                            R.id.relativt_schema3,
                            R.id.relativt_schema4,
                            R.id.relativt_schema5
                    };

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

                        RelativeLayout schema_space = (RelativeLayout) findViewById(relWeekIDS[i]);
                        RelativeLayout schema_dayce = (RelativeLayout) findViewById(relDayIDS[i]);

                        int status = 0;
                        String StartTid = "";
                        double starttid = 0;
                        String SlutTid;
                        double sluttid;
                        String öpö = "";

                        for (int j = 0; j < desert.length; j++) {
                            status++;
                            try {
                                if (status == 1) {
                                    //lite matte här
                                    StartTid = desert[j];
                                    String[] time = StartTid.split(":");
                                    starttid = Double.parseDouble(time[0]) - 8 + (Double.parseDouble(time[1])) / 60;

                                }
                                if (status == 2) {
                                    //hämta lektionstext
                                    öpö = desert[j];
                                }
                                if (status == 3) {
                                    //lite mer matte
                                    status = 0;
                                    SlutTid = desert[j];
                                    String[] time = SlutTid.split(":");
                                    sluttid = Double.parseDouble(time[0]) - 8 + Double.parseDouble(time[1]) / 60;

                                    LinearLayout linear = new LinearLayout(getBaseContext());
                                    //linear.setWeightSum(11);
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



                                    LinearLayout linearDay = new LinearLayout(getBaseContext());
                                    linearDay.setOrientation(LinearLayout.VERTICAL);
                                    LinearLayout.LayoutParams LinDayParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)((sluttid - starttid) * 90 * getResources().getDimension(R.dimen.dp_unit)));
                                    LinDayParams.setMargins(0,(int)(starttid * 90 * getResources().getDimension(R.dimen.dp_unit)),0,0);
                                    linearDay.setLayoutParams(LinDayParams);
                                    //linearDay.setBackgroundColor(Color.parseColor("#70ff5a36"));

                                    TextView dayTop = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams TopDayParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(15 * getResources().getDimension(R.dimen.dp_unit)));
                                    dayTop.setLayoutParams(TopDayParams);
                                    dayTop.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                                    dayTop.setTextSize(12);
                                    dayTop.setTextColor(Color.parseColor("#1f1f1f"));
                                    dayTop.setText(StartTid);
                                    linearDay.addView(dayTop);

                                    TextView dayMid = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams MidDayParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(((sluttid - starttid) * 90 - 30) * getResources().getDimension(R.dimen.dp_unit)));
                                    dayMid.setLayoutParams(TopDayParams);
                                    dayMid.setGravity(Gravity.CENTER);
                                    dayMid.setTextSize(20);
                                    dayMid.setTextColor(Color.parseColor("#1f1f1f"));
                                    dayMid.setText(öpö);
                                    linearDay.addView(dayMid);

                                    TextView dayBot = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams BotDayParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(15 * getResources().getDimension(R.dimen.dp_unit)));
                                    dayBot.setLayoutParams(TopDayParams);
                                    dayBot.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                    dayBot.setTextSize(12);
                                    dayBot.setTextColor(Color.parseColor("#1f1f1f"));
                                    dayBot.setText(SlutTid);
                                    linearDay.addView(dayBot);

                                    schema_dayce.addView(linearDay);

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
            makeSchemaDag();
        }

        the_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    makeSchemaVecka();
                    DayPref = false;
                    DayPrefFile.delete();
                } else {
                    makeSchemaDag();
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

    public void makeSchemaDag() {

        currentDay = 2;

        final LinearLayout schemaVeckaLayout = (LinearLayout) findViewById(R.id.schema_vecka_layout);
        final LinearLayout schemaDagLayout = (LinearLayout) findViewById(R.id.schema_dag_layout);

        schemaVeckaLayout.setVisibility(View.GONE);
        schemaDagLayout.setVisibility(View.VISIBLE);

    }

    public void makeSchemaVecka() {

        final LinearLayout schemaVeckaLayout = (LinearLayout) findViewById(R.id.schema_vecka_layout);
        final LinearLayout schemaDagLayout = (LinearLayout) findViewById(R.id.schema_dag_layout);

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
            String[] veckodagar = {"Måndag Mån", "Tisdag Tis", "Onsdag Ons", "Torsdag Tors", "Fredag Fre"};
            String[] stringarraytemp;
            int size;

            try {
                doc = Jsoup.connect("https://skolmaten.se/norra-real/").get();
                title = doc.select("div[class=row]").text();
                size = doc.select("div[class=row]").size();
                stringtemp = title;
                int status = 0;

                if (size == 0){

                    realDeal = "/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/1";

                } else {

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
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    };

    public void skolMaten() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    matsedel();

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private class getMat extends Thread {
        public void run() {


            htmldownloader H = new htmldownloader();
            H.start();
            try {
                H.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            skolMaten();
        }
    }

    public void matsedel() {

        TextView textView1 = (TextView) findViewById(R.id.test_text1);
        TextView textView2 = (TextView) findViewById(R.id.test_text2);
        TextView textView3 = (TextView) findViewById(R.id.test_text3);
        TextView textView4 = (TextView) findViewById(R.id.test_text4);
        TextView textView5 = (TextView) findViewById(R.id.test_text5);

        //TextView dagens = (TextView) findViewById(R.id.dagens_mat);

        LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

        progressLayout.setVisibility(View.GONE);

        mat = realDeal.split("/maq1/");

        if (mat.length == 7) {

            textView1.setText(mat[1]);
            textView2.setText(mat[2]);
            textView3.setText(mat[3]);
            textView4.setText(mat[4]);
            textView5.setText(mat[5]);

            //dagens.setText(mat[currentDay]);

        } else {

            textView1.setText(mat[1].substring(12));
            textView2.setText(mat[2].substring(12));
            textView3.setText(mat[3].substring(12));
            textView4.setText(mat[4].substring(12));
            textView5.setText(mat[5].substring(12));

            //dagens.setText(mat[currentDay].substring(12));
        }
    }

    public void röstning(View view) {

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        final TextView ratingtext = (TextView) findViewById(R.id.rating_text);
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        view.startAnimation(anim_button_click);
        ratingtext.setText(String.valueOf(ratingBar.getRating()));

    }

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
