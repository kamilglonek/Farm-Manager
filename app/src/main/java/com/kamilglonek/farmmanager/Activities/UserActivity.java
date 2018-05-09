package com.kamilglonek.farmmanager.Activities;

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
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.kamilglonek.farmmanager.Fragments.Tab1;
import com.kamilglonek.farmmanager.Fragments.Tab2;
import com.kamilglonek.farmmanager.Fragments.Tab3;
import com.kamilglonek.farmmanager.Fragments.Tab4;
import com.kamilglonek.farmmanager.Fragments.Tab5;
import com.kamilglonek.farmmanager.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton addButton, fab1, fab2;
    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;

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

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFarm(ParseUser.getCurrentUser().toString(), ParseUser.getCurrentUser().toString() + "' farm", ParseQuery.getQuery("animalType").toString());

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        } else if (id == R.id.nav_settings) {
            //addFarm(ParseUser.getCurrentUser().toString(), ParseUser.getCurrentUser().toString()+"' farm", ParseQuery.getQuery("animalType").toString());
            //addFarm("Test owner", "Test", "Test");
        } else if (id == R.id.nav_logout) {
            ParseUser.getCurrentUser().logOut();
            Intent loginIntent = new Intent(UserActivity.this, LoginActivity.class);
            UserActivity.this.startActivity(loginIntent);
        } else if (id == R.id.nav_personal_list) {
            Intent listIntent = new Intent(UserActivity.this, PersonalList.class);
            UserActivity.this.startActivity(listIntent);
        }
        return false;
    }

    //////////////////////////////////// add farm

    public void addFarm(String farmOwner, String farmName, String animalType) {

        JSONObject farm = new JSONObject();
        try {
            farm.put("farmOwner", farmOwner).put("farmName", farmName).put("animalType", animalType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParseObject parseFarm = new ParseObject("Farm");
        parseFarm.put("Farm", farm);
        parseFarm.saveInBackground();
    }

    // tabs

    public static class MyPagerAdapter extends FragmentPagerAdapter {

        private static int TAB_ITEMS = 5;
        private static String TAB_1_TITLE = "Sow";
        private static String TAB_2_TITLE = "Litter";
        private static String TAB_3_TITLE = "Callendar";
        private static String TAB_4_TITLE = "Injections";
        private static String TAB_5_TITLE = "Recepies";


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new Tab1();
                    break;
                case 1:
                    fragment = new Tab2();
                    break;
                case 2:
                    fragment = new Tab3();
                    break;
                case 3:
                    fragment = new Tab4();
                    break;
                case 4:
                    fragment = new Tab5();
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