package com.example.smarttechnology;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttechnology.Finance.FinanceAdapter;
import com.example.smarttechnology.Finance.FinanceDataEntryClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class statementlayout extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    private ListView listView;
    DatabaseReference databaseReference;
    private List<FinanceDataEntryClass> statementList;
    private FinanceAdapter financeAdapter;

    public  double income ;
    public  double totalIncome;
    public  double expense;
    public  double totalExpense;
    public  double currentAmmount;
    public  String getincomeexpense;

    TextView incomeText,expenseText,ammountText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statementlayout);

        getSupportActionBar().setTitle("Statement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView = findViewById(R.id.statementlistid);

        incomeText = findViewById(R.id.totalIncome);
        expenseText = findViewById(R.id.totalExpense);
        ammountText = findViewById(R.id.currentAmmount);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();


        statementList = new ArrayList<>();

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                getStatement();

            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }


    }

    private void getStatement() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Finance");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                statementList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    FinanceDataEntryClass statement = snapshot.getValue(FinanceDataEntryClass.class);



                    statementList.add(statement);

                    getincomeexpense =  statement.getInorEx().toString();

                    if(getincomeexpense.equals("Expense")){

                        expense = Double.valueOf(statement.getAmmount());
                        totalExpense += expense;

                    }else if(getincomeexpense.equals("Income")){

                        income = Double.valueOf(statement.getAmmount());
                        totalIncome += income;

                    }

                    currentAmmount = totalIncome - totalExpense;

                }

                financeAdapter = new FinanceAdapter(statementlayout.this,statementList);
                listView.setAdapter(financeAdapter);

                incomeText.setText(""+totalIncome);
                expenseText.setText(""+totalExpense);
                ammountText.setText(""+currentAmmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
