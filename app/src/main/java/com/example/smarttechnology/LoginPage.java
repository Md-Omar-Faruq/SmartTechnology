package com.example.smarttechnology;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class LoginPage extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;

    ImageView imageView;
    EditText emailEditText,passEditText;
    CheckBox rememberCheckBox;
    TextView forgotText,createAccount;
    Button button;
    LinearLayout linearLayout,buttonLayout;
    Animation buttomAnimation,topAnimation,leftAnimation,rightAnimation;
    Boolean emailAddressChaker;
    FirebaseUser user;



    SharedPreferences sp;

    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        getSupportActionBar().setTitle("Login");

        imageView = findViewById(R.id.loginImage);
        emailEditText = findViewById(R.id.loginEmail);
        passEditText = findViewById(R.id.loginPass);

        forgotText = findViewById(R.id.forgotTextView);
        createAccount = findViewById(R.id.createAccountTextView);
        button = findViewById(R.id.loginButton);
        linearLayout = findViewById(R.id.rememberandforgot);
        buttonLayout = findViewById(R.id.lgButtonandCreate);

        progressDialog = new ProgressDialog(LoginPage.this);

        rememberCheckBox = findViewById(R.id.RememberCheckBox);
        Paper.init(this);

        firebaseAuth = FirebaseAuth.getInstance();


        buttomAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.buttomanimation);
        topAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.topanimation);
        leftAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);
        rightAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_to_left);

        imageView.startAnimation(topAnimation);
        emailEditText.startAnimation(rightAnimation);
        passEditText.startAnimation(leftAnimation);
        linearLayout.startAnimation(leftAnimation);
        buttonLayout.startAnimation(buttomAnimation);

        sp = getSharedPreferences("login",MODE_PRIVATE);

        try {

                    FirebaseUser cu = firebaseAuth.getCurrentUser();

                    if (cu != null) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }

        }catch (Exception e){
    }
    }

    public void CreateAccount(View view) {

        startActivity(new Intent(getApplicationContext(),RegistationForm.class));
        finish();
    }

    public void LoginButtonListener(View view) {

        String email = emailEditText.getText().toString().trim();
        String password = passEditText.getText().toString().trim();

        if(email.isEmpty()){

            emailEditText.setError("Entera an Email Address");
            emailEditText.requestFocus();
            return;

        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            emailEditText.setError("Enter an valid email address");
            emailEditText.requestFocus();
            return;
        }

        if(password.isEmpty()){

            passEditText.setError("Enter a password");
            passEditText.requestFocus();
            return;

        }

        if(password.length() < 6 ){

            passEditText.setError("Minimum length of a password should be 6");
            passEditText.requestFocus();
            return;
        }

        progressDialog.setTitle("Loging Account");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        boolean wifiConnection,mobileConntectio;
        ConnectivityManager conManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = conManager.getActiveNetworkInfo();


        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    VerifyEmailAddress();
                                    progressDialog.dismiss();

                                } else {
                                    progressDialog.dismiss();
                                    String massage = "Login Filed... Please Chack your Email and Password";
                                    Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
                                }


                            }
                        });

            }
        }else {
            progressDialog.dismiss();
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }



    }


    private void VerifyEmailAddress(){

        user = firebaseAuth.getInstance().getCurrentUser();
        emailAddressChaker = user.isEmailVerified();

        if(emailAddressChaker){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }else {
            String massage = "GoTo your Email and verify your Email Fast.. " +
                    "you chack your Email then your Recive a link, you Click this link and " +
                    "verify your Email, After your Email is verifide Then you Login Successfully....";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }

    }

    public void ForgotPassword(View view) {
        startActivity(new Intent(getApplicationContext(),ForgotPasswordLayout.class));
    }

    @Override
    public void onBackPressed() {

        alertDialogBuilder = new AlertDialog.Builder(LoginPage.this);
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
}
