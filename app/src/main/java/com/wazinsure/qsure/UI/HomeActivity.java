package com.wazinsure.qsure.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.sax.StartElementListener;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.helpers.DatabaseHelper;
import com.wazinsure.qsure.service.SaveSharedPreference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, User_FirsttimeFragment.OnFragmentInteractionListener,User_NotFirstTimeFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mDatabaseHelper = new DatabaseHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initContentHomeFragments();

    }

    public void  initContentHomeFragments(){
        Cursor result = mDatabaseHelper.getAllData();

        fragmentManager = getSupportFragmentManager();

        //retrieve data from SQlite DB

        //check if there are any records
        if(result.getCount() == 0 ){


            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {


                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, new User_FirsttimeFragment())
                            .commit();

                }
            });

        }
        else if (result.getCount() !=0 ){

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, new User_NotFirstTimeFragment())
                            .commit();
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
            logout();
        }


        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {


        }
//        }else if (id == R.id.nav_login){
//            Intent intent = new Intent( this, LoginActivity.class);
//            startActivity(intent);
//        }
        else if (id == R.id.nav_profile) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),UpdateProfileActivity.class);
                    startActivity(intent);
                }
            });

        } else if (id == R.id.rate_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    boolean twice;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (twice == true){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                System.exit(0);
            }

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toasty.info(getBaseContext(), "Please press Back again to exit", Toast.LENGTH_SHORT, true).show();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    twice = false;
                }
            },3000);
            twice = true;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
