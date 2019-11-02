package com.example.smarttechnology;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    SharedPreferences sp;

    private AlertDialog.Builder alertDialogBuilder;

    ActionBarDrawerToggle toggle;
    SliderLayout sliderLayout;
    NavigationView navigationView;
    DrawerLayout drawerLayout;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    String currentUserId;


    private CircularImageView navHeaderCircleImage;
    private TextView navHeaderName,navHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Registation");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerId);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigationId);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navHeaderCircleImage = navView.findViewById(R.id.navHeaderImageView);
        navHeaderName = navView.findViewById(R.id.navHeaderName);
        navHeaderEmail = navView.findViewById(R.id.navHeaderEmail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(1); //set scroll delay in seconds :

        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String uName = dataSnapshot.child("fullName").getValue().toString();
                    String uEmail = dataSnapshot.child("email").getValue().toString();
                    String uImage = dataSnapshot.child("image").getValue().toString();

                    navHeaderName.setText(uName);
                    navHeaderEmail.setText(uEmail);
                    try{
                        Picasso.get().load(uImage).placeholder(R.drawable.loginicon2).into(navHeaderCircleImage);
                    }catch (Exception e){}

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setSliderViews();
    }

    // LogOut Menu Action Listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSliderViews() {

        for (int i = 0; i <= 27; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.cpp1);
                    sliderView.setDescription("");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.cpp3);
                    sliderView.setDescription("");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.p1);
                    sliderView.setDescription("");
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.p2);
                    sliderView.setDescription("");
                    break;
                case 4:
                    sliderView.setImageDrawable(R.drawable.p3);
                    sliderView.setDescription("");
                    break;
                case 5:
                    sliderView.setImageDrawable(R.drawable.p4);
                    sliderView.setDescription("");
                    break;
                case 6:
                    sliderView.setImageDrawable(R.drawable.p5);
                    sliderView.setDescription("");
                    break;
                case 7:
                    sliderView.setImageDrawable(R.drawable.p6);
                    sliderView.setDescription("");
                    break;
                case 8:
                    sliderView.setImageDrawable(R.drawable.p7);
                    sliderView.setDescription("");
                    break;
                case 9:
                    sliderView.setImageDrawable(R.drawable.p8);
                    sliderView.setDescription("");
                    break;
                case 10:
                    sliderView.setImageDrawable(R.drawable.p9);
                    sliderView.setDescription("");
                    break;
                case 11:
                    sliderView.setImageDrawable(R.drawable.p10);
                    sliderView.setDescription("");
                    break;
                case 12:
                    sliderView.setImageDrawable(R.drawable.p11);
                    sliderView.setDescription("");
                    break;
                case 13:
                    sliderView.setImageDrawable(R.drawable.p12);
                    sliderView.setDescription("");
                    break;
                case 14:
                    sliderView.setImageDrawable(R.drawable.p13);
                    sliderView.setDescription("");
                    break;
                case 15:
                    sliderView.setImageDrawable(R.drawable.p14);
                    sliderView.setDescription("");
                    break;
                case 16:
                    sliderView.setImageDrawable(R.drawable.p15);
                    sliderView.setDescription("");
                    break;
                case 17:
                    sliderView.setImageDrawable(R.drawable.p16);
                    sliderView.setDescription("");
                    break;
                case 18:
                    sliderView.setImageDrawable(R.drawable.p7);
                    sliderView.setDescription("");
                    break;
                case 19:
                    sliderView.setImageDrawable(R.drawable.phython2);
                    sliderView.setDescription("");
                    break;
                case 20:
                    sliderView.setImageDrawable(R.drawable.p18);
                    sliderView.setDescription("");
                    break;
                case 21:
                    sliderView.setImageDrawable(R.drawable.p19);
                    sliderView.setDescription("");
                    break;
                case 22:
                    sliderView.setImageDrawable(R.drawable.p20);
                    sliderView.setDescription("");
                    break;
                case 23:
                    sliderView.setImageDrawable(R.drawable.p21);
                    sliderView.setDescription("");
                    break;
                case 24:
                    sliderView.setImageDrawable(R.drawable.p22);
                    sliderView.setDescription("");
                    break;
                case 25:
                    sliderView.setImageDrawable(R.drawable.p23);
                    sliderView.setDescription("");
                    break;
                case 26:
                    sliderView.setImageDrawable(R.drawable.p24);
                    sliderView.setDescription("");
                    break;
                case 27:
                    sliderView.setImageDrawable(R.drawable.phython1);
                    sliderView.setDescription("");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            //sliderView.setDescription("setDescription " + (i + 1));
            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(MainActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }

    public void admissionCardViewListener(View view) {

        try {
            Intent i = new Intent(MainActivity.this,AdmissionLayout.class);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void serviceListener(View view) {
        Intent intent = new Intent(MainActivity.this,ServiveLayout.class);
        startActivity(intent);
    }

    public void courdeListener(View view) {
        Intent intent = new Intent(MainActivity.this,CourceLayout.class);
        startActivity(intent);
    }

    public void StudentCardViewListener(View view) {
        Intent intent = new Intent(MainActivity.this,ShowAllStudent.class);
        startActivity(intent);
    }

    public void TeacherCardViewListener(View view) {
        Intent intent = new Intent(MainActivity.this,ShowAllTeacher.class);
        startActivity(intent);
    }

    public void PhymentCardViewListener(View view) {

        Intent intent = new Intent(MainActivity.this, PaymentList.class);
        startActivity(intent);


    }

    public void LogOut(){

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginPage.class));
        finish();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.navProfileMenuId:
                Intent intent = new Intent(getApplicationContext(),Dashboard.class);
                intent.putExtra("navId","profile");
                startActivity(intent);
                break;

            case R.id.navUserMenuId:
                Intent intent1 = new Intent(getApplicationContext(),Dashboard.class);
                intent1.putExtra("navId","user");
                startActivity(intent1);
                break;

            case R.id.nav_chat:
                Intent intent2 = new Intent(getApplicationContext(),Dashboard.class);
                intent2.putExtra("navId","chat");
                startActivity(intent2);
                break;

            case R.id.navLogoutMenuId:
                LogOut();
                break;

            case R.id.navFeedMenuId:
                startActivity(new Intent(MainActivity.this,FeedBackLayout.class));
                break;

            case R.id.navRattingMenuId:
                rattingThisApps();
                break;

            case R.id.navShareMenuId:
                shareThisApps();
                break;


        }

        return false;
    }

    public void shareThisApps(){

        Intent shareIntern = new Intent(Intent.ACTION_SEND);
        shareIntern.setType("text/plain");

        String shareBody = "Smart Technology Apps";
        String shareSubject = "This App is Usefull to A Computer IT Center. Student, Teacher,Employee or Any Kind Of Information in your IT Center " +
                "Store in Online Server, you Access this Date when your needed ! \n com.example.smarttechnology";

        shareIntern.putExtra(Intent.EXTRA_TEXT,shareBody);
        shareIntern.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

        startActivity(Intent.createChooser(shareIntern,"Share With"));

    }

    private void rattingThisApps() {

        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("text/plain");

        String subject = "Smart Technology Apps";
        String massage = "This App is Usefull to A Computer IT Center. Student, Teacher,Employee or Any Kind Of Information in your IT Center " +
                "Store in Online Server, you Access this Date when your needed ! \n com.example.smarttechnology";

        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,massage);
        intent.setData(Uri.parse(""));
        startActivity(intent);*/
        Toast.makeText(getApplicationContext(),"This Apps Not Uploded By Google PlayStore. (Processing....)",Toast.LENGTH_LONG).show();

    }

    public void EmployeeViewListener(View view) {
        Intent intent = new Intent(MainActivity.this,ShowAllEmployee.class);
        startActivity(intent);
    }

    public void PostCardViewListener(View view) {

        Intent intent = new Intent(MainActivity.this, PostLayout.class);
        startActivity(intent);

    }

    public void FinanceCardviewListener(View view) {

        Intent intent = new Intent(MainActivity.this, Expance.class);
        startActivity(intent);

    }

    public void SataementCardviewListener(View view) {

        Intent intent = new Intent(MainActivity.this,statementlayout.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder.setMessage("do you want to exit this apps ?");

        alertDialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void noticCardView(View view) {

    Intent intent = new Intent(MainActivity.this,NoticBord.class);
    startActivity(intent);

    }
}
