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

import com.example.smarttechnology.Teacher.TeacherAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class singleTeacherDetails extends AppCompatActivity {

    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    private String teacheremail;

    private CircularImageView teacherCircleImage;
    private TextView id,name,mobile,fname,mname,email,subject,gender,country
            ,birthDate,addDate,presentAddr,permanentAddr;

    private ImageView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_teacher_details);

        getSupportActionBar().setTitle("Teacher Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        teacheremail=getIntent().getStringExtra("teacherId");

        teacherCircleImage = findViewById(R.id.singleTeacherImage);
        id = findViewById(R.id.singleTeacherId);
        name = findViewById(R.id.singleTeacherName);
        mobile = findViewById(R.id.singleTeacherMobile);
        fname = findViewById(R.id.singleTeacherFname);
        mname = findViewById(R.id.singleTeacherMnme);
        email = findViewById(R.id.singleTeacherEmail);
        subject = findViewById(R.id.singleTeacherSubject);
        gender = findViewById(R.id.singleTeacherGender);
        country = findViewById(R.id.singleTeacherCountry);
        birthDate = findViewById(R.id.singleTeacherBirthDate);
        addDate = findViewById(R.id.singleTeacherAddDate);
        presentAddr = findViewById(R.id.singleTeacherPresentAddr);
        permanentAddr = findViewById(R.id.singleTeacherPermanentAddr);

        call = findViewById(R.id.teacherCall);


        Query query = FirebaseDatabase.getInstance().getReference("Teacher_Addmission").orderByChild("teacherId").equalTo(teacheremail);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    TeacherAdd teacherAdd = ds.getValue(TeacherAdd.class);

                    try {
                        Picasso.get().load(teacherAdd.getTeacherImageUri()).placeholder(R.drawable.loginicon2).into(teacherCircleImage);

                    }catch (Exception e){}

                    id.setText(teacherAdd.getTeacherId());
                    name.setText(teacherAdd.getTeacherName());
                    mobile.setText(teacherAdd.getTeacherMobileNo());
                    fname.setText(teacherAdd.getTeacherFname());
                    mname.setText(teacherAdd.getTeacherMname());
                    email.setText(teacherAdd.getTeacherEmail());
                    subject.setText(teacherAdd.getTeacherSubject());
                    gender.setText(teacherAdd.getTeacherGender());
                    country.setText(teacherAdd.getTeacherCountry());
                    birthDate.setText(teacherAdd.getTeacherBrithDate());
                    addDate.setText(teacherAdd.getTeacherAddDate());
                    presentAddr.setText(teacherAdd.getTeacherPresentAddr());
                    permanentAddr.setText(teacherAdd.getTeacherPermanentAddr());
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

    public void sendteacherMassage(View view) {

        Intent intent = new Intent(singleTeacherDetails.this,SendSms.class);
        intent.putExtra("number",mobile.getText().toString());
        startActivity(intent);
    }

    public void TCALL(View view) {

        String nu = mobile.getText().toString();

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + nu));

        if (ActivityCompat.checkSelfPermission(singleTeacherDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);

    }
}
