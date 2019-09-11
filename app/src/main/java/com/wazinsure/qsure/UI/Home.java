package com.wazinsure.qsure.UI;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.service.SaveSharedPreference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Home extends AppCompatActivity
        implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.buy_cover)
    CardView buyCoverCard;
    @BindView(R.id.my_policies)
    CardView myPoliciesCard;
    @BindView(R.id.lodge_claim)
    CardView lodgeClaimsCard;
    @BindView(R.id.renewals)
    CardView renewalsCard;
    @BindView(R.id.quiz)
    CardView  quizCard;
    @BindView(R.id.faqs)
    CardView faqsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Add click listener to the cards
        buyCoverCard.setOnClickListener(this);
        myPoliciesCard.setOnClickListener(this);
        lodgeClaimsCard.setOnClickListener(this);
        renewalsCard.setOnClickListener(this);
        quizCard.setOnClickListener(this);
        faqsCard.setOnClickListener(this);
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
        // automatically handle clicks on the Home/Up button, so long
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

        }else if (id == R.id.nav_login){
            Intent intent = new Intent( this, LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.rate_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        Intent i;

        switch (view.getId()) {

            case R.id.buy_cover :i = new Intent(this,BuyCoverActivity.class);
            startActivity(i);break;
            case R.id.my_policies :i= new Intent(this, MyPoliiesActivity.class);
            startActivity(i);break;
            case R.id.lodge_claim: i= new Intent(this,LodgeClaimActivity.class);
            startActivity(i);break;
            case R.id.renewals: i= new Intent(this, RenewalsActivity.class);
            startActivity(i);break;
            case R.id.quiz: i= new Intent(this,QuizActivity.class);
            startActivity(i);break;
            case R.id.faqs: i= new Intent(this,FAQsActivity.class);
            startActivity(i);break;

            default:break;

        }
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
}
