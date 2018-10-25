package calculinc.google.httpssites.skol_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.apache.commons.lang3.StringEscapeUtils;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.DialogFragment;

import calculinc.google.httpssites.skol_app.Matsedel.FirstFragment;
import calculinc.google.httpssites.skol_app.Matsedel.SecondFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String id_number;
    String fyra_sista;

    boolean loginNameAccepted = false;
    boolean loginSurnameAccepted = false;
    boolean loginPhoneAccepted = false;
    boolean loginPersonalIdAccepted = false;
    boolean loginPassCodeAccepted = false;

    final String myTag = "DocsUpload";

    double matsedelrating = 0;
    int matsedelratingAmountOfVotes = 0;
    double matsedelratingTotal = 0;
    double matsedelMedian = 0;
    double matsedelStdev = 0;

    String personalVoteNumber = "";
    String votingID;
    String downloaddatum = "";
    String personalVote = "";

    int currentWeek = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    int currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
    int currentHour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
    int currentMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE);

    int downloadWeek, focusDay;

    String schemaFileName = "Nova.txt";
    File schemaFile;
    String loginFileName = "login.txt";
    File loginFile;
    boolean downloadSuccess;
    boolean loggenIn = false;

    ArrayList<String> urlCheck = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    String LoginDataBaseKey = "11SYOpe7-x_N2xQtjjgs7nUD9t7nRRBvp59O694rrmHc";
    String MatvoteDataBaseKey = "1KWnx2XtVrc229M2ixsgu5xXkpaxkHwZMf0nZdElxWRM";
    String CommercialDataBaseKey = "1_-tmo7u4h0Z57coNfbzX945wrHy6g0ixdTgy-cjaMJE";
    String[] mat;
    String dagensMat;
    String foodMenu = "/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln/maq1/För lång kö till matsalen. Kunde inte se matsedeln";

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
        //viewPager();
        recyclerViewAdapter();
        //theSwtich();

        urlCheck.add("1");
        urlCheck.add("2");

        Log.i(myTag, "OnCreate()");

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final EditText passCode = (EditText) findViewById(R.id.passcode);
        EditText novaCode = (EditText) findViewById(R.id.nova_code);
        name.addTextChangedListener(nameTextWatcher);
        surname.addTextChangedListener(surnameTextWatcher);
        phone.addTextChangedListener(phoneTextWatcher);
        personal_id.addTextChangedListener(personalIdTextWatcher);
        passCode.addTextChangedListener(passCodeTextWatcher);
        novaCode.addTextChangedListener(novaTextWatcher);

        recyclerView.smoothScrollToPosition(0);
        start();

        TextView t2 = (TextView) findViewById(R.id.register_link);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
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

    private TextWatcher novaTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            novaCodeCheck(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    public void nameCheck(String content){
        loginNameAccepted = content.length() >= 1;

        final View view = findViewById(R.id.checkbox_name);
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

        final View view = findViewById(R.id.checkbox_surname);
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

        final View view = findViewById(R.id.checkbox_telephone);
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

        final View view =  findViewById(R.id.checkbox_personalid);
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

        final View view = findViewById(R.id.checkbox_passcode);
        if (loginPassCodeAccepted) {
            view.setBackgroundResource(R.drawable.login_ok);
            //Update indicator here
        } else {
            view.setBackgroundResource(R.drawable.login_not_ok);
            //Update indicator here
        }
    }

    public void novaCodeCheck(String content){
        if (content.length() == 4) {
            EditText novaCode = (EditText) findViewById(R.id.nova_code);
            fyra_sista = novaCode.getText().toString();

            try {
                FileInputStream fis = openFileInput(loginFileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                String[] loginParams = sb.toString().split("%");

                StringBuilder loginContent = new StringBuilder();
                for (int i = 0; i < loginParams.length; i++) {
                    if (i != loginParams.length-1){
                        loginContent.append(loginParams[i]);
                        loginContent.append("%");
                    } else {
                        loginContent.append(fyra_sista);
                    }
                }

                FileOutputStream outputStream;
                outputStream = openFileOutput(loginFileName,MODE_PRIVATE);
                outputStream.write(loginContent.toString().getBytes());
                outputStream.close();
                loggenIn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        EditText name = (EditText) findViewById(R.id.name1);
        EditText surname = (EditText) findViewById(R.id.name2);
        EditText phone = (EditText) findViewById(R.id.mobile_number);
        EditText personalId = (EditText) findViewById(R.id.personalid);
        EditText novacode = (EditText) findViewById(R.id.nova_code);
        EditText passcode = (EditText) findViewById(R.id.passcode);

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

                name.setText(loginParams[0]);
                surname.setText(loginParams[1]);
                phone.setText(loginParams[2]);
                personalId.setText(id_number);
                novacode.setText(fyra_sista);
                passcode.setText(loginParams[4]);

                final View nameIcon = findViewById(R.id.checkbox_name);
                final View surnameIcon = findViewById(R.id.checkbox_surname);
                final View phoneIcon = findViewById(R.id.checkbox_telephone);
                final View personalIdIcon = findViewById(R.id.checkbox_personalid);
                final View passcodeIcon = findViewById(R.id.checkbox_passcode);

                nameIcon.setBackgroundResource(R.drawable.login_locked);
                surnameIcon.setBackgroundResource(R.drawable.login_locked);
                phoneIcon.setBackgroundResource(R.drawable.login_locked);
                personalIdIcon.setBackgroundResource(R.drawable.login_locked);
                passcodeIcon.setBackgroundResource(R.drawable.login_locked);
            } else {
                name.setFocusable(true);
                surname.setFocusable(true);
                phone.setFocusable(true);
                personalId.setFocusable(true);
                passcode.setFocusable(true);

                name.setFocusableInTouchMode(true);
                surname.setFocusableInTouchMode(true);
                phone.setFocusableInTouchMode(true);
                personalId.setFocusableInTouchMode(true);
                passcode.setFocusableInTouchMode(true);
            }
        }catch (IOException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void loginSubmit (View view) {
        final Animation anim_button_click = AnimationUtils.loadAnimation(this, R.anim.anim_button_click);
        view.startAnimation(anim_button_click);

        TextView Error = (TextView) findViewById(R.id.login_error_text);
        Error.setVisibility(View.INVISIBLE);

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final EditText passcode = (EditText) findViewById(R.id.passcode);

        final String Namn = name.getText().toString();
        final String Efternamn = surname.getText().toString();
        final String Mobil = phone.getText().toString();
        id_number = personal_id.getText().toString();
        final String passCode = passcode.getText().toString();
        final String Datum = id_number;
        votingID = id_number + Namn + Efternamn;

        nameCheck(Namn);
        surnameCheck(Efternamn);
        phoneCheck(Mobil);
        personalIdCheck(id_number);
        passCodeCheck(passCode);

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
                            String tempo = person.getJSONObject(7).getString("v");
                            long JsonCode = Math.round(Double.parseDouble(tempo));

                            if (JsonNamn.equals(Namn) && JsonEfternamn.equals(Efternamn) && JsonMobil.equals(Mobil) && JsonDatum.equals(Datum) && JsonCode == Long.parseLong(passCode)) {
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
                        loginConnectFail();
                    }
                }
            } ).execute("https://spreadsheets.google.com/tq?key=" + LoginDataBaseKey);

        } else {
            loginFillFail();
        }
    }

    public void loginSucces() {
        final Button LoginButt = (Button) findViewById(R.id.login_button);
        final Button LogoutButt = (Button) findViewById(R.id.logout_button);
        TextView Error = (TextView) findViewById(R.id.login_error_text);

        LoginButt.setVisibility(View.GONE);
        LogoutButt.setVisibility(View.VISIBLE);

        Error.setVisibility(View.INVISIBLE);

        final View nameIcon = findViewById(R.id.checkbox_name);
        final View surnameIcon = findViewById(R.id.checkbox_surname);
        final View phoneIcon = findViewById(R.id.checkbox_telephone);
        final View personalIdIcon = findViewById(R.id.checkbox_personalid);
        final View passcodeIcon = findViewById(R.id.checkbox_passcode);

        nameIcon.setBackgroundResource(R.drawable.login_locked);
        surnameIcon.setBackgroundResource(R.drawable.login_locked);
        phoneIcon.setBackgroundResource(R.drawable.login_locked);
        personalIdIcon.setBackgroundResource(R.drawable.login_locked);
        passcodeIcon.setBackgroundResource(R.drawable.login_locked);

        final EditText name = (EditText) findViewById(R.id.name1);
        final EditText surname = (EditText) findViewById(R.id.name2);
        final EditText phone = (EditText) findViewById(R.id.mobile_number);
        final EditText personal_id = (EditText) findViewById(R.id.personalid);
        final EditText fyrasista = (EditText) findViewById(R.id.nova_code);
        final EditText passcode = (EditText) findViewById(R.id.passcode);

        name.setFocusable(false);
        surname.setFocusable(false);
        phone.setFocusable(false);
        personal_id.setFocusable(false);
        passcode.setFocusable(false);

        name.setFocusableInTouchMode(false);
        surname.setFocusableInTouchMode(false);
        phone.setFocusableInTouchMode(false);
        personal_id.setFocusableInTouchMode(false);
        passcode.setFocusableInTouchMode(false);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loginFail() {

        TextView Error = (TextView) findViewById(R.id.login_error_text);

        Error.setVisibility(View.VISIBLE);
        Error.setText("- Användare inte hittad -");
    }

    public void loginFillFail() {

        TextView Error = (TextView) findViewById(R.id.login_error_text);

        Error.setVisibility(View.VISIBLE);
        Error.setText("- Fyll i alla fälten korrekt -");
    }

    public void loginConnectFail() {

        TextView Error = (TextView) findViewById(R.id.login_error_text);

        Error.setVisibility(View.VISIBLE);
        Error.setText("- Kunde inte ansluta -");
    }

    public void logoutSubmit (View view) {
        EditText name = (EditText) findViewById(R.id.name1);
        EditText surname = (EditText) findViewById(R.id.name2);
        EditText phone = (EditText) findViewById(R.id.mobile_number);
        EditText personalId = (EditText) findViewById(R.id.personalid);
        EditText passcode = (EditText) findViewById(R.id.passcode);

        final Button LoginButt = (Button) findViewById(R.id.login_button);
        final Button LogoutButt = (Button) findViewById(R.id.logout_button);
        LoginButt.setVisibility(View.VISIBLE);
        LogoutButt.setVisibility(View.GONE);

        loginFile.delete();
        loggenIn = false;

        name.setFocusable(true);
        surname.setFocusable(true);
        phone.setFocusable(true);
        personalId.setFocusable(true);
        passcode.setFocusable(true);

        name.setFocusableInTouchMode(true);
        surname.setFocusableInTouchMode(true);
        phone.setFocusableInTouchMode(true);
        personalId.setFocusableInTouchMode(true);
        passcode.setFocusableInTouchMode(true);

        nameCheck(name.getText().toString());
        surnameCheck(surname.getText().toString());
        phoneCheck(phone.getText().toString());
        personalIdCheck(personalId.getText().toString());
        passCodeCheck(passcode.getText().toString());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

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
                    //theSwtich();
                    //fidget_spinner();
                }
            });

            schemaTimeRefresh onSelectTimeSync = new schemaTimeRefresh();
            onSelectTimeSync.start();
            try {
                onSelectTimeSync.join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            /**String nameOfFile = "id:" + id_number + "week:" + downloadWeek + "_" + schemaFileName;
            schemaFile = new File(getFilesDir() + "/" + nameOfFile);
            String downloadID = id_number;

            DownloadFile M = new DownloadFile(downloadID,downloadWeek,nameOfFile);
            M.start();
            try {
                M.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }   **/

            //drawSchema();
            drawSchemaTemp(true);
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
        }
    }

    private class loginSelect extends Thread {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //fidget_spinner();
                }
            });


        }
    }

    private class schemaTimeRefresh extends Thread {
        public void run() {
            currentWeek = GregorianCalendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            currentDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            currentHour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
            currentMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE);

            boolean next = currentHour > 18;
            switch (currentDay){
                case 0: downloadWeek = currentWeek;
                focusDay = 1;
                break;

                case 1: downloadWeek = currentWeek;
                focusDay = next ? 2 : 1;
                break;

                case 2: downloadWeek = currentWeek;
                focusDay = next ? 3 : 2;
                break;

                case 3: downloadWeek = currentWeek;
                focusDay = next ? 4 : 3;
                break;

                case 4: downloadWeek = currentWeek;
                focusDay = next ? 5 : 4;
                break;

                case 5: downloadWeek = next ? currentWeek + 1: currentWeek;
                focusDay = next ? 1 : 5;
                break;

                case 6: downloadWeek = currentWeek + 1;
                focusDay = 1;
                break;
            }
        }
    }

    public void drawSchemaTemp(final boolean status) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewFlipper vf = (ViewFlipper) findViewById(R.id.schema_week_vf);

                try {

                    ImageView imageView = (ImageView) findViewById(R.id.temp_weekly);

                    String imageUrl = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=81530/sv-se&type=0&id=" + id_number + fyra_sista + "&period=&week=" + downloadWeek + "&mode=0&printer=0&colors=2&head=5&clock=7&foot=1&day=0&width=360&height=600";

                    new DownloadImageTask((ImageView) findViewById(R.id.temp_weekly)) .execute(imageUrl);

                    if (status) {

                        vf.setDisplayedChild(1);
                    } else {

                        vf.setDisplayedChild(2);
                    }

                } catch (Exception e) {

                    vf.setDisplayedChild(2);

                    e.printStackTrace();
                }
            }
        });

    }

    private ArrayList<ArrayList<String>> instagramFeedParser(String url) {

        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> caption = new ArrayList<>();

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        Document doc;

        try {

            doc = Jsoup.connect(url).get();
            Elements script = doc.select("script");

            Element picUrls = script.get(3);

            String[] rawTextCaption = picUrls.toString().split("\"text\":\"");
            String[] rawTextImageUrl = picUrls.toString().split("display_url\":\"");

            for (int i = 1; i < rawTextImageUrl.length ; i++) {

                imageUrls.add(rawTextImageUrl[i].split("\",\"edge_liked_by\":")[0]);

            }

            for (int i = 1; i < rawTextCaption.length ; i++) {

                String temp = rawTextCaption[i].split("shortcode")[0];
                temp = temp.substring(0,temp.length() - 7);
                temp = StringEscapeUtils.unescapeJava(temp);

                caption.add(temp);
                
            }

            data.add(0,imageUrls);
            data.add(1,caption);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }

    private class newsFeedDownload extends Thread {
        public void run() {

            ArrayList<String> imageUrls;
            ArrayList<String> caption;
            ArrayList<String> commercialData;
            ArrayList<ArrayList<String>> data;

            data = instagramFeedParser("https://www.instagram.com/norraselevkar/");

            imageUrls = data.get(0);
            caption = data.get(1);
            commercialFeedDownload(imageUrls,caption);

        }

    }

    private void commercialFeedDownload(final ArrayList<String> imageUrls, final ArrayList<String> captionData) {

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {

                ArrayList<String> commercialData = new ArrayList<>();

                try {
                    JSONArray rows = object.getJSONArray("rows");

                    if (rows.length() == 0)
                    {
                        if ( !imageUrls.get(0).equals(urlCheck.get(0)) || !commercialData.get(2).equals(urlCheck.get(1))) {

                            applyNewsFeed(imageUrls, captionData, commercialData);
                            urlCheck.add(0,imageUrls.get(0));
                            urlCheck.add(1,commercialData.get(2));

                        }

                    } else {

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

                            if ( !imageUrls.get(0).equals(urlCheck.get(0)) || !commercialData.get(2).equals(urlCheck.get(1))) {

                                applyNewsFeed(imageUrls, captionData, commercialData);
                                urlCheck.add(0,imageUrls.get(0));
                                urlCheck.add(1,commercialData.get(2));

                            }

                        }

                    }

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

            } catch (Exception e) {
                e.printStackTrace();
                foodMenu = "/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/Meny saknas/maq1/1";
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
                    personalVote = "";

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
                        personalVoteNumber = data[2];

                    } catch (IOException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    downloaddatum = filedatum;
                    try {
                        JSONArray rows = object.getJSONArray("rows");
                        JSONObject row = rows.getJSONObject(0);
                        JSONArray columns = row.getJSONArray("c");
                        matsedelrating = columns.getJSONObject(0).getDouble("v");
                        matsedelratingTotal = columns.getJSONObject(1).getDouble("v");
                        matsedelratingAmountOfVotes = columns.getJSONObject(2).getInt("v");
                        matsedelMedian = columns.getJSONObject(4).getDouble("v");
                        matsedelStdev = columns.getJSONObject(3).getDouble("v");
                        downloaddatum = columns.getJSONObject(5).getString("v") + "/" + columns.getJSONObject(6).getString("v") + "/" + columns.getJSONObject(7).getString("v");

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }

                    boolean qwerty = !downloaddatum.equals(filedatum);

                    final ViewFlipper matVf = (ViewFlipper) findViewById(R.id.mat_vf);
                    final Button button = (Button) findViewById(R.id.matsedel_button);

                    if (qwerty) {
                        matVf.setDisplayedChild(1);
                        button.setClickable(true);
                    } else {
                        matVf.setDisplayedChild(2);
                        button.setClickable(false);
                    }

                    long hipsterscore = 0;

                    final TextView ratingText = (TextView) findViewById(R.id.rating_text);
                    final RatingBar ratingBarOutput = (RatingBar) findViewById(R.id.rating_output_view);

                    ratingBarOutput.setRating((float)matsedelrating);
                    ratingText.setText(String.valueOf(matsedelrating));

                    final TextView statistikPersonalVote = (TextView) findViewById(R.id.statistik_personal_vote);
                    final TextView statistikMedian = (TextView) findViewById(R.id.statistik_median);
                    final TextView statistikStdev = (TextView) findViewById(R.id.statistik_stdev);
                    final TextView statistikAmountOfVotes = (TextView) findViewById(R.id.statistik_amount_of_votes);
                    final TextView statistikPersonalNumber = (TextView) findViewById(R.id.statistik_nummer);
                    final TextView statistikHipsterScore = (TextView) findViewById(R.id.statistik_hipster);

                    try {
                        hipsterscore = Math.round(20 * Math.abs(matsedelrating - Double.parseDouble(personalVote)));

                    } catch (NumberFormatException | NullPointerException e) {
                        e.printStackTrace();
                    }

                    statistikStdev.setText(String.valueOf(Math.round(matsedelStdev * 100d)/100d));
                    statistikAmountOfVotes.setText(String.valueOf(matsedelratingAmountOfVotes));
                    statistikMedian.setText(String.valueOf(matsedelMedian));
                    statistikPersonalNumber.setText("#" + personalVoteNumber);
                    statistikHipsterScore.setText(String.valueOf(hipsterscore));
                    statistikPersonalVote.setText(personalVote);

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

                    final TextView weekTextView = (TextView) findViewById(R.id.matsedel_belt_vecka_indikator);

                    int foodDay = GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

                    if (foodDay == 0 || foodDay == 6 || foodDay ==7) {
                        foodDay = 1;
                    }

                    if ( currentDay < 6) {

                        weekTextView.setText( "Vecka " + String.valueOf(currentWeek));


                    } else if ( currentDay == 6) {

                        int week = currentWeek + 1;
                        weekTextView.setText( "Vecka " + String.valueOf(week));

                    }

                    String[] veckodagar = {"Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag"};

                    LinearLayout progressLayout = (LinearLayout) findViewById(R.id.progress_layout);

                    progressLayout.setVisibility(View.GONE);

                    mat = foodMenu.split("/maq1/");

                    String[] charSwap = { "Å", "Ä", "Ö", "å", "ä", "ö"};
                    String[] charSwap2 = { "%c3%85", "%c3%84", "%c3%96", "%c3%a5", "%c3%a4", "%c3%b6"};

                    dagensMat = mat[foodDay].replace("\n\n","\n");

                    for (int i = 0; i < 6 ; i++) {

                        dagensMat = dagensMat.replace( charSwap[i] , charSwap2[i] );
                    }

                    for (int i = 0; i < 5 ; i++) {

                        TextView Dagens = (TextView) findViewById(matdayTitle[i]);
                        LinearLayout dagensMatBox = (LinearLayout) findViewById(matdayBox[i]);
                        TextView foodtext = (TextView) findViewById(matdayText[i]);

                        foodtext.setText(mat[i + 1]);
                        dagensMatBox.setBackgroundResource(R.drawable.rect_matsedel_days);
                        foodtext.setTextColor(getResources().getColor(R.color.colorMatSedelBoxText));
                        Dagens.setText(veckodagar[i]);

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
        final Button button = (Button) findViewById(R.id.matsedel_button);

        button.setClickable(false);
        //lol

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

    public void postMatsedelRatingData(String matrating) {

        try {
            String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSe7dUHRpnk226B_XWINpGxTJYrMuBjOqSQ7mAc1tDJgg36iJg/formResponse";
            HttpRequest mReq = new HttpRequest();

            String data = "entry.1218691264=" + votingID + "&"
                    + "entry.1809891164=" + matrating + "&"
                    + "entry.83499785=" + dagensMat;

            String response = mReq.sendPost(fullUrl, data);
            Log.i(myTag, response);

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
                outputStream.write(separator.getBytes());
                outputStream.write(String.valueOf(matsedelratingAmountOfVotes + 1).getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    final RatingBar ratingBarOutput = (RatingBar) findViewById(R.id.rating_output_view);
                    final TextView ratingText = (TextView) findViewById(R.id.rating_text);
                    final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
                    final TextView statistikPersonalVote = (TextView) findViewById(R.id.statistik_personal_vote);
                    final TextView statistikMedian = (TextView) findViewById(R.id.statistik_median);
                    final TextView statistikStdev = (TextView) findViewById(R.id.statistik_stdev);
                    final TextView statistikAmountOfVotes = (TextView) findViewById(R.id.statistik_amount_of_votes);
                    final TextView statistikPersonalNumber = (TextView) findViewById(R.id.statistik_nummer);
                    final TextView statistikHipsterScore = (TextView) findViewById(R.id.statistik_hipster);

                    statistikStdev.setText(String.valueOf(Math.round(matsedelStdev * 100d)/100d));
                    statistikAmountOfVotes.setText(String.valueOf(matsedelratingAmountOfVotes + 1));

                    if (matsedelratingAmountOfVotes == 0) {
                        statistikMedian.setText(String.valueOf(ratingBar.getRating()));

                    } else {
                        statistikMedian.setText(String.valueOf(matsedelMedian));

                    }
                    int number = matsedelratingAmountOfVotes + 1;
                    long hipsterscore = 0;

                    float tempChange = (float)((matsedelratingTotal + ratingBar.getRating()) / (matsedelratingAmountOfVotes + 1.0D));

                    try {
                        hipsterscore = Math.round(20 * Math.abs(tempChange  - ratingBar.getRating()));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    ratingBarOutput.setRating(tempChange);
                    ratingText.setText(String.valueOf(tempChange));
                    statistikHipsterScore.setText(String.valueOf(hipsterscore));
                    statistikPersonalNumber.setText("#" + number);

                    final ViewFlipper matVf = (ViewFlipper) findViewById(R.id.mat_vf);

                    matVf.setDisplayedChild(2);
                    statistikPersonalVote.setText(String.valueOf(ratingBar.getRating()));

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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
                drawSchemaTemp(false);
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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
