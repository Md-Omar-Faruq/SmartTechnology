package com.example.smarttechnology;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SendSms extends AppCompatActivity {

    private EditText number,text;
    String getnumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        getSupportActionBar().setTitle("Send SMS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        number = findViewById(R.id.massageNumber);
        text = findViewById(R.id.massageText);

        getnumber = getIntent().getStringExtra("number");
        number.setText(getnumber);
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

    public void SendSMS(View view) {

        int chakePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(chakePermission == PackageManager.PERMISSION_GRANTED){

            massage();

        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},0);
        }


    }

    private void massage() {

        String phoneNumber = number.getText().toString().trim();
        String massage = text.getText().toString().trim();

        if(phoneNumber.isEmpty()){
            number.setError("Enter Number");
            number.requestFocus();
            return;
        }

        if(massage.isEmpty()){
            text.setError("Enter your massage");
            text.requestFocus();
            return;
        }

        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phoneNumber,null,massage,null,null);
        Toast.makeText(getApplicationContext(),"Massage Sent",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case 0:
                if(grantResults.length >= 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    massage();
                }else {
                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                }
        }
    }
}
