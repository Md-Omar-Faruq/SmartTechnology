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

import com.example.smarttechnology.Student.StudentAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class singleStudentDetails extends AppCompatActivity {


    private String studentemail;
    private CircularImageView image;
    private TextView id, nama, institute, mobile, Fname, Mname, email, batch, gender, course, courseFees, country, BitthDate, AddDate, present, permanent;
    private ImageView call, massage;

    FirebaseStorage firebaseStorage;
    //DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    static final int REQUEST_CALL = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_student_details);

        getSupportActionBar().setTitle("Student Details");

        image = findViewById(R.id.singleStudentImage);
        id = findViewById(R.id.singlestudentID);
        nama = findViewById(R.id.singlestudentName);
        institute = findViewById(R.id.singlestudentInstitute);
        mobile = findViewById(R.id.singlestudentMobielNo);
        Fname = findViewById(R.id.singlestudentFname);
        Mname = findViewById(R.id.singlestudentMname);
        email = findViewById(R.id.singlestudentEmail);
        batch = findViewById(R.id.singlestudentBatch);
        gender = findViewById(R.id.singlestudentGender);
        course = findViewById(R.id.singlestudentCourse);
        courseFees = findViewById(R.id.singlestudentCourseFees);
        country = findViewById(R.id.singlestudentCountry);
        BitthDate = findViewById(R.id.singleStudentBirthDate);
        AddDate = findViewById(R.id.singleStudentAddDate);
        present = findViewById(R.id.singlestudentPresentAddr);
        permanent = findViewById(R.id.singlestudentPermanentAddr);

        call = findViewById(R.id.studentCall);
        massage = findViewById(R.id.studentMasage);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        studentemail = getIntent().getStringExtra("student_mobileNo");

        Query query = FirebaseDatabase.getInstance().getReference("Student_Addmission").orderByChild("student_mobileNo").equalTo(studentemail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    StudentAdd teacherAdd = ds.getValue(StudentAdd.class);


                    try {
                        Picasso.get().load(teacherAdd.getStudentimageUri()).placeholder(R.drawable.loginicon2).into(image);

                    } catch (Exception e) {
                    }

                    id.setText(teacherAdd.getStudent_id());
                    nama.setText(teacherAdd.getStudent_name());
                    institute.setText(teacherAdd.getStudent_institute());
                    mobile.setText(teacherAdd.getStudent_mobileNo());
                    Fname.setText(teacherAdd.getStudent_Fname());
                    Mname.setText(teacherAdd.getStudent_Mname());
                    email.setText(teacherAdd.getStudent_email());
                    batch.setText(teacherAdd.getStudent_batch());
                    gender.setText(teacherAdd.getStudent_gender());
                    course.setText(teacherAdd.getStudent_cource());
                    courseFees.setText(teacherAdd.getCourse_fees());
                    country.setText(teacherAdd.getStudent_country());
                    BitthDate.setText(teacherAdd.getStudent_birthDate());
                    AddDate.setText(teacherAdd.getStudent_admissionDate());
                    present.setText(teacherAdd.getStudent_presentAddr());
                    permanent.setText(teacherAdd.getStudent_parmanentAddr());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error : " + databaseError, Toast.LENGTH_LONG).show();

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


    public void SendMassageStudent(View view) {

        Intent intent = new Intent(singleStudentDetails.this,SendSms.class);
        intent.putExtra("number",mobile.getText().toString());
        startActivity(intent);
    }



    public void SendCall(View view) {

        /*Intent intent = new Intent(singleStudentDetails.this,SendSms.class);
        intent.putExtra("number",mobile.getText().toString());
        startActivity(intent);*/

        makeCall();


    }

    public void makeCall(){


        String number = mobile.getText().toString();
        if(number.trim().length()>0){

            if(ContextCompat.checkSelfPermission(singleStudentDetails.this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(singleStudentDetails.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else {
                String dial = ("tel:"+number);
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }else {
            Toast.makeText(getApplicationContext(),"Phone Number Not Pound",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode==REQUEST_CALL){
           if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               makeCall();
           }else {
               Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
           }
       }
    }
}
