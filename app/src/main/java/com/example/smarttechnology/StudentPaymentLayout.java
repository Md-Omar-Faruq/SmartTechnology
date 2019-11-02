package com.example.smarttechnology;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttechnology.Student.StudentAdd;
import com.example.smarttechnology.StudentPaymentEntry.FeesPaymentEntryClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class StudentPaymentLayout extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    String studentP_id,studentP_name,studentP_mobileNo,studentP_Fname,studentP_Mname,
            studentP_email,studentP_batch,studentP_cource,payment_Date,studentP_courseFees,course_Fees,
            discount_Ammount,after_Total,paid_Ammount,due_Ammount;



    private StorageTask progressTask;
    ProgressDialog progressDialog;

    private StudentAdd studentAdd;

    private String studentPayment;
    private CircularImageView image;
    private TextView id, name, mobile, Fname, Mname, Email, batch,course,paymentDate,courseFees,
            afterTTv,dueammountTv;
    private EditText discountET,paidAmmountET;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceUpdate;

    int TotalA,discountA,afterTA,paidA,dueA;


    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment_layout);

        getSupportActionBar().setTitle("Student Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        image = findViewById(R.id.paymentStudentImage);
        id = findViewById(R.id.paymenttudentID);
        name = findViewById(R.id.paymentstudentName);
        mobile = findViewById(R.id.paymentstudentMobielNo);
        Fname = findViewById(R.id.paymentstudentFname);
        Mname = findViewById(R.id.paymentstudentMname);
        Email = findViewById(R.id.paymentstudentEmail);
        batch = findViewById(R.id.singlestudentBatch);
        course = findViewById(R.id.paymentstudentCourse);
        paymentDate = findViewById(R.id.paymentDateTextView);
        courseFees = findViewById(R.id.paymentAmmount);

        discountET = findViewById(R.id.discountAmmount);
        afterTTv = findViewById(R.id.afterTotalAmmount);
        paidAmmountET = findViewById(R.id.paidAmmount);
        dueammountTv = findViewById(R.id.dueAmmount);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

        databaseReference = FirebaseDatabase.getInstance().getReference("Payment_History");
        progressDialog = new ProgressDialog(StudentPaymentLayout.this);

        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        paymentDate.setText(currentDay+"/"+(currentMonth)+"/"+currentYear);

        studentPayment = getIntent().getStringExtra("student_id");

        String key = getIntent().getExtras().get("student_id").toString();

        databaseReferenceUpdate = FirebaseDatabase.getInstance().getReference().child("Student_Addmission").child(key);


        Query query = FirebaseDatabase.getInstance().getReference("Student_Addmission").orderByChild("student_id").equalTo(studentPayment);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    studentAdd = ds.getValue(StudentAdd.class);
                    try {
                        Picasso.get().load(studentAdd.getStudentimageUri()).placeholder(R.drawable.loginicon2).into(image);

                    } catch (Exception e) {
                    }

                    id.setText(studentAdd.getStudent_id());
                    name.setText(studentAdd.getStudent_name());
                    mobile.setText(studentAdd.getStudent_mobileNo());
                    Fname.setText(studentAdd.getStudent_Fname());
                    Mname.setText(studentAdd.getStudent_Mname());
                    Email.setText(studentAdd.getStudent_email());
                    batch.setText(studentAdd.getStudent_batch());
                    course.setText(studentAdd.getStudent_cource());
                    courseFees.setText(studentAdd.getCourse_fees());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error : " + databaseError, Toast.LENGTH_LONG).show();

            }
        });

        // Discount EditText Listener
        discountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    Initialize();

                    afterTA=TotalA-discountA;
                    afterTTv.setText(""+afterTA);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    Initialize();

                    afterTA=TotalA-discountA;
                    afterTTv.setText(""+afterTA);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
        // Discount EditText Listener End

        paidAmmountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    Initialize();

                    afterTA=Integer.parseInt(afterTTv.getText().toString());
                    paidA=Integer.parseInt(paidAmmountET.getText().toString());

                    dueA=afterTA-paidA;
                    dueammountTv.setText(""+dueA);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    Initialize();

                    afterTA=Integer.parseInt(afterTTv.getText().toString());
                    paidA=Integer.parseInt(paidAmmountET.getText().toString());

                    dueA=afterTA-paidA;
                    dueammountTv.setText(""+dueA);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void SelectPaymentDate(View view) {


        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                paymentDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }

    public void PaymentButonListener(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                studentPayment();
            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }
        }

    public void studentPayment(){

        studentP_id = id.getText().toString().trim();
        studentP_name = name.getText().toString().trim();
        studentP_mobileNo = mobile.getText().toString().trim();
        studentP_Fname = Fname.getText().toString().trim();
        studentP_Mname = Mname.getText().toString().trim();
        studentP_email = Email.getText().toString().trim();
        studentP_batch = batch.getText().toString().trim();
        studentP_cource = course.getText().toString().trim();
        payment_Date = paymentDate.getText().toString().trim();
        studentP_courseFees = courseFees.getText().toString().trim();
        discount_Ammount =discountET.getText().toString().trim();
        after_Total = afterTTv.getText().toString().trim();
        paid_Ammount = paidAmmountET.getText().toString().trim();
        due_Ammount = dueammountTv.getText().toString().trim();


        if (payment_Date.isEmpty()) {
            id.setError("Enter Student Id");
            paymentDate.setError("Select Payment Date");
            paymentDate.requestFocus();
            return;
        }
        if (discount_Ammount.isEmpty()) {
            discountET.setError("Enter Discount Ammount,If No Discount To Set Default Value 0.00");
            discountET.requestFocus();
            return;
        }

        progressDialog.setTitle("Student Admission");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();


        FeesPaymentEntryClass feesEntryClass = new FeesPaymentEntryClass(

                studentP_id,
                studentP_name,
                studentP_mobileNo,
                studentP_Fname,
                studentP_Mname,
                studentP_email,
                studentP_batch,
                studentP_cource,
                payment_Date,
                studentP_courseFees,
                discount_Ammount,
                after_Total,
                paid_Ammount,
                due_Ammount
        );

        String unickey = databaseReference.push().getKey();
        databaseReference.child(unickey).setValue(feesEntryClass);

        UpdateStudentFees();

        Toast.makeText(getApplicationContext(),"Payment Successfull..",Toast.LENGTH_LONG).show();

        progressDialog.dismiss();

        discountET.setText("");
        afterTTv.setText("");
        paidAmmountET.setText("");
        dueammountTv.setText("");

        Intent intent = new Intent(StudentPaymentLayout.this,Expance.class);
        intent.putExtra("paidAmmount",paid_Ammount);
        startActivity(intent);
        this.finish();

    }

    public void Initialize(){

        TotalA = Integer.parseInt(courseFees.getText().toString());
        discountA = Integer.parseInt(discountET.getText().toString());

    }

    public void UpdateStudentFees(){

        databaseReferenceUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("course_fees").setValue(due_Ammount);

                Toast.makeText(getApplicationContext(),"Data Update Successfully...",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Data Update Filed...",Toast.LENGTH_SHORT).show();
            }
        });
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
