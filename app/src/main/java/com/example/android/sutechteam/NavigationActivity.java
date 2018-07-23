package com.example.android.sutechteam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    LoginActivity loginActivity;
    public int y = LoginActivity.getLogin();
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    private String name;
    private Uri photoUrl;
    private ImageView mPic;
    private TextView usrname;
    private TextView mail;
    private String gmail;

    public void onStart() {
        super.onStart();
        if(y!=1){
        mAuth.addAuthStateListener(mAuthListner);}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        if(y!= 1) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        /*if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }*/

            mAuth = FirebaseAuth.getInstance();
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        startActivity(new Intent(NavigationActivity.this, LoginActivity.class));
                    }
                }
            };


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            mPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
            usrname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
            mail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_mail);
            // usrname.setText(user.getDisplayName());
            //mail.setText(LoginActivity.getGmail());

            getCurrentInfo();

        }
        if(y==1){
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            mPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
            usrname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
            mail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_mail);
            // usrname.setText(user.getDisplayName());*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
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
        if (id == R.id.Logout) {
            if (y == 1) {
                Intent logout = new Intent(NavigationActivity.this, LoginActivity.class);
                startActivity(logout);
            } else {
                mAuth.signOut();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent music = new Intent(NavigationActivity.this, MediaPlayer.class);
            startActivity(music);
        } else if (id == R.id.nav_gallery) {
            Intent contact = new Intent(NavigationActivity.this, ContactsActivity.class);
            startActivity(contact);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getCurrentInfo() {
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        if (User != null) {
            for (UserInfo profile : User.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();
                name = profile.getDisplayName();
                gmail = profile.getEmail();
                photoUrl = profile.getPhotoUrl();
                usrname.setText(name);
                mail.setText(gmail);

                Picasso.with(getApplicationContext())
                        .load(photoUrl.toString())
                        .placeholder(R.drawable.bitslogo)
                        .resize(100, 100)
                        .transform(new CircularTransformation())
                        .centerCrop()
                        .into(mPic);

            };
        }
    }
}
