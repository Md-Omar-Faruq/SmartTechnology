package com.example.smarttechnology;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smarttechnology.Employee.EmployeeAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class singleEmployeeDetails extends AppCompatActivity {

    private String teacheremail;

    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    static final int REQUEST_CALL = 11;

    private CircularImageView employeeCircularImage;
    private TextView eid,ename,emobile,efname,emname,eemail,ebirthDate,eGender,eAddDate,eCountry,
            ePresentAddress,ePermanentAddr;

    private ImageView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_employee_details);

        getSupportActionBar().setTitle("Employee Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        employeeCircularImage = findViewById(R.id.singleEmployeeImage);
        eid = findViewById(R.id.singleEmployeeId);
        ename = findViewById(R.id.singleEmployeerName);
        emobile = findViewById(R.id.singleEmployeeMobile);
        efname = findViewById(R.id.singleEmployeeFname);
        emname = findViewById(R.id.singleEmployeeMname);
        eemail = findViewById(R.id.singleEmployeeEmail);
        ebirthDate = findViewById(R.id.singleEmployeeBirthDate);
        eGender = findViewById(R.id.singleEmployeeGender);
        eAddDate = findViewById(R.id.singleEmployeeAddDate);
        eCountry = findViewById(R.id.singleEmployeeCountry);
        ePresentAddress = findViewById(R.id.singleEmployeePresentAddr);
        ePermanentAddr = findViewById(R.id.singleEmployeePermanentAddr);

        call = findViewById(R.id.employeeCall);

        teacheremail=getIntent().getStringExtra("employMobileNo");

        Query query = FirebaseDatabase.getInstance().getReference("Employee_Addmission").orderByChild("employMobileNo").equalTo(teacheremail);
        //showProfile(teacheremail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    EmployeeAdd employeeAdd = ds.getValue(EmployeeAdd.class);

                    try {
                        Picasso.get().load(employeeAdd.getEmployimageUri()).placeholder(R.drawable.loginicon2).into(employeeCircularImage);

                    }catch (Exception e){}

                    eid.setText(employeeAdd.getEmployId());
                    ename.setText(employeeAdd.getEmployName());
                    emobile.setText(employeeAdd.getEmployMobileNo());
                    efname.setText(employeeAdd.getEmployFname());
                    emname.setText(employeeAdd.getEmployMname());
                    eemail.setText(employeeAdd.getEmployEmail());
                    ebirthDate.setText(employeeAdd.getEmployBirthDate());
                    eGender.setText(employeeAdd.getEmployGender());
                    eAddDate.setText(employeeAdd.getEmployAddDate());
                    eCountry.setText(employeeAdd.getEmployCountrt());
                    ePresentAddress.setText(employeeAdd.getEmployPresentAddr());
                    ePermanentAddr.setText(employeeAdd.getEmployPermanentAddr());


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(getApplicationContext(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
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

    public void SendMassageEmployee(View view) {

        Intent intent = new Intent(singleEmployeeDetails.this,SendSms.class);
        intent.putExtra("number",emobile.getText().toString());
        startActivity(intent);

    }


    public void SendCallEmployee(View view) {

        String number = emobile.getText().toString();
        if(number.trim().length()>0){

            if(ContextCompat.checkSelfPermission(singleEmployeeDetails.this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(singleEmployeeDetails.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else {
                String dial = ("tel:"+number);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }else {
            Toast.makeText(getApplicationContext(),"Phone Number Not Pound",Toast.LENGTH_LONG).show();
        }

    }
}
