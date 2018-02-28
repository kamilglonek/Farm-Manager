package com.kamilglonek.farmmanager.Activities;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

    //// tabbed activity
    private String [] tabs = {"Sow", "Litter", "Callendar", "Injections", "Recepies"};
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionsPagerAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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
                addFarm(ParseUser.getCurrentUser().toString(), ParseUser.getCurrentUser().toString()+"' farm", ParseQuery.getQuery("animalType").toString());

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


        //// tabbed activity
        // initialization with section adapter

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void animateFab() {
            if(isOpen) {
                addButton.startAnimation(rotateBackward);
                fab1.startAnimation(fabClose);
                fab2.startAnimation(fabClose);
                fab1.setClickable(false);
                fab2.setClickable(false);
                isOpen=false;
            }
            else{
                addButton.startAnimation(rotateForward);
                fab1.startAnimation(fabOpen);
                fab2.startAnimation(fabOpen);
                fab1.setClickable(true);
                fab2.setClickable(true);
                isOpen=true;
            }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.nav_account){

        }
        else if(id == R.id.nav_settings){
            //addFarm(ParseUser.getCurrentUser().toString(), ParseUser.getCurrentUser().toString()+"' farm", ParseQuery.getQuery("animalType").toString());
            //addFarm("Test owner", "Test", "Test");
        }
        else if (id == R.id.nav_logout){
            ParseUser.getCurrentUser().logOut();
            Intent loginIntent = new Intent(UserActivity.this, LoginActivity.class);
            UserActivity.this.startActivity(loginIntent);
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    //////////////////////////////////// add farm

    public void addFarm(String farmOwner, String farmName, String animalType) {

        JSONObject farm = new JSONObject();
        try{
            farm.put("farmOwner", farmOwner).put("farmName", farmName).put("animalType", animalType);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        ParseObject parseFarm = new ParseObject("Farm");
        parseFarm.put("Farm", farm);
        parseFarm.saveInBackground();

    }



    ///// fragments
    // a placeholder fragment containind a simple view

    public static class PlaceholderFragment extends Fragment {

        // fragment arg representing the section for this fragment
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {

        }

        // returns new instance of this fragmenta for the given section number
        public static PlaceholderFragment newInstance(int sectionnumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                return inflater.inflate(R.layout.fragment_tab1, container, false);// this is root view
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                return inflater.inflate(R.layout.fragment_tab2, container, false);
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                return inflater.inflate(R.layout.fragment_tab3, container, false);
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                return inflater.inflate(R.layout.fragment_tab4, container, false);
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
                return inflater.inflate(R.layout.fragment_tab5, container, false);
            } else return null;

        }

    }

    /////////// fragemts, tabs

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle (int position) {
            switch (position) {
                case 0:
                    return "Sow";
                case 1:
                    return "Litter";
                case 2:
                    return "Callendar";
                case 3:
                    return "Injections";
                case 5:
                    return "Recepies";
            }
            return null;
        }
    }

}