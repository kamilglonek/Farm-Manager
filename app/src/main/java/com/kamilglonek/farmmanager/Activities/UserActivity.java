package com.kamilglonek.farmmanager.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kamilglonek.farmmanager.Fragments.Tab1;
import com.kamilglonek.farmmanager.Fragments.Tab2;
import com.kamilglonek.farmmanager.Fragments.Tab3;
import com.kamilglonek.farmmanager.Fragments.Tab4;
import com.kamilglonek.farmmanager.Fragments.Tab5;
import com.kamilglonek.farmmanager.R;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton addButton, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    Button bAddFab1, bAddFab2;
    int tabID = 0;
    EditText etChooseBirthdate;
    Calendar myCalendar;

    //// drawer, side navigation bar
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    //tabs, my pager
    private MyPagerAdapter myPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ///// floating action button

        addButton = (FloatingActionButton) findViewById(R.id.addButton);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        bAddFab1 = (Button) findViewById(R.id.bAddSow);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        //////  side navigation bar

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // method invoked only when the actionBar is not null.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(this);

        //tabs
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabID = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //callendar
        myCalendar = Calendar.getInstance();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addFarm(ParseUser.getCurrentUser().toString(), ParseUser.getCurrentUser().toString() + "' farm", ParseQuery.getQuery("animalType").toString());

                switch(tabID) {
                    case 0: // floating action button functionalities for Tab1
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.dialog_add_sow, null);
                        final EditText etSowID = (EditText) mView.findViewById(R.id.etSowID);
                        final EditText etInfo = (EditText) mView.findViewById(R.id.etInfo);
                        bAddFab1 = (Button) mView.findViewById(R.id.bAddSow);
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();
                        bAddFab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                            public void onClick(View v) {
                            String sowID = etSowID.getText().toString();
                            String info = etInfo.getText().toString();

                            Fragment tab1 = (Tab1) getSupportFragmentManager().getFragments().get(0);
                            Tab1 frag = (Tab1) tab1;

                            try {
                                frag.uploadSowList(sowID, info, false, 0);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                         });
                        break;
                    case 1:
                        AlertDialog.Builder mBuilder2 = new AlertDialog.Builder(UserActivity.this);
                        View mView2 = getLayoutInflater().inflate(R.layout.dialog_add_litter, null);
                        final EditText etParentID = (EditText) mView2.findViewById(R.id.etParentID);
                        final EditText etAmount = (EditText) mView2.findViewById(R.id.etAmount);
                        etChooseBirthdate = (EditText) mView2.findViewById(R.id.etInputBirthdate);
                        bAddFab2 = (Button) mView2.findViewById(R.id.bAddLitter);
                        mBuilder2.setView(mView2);
                        final AlertDialog dialog2 = mBuilder2.create();
                        dialog2.show();

                        etChooseBirthdate = (EditText) mView2.findViewById(R.id.etInputBirthdate);
                        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, month);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                updateLabel();
                            }
                        };

                        etChooseBirthdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(UserActivity.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        bAddFab2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String parentID = etParentID.getText().toString();
                                String amount = etAmount.getText().toString();
                                int amountToUpload = Integer.parseInt(amount);
                                String myFormat = "dd/MM/YY";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                                String birthdate = sdf.format(myCalendar.getTime());

 //                               FragmentManager.BackStackEntry backStackEntryAt = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
 //                               Fragment currentFragment = (Fragment) backStackEntryAt;

                                FragmentManager fm = getSupportFragmentManager();

                                for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++){
                                    System.out.println("Found fragment: " + fm.getBackStackEntryAt(entry).getId());
                                }

                                Fragment tab2 = getSupportFragmentManager().getFragments().get(1);
                                Tab2 frag2 = (Tab2) tab2;

                                try {
                                    frag2.uploadLitterList(parentID, birthdate, amountToUpload, false, 0);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dialog2.dismiss();
                            }
                        });
                        break;
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void updateLabel(){
        String myFormat = "dd/MM/YY";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        etChooseBirthdate.setText(sdf.format(myCalendar.getTime()));
        String date = sdf.format(myCalendar.getTime());
        System.out.println(date+"-----------------------------------------");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void animateFab() {
        if (isOpen) {
            addButton.startAnimation(rotateBackward);
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        } else {
            addButton.startAnimation(rotateForward);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_account) {
            //drawer layout my account

        } else if (id == R.id.nav_settings) {
            //drawer layout settings

        } else if (id == R.id.nav_logout) {
            //drawer layout logout

            ParseUser.getCurrentUser().logOut();
            Intent loginIntent = new Intent(UserActivity.this, LoginActivity.class);
            UserActivity.this.startActivity(loginIntent);
        } else if (id == R.id.nav_personal_list) {
            //drawer layout personalList, if you want to customize your list of things to do

            Intent listIntent = new Intent(UserActivity.this, PersonalList.class);
            UserActivity.this.startActivity(listIntent);
        }
        return false;
    }

    //////////////////////////////////// add farm

//    public void addFarm(String farmOwner, String farmName, String animalType) {
//
//        JSONObject farm = new JSONObject();
//        try {
//            farm.put("farmOwner", farmOwner).put("farmName", farmName).put("animalType", animalType);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        ParseObject parseFarm = new ParseObject("Farm");
//        parseFarm.put("Farm", farm);
//        parseFarm.saveInBackground();
//    }

    // tabs

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int TAB_ITEMS = 5;
        private static String TAB_1_TITLE = "Sow";
        private static String TAB_2_TITLE = "Litter";
        private static String TAB_3_TITLE = "To do";
        private static String TAB_4_TITLE = "Callednar";
        private static String TAB_5_TITLE = "Recepies";

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = Tab1.newInstance("Sow", "1");
                    break;
                case 1:
                    fragment = Tab2.newInstance("Litter", "2");
                    break;
                case 2:
                    fragment = Tab3.newInstance("To do", "3");
                    break;
                case 3:
                    fragment = Tab4.newInstance("Callendar", "4");
                    break;
                case 4:
                    fragment = Tab5.newInstance("Recepies", "5");
                    break;
                default:
                    return null;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_ITEMS;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }
    }
}