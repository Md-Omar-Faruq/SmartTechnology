package com.example.smarttechnology;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistationForm extends AppCompatActivity {

    private AlertDialog.Builder alertDialogBuilder;

    String user_fullname,user_name,user_email,user_password,user_confirm_pass,user_birthDate,user_gender;
    private EditText userfullName,userName,userEmail,userPassword,userconfirmPass;
    private TextView userbirthDate;
    private RadioButton genderButton;
    private RadioGroup radioGroup;
    private FirebaseAuth firebaseAuth;
    private Button registationButton;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;


    private String key;


    private DatabaseReference databaseReference;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation_form_layout);

        getSupportActionBar().setTitle("Registation Form");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userfullName = findViewById(R.id.userfullName);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        userconfirmPass = findViewById(R.id.userConfirmPass);
        userbirthDate = findViewById(R.id.userdateOFbirth);
        radioGroup = findViewById(R.id.radioGroup);
        registationButton = findViewById(R.id.registationButtonID);
        progressBar = findViewById(R.id.registationProgressBar);

        progressDialog = new ProgressDialog(RegistationForm.this);

        firebaseAuth = FirebaseAuth.getInstance();

        registationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistationButton();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                alertDialogBuilder = new AlertDialog.Builder(RegistationForm.this);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void SetBirthDateButton(View view) {

        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        // User BirthDate Selection
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                userbirthDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();

    }

    // Get Data With Firebase
    public void RegistationButton() {

        // Gender Selection Coad
        int selectedid = radioGroup.getCheckedRadioButtonId();
        genderButton = findViewById(selectedid);


        user_fullname = userfullName.getText().toString().trim();
        user_name = userName.getText().toString().trim();

        user_email = userEmail.getText().toString().trim();
        user_password = userPassword.getText().toString().trim();

        user_confirm_pass = userconfirmPass.getText().toString().trim();
        user_birthDate = userbirthDate.getText().toString().trim();
        //user_gender = genderButton.getText().toString();



        if(user_fullname.isEmpty()){

            userfullName.setError("Enter a full Name");
            userfullName.requestFocus();
            return;
        }
        if(user_name.isEmpty()){

            userName.setError("Enter a User Name");
            userName.requestFocus();
            return;
        }
        if(user_email.isEmpty()){

            userEmail.setError("Enter an email address");
            userEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){

            userEmail.setError("Enter an valid email address");
            userEmail.requestFocus();
            return;
        }
        if(user_password.isEmpty()){

            userPassword.setError("Enter a password");
            userPassword.requestFocus();
            return;
        }
        if(user_password.length() < 6 ){

            userPassword.setError("Minimum length of a password should be 6");
            userPassword.requestFocus();
            return;
        }
        if(user_confirm_pass.isEmpty()){

            userconfirmPass.setError("Enter a Confirm Password");
            userconfirmPass.requestFocus();
            return;
        }
        if(user_birthDate.isEmpty()){

            Toast.makeText(getApplicationContext(),"Select your Birth Date",Toast.LENGTH_SHORT).show();
            userbirthDate.requestFocus();
            return;
        }
        if(selectedid<=0){

                Toast.makeText(getApplicationContext(),"Select Gender",Toast.LENGTH_SHORT).show();
                return;
        }
        if(!user_password.equals(user_confirm_pass)){

            Toast.makeText(getApplicationContext(),"Confirm Password Not Match with Password",Toast.LENGTH_LONG).show();
            userconfirmPass.requestFocus();
            return;
        }

            //progressBar.setVisibility(View.VISIBLE);
            progressDialog.setTitle("Registation");
            progressDialog.setMessage("Please wait...");


            user_gender = genderButton.getText().toString();
            progressDialog.show();
            // Set Email and Password with FireBase Authentication
            firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();

                    if(task.isSuccessful()){

                        finish();
                        sendEmailVerificationMassage();
                    }else {


                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),"User Is Alredy Registed",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(),"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }); // Set Email and Password with FireBase Authentication (End)


    }

    public void SendUserInfoInFirebaseDatavase(){

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        String email = currentUser.getEmail();

        HashMap<Object, String> hasmap = new HashMap<>();

        hasmap.put("fullName",user_fullname);
        hasmap.put("userName",user_name);
        hasmap.put("email",email);
        hasmap.put("password",user_password);
        hasmap.put("birthDate",user_birthDate);
        hasmap.put("gender",user_gender);
        hasmap.put("uid",uid);
        hasmap.put("onlineStatus","online");
        hasmap.put("typingTo","noOne");
        hasmap.put("image","");
        hasmap.put("cover","");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("User_Registation");
        databaseReference.child(uid).setValue(hasmap);


    }

    public void sendEmailVerificationMassage(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        String massage = "Registation Successful, we've sent you a mail. Please check and verify your Email... " +
                                "GoTo your Email and verify your Email Fast.. " +
                                "you chack your Email then your Recive a link From SmartTechnology, you Click this link and " +
                                "verify your Email, After your Email is verifide Then you Login Successfully...";


                        AlertDialog.Builder alertDialogBuilder;
                        alertDialogBuilder = new AlertDialog.Builder(RegistationForm.this);
                        alertDialogBuilder.setTitle("Email Verification");
                        alertDialogBuilder.setMessage(massage);

                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        finish();
                        Intent i = new Intent(getApplicationContext(),LoginPage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        SendUserInfoInFirebaseDatavase();
                        firebaseAuth.signOut();

                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"Error "+error,Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                    }

                }
            });
        }
    }


}
