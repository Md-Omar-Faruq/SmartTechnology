package com.example.smarttechnology;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_screen extends AppCompatActivity {

     TextView textView;
     ImageView imageView;
     Animation buttomAnimation,topAnimation;
     LinearLayout first,second;
     FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Hidding The ActionBar
        getSupportActionBar().hide();

        //Hidding The Title Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();

        first = findViewById(R.id.fristLayout);
        second = findViewById(R.id.secondLayout);

        imageView = findViewById(R.id.splashImage);
        textView = findViewById(R.id.splashText);
        buttomAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.buttomanimation);
        topAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.topanimation);

        textView.setSelected(true);

        first.startAnimation(topAnimation);
        second.startAnimation(buttomAnimation);




        Thread thread = new Thread(){

            @Override
            public void run() {

                try{

                    sleep(7000);
                    //onStart();
                    Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                    startActivity(intent);
                    finish();
                    super.run();
                }catch (Exception e){ e.printStackTrace(); }

            }
        };
        thread.start();

    }

}
