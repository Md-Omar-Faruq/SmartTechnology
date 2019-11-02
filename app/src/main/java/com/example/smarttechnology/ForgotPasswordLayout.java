package com.example.smarttechnology;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordLayout extends AppCompatActivity {

    private EditText forgitPassword;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_layout);

        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        forgitPassword = findViewById(R.id.forgotPasswordEmail);

        firebaseAuth = FirebaseAuth.getInstance();
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

    public void resetPasswordbutton(View view) {

        String email = forgitPassword.getText().toString().trim();

        if(email.isEmpty()){
            forgitPassword.setError("Ente your email");
            forgitPassword.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    String massage = "Please check your email Account, If you want to reset your password...";
                    Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginPage.class));
                }else {
                    String massage = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
