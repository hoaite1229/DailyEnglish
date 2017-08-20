package org.skv.dailyenglish;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String JSON_URL = "http://kevinynas.synology.me/dailyenglish/getword.php";

    static String receivedWord[] = new String[5];
    static String receivedExplanation[] = new String[5];

    private boolean isUpdated = false;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // For Alarm Function
    private static MainActivity inst;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    Context context;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if ringing, popup dialog.
        Intent intent = getIntent();
        if (intent.hasExtra("ringing")) {
            boolean ringing = intent.getExtras().getBoolean("ringing");
            if (ringing) {
                Log.i("MainActivity", "ringing is true");
                dialogSimple();
            } else {
                Log.i("MainActivity", "ringing is false");
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        processFile(context);

        MobileAds.initialize(this, "");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new CircularViewPagerHandler(mViewPager));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Button leftButton = (Button) findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });

        Button rightButton = (Button) findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("keviny", "call onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            if(sectionNumber == 1)
                args.putInt(ARG_SECTION_NUMBER, 5);
            else if(sectionNumber == 8)
                args.putInt(ARG_SECTION_NUMBER, 0);
            else
                args.putInt(ARG_SECTION_NUMBER, sectionNumber-2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            if(getArguments().getInt(ARG_SECTION_NUMBER) != 5) {
                Typeface typeFace = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Voces_Regular.ttf");
                TextView textView1 = (TextView) rootView.findViewById(R.id.word_textView);
                textView1.setTypeface(typeFace);
                textView1.setText(receivedWord[getArguments().getInt(ARG_SECTION_NUMBER)]);
                TextView textView2 = (TextView) rootView.findViewById(R.id.explanation_textView);
                textView2.setTypeface(typeFace);
                textView2.setText(receivedExplanation[getArguments().getInt(ARG_SECTION_NUMBER)]);
            } else {
                NativeExpressAdView adView = (NativeExpressAdView)rootView.findViewById(R.id.adView);

                AdRequest request = new AdRequest.Builder().build();
                adView.loadAd(request);

                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        Log.i("SK-DEBUG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                        Log.i("SK-DEBUG", "onAdFailedToLoad : " + errorCode);
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                        Log.i("SK-DEBUG", "onAdOpened");
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                        Log.i("SK-DEBUG", "onAdLeftApplication");
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the user is about to return
                        // to the app after tapping on an ad.
                        Log.i("SK-DEBUG", "onAdClosed");
                    }
                });
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 5";
                case 1:
                    return "SECTION 1";
                case 2:
                    return "SECTION 2";
                case 3:
                    return "SECTION 3";
                case 4:
                    return "SECTION 4";
                case 5:
                    return "SECTION 5";
                case 6:
                    return "SECTION 6";
                case 7:
                    return "SECTION 1";
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            if(isUpdated()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_alarm) {
            Log.i("DailyEnglish", "ALARM BUTTON CLICK !!");
            TimePickerFragment mTimePickerFragment = new TimePickerFragment();
            mTimePickerFragment.setActivity(this);
            mTimePickerFragment.show(getSupportFragmentManager(), "FRAGMENT_TAG");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean createAlarm(int hourOfDay, int minute) {
        Log.i("DailyEnglish", "createAlarm!!!");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.i("DailyEnglish", "alarm clock check : "+calendar.getTime().toString());

        return true;
    }

    public void dialogSimple() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("Do you want to Stop Ringing ?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        Log.i("DailyEnglish", "Alarm Off 111 ");
                        AlarmReceiver.stopRinging();

                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("Title");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.ic_menu_gallery);
        alert.show();
    }

    public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        private ViewPager mViewPager;

        public CircularViewPagerHandler(final ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onPageSelected(final int position) {
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (mViewPager.getCurrentItem() == 0) {
                    mViewPager.setCurrentItem(lastPosition -1, false);
                } else if (mViewPager.getCurrentItem() == lastPosition) {
                    mViewPager.setCurrentItem(1, false);
                }
            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }
    }

    private void processFile (Context context) {
        String fileName = "word.txt";
        File file= context.getFileStreamPath(fileName);
        if(file.exists()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String timeReset = formatter.format(file.lastModified());
            try {
                Date lastModified = formatter.parse(timeReset);
                Date currentDate = new Date();

                long diff = currentDate.getTime() - lastModified.getTime();
                float diffDay = ( currentDate.getTime() - lastModified.getTime() ) / (24 * 60 * 60 * 1000);
                long diffTime = 0;
                if(diffDay < 1)
                    diffTime = 6 * 60 * 60 * 1000;
                else
                    diffTime = 30 * 60 * 60 * 1000;

                if(diff > diffTime)
                    getDataFromServer(fileName, context);
                else
                    loadDataFromFile(fileName, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                getDataFromServer(fileName, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadDataFromFile(String fileName, Context context) {
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString;

                int i = 0;
                String string = "";
                while ((receivedString = bufferedReader.readLine()) != null) {
                    if(i % 5 != 0) {
                        string += receivedString;

                        if(i % 5 == 1) {
                            receivedWord[i/5] = string;
                            string = "";
                        } else if(i % 5 == 4) {
                            receivedExplanation[i/5] = string;
                            string = "";
                        } else
                            string += "\n";
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDataToFile(String fileName, Context context, Integer[] numbers, String[] words, String[] pronunciations, String[] meanings, String[] sentences) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            String string = "";

            for(int i=0; i<5; i++) {
                string = numbers[i] + "\n" + words[i] + "\n" + pronunciations[i] + "\n" + meanings[i] + "\n" + sentences[i] + "\n";
                outputStreamWriter.write(string);
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    private boolean isUpdated() {
        return isUpdated;
    }

    private void processJSON(String fileName, Context context, String json) {
        ParseJSON pj = new ParseJSON(json);
        pj.parseJSON();

        saveDataToFile(fileName, context, ParseJSON.numbers, ParseJSON.words, ParseJSON.pronunciations, ParseJSON.meanings, ParseJSON.sentences);
        loadDataFromFile(fileName, context);

        setUpdated(true);
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    private void getDataFromServer(final String fileName, final Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processJSON(fileName, context, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
