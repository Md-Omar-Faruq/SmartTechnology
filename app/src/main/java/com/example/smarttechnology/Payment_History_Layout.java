package com.example.smarttechnology;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.smarttechnology.StudentPaymentEntry.FeesPaymentEntryClass;
import com.example.smarttechnology.StudentPaymentEntry.Payment_History_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Payment_History_Layout extends AppCompatActivity {

    DatabaseReference databaseReference;
    private ListView listView;
    private List<FeesPaymentEntryClass> historyList;
    private Payment_History_Adapter listadapter;

    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__history__layout);

        getSupportActionBar().setTitle("Payment History");

        listView = findViewById(R.id.listViewPaymentHistory);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        studentId = getIntent().getStringExtra("student_id");

        databaseReference = FirebaseDatabase.getInstance().getReference("Payment_History");
        historyList = new ArrayList<>();

        listadapter = new Payment_History_Adapter(Payment_History_Layout.this,historyList);

        Query query = FirebaseDatabase.getInstance().getReference("Payment_History").orderByChild("studentPid").equalTo(studentId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                historyList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    FeesPaymentEntryClass paymentList = ds.getValue(FeesPaymentEntryClass.class);
                    historyList.add(paymentList);
                }
                listView.setAdapter(listadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error : " + databaseError, Toast.LENGTH_LONG).show();

            }
        });
    }
}

