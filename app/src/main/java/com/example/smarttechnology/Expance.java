package com.example.smarttechnology;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttechnology.Finance.FinanceDataEntryClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class Expance extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    private String[] transtionList;
    private DatePickerDialog datePickerDialog;
    private TextView showTranstionDate,narrationText;
    private RadioGroup radioGroup;
    private RadioButton genderButton;
    private EditText ammountEditText;
    private Spinner transtionListSpinner;

    private ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    String paidAmmount = null;
    private  String transactionDate,inorex,ammount,narration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expance);

        getSupportActionBar().setTitle("Finance Entry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        transtionList = getResources().getStringArray(R.array.Transaction_List);


        showTranstionDate = findViewById(R.id.transtionDateTextView);
        radioGroup = findViewById(R.id.radioGroupTransaction);
        ammountEditText = findViewById(R.id.transtionAmmountid);
        transtionListSpinner = findViewById(R.id.transtionspnnierid);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

        paidAmmount = getIntent().getStringExtra("paidAmmount");

        try{
            if(paidAmmount.isEmpty()){

            }else {
                ammountEditText.setText(paidAmmount);
            }
        }catch (Exception e){

        }


        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        showTranstionDate.setText(currentDay+"/"+(currentMonth)+"/"+currentYear);

        progressDialog = new ProgressDialog(Expance.this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Finance");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this,R.layout.transtionlistlayout,R.id.transtionLayoutTextView,transtionList);
        transtionListSpinner.setAdapter(adapter);


    }

    public void SelectTranstionDate(View view) {

        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showTranstionDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();

    }

    public void TranstionButonListener(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                int selectedid = radioGroup.getCheckedRadioButtonId();
                genderButton = findViewById(selectedid);


                transactionDate = showTranstionDate.getText().toString().trim();
                ammount = ammountEditText.getText().toString().trim();
                narration = transtionListSpinner.getSelectedItem().toString().trim();

                if(transactionDate.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Select Transtion Date ",Toast.LENGTH_LONG).show();
                    showTranstionDate.requestFocus();
                    return;
                }

                if (selectedid <= 0) {

                    Toast.makeText(getApplicationContext(), "Select Income Or Expense", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(ammount.isEmpty()){
                    ammountEditText.setError("Enter Ammount ");
                    ammountEditText.requestFocus();
                    return;
                }

                if(transtionListSpinner.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Select Narration",Toast.LENGTH_SHORT).show();
                    transtionListSpinner.requestFocus();
                    return;
                }


                progressDialog.setTitle("Finance Details");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                inorex = genderButton.getText().toString().trim();

                String timeStamp = String.valueOf(System.currentTimeMillis());
                String key = databaseReference.push().getKey();

                FinanceDataEntryClass financeClass = new FinanceDataEntryClass(
                        transactionDate,
                        inorex,
                        ammount,
                        narration,
                        timeStamp
                );

                databaseReference.child(key).setValue(financeClass);

                Toast.makeText(getApplicationContext(),"Data Add Successfull..",Toast.LENGTH_LONG).show();
                clearData();
                progressDialog.dismiss();

            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }



    }

    public void clearData(){

        showTranstionDate.setText("");
        radioGroup.setClickable(false);
        ammountEditText.setText("");
        transtionListSpinner.setSelection(0);

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
}
