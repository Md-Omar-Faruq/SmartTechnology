package com.example.smarttechnology;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class FeedBackLayout extends AppCompatActivity {

    private EditText name,email,massage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_layout);

        getSupportActionBar().setTitle("FeedBack");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.feedBackName);
        email = findViewById(R.id.feedBackEmail);
        massage = findViewById(R.id.feedBackMassage);
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

    public void ClearButton(View view) {

        name.setText("");
        email.setText("");
        massage.setText("");

    }

    public void SendButton(View view) {

        String eName,eEmail,eMassage;

        eName = name.getText().toString().trim();
        eEmail = email.getText().toString().trim();
        eMassage = massage.getText().toString().trim();

        if(eName.isEmpty()){

            name.setError("Enter your Name");
            name.requestFocus();
            return;

        }

        if(eEmail.isEmpty()){

            email.setError("Enter your Email");
            email.requestFocus();
            return;

        }

        if(eMassage.isEmpty()){

            massage.setError("Enter your Massage");
            massage.requestFocus();
            return;

        }


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/email");

        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"fmd92330@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,"FeedBack Form Apps");
        intent.putExtra(Intent.EXTRA_TEXT,"Name : "+name+"\n Email"+eEmail+"\n FeedBack"+massage);
        startActivity(Intent.createChooser(intent,"FeedBack With"));

    }
}
