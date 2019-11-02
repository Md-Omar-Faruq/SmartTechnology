package com.example.smarttechnology;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    BottomNavigationView navigationView;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navigationView = findViewById(R.id.buttomNabigationId);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        String navid = getIntent().getStringExtra("navId");

        if(navid.equals("profile")){
            // Default Fragment
            actionBar.setTitle("Profile");
            ProfileFragment profileFragment = new ProfileFragment();
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.contentid,profileFragment,"");
            ft2.commit();

        }else if(navid.equals("user")){
            // user fragment
            actionBar.setTitle("User");
            UserFragment userFragment = new UserFragment();
            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
            ft3.replace(R.id.contentid,userFragment,"");
            ft3.commit();

        }else{
            // Chat fragment
            actionBar.setTitle("Chats");
            ChatListFragment ChatFragment = new ChatListFragment();
            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
            ft4.replace(R.id.contentid,ChatFragment,"");
            ft4.commit();

        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){


                        case R.id.profileMenuNav:
                            // profile fragment
                            actionBar.setTitle("Profile");
                            ProfileFragment profileFragment = new ProfileFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.contentid,profileFragment,"");
                            ft2.commit();
                            return true;

                        case R.id.frindsMenuNav:
                            // user fragment
                            actionBar.setTitle("User");
                            UserFragment userFragment = new UserFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.contentid,userFragment,"");
                            ft3.commit();
                            return true;

                        case R.id.ChatMenuNav:
                            // Chat fragment
                            actionBar.setTitle("Chats");
                            ChatListFragment ChatFragment = new ChatListFragment();
                            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.contentid,ChatFragment,"");
                            ft4.commit();
                            return true;

                    }
                    return false;
                }
            };
}
