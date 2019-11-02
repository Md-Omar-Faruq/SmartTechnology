package com.example.smarttechnology;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.Student.StudentAdd;
import com.example.smarttechnology.StudentPayment.StudentPaymentListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentList extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    private RecyclerView recyclerView;
    private StudentPaymentListAdapter paymentAdapter;
    private List<StudentAdd> studentList;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);

        getSupportActionBar().setTitle("Student List with Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.paymentallStudentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

        progressDialog = new ProgressDialog(PaymentList.this);

        studentList = new ArrayList<>();

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                getAllStudentwitnPayment();
            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }

    }

    private void getAllStudentwitnPayment() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Student_Addmission");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    StudentAdd studentAdd = ds.getValue(StudentAdd.class);
                    studentList.add(studentAdd);

                }

                paymentAdapter = new StudentPaymentListAdapter(PaymentList.this,studentList);
                recyclerView.setAdapter(paymentAdapter);

                paymentAdapter.setOnItemClickListener(new StudentPaymentListAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {

                        String email = studentList.get(position).getStudent_id();

                        Intent intent = new Intent(PaymentList.this, StudentPaymentLayout.class);
                        intent.putExtra("student_id",email);
                        startActivity(intent);

                    }

                    @Override
                    public void OnFeesPayment(int position) {

                        String email = studentList.get(position).getStudent_id();

                        Intent intent = new Intent(PaymentList.this, StudentPaymentLayout.class);
                        intent.putExtra("student_id",email);
                        startActivity(intent);

                    }

                    @Override
                    public void OnPaymentHistory(int position) {

                        String email = studentList.get(position).getStudent_id();

                        Intent intent = new Intent(PaymentList.this, Payment_History_Layout.class);
                        intent.putExtra("student_id",email);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(getApplicationContext(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void searchStudent(final String query){

        databaseReference = FirebaseDatabase.getInstance().getReference("Student_Addmission");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    StudentAdd studentAdd = ds.getValue(StudentAdd.class);

                    if(studentAdd.getStudent_name().toLowerCase().contains(query.toLowerCase()) ||
                            studentAdd.getStudent_email().toLowerCase().contains(query.toLowerCase()) ||
                            studentAdd.getStudent_batch().toLowerCase().contains(query.toLowerCase()) ||
                            studentAdd.getStudent_institute().toLowerCase().contains(query.toLowerCase()) ||
                            studentAdd.getStudent_country().toLowerCase().contains(query.toLowerCase()) ||
                            studentAdd.getStudent_mobileNo().toLowerCase().contains(query.toLowerCase())){

                        studentList.add(studentAdd);

                    }



                }

                paymentAdapter = new StudentPaymentListAdapter(PaymentList.this,studentList);
                recyclerView.setAdapter(paymentAdapter);

                paymentAdapter.setOnItemClickListener(new StudentPaymentListAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {

                        String email = studentList.get(position).getStudent_id();

                        Intent intent = new Intent(PaymentList.this, StudentPaymentLayout.class);
                        intent.putExtra("student_id",email);
                        startActivity(intent);

                    }

                    @Override
                    public void OnFeesPayment(int position) {

                        String email = studentList.get(position).getStudent_id();

                        Intent intent = new Intent(PaymentList.this, StudentPaymentLayout.class);
                        intent.putExtra("student_id",email);
                        startActivity(intent);

                    }

                    @Override
                    public void OnPaymentHistory(int position) {

                        String email = studentList.get(position).getStudent_id();

                        Intent intent = new Intent(PaymentList.this, Payment_History_Layout.class);
                        intent.putExtra("student_id",email);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(getApplicationContext(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.searchmenu,menu);

        MenuItem item = menu.findItem(R.id.searchId);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchStudent(s);
                }else {
                    getAllStudentwitnPayment();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchStudent(s);
                }else {
                    getAllStudentwitnPayment();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
