package calculinc.google.httpssites.skol_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.Line;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.DialogFragment;

import calculinc.google.httpssites.skol_app.Matsedel.FirstFragment;
import calculinc.google.httpssites.skol_app.Matsedel.SecondFragment;
import calculinc.google.httpssites.skol_app.days.Friday;
import calculinc.google.httpssites.skol_app.days.Monday;
import calculinc.google.httpssites.skol_app.days.Thursday;
import calculinc.google.httpssites.skol_app.days.Tuesday;
import calculinc.google.httpssites.skol_app.days.Wednesday;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Menu menu1;

    String id_number;
    String fyra_sista;

    boolean loginNameAccepted = false;
    boolean loginSurnameAccepted = false;
    boolean loginPhoneAccepted = false;
    boolean loginPersonalIdAccepted = false;
    boolean loginPassCodeAccepted = false;

    final String myTag = "DocsUpload";

    double matsedelrating;
    double matsedelratingAmountOfVotes;
    double matsedelratingTotal;
    String votingID;
    String downloaddatum = "";
    String personalVote;

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
    boolean loggenIn = false;
    boolean DayPref;
    File DayPrefFile;

    ArrayList<String> urlCheck = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    final String[] genderStrings = {
            "Kvinna",
            "Man",
            "Annat", "Annat", "Annat"
    };

    String LoginDataBaseKey = "11SYOpe7-x_N2xQtjjgs7nUD9t7nRRBvp59O694rrmHc";
    String MatvoteDataBaseKey = "1KWnx2XtVrc229M2ixsgu5xXkpaxkHwZMf0nZdElxWRM";
    String CommercialDataBaseKey = "1_-tmo7u4h0Z57coNfbzX945wrHy6g0ixdTgy-cjaMJE";
    String[] mat;
    String dagensMat;
    String foodMenu = "/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln";
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

        navigationView.setCheckedItem(R.id.nav_nyheter);

        schemaTimeRefresh bootUpTimeSync = new schemaTimeRefresh();
        bootUpTimeSync.start();

        getSavedLoginContent();

        viewFuckingPager();
        viewPager();
        recyclerViewAdapter();
        theSwtich();

        Log.i(myTag, "OnCreate()");

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final EditText passCode = (EditText) findViewById(R.id.passcode);
        name.addTextChangedListener(nameTextWatcher);
        surname.addTextChangedListener(surnameTextWatcher);
        phone.addTextChangedListener(phoneTextWatcher);
        personal_id.addTextChangedListener(personalIdTextWatcher);
        passCode.addTextChangedListener(passCodeTextWatcher);

        recyclerView.smoothScrollToPosition(0);
        start();
    }

    public void start() {

        if (loggenIn) {

            nyheterSelect click = new nyheterSelect();
            click.start();

        } else {

            ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
            vf.setDisplayedChild(5);
        }

    }

    public void freshLogin(View view) {

        final ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setCheckedItem(R.id.nav_login);
        vf.setDisplayedChild(4);

    }

    public void infoClick(View view) {

        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        view.startAnimation(anim_button_click);

        DialogFragment newFragment = new InfoBox();
        newFragment.show(getSupportFragmentManager(), "vafan");


    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            nameCheck(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher surnameTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            surnameCheck(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher phoneTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            phoneCheck(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher personalIdTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            personalIdCheck(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    private TextWatcher passCodeTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            passCodeCheck(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    public void nameCheck(String content){
        loginNameAccepted = content.length() >= 1;

        final View view = (View) findViewById(R.id.checkbox_name);
        if (loginNameAccepted) {
            view.setBackgroundResource(R.drawable.login_ok);
            //Update indicator here
        } else {
            view.setBackgroundResource(R.drawable.login_not_ok);
            //Update indicator here
        }
    }

    public void surnameCheck(String content){
        loginSurnameAccepted = content.length() >= 1;

        final View view = (View) findViewById(R.id.checkbox_surname);
        if (loginSurnameAccepted) {
            view.setBackgroundResource(R.drawable.login_ok);
            //Update indicator here
        } else {
            view.setBackgroundResource(R.drawable.login_not_ok);
            //Update indicator here
        }
    }

    public void phoneCheck(String content){
        loginPhoneAccepted = content.length() == 10;

        final View view = (View) findViewById(R.id.checkbox_telephone);
        if (loginPhoneAccepted) {
            view.setBackgroundResource(R.drawable.login_ok);
            //Update indicator here
        } else {
            view.setBackgroundResource(R.drawable.login_not_ok);
            //Update indicator here
        }
    }

    public void personalIdCheck(String content){
        loginPersonalIdAccepted = content.length() == 6;
        final View view = (View) findViewById(R.id.checkbox_personalid);
        if (loginPersonalIdAccepted) {
            view.setBackgroundResource(R.drawable.login_ok);
            //Update indicator here
        } else {
            view.setBackgroundResource(R.drawable.login_not_ok);
            //Update indicator here
        }
    }

    public void passCodeCheck(String content){
        loginPassCodeAccepted = content.length() == 4;
        final View view = (View) findViewById(R.id.checkbox_passcode);
        if (loginPassCodeAccepted) {
            view.setBackgroundResource(R.drawable.login_ok);
            //Update indicator here
        } else {
            view.setBackgroundResource(R.drawable.login_not_ok);
            //Update indicator here
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
                    return new SecondFragment();
                case 1:
                    return new FirstFragment();
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

    public void recyclerViewAdapter() {

        // set up the RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.nyheter_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

    }

    public void getSavedLoginContent() {
        loginFile = new File(getFilesDir() + "/" + loginFileName);
        loggenIn = loginFile.exists();
        try {
            if (loggenIn) {
                final Button LoginButt = (Button) findViewById(R.id.login_button);
                final Button LogoutButt = (Button) findViewById(R.id.logout_button);
                LoginButt.setVisibility(View.GONE);
                LogoutButt.setVisibility(View.VISIBLE);

                FileInputStream fis = openFileInput(loginFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                String[] loginParams = sb.toString().split("%");
                id_number = loginParams[3];
                fyra_sista = loginParams[5];
                votingID = id_number + loginParams[0] + loginParams[1];

                EditText name = (EditText) findViewById(R.id.name1);
                EditText surname = (EditText) findViewById(R.id.name2);
                EditText phone = (EditText) findViewById(R.id.mobile_number);
                EditText personalId = (EditText) findViewById(R.id.personalid);
                EditText novacode = (EditText) findViewById(R.id.nova_code);
                EditText passcode = (EditText) findViewById(R.id.passcode);

                name.setText(loginParams[0]);
                surname.setText(loginParams[1]);
                phone.setText(loginParams[2]);
                personalId.setText(id_number);
                novacode.setText(fyra_sista);
                passcode.setText(loginParams[4]);

                nameCheck(loginParams[0]);
                surnameCheck(loginParams[1]);
                phoneCheck(loginParams[2]);
                personalIdCheck(id_number);
                passCodeCheck(loginParams[4]);
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
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final EditText passcode = (EditText) findViewById(R.id.passcode);

        final String Namn = name.getText().toString();
        final String Efternamn = surname.getText().toString();
        final String Mobil = phone.getText().toString();
        id_number = personal_id.getText().toString();
        final String Code = passcode.getText().toString() + ".0";
        final String Datum = id_number;
        votingID = id_number + Namn + Efternamn;

        if (loginNameAccepted && loginSurnameAccepted && loginPhoneAccepted && loginPersonalIdAccepted && loginPassCodeAccepted) {
            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {
                    boolean logSucc = false;
                    try {
                        JSONArray rows = object.getJSONArray("rows");
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject row = rows.getJSONObject(i);
                            JSONArray person = row.getJSONArray("c");
                            String JsonNamn = person.getJSONObject(1).getString("v");
                            String JsonEfternamn = person.getJSONObject(2).getString("v");
                            char[] M = person.getJSONObject(3).getString("v").toCharArray();
                            String JsonMobil = "" + M[0] + M[1] + M[2] + M[4] + M[5] + M[6] + M[7] + M[8] + M[9] + M[10];
                            char[] D = person.getJSONObject(5).getString("f").toCharArray();
                            String JsonDatum = "" + D[2] + D[3] + D[5] + D[6] + D[8] + D[9];
                            String JsonCode = person.getJSONObject(7).getString("v");

                            if (JsonNamn.equals(Namn) && JsonEfternamn.equals(Efternamn) && JsonMobil.equals(Mobil) && JsonDatum.equals(Datum) && JsonCode.equals(Code)) {
                                logSucc = true;
                                i = rows.length();
                            }
                        }
                        if (logSucc){
                            loginSucces();
                        } else {
                            loginFail();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        loginFail();
                    }
                }
            } ).execute("https://spreadsheets.google.com/tq?key=" + LoginDataBaseKey);

        }
    }

    public void loginSucces() {
        final Button LoginButt = (Button) findViewById(R.id.login_button);
        final Button LogoutButt = (Button) findViewById(R.id.logout_button);
        LoginButt.setVisibility(View.GONE);
        LogoutButt.setVisibility(View.VISIBLE);

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final EditText fyrasista = (EditText) findViewById(R.id.nova_code);
        final EditText passcode = (EditText) findViewById(R.id.passcode);

        final String Namn = name.getText().toString();
        final String Efternamn = surname.getText().toString();
        final String Mobil = phone.getText().toString();
        id_number = personal_id.getText().toString();
        fyra_sista = fyrasista.getText().toString();
        final String passCode = passcode.getText().toString();

        StringBuilder loginContent = new StringBuilder();{
            loginContent.append(Namn);
            loginContent.append("%");
            loginContent.append(Efternamn);
            loginContent.append("%");
            loginContent.append(Mobil);
            loginContent.append("%");
            loginContent.append(id_number);
            loginContent.append("%");
            loginContent.append(passCode);
            loginContent.append("%");
            loginContent.append(fyra_sista);
        }

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(loginFileName,MODE_PRIVATE);
            outputStream.write(loginContent.toString().getBytes());
            outputStream.close();
            loggenIn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginFail() {

    }

    public void logoutSubmit (View view) {
        final Button LoginButt = (Button) findViewById(R.id.login_button);
        final Button LogoutButt = (Button) findViewById(R.id.logout_button);
        LoginButt.setVisibility(View.VISIBLE);
        LogoutButt.setVisibility(View.GONE);

        loginFile.delete();
        loggenIn = false;
    }

    public void registerSubmit (View view) {
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);
        view.startAnimation(anim_button_click);

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);

        final String Namn = name.getText().toString();
        final String Efternamn = surname.getText().toString();
        final String Mobil = phone.getText().toString();
        id_number = personal_id.getText().toString();
        final String datum;{
            char[] siffra = id_number.toCharArray();
            if (siffra[0] == '9') {
                datum = "19" + siffra[0] + siffra[1] + "-" + siffra[2] + siffra[3] + "-" + siffra[4] + siffra[5];
            } else {
                datum = "20" + siffra[0] + siffra[1] + "-" + siffra[2] + siffra[3] + "-" + siffra[4] + siffra[5];
            }
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //postFormRegisterData(Namn, Efternamn, Mobil, datum);
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
            drawer.closeDrawer(GravityCompat.START);
            if (loggenIn) {
                vf.setDisplayedChild(0);
                nyheterSelect click = new nyheterSelect();
                click.start();
            } else {vf.setDisplayedChild(5);}

        } else if (id == R.id.nav_schema) {

            toolbar.setTitle(R.string.Tab_2);
            drawer.closeDrawer(GravityCompat.START);
            if (loggenIn) {
                vf.setDisplayedChild(1);
                schemaSelect click = new schemaSelect();
                click.start();
            } else {vf.setDisplayedChild(5);}

        } else if (id == R.id.nav_matsedel) {

            toolbar.setTitle(R.string.Tab_3);
            drawer.closeDrawer(GravityCompat.START);
            if (loggenIn) {
                vf.setDisplayedChild(2);
                matsedelSelect click = new matsedelSelect();
                click.start();
            } else {vf.setDisplayedChild(5);}

        } else if (id == R.id.nav_laxor) {

            toolbar.setTitle(R.string.Tab_4);
            drawer.closeDrawer(GravityCompat.START);
            if (loggenIn) {
                vf.setDisplayedChild(3);
                läxorSelect click = new läxorSelect();
                click.start();
            } else {vf.setDisplayedChild(5);}

        } else if (id == R.id.nav_login) {

            toolbar.setTitle(R.string.Tab_5);
            drawer.closeDrawer(GravityCompat.START);
            vf.setDisplayedChild(4);
            loginSelect click = new loginSelect();
            click.start();
        }

        return true;
    }

    private class nyheterSelect extends Thread {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override

                public void run() {
                    newsFeedDownload H = new newsFeedDownload();
                    H.start();
                }
            });


        }
    }

    private class schemaSelect extends Thread {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    theSwtich();
                    fidget_spinner();
                }
            });

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

    private class matsedelSelect extends Thread {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            getVoteRight G = new getVoteRight();
            G.start();

            foodSchemeDownload H = new foodSchemeDownload();
            H.start();
            try {
                H.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            applyMenu();
        }
    }

    private class läxorSelect extends Thread {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            schemaArrayFixer lloldawd = new schemaArrayFixer();
            lloldawd.start();
        }
    }

    private class loginSelect extends Thread {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fidget_spinner();
                }
            });


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

                        String StartTid = "";
                        double starttid = 0;
                        String SlutTid;
                        double sluttid;
                        String öpö = "";

                        for (int j = 0; j < desert.length; j++) {
                            int status = j % 3;
                            try {
                                if (status == 0) {
                                    //lite matte här
                                    StartTid = desert[j];
                                    String[] time = StartTid.split(":");
                                    starttid = Double.parseDouble(time[0]) - 8 + (Double.parseDouble(time[1])) / 60;

                                }
                                if (status == 1) {
                                    //hämta lektionstext
                                    öpö = desert[j];
                                }
                                if (status == 2) {
                                    //lite mer matte
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

                                    /**LinearLayout linearDay = new LinearLayout(getBaseContext());
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

                                    schema_dayce.addView(linearDay); **/

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

    private class newsFeedDownload extends Thread {
        public void run() {
            Document doc;

            try {

                doc = Jsoup.connect("http://instagram.com/norraselevkar/").get();
                Elements script = doc.select("script");

                Element picUrls = script.get(2);

                String[] rawText = picUrls.toString().split(" ");

                ArrayList<String> imageUrls = new ArrayList<>();
                ArrayList<String> caption = new ArrayList<>();
                ArrayList<String> commercialData = new ArrayList<>();

                String temp;
                StringBuilder sb = new StringBuilder();

                int status = 0;

                for (int i = 0; i < rawText.length; i++) {

                    if(rawText[i].equals("\"display_src\":") ) {

                        imageUrls.add(rawText[i +1 ].replace("\"","").replace(",",""));
                    }


                    if (rawText[i].equals("\"comments\":")) {

                        status = 0;

                        temp = sb.toString().replace("\",","");

                        temp = StringEscapeUtils.unescapeJava(temp);

                        caption.add(temp.substring(1));

                        sb.setLength(0);

                    }


                    if (status == 1) {

                        sb.append(rawText[i]);
                        sb.append(" ");

                    }

                    if (rawText[i].equals("\"caption\":")) {

                        status = 1;
                    }

                }

                if ( !imageUrls.equals(urlCheck) ) {

                    urlCheck = imageUrls;
                    applyNewsFeed(imageUrls, caption, commercialData);
                    commercialFeedDownload(imageUrls, caption, commercialData);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private void commercialFeedDownload(final ArrayList<String> imageUrls, final ArrayList<String> caption, final ArrayList<String> commercialData) {

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {

                try {
                    JSONArray rows = object.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {

                        commercialData.clear();

                        JSONObject row = rows.getJSONObject(i);
                        JSONArray columns = row.getJSONArray("c");
                        String urlString = columns.getJSONObject(0).getString("v");
                        String name = columns.getJSONObject(1).getString("v");
                        String caption = columns.getJSONObject(2).getString("v");
                        String color = columns.getJSONObject(3).getString("v");

                        commercialData.add(urlString);
                        commercialData.add(name);
                        commercialData.add(caption);
                        commercialData.add(color);
                    }

                    applyNewsFeed(imageUrls, caption, commercialData );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } ).execute("https://spreadsheets.google.com/tq?key=" + CommercialDataBaseKey);

    }

    public void applyNewsFeed(final ArrayList<String> imageUrls, final ArrayList<String> caption, final ArrayList<String> commercialData) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter = new MyRecyclerViewAdapter(getApplicationContext(), caption, imageUrls, commercialData );
                recyclerView.setAdapter(adapter);

                final LinearLayout progressbar = (LinearLayout) findViewById(R.id.nyheter_progress_bar);
                progressbar.setVisibility(View.GONE);

            }
        });

    }

    private class foodSchemeDownload extends Thread {
        public void run() {
            Document doc;
            int size;

            try {

                if (GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                    doc = Jsoup.connect("https://skolmaten.se/norra-real/rss/days/?limit=7").get();
                } else {

                    doc = Jsoup.connect("https://skolmaten.se/norra-real/rss/weeks/").get();
                }

                size = doc.select("description").size();
                foodMenu = "";

                for (int i = 0; i < 6 ; i++) {

                    if (i != 5) {

                        Elements description = doc.select("description:eq(2)");

                        Element singlePiece = description.get(i);

                        foodMenu = foodMenu + "/maq1/" + singlePiece.text();
                    } else {

                        String[] temporary = foodMenu.split("<br/>");
                        foodMenu = "";

                        for (int j = 0; j < temporary.length ; j++) {

                            if (j != temporary.length - 1) {

                                foodMenu = foodMenu + temporary[j] + "\n\n";
                            } else {

                                foodMenu = foodMenu + temporary[j];
                            }

                        }

                    }

                }

                if (size == 0 ){

                    foodMenu = "/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/1";

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private class getVoteRight extends Thread {
        public void run() {
            new DownloadWebpageTask(new AsyncResult() {
                @Override
                public void onResult(JSONObject object) {

                    String matVoteFileName = "mat.txt";
                    File matfil = new File(getFilesDir() + "/" + matVoteFileName);
                    String filedatum = "";

                    //Temporary code for animation-testing
                    //matfil.delete();
                    //Delete the above when necessary

                    try {
                        FileInputStream fis = openFileInput(matVoteFileName);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader bufferedReader = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line);
                        }
                        String[] data = sb.toString().split("%");
                        filedatum = data[0];
                        personalVote = data[1];

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    downloaddatum = filedatum;
                    try {
                        JSONArray rows = object.getJSONArray("rows");
                        JSONObject row = rows.getJSONObject(0);
                        JSONArray columns = row.getJSONArray("c");
                        matsedelrating = columns.getJSONObject(0).getDouble("v");
                        matsedelratingTotal = columns.getJSONObject(1).getDouble("v");
                        matsedelratingAmountOfVotes = columns.getJSONObject(2).getDouble("v");
                        downloaddatum = columns.getJSONObject(4).getString("v") + "/" + columns.getJSONObject(6).getString("v") + "/" + columns.getJSONObject(7).getString("v");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final boolean qwerty = !downloaddatum.equals(filedatum);

                    final ViewFlipper matVf = (ViewFlipper) findViewById(R.id.mat_vf);

                    if (qwerty) {
                        matVf.setDisplayedChild(1);
                    } else {
                        matVf.setDisplayedChild(2);
                    }

                    final TextView ratingText = (TextView) findViewById(R.id.rating_text);
                    final RatingBar ratingBarOutput = (RatingBar) findViewById(R.id.rating_output_view);

                    ratingBarOutput.setRating((float)matsedelrating);
                    ratingText.setText(String.valueOf(matsedelrating));

                    final TextView statistik = (TextView) findViewById(R.id.statistik);
                    statistik.setText(personalVote);

                    /**
                     final TextView klickahär = (TextView) findViewById(R.id.klicka_rösta_mat);
                     final LinearLayout matbelt = (LinearLayout) findViewById(R.id.matsedel_belt);

                     if (qwerty) {

                     klickahär.setVisibility(View.GONE);
                     ratingText.setVisibility(View.VISIBLE);
                     ratingBarOutput.setVisibility(View.VISIBLE);
                     matbelt.setVisibility(View.GONE);

                     } else {

                     klickahär.setVisibility(View.GONE);
                     ratingText.setVisibility(View.VISIBLE);
                     ratingBarOutput.setVisibility(View.VISIBLE);
                     matbelt.setVisibility(View.VISIBLE);

                     }**/

                }
            } ).execute("https://spreadsheets.google.com/tq?key=" + MatvoteDataBaseKey );
        }
    }

    public void applyMenu() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {

                    final int[] matdayBox = {
                            R.id.matsedel_monday,
                            R.id.matsedel_tuesday,
                            R.id.matsedel_wednesday,
                            R.id.matsedel_thursday,
                            R.id.matsedel_friday
                    };

                    final int[] matdayTitle = {
                            R.id.mat_title_måndag,
                            R.id.mat_title_tisdag,
                            R.id.mat_title_onsdag,
                            R.id.mat_title_torsdag,
                            R.id.mat_title_fredag
                    };

                    final int[] matdayText = {
                            R.id.test_text1,
                            R.id.test_text2,
                            R.id.test_text3,
                            R.id.test_text4,
                            R.id.test_text5
                    };

                    LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

                    progressLayout.setVisibility(View.GONE);

                    mat = foodMenu.split("/maq1/");

                    String[] charSwap = { "Å", "Ä", "Ö", "å", "ä", "ö"};
                    String[] charSwap2 = { "%c3%85", "%c3%84", "%c3%96", "%c3%a5", "%c3%a4", "%c3%b6"};

                    dagensMat = mat[foodDay ].replace("\n\n","\n");

                    for (int i = 0; i < 6 ; i++) {

                        dagensMat = dagensMat.replace( charSwap[i] , charSwap2[i] );
                    }

                    for (int i = 0; i < 5 ; i++) {

                        TextView foodtext = (TextView) findViewById(matdayText[i]);
                        foodtext.setText(mat[i + 1]);

                    }

                    if( GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY )
                    {
                        TextView Dagens = (TextView) findViewById(matdayTitle[foodDay - 1]);
                        LinearLayout dagensMatBox = (LinearLayout) findViewById(matdayBox[foodDay-1]);
                        TextView foodtext = (TextView) findViewById(matdayText[foodDay-1]);

                        Dagens.setText("Dagens Mat");
                        Dagens.setTextColor(getResources().getColor(R.color.colorMatsedelHighLight));
                        foodtext.setTextColor(getResources().getColor(R.color.colorMatsedelHighLight));
                        dagensMatBox.setBackgroundResource(R.drawable.rect_matsedel_days_highlight);
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void röstning(View view) {

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        view.startAnimation(anim_button_click);
        final String matrating = String.valueOf(ratingBar.getRating()).replace(".",",");

        String[] charSwap = { "Å", "Ä", "Ö", "å", "ä", "ö"};
        String[] charSwap2 = { "%c3%85", "%c3%84", "%c3%96", "%c3%a5", "%c3%a4", "%c3%b6"};

        for (int i = 0; i < 6 ; i++) {
            votingID = votingID.replace( charSwap[i] , charSwap2[i] );
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                postMatsedelRatingData(matrating);

            }
        });
        t.start();

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

            String data = "entry.1218691264=" + votingID + "&"
                    + "entry.1809891164=" + matrating + "&"
                    + "entry.83499785=" + dagensMat;

            String response = mReq.sendPost(fullUrl, data);
            Log.i(myTag, response);

            //File update
            final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
            String separator = "%";
            String matVoteFileName = "mat.txt";
            File matfil = new File(getFilesDir() + "/" + matVoteFileName);
            matfil.delete();
            try {
                FileOutputStream outputStream = openFileOutput(matVoteFileName,MODE_PRIVATE);
                outputStream.write(downloaddatum.getBytes());
                outputStream.write(separator.getBytes());
                outputStream.write(String.valueOf(ratingBar.getRating()).getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //UI code block
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final RatingBar ratingBarOutput = (RatingBar) findViewById(R.id.rating_output_view);
                    final TextView ratingText = (TextView) findViewById(R.id.rating_text);
                    final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
                    final TextView statistik = (TextView) findViewById(R.id.statistik);

                    float tempChange = (float)((matsedelratingTotal + ratingBar.getRating()) / (matsedelratingAmountOfVotes + 1.0D));

                    ratingBarOutput.setRating(tempChange);
                    ratingText.setText(String.valueOf(tempChange));

                    //On successful vote
                    final ViewFlipper matVf = (ViewFlipper) findViewById(R.id.mat_vf);

                    matVf.setDisplayedChild(2);
                    statistik.setText(String.valueOf(ratingBar.getRating()));


                    /**
                    final TextView klickahär = (TextView) findViewById(R.id.klicka_rösta_mat);
                    final LinearLayout matbelt = (LinearLayout) findViewById(R.id.matsedel_belt);
                    final LinearLayout viewpagerlayout = (LinearLayout) findViewById(R.id.layout_pager);

                    klickahär.setVisibility(View.GONE);
                    ratingText.setVisibility(View.VISIBLE);
                    ratingBarOutput.setVisibility(View.VISIBLE);
                    matbelt.setVisibility(View.GONE);

                    LinearLayout.LayoutParams paramsBlank2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, Math.round(getResources().getDimension(R.dimen.matchange)));
                    viewpagerlayout.setLayoutParams(paramsBlank2);
                     **/
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void spammaSverigesElevkårer(View view) {

        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);

        view.startAnimation(anim_button_click);

        String name;

        for (int i = 0; i < 25 ; i++) {

            name = "";

            for (int j = 0; j < 10; j++) {

                if( j == 0){
                    Random rand = new Random();
                    int n = rand.nextInt(91 - 65) + 65;
                    name += (char)n;
                }

                if( j < 5){

                    Random rand = new Random();
                    int n = rand.nextInt(123 - 97) + 97;

                    name += (char)n;
                }

                if( j == 5){
                    Random rand = new Random();
                    int n = rand.nextInt(91 - 65) + 65;
                    name += "+" + (char)n;
                }

                if( j > 5){

                    Random rand = new Random();
                    int n = rand.nextInt(123 - 97) + 97;

                    name += (char)n;
                }

                if( j == 9){

                    name += "sson";
                }

            }

            try {

                String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSeNNzgiaKPlpcedoqK9ZTFGa6oHhtsRdUiPLvAK5ucs1UE3xw/formResponse";
                HttpRequest mReq = new HttpRequest();

                String data = "entry.1357747958=Norra+Real+Elevkår&entry.60332854=Årets+Elevkår&" +
                        "entry.1413369446=" + name + "&entry.365068267=Stora+framsteg+inom+IT-fronten...";

                String response = mReq.sendPost(fullUrl, data);
                Log.i(myTag, response);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void fidget_spinner(){

        Spinner spinnerSchemaType = (Spinner) findViewById(R.id.typ_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter adapterTyp = ArrayAdapter.createFromResource(this, R.array.typ_array, R.layout.spinner_schema_layout);
        spinnerSchemaType.setAdapter(adapterTyp);

// Specify the layout to use when the list of choices appears
        adapterTyp.setDropDownViewResource(R.layout.spinner_schema_dropdown_layout);

// Apply the adapter to the spinner
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
