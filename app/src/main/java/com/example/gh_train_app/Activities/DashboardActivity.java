package com.example.gh_train_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gh_train_app.Fragments.AboutFragment;
import com.example.gh_train_app.Fragments.AddTrainFragment;
import com.example.gh_train_app.Fragments.AddTrainStationFragment;
import com.example.gh_train_app.Fragments.TrainTerminalFragment;
import com.example.gh_train_app.Fragments.ViewTrainsFragment;
import com.example.gh_train_app.Fragments.DashboardItemFragment;
import com.example.gh_train_app.Fragments.FareEnquiryFragment;
import com.example.gh_train_app.Fragments.HistoryFragment;
import com.example.gh_train_app.Fragments.HomeFragment;
import com.example.gh_train_app.Fragments.ProfileFragment;
import com.example.gh_train_app.Fragments.SettingsFragment;
import com.example.gh_train_app.Fragments.ViewTicketFragment;
import com.example.gh_train_app.OthersFiles.CommonMethod;
import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{
    private ActionBar actionBar;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Context context;
    private  NavigationView nav_view;
    private boolean isHomePage;
    private DrawerLayout drawer;
    private Handler handler = new Handler();
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initToolbar();
        initNavigationMenu();

    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Tools.setSystemBarColor(this);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        displaySelectedScreen(R.id.nav_home);

        updateNavHeader();

    }

    public void initNavigationMenu() {
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener( this );
        Menu nav_Menu = nav_view.getMenu();
        if (currentUser.getUid().equals("JBeQLRXNfjPieBseHwM15vYiJOt2") || currentUser.getUid().equals("FVlHknJf9nOUhf8i0bTUOI09MQz1") || currentUser.getUid().equals("1eMeTrpvM9YkCWJfCd4TrC79uKm2")){
//            nav_Menu.findItem(R.id.nav_add_notification).setVisible(true);
            nav_Menu.findItem(R.id.nav_add_train_station).setVisible(true);
            nav_Menu.findItem(R.id.nav_insert_fare).setVisible(true);
            nav_Menu.findItem(R.id.nav_dashboard_item).setVisible(true);
        }else{
//            nav_Menu.findItem(R.id.nav_add_notification).setVisible(false);
            nav_Menu.findItem(R.id.nav_add_train_station).setVisible(false);
            nav_Menu.findItem(R.id.nav_insert_fare).setVisible(false);
            nav_Menu.findItem(R.id.nav_dashboard_item).setVisible(false);
        }

         drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // open drawer at start
        drawer.openDrawer(GravityCompat.START);
    }

    public void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, newFragment).setTransition(FragmentTransaction.TRANSIT_NONE);
        transaction.addToBackStack(newFragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }



    @SuppressLint("NonConstantResourceId")
    public void displaySelectedScreen(int itemId) {
        switch (itemId) {
            case R.id.nav_home:
                replaceFragment(new HomeFragment());
                isHomePage = true;
                break;
            case R.id.nav_book_ticket:
                replaceFragment( new ViewTrainsFragment());
                isHomePage = false;
                break;
            case R.id.nav_view_ticket:
               replaceFragment( new ViewTicketFragment());
                isHomePage = false;
                break;
            case R.id.nav_fare_enquiry:
                replaceFragment( new FareEnquiryFragment());
                isHomePage = false;
                break;
            case R.id.nav_maps:
                replaceFragment(new TrainTerminalFragment());
                isHomePage = false;
                break;
            case R.id.nav_insert_fare:
                replaceFragment(new AddTrainFragment());
                isHomePage = false;
                break;
            case R.id.nav_add_train_station:
                replaceFragment(new AddTrainStationFragment());
                isHomePage = false;
                break;
            case R.id.nav_dashboard_item:
                replaceFragment(new DashboardItemFragment());
                isHomePage = false;
                break;
            case R.id.nav_settings:
                replaceFragment(new SettingsFragment());
                isHomePage = false;
                break;
            case R.id.nav_help:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "Isaac Ansong", "iansong@st.vvu.edu.gh", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.nav_about:
                replaceFragment(new AboutFragment());
                isHomePage = false;
                break;
            case R.id.nav_history:
                replaceFragment(new HistoryFragment());
                isHomePage = false;
                break;
            case R.id.nav_profile:
                replaceFragment(new ProfileFragment());
                isHomePage = false;
                break;
//            case R.id.nav_notification:
//                replaceFragment(new NotificationFragment());
//            isHomePage = false;
//                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            showAlert();
        } else if (id == R.id.action_refresh) {
            displaySelectedScreen( R.id.nav_history);
        }
        return super.onOptionsItemSelected(item);
    }


    public void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Logout?.");
        builder.setTitle("Oops!!!");
        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setCancelable(false);
        builder .setPositiveButton("OK", (dialog, id) -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });
        builder.setNegativeButton("CANCEL", (dialog, id) -> {
        });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (isHomePage) {
            CommonMethod.showExit("Are you sure you want to exit?.", DashboardActivity.this);
            return;
        }
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            CommonMethod.showExit("Are you sure you want to exit?.", DashboardActivity.this);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }


    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        View headerView = navigationView.getHeaderView( 0 );
        TextView navUsername = headerView.findViewById( R.id.username );
        TextView navUserMail = headerView.findViewById( R.id.useremail );
        ImageView navUserPhot = headerView.findViewById( R.id.userprofile );

        navUserMail.setText( currentUser.getEmail());
        navUsername.setText( currentUser.getDisplayName() );

        Glide.with( this ).load( currentUser.getPhotoUrl() ).into( navUserPhot );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreen(item.getItemId());
        drawer.closeDrawers();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed( runnable = () -> {
            //do something
            updateNavHeader();
            handler.postDelayed(runnable, 1000);
        }, 1000);

    }
}
