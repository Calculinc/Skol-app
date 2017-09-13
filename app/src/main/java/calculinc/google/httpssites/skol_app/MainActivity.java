package calculinc.google.httpssites.skol_app;

import android.content.Context;
import android.media.Rating;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
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

import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import calculinc.google.httpssites.skol_app.Matsedel.FirstFragment;
import calculinc.google.httpssites.skol_app.Matsedel.SecondFragment;
import calculinc.google.httpssites.skol_app.days.Friday;
import calculinc.google.httpssites.skol_app.days.Monday;
import calculinc.google.httpssites.skol_app.days.Thursday;
import calculinc.google.httpssites.skol_app.days.Tuesday;
import calculinc.google.httpssites.skol_app.days.Wednesday;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String id_number;
    int genderInteger = 0;

    final String myTag = "DocsUpload";

    double matsedelrating;

    int currentWeek = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    int currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
    int currentHour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int currentMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE);

    int foodDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

    int downloadWeek, focusDay;

    String schemaFileName = "Nova.txt";
    File schemaFile;
    String loginFileName = "login.txt";
    File loginFile;
    boolean downloadSuccess;
    boolean DayPref;
    File DayPrefFile;

    final String[] genderStrings = {
            "Kvinna",
            "Man",
            "Annat", "Annat", "Annat"
    };

    String[] mat;
    String realDeal = "/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln";
    String title;
    String tempSchema = "10:55%maq1ekax%RELREL01 NSJ A13%maq1ekax%12:10%maq1ekax%12:40%maq1ekax%MATTID%maq1ekax%13:00%maq1ekax%13:10%maq1ekax%SVESVE03 PLA B13%maq1ekax%14:10%maq1ekax%14:20%maq1ekax%SVESVE03 PLA B13%maq1ekax%15:20%day%08:20%maq1ekax%ENGENG07 JHA C11%maq1ekax%09:35%maq1ekax%09:45%maq1ekax%GYAR NSJ,MOR B41,B42%maq1ekax%10:45%maq1ekax%10:50%maq1ekax%MATTID%maq1ekax%11:10%maq1ekax%11:35%maq1ekax%MATMAT05 BAM A23%maq1ekax%12:35%maq1ekax%12:45%maq1ekax%14:00%maq1ekax%studiebesök KBN%maq1ekax%TTF/GYARB%maq1ekax%17:00%day%09:50%maq1ekax%KEBI MOR A41%maq1ekax%11:05%maq1ekax%11:30%maq1ekax%MATTID%maq1ekax%11:50%maq1ekax%12:10%maq1ekax%MATMAT05 BAM B14%maq1ekax%13:40%maq1ekax%13:50%maq1ekax%PRRPRR01 LZH B32%maq1ekax%15:05%maq1ekax%15:15%maq1ekax%RELREL01 NSJ A01%maq1ekax%16:30%day%09:50%maq1ekax%ENGENG07 JHA A14%maq1ekax%11:05%maq1ekax%11:25%maq1ekax%MATTID%maq1ekax%11:45%maq1ekax%12:00%maq1ekax%Mentorstid BAM B12%maq1ekax%12:30%maq1ekax%12:30%maq1ekax%SVESVE03 PLA B12%maq1ekax%13:30%maq1ekax%13:40%maq1ekax%FYSFYS02 LAD A32%maq1ekax%14:50%maq1ekax%15:15%maq1ekax%EGEN STUDIETID NSJ,MOR%maq1ekax%16:15%day%08:20%maq1ekax%PRRPRR01 LZH A21%maq1ekax%09:35%maq1ekax%09:45%maq1ekax%KEBI (a MOR A42%maq1ekax%11:15%maq1ekax%11:35%maq1ekax%MATTID%maq1ekax%11:55";

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
        kek.setText(String.valueOf(currentHour));

        schemaTimeRefresh bootUpTimeSync = new schemaTimeRefresh();
        bootUpTimeSync.start();

        getSavedLoginContent();

        Log.i(myTag, "OnCreate()");
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
        pager.setCurrentItem(focusDay - 1);

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

    public void getSavedLoginContent() {
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
                String[] loginParams = sb.toString().split("%");
                id_number = loginParams[4];
                genderInteger = Integer.parseInt(loginParams[5]);

                EditText name = (EditText) findViewById(R.id.name1);
                EditText surname = (EditText) findViewById(R.id.name2);
                EditText phone = (EditText) findViewById(R.id.mobile_number);
                EditText city = (EditText) findViewById(R.id.city);
                EditText personal_id = (EditText) findViewById(R.id.personalid);

                name.setText(loginParams[0]);
                surname.setText(loginParams[1]);
                phone.setText(loginParams[2]);
                city.setText(loginParams[3]);
                personal_id.setText(id_number);

            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginSubmit (View view) {
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);
        view.startAnimation(anim_button_click);

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText city = (EditText) findViewById(R.id.city);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final Spinner spinnerLoginGender = (Spinner) findViewById(R.id.fidget_spinner);

        final String Namn = name.getText().toString();
        final String Efternamn = surname.getText().toString();
        final String Mobil = phone.getText().toString();
        final String Stad = city.getText().toString();
        id_number = personal_id.getText().toString();
        final String datum;{
            char[] siffra = id_number.toCharArray();
            if (siffra[0] == '9') {
                datum = "19" + siffra[0] + siffra[1] + "-" + siffra[2] + siffra[3] +"-" + siffra[4] + siffra[5];
            } else {
                datum = "20" + siffra[0] + siffra[1] + "-" + siffra[2] + siffra[3] +"-" + siffra[4] + siffra[5];
            }
        }
        final int id_spinneritemgender = spinnerLoginGender.getSelectedItemPosition();

        StringBuilder loginContent = new StringBuilder();{
            loginContent.append(Namn);
            loginContent.append("%");
            loginContent.append(Efternamn);
            loginContent.append("%");
            loginContent.append(Mobil);
            loginContent.append("%");
            loginContent.append(Stad);
            loginContent.append("%");
            loginContent.append(id_number);
            loginContent.append("%");
            loginContent.append(String.valueOf(id_spinneritemgender));
        }

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(loginFileName,MODE_PRIVATE);
            outputStream.write(loginContent.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                postFormRegisterData(Namn, Efternamn, Mobil, Stad, datum, id_spinneritemgender);

            }
        });
        t.start();
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
            //start loading animation here (spinning circle and "laddar..." text)

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

    private class schemaTimeRefresh extends Thread {
        public void run() {
            currentWeek = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            currentHour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
            currentMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE);

            foodDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

            if (foodDay == 0 || foodDay == 6 || foodDay ==7) {
                foodDay = 1;
            }
            if (currentHour < 18) {
                focusDay = currentDay;
            } else {
                focusDay = currentDay + 1;
            }
            if (focusDay == 0 || focusDay == 6 || focusDay == 7) {
                focusDay = 1;
                downloadWeek = currentWeek + 1;
            } else {
                downloadWeek = currentWeek;
            }
        }
    }

    private class schemaSelect extends Thread {
        public void run() {

            schemaTimeRefresh onSelectTimeSync = new schemaTimeRefresh();
            onSelectTimeSync.start();
            try {
                onSelectTimeSync.join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            String nameOfFile = "id:" + id_number + "week:" + downloadWeek + "_" + schemaFileName;
            schemaFile = new File(getFilesDir() + "/" + nameOfFile);
            String downloadID = id_number;

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
        public void run() {

            schemaTimeRefresh onSelectTimeSync = new schemaTimeRefresh();
            onSelectTimeSync.start();
            try {
                onSelectTimeSync.join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
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

                if (!schemaFile.exists()) {
                    //String[] druvor = sb.toString().split("%day%");
                    String[] druvor = tempSchema.split("%day%");

                    final int[] relWeekIDS = {
                            R.id.relativt_schema1,
                            R.id.relativt_schema2,
                            R.id.relativt_schema3,
                            R.id.relativt_schema4,
                            R.id.relativt_schema5
                    };
                    final int[] relDayLay = {
                            R.layout.monday_frag,
                            R.layout.tuesday_frag,
                            R.layout.wednesday_frag,
                            R.layout.thursday_frag,
                            R.layout.friday_frag
                    };
                    final int[] relDayIDS = {
                            R.id.relative_layout_monday,
                            R.id.relative_layout_tuesday,
                            R.id.relative_layout_wednesday,
                            R.id.relative_layout_thursday,
                            R.id.relative_layout_friday,
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

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View pagerItemInflater = inflater.inflate(relDayLay[i], null);

                        RelativeLayout schema_space = (RelativeLayout) findViewById(relWeekIDS[i]);
                        RelativeLayout schema_dayce = (RelativeLayout) pagerItemInflater.findViewById(relDayIDS[i]);

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
                                    lektion.setBackgroundResource(R.drawable.rect_view_vecka_day_header);
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
                                    linearDay.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorSchemaViewDayLektionBackground));

                                    TextView dayTop = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams TopDayParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(15 * getResources().getDimension(R.dimen.dp_unit)));
                                    dayTop.setLayoutParams(TopDayParams);
                                    dayTop.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                                    dayTop.setTextSize(12);
                                    dayTop.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorSchemaViewDayLektionText));
                                    dayTop.setText(StartTid);
                                    linearDay.addView(dayTop);

                                    TextView dayMid = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams MidDayParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT); //(int)(((sluttid - starttid) * 90 - 30) * getResources().getDimension(R.dimen.dp_unit))
                                    dayMid.setLayoutParams(MidDayParams);
                                    dayMid.setGravity(Gravity.CENTER);
                                    dayMid.setTextSize(20);
                                    dayMid.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorSchemaViewDayLektionText));
                                    dayMid.setText(öpö);
                                    linearDay.addView(dayMid);

                                    TextView dayBot = new TextView(getBaseContext());
                                    LinearLayout.LayoutParams BotDayParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int)(15 * getResources().getDimension(R.dimen.dp_unit)));
                                    dayBot.setLayoutParams(BotDayParams);
                                    dayBot.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                                    dayBot.setTextSize(12);
                                    dayBot.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorSchemaViewDayLektionText));
                                    dayBot.setText(SlutTid);
                                    linearDay.addView(dayBot);

                                    linearDay.setBackgroundResource(R.drawable.rect_view_vecka_day_header);

                                    schema_dayce.addView(linearDay);

                                }
                            } catch (NumberFormatException | NullPointerException e) {
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
            int size;

            try {
                doc = Jsoup.connect("http://skolmaten.se/norra-real/rss/").get();
                title = doc.select("description").text();
                size = doc.select("description").size();
                realDeal = "";

                for (int i = 0; i < 6 ; i++) {
                    
                    if (i != 5) {

                        Elements description = doc.select("description:eq(2)");

                        Element singlePiece = description.get(i);

                        realDeal = realDeal + "/maq1/" + singlePiece.text();
                    } else {

                        String[] temporary = realDeal.split("<br/>");
                        realDeal = "";

                        for (int j = 0; j < temporary.length ; j++) {

                            if (j != temporary.length - 1) {

                                realDeal = realDeal + temporary[j] + "\n\n";
                            } else {

                                realDeal = realDeal + temporary[j];
                            }

                            
                        }
                        
                    }
                    
                }

                if (size == 0 ){

                    realDeal = "/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/1";

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

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

        final TextView ratingtext = (TextView) findViewById(R.id.rating_text);
        final RatingBar ratingbarOutput = (RatingBar) findViewById(R.id.rating_output_view);

        LinearLayout monday = (LinearLayout) findViewById(R.id.matsedel_monday);
        LinearLayout tuesday = (LinearLayout) findViewById(R.id.matsedel_tuesday);
        LinearLayout wednesday = (LinearLayout) findViewById(R.id.matsedel_wednesday);
        LinearLayout thursday = (LinearLayout) findViewById(R.id.matsedel_thursday);
        LinearLayout friday = (LinearLayout) findViewById(R.id.matsedel_friday);

        TextView dagens = (TextView) findViewById(R.id.dagens_mat);

        LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

        progressLayout.setVisibility(View.GONE);

        mat = realDeal.split("/maq1/");

        if (mat.length == 6) {

            if (foodDay < 2) {

                monday.setVisibility(View.VISIBLE);
                textView1.setText(mat[1]);

            } else {

                monday.setVisibility(View.GONE);
            }

            if (foodDay < 3) {

                tuesday.setVisibility(View.VISIBLE);
                textView2.setText(mat[3-foodDay]);

            } else {

                tuesday.setVisibility(View.GONE);
            }

            if (foodDay < 4) {
                wednesday.setVisibility(View.VISIBLE);
                textView3.setText(mat[4-foodDay]);

            } else {

                wednesday.setVisibility(View.GONE);
            }

            if (foodDay < 5) {
                thursday.setVisibility(View.VISIBLE);
                textView4.setText(mat[5-foodDay]);

            } else {

                thursday.setVisibility(View.GONE);
            }

            if (foodDay < 6) {
                friday.setVisibility(View.VISIBLE);
                textView5.setText(mat[6-foodDay]);

            } else {

                friday.setVisibility(View.GONE);
            }

            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {

                    jsonParseMatrating(object);

                    ratingtext.setText(String.valueOf(matsedelrating));
                    ratingbarOutput.setStepSize( 0.01f);
                    ratingbarOutput.setRating((float)matsedelrating);

                }
            } ).execute("https://spreadsheets.google.com/tq?key=1KWnx2XtVrc229M2ixsgu5xXkpaxkHwZMf0nZdElxWRM");


            dagens.setText(mat[1]);

        } else {

            textView1.setText(mat[1]);
            textView2.setText(mat[2]);
            textView3.setText(mat[3]);
            textView4.setText(mat[4]);
            textView5.setText(mat[5]);

            dagens.setText(mat[1]);
        }
    }

    public void röstning(View view) {

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        view.startAnimation(anim_button_click);
        final String matrating = String.valueOf(ratingBar.getRating()).replace(".",",");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                postMatsedelRatingData(matrating);

            }
        });
        t.start();

    }

    private void jsonParseMatrating(JSONObject object) {

        final TextView ratingtext = (TextView) findViewById(R.id.rating_text);

        try {
            JSONArray rows = object.getJSONArray("rows");
            JSONObject row = rows.getJSONObject(0);
            JSONArray columns = row.getJSONArray("c");
            matsedelrating = columns.getJSONObject(6).getDouble("v");


            ratingtext.setText(String.valueOf(matsedelrating));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void postFormRegisterData(String Namn, String Efternamn, String Mobil, String Stad, String Födsel, int gender) {

        String logingender = genderStrings[gender];
        try {
            String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfbgeZ2UMpuHDqSagZc2u39tjNhzmEF0toBYRsfFIyz6psiew/formResponse";
            HttpRequest mReq = new HttpRequest();

            String data = "entry.669832305=" + Namn + "&"
                    + "entry.1465471991=" + Efternamn + "&"
                    + "entry.1588314371=" + Mobil + "&"
                    + "entry.1054214164=" + Stad + "&"
                    + "entry.1765914386=" + Födsel + "&" //1999-11-06
                    + "entry.1179920717=" + logingender;

            String response = mReq.sendPost(fullUrl, data);
            Log.i(myTag, response);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postMatsedelRatingData(String matrating) {


        try {
            String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSe7dUHRpnk226B_XWINpGxTJYrMuBjOqSQ7mAc1tDJgg36iJg/formResponse";
            HttpRequest mReq = new HttpRequest();

            String data = "entry.1218691264=" + id_number + "&"
                    + "entry.1809891164=" + matrating ;

            String response = mReq.sendPost(fullUrl, data);
            Log.i(myTag, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    public void fidget_spinner(){

        Spinner spinnerLoginGender = (Spinner) findViewById(R.id.fidget_spinner);
        Spinner spinnerSchemaType = (Spinner) findViewById(R.id.typ_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapterGender = ArrayAdapter.createFromResource(this, R.array.gender_array, R.layout.spinner_login_layout);
        spinnerLoginGender.setAdapter(adapterGender);

        ArrayAdapter adapterTyp = ArrayAdapter.createFromResource(this, R.array.typ_array, R.layout.spinner_schema_layout);
        spinnerSchemaType.setAdapter(adapterTyp);

// Specify the layout to use when the list of choices appears
        adapterGender.setDropDownViewResource(R.layout.spinner_login_dropdown_layout);
        adapterTyp.setDropDownViewResource(R.layout.spinner_schema_dropdown_layout);
// Apply the adapter to the spinner
        spinnerLoginGender.setAdapter(adapterGender);
        spinnerSchemaType.setAdapter(adapterTyp);

        spinnerLoginGender.setSelection(genderInteger);

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

            final int MEGABYTE = 1024 * 1024;

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
