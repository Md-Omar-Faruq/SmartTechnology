package com.example.smarttechnology;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdmissionLayout extends AppCompatActivity {


    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_layout);

        getSupportActionBar().setTitle("Admission Form");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admissionmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.studentmenu:

                if(activeInfo != null && activeInfo.isConnected()){
                    wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
                    mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

                    if(wifiConnection || mobileConntectio){

                        Intent intentstudent = new Intent(AdmissionLayout.this,StudentAdmission.class);
                        startActivity(intentstudent);
                        break;

                    }
                }else {
                    String massage = "No Internet Connection Please Connect the Device to Internet";
                    Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.teachertmenu:

                if(activeInfo != null && activeInfo.isConnected()){
                    wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
                    mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

                    if(wifiConnection || mobileConntectio){

                        Intent intentteacher = new Intent(AdmissionLayout.this,TeacherAddmission.class);
                        startActivity(intentteacher);
                        break;

                    }
                }else {
                    String massage = "No Internet Connection Please Connect the Device to Internet";
                    Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.employtmenu:

                if(activeInfo != null && activeInfo.isConnected()){
                    wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
                    mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

                    if(wifiConnection || mobileConntectio){

                        Intent intentemploy = new Intent(AdmissionLayout.this,EmployAdmission.class);
                        startActivity(intentemploy);
                        break;

                    }
                }else {
                    String massage = "No Internet Connection Please Connect the Device to Internet";
                    Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
                }
                break;

            case android.R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void TeacherAddButton(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                Intent intent = new Intent(AdmissionLayout.this,TeacherAddmission.class);
                startActivity(intent);

            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }
    }

    public void EmployAddButton(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                Intent intent = new Intent(AdmissionLayout.this,EmployAdmission.class);
                startActivity(intent);

            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }



    }

    public void StudentAddButton(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                Intent intent = new Intent(AdmissionLayout.this,StudentAdmission.class);
                startActivity(intent);

            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }


    }
}
