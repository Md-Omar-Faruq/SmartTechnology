package com.example.smarttechnology;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttechnology.Student.StudentAdd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

import java.util.ArrayList;

public class StudentAdmission extends AppCompatActivity implements OnCountryPickerListener {


    String student_name,student_institute,student_mobileNo,student_Fname,student_Mname,
            student_email,student_batch,student_birthDate,student_admissionDate,student_country,student_gender
            ,student_cource,student_courseFees,student_presentAddr,student_parmanentAddr;

    private TextView showAdmissionDate,showCourseItem,showBirthDate,countryNameTextView;
    private ImageView countryFlagImageView;
    private DatePickerDialog datePickerDialog;
    private CircularImageView circularImageView;

    private StorageTask progressTask;

    private ProgressDialog progressDialog;

    private RadioButton genderButton;
    private RadioGroup radioGroup;

    // Image uri
    private Uri imageUri;

    // Dicleration Firebase DatabaseRefrence
    DatabaseReference databaseReference;
    StorageReference storageReference;

    private Button admissionButton;
    private EditText studentName, currentInstitute, mobileNumber, fatherName, motherName,
            email, batch, presentAddress, permanentAddress,courseFees;

    private CountryPicker countryPicker;

    private static final int IMAGE_PIC_COAD = 1000;
    private static final int PERMISSION_COAD = 1001;



    String[] listItem;
    boolean[] checkedItem;
    ArrayList<Integer> mUserlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_admission);

        getSupportActionBar().setTitle("Student Admission");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showAdmissionDate = findViewById(R.id.admissionDate);
        showBirthDate = findViewById(R.id.dateOFbirth);
        showCourseItem = findViewById(R.id.showcourseitemTextView);
        circularImageView = findViewById(R.id.studentCircleImageView);
        countryFlagImageView = findViewById(R.id.countryPickerImageview);
        countryNameTextView = findViewById(R.id.countryPickerTextView);

        // Initial View

        studentName = findViewById(R.id.studentName);
        currentInstitute = findViewById(R.id.instituteName);
        mobileNumber = findViewById(R.id.mobileNo);
        fatherName = findViewById(R.id.fatherName);
        motherName = findViewById(R.id.motherName);
        email = findViewById(R.id.studentEmail);
        batch = findViewById(R.id.batch);
        presentAddress = findViewById(R.id.presentAddressEditText);
        permanentAddress = findViewById(R.id.permanenttAddressEditText);
        courseFees = findViewById(R.id.courseFees);

        radioGroup = findViewById(R.id.studentRadioGroup);

        progressDialog = new ProgressDialog(StudentAdmission.this);

        // set current Date in Admission dateView
        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        showAdmissionDate.setText(currentDay+"/"+(currentMonth)+"/"+currentYear);


        // Initial Firebase Database Refrence
        databaseReference = FirebaseDatabase.getInstance().getReference("Student_Addmission");
        storageReference = FirebaseStorage.getInstance().getReference("Student_Addmission");

        listItem = getResources().getStringArray(R.array.Course_Item);
        checkedItem = new boolean[listItem.length];

        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

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

    // Admission Date
    public void SetAdmissionDateButton(View view) {


        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showAdmissionDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }


    // Multiple Item Selected Button
    public void CourseSelectedButton(View view) {

        AlertDialog.Builder mBulder = new AlertDialog.Builder(StudentAdmission.this);
        mBulder.setTitle("Item Available in Course");
        mBulder.setMultiChoiceItems(listItem, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if(isChecked){
                    if(!mUserlist.contains(position)){
                    mUserlist.add(position);
                    }else {
                        mUserlist.remove(position);
                    }
                }

            }

        });
        mBulder.setCancelable(false);

        // Aleart Dialog PositiveButton
        mBulder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            String item = "";
                for (int i = 0; i < mUserlist.size(); i++) {
                    item = item + listItem[mUserlist.get(i)];

                    if(i != mUserlist.size() -1){
                        item = item + ", ";
                    }
                }
                showCourseItem.setText(item);
            }
        });

        // Aleart Dialog NagetiveButton
        mBulder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Aleart Diaglog Nu
        mBulder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkedItem.length; i++) {
                    checkedItem[i]=false;
                    mUserlist.clear();
                    showCourseItem.setText("");
                }
            }
        });

        AlertDialog mDiaglog = mBulder.create();
        mDiaglog.show();

    }/*CourseSelectedButton End*/


    // Image Load Permission
    public void CircleImageViewListener(View view) {

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_DENIED){

            // Permission Not Grented , if Request
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,PERMISSION_COAD);
        }else {
            // Permission Aleartly Grenter
            PickImageFromGlarry();
        }

    }else {
        //System OS is less then marshmallow
        PickImageFromGlarry();
    }

    }

    // Image Load Form Glarry
    private void PickImageFromGlarry() {

    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    startActivityForResult(intent,IMAGE_PIC_COAD);
    }


    // Handle Result of Run time Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_COAD:

                if(grantResults.length > 0 && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                    PickImageFromGlarry();
                }else {
                    Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
                }
        }
    }

    // Handle Result of Run time Pick Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIC_COAD && data != null && data.getData() != null){

            imageUri=data.getData();

            circularImageView.setImageURI(data.getData());
        }



    }


    // BirthDate Picker Listener
    public void SetBirthDateButton(View view) {

        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showBirthDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();

    }

    public void CountryPickerButtonListener(View view) {

        countryPicker.showDialog(getSupportFragmentManager());

    }

    // Country Picker Listener
    @Override
    public void onSelectCountry(Country country) {

        // Get Country Name and Flag From CountryPicker
        countryFlagImageView.setImageResource(country.getFlag());
        countryNameTextView.setText(country.getName());
    }

    // take a Image Extention from user
    public String getFileExtenTion(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }



    public void StudentAdmissionButton(View view) {

        if (progressTask != null && progressTask.isInProgress()) {
            Toast.makeText(getApplicationContext(), "Uploading in Progress ", Toast.LENGTH_LONG).show();
        } else {

            if (imageUri==null) {

                Toast.makeText(getApplicationContext(), "Select your image", Toast.LENGTH_LONG).show();
                circularImageView.requestFocus();
                return;
            }

            // store image extention to StorageReference
            StorageReference rf=storageReference.child(System.currentTimeMillis()+"."+getFileExtenTion(imageUri));

            // Gender Selection Coad
            int selectedid = radioGroup.getCheckedRadioButtonId();
            genderButton = findViewById(selectedid);


            student_name = studentName.getText().toString().trim();
            student_institute = currentInstitute.getText().toString().trim();
            student_mobileNo = mobileNumber.getText().toString().trim();
            student_Fname = fatherName.getText().toString().trim();
            student_Mname = motherName.getText().toString().trim();
            student_email = email.getText().toString().trim();
            student_batch = batch.getText().toString().trim();
            student_birthDate = showBirthDate.getText().toString().trim();
            student_country = countryNameTextView.getText().toString().trim();
            student_admissionDate = showAdmissionDate.getText().toString().trim();
            student_presentAddr = presentAddress.getText().toString().trim();
            student_parmanentAddr = permanentAddress.getText().toString().trim();
            student_cource = showCourseItem.getText().toString().trim();
            student_courseFees = courseFees.getText().toString().trim();





            if (student_name.isEmpty()) {
                studentName.setError("Enter Student Name");
                studentName.requestFocus();
                return;
            }
            if (student_institute.isEmpty()) {
                currentInstitute.setError("Enter Institute Name");
                currentInstitute.requestFocus();
                return;
            }
            if (student_mobileNo.isEmpty()) {
                mobileNumber.setError("Enter Mobile Number");
                mobileNumber.requestFocus();
                return;
            }
            if (student_Fname.isEmpty()) {
                fatherName.setError("Enter Father Name");
                fatherName.requestFocus();
                return;
            }
            if (student_Mname.isEmpty()) {
                motherName.setError("Enter Mother Name");
                motherName.requestFocus();
                return;
            }
            if (student_email.isEmpty()) {
                email.setError("Enter Email");
                email.requestFocus();
                return;
            }
            if (student_batch.isEmpty()) {
                batch.setError("Enter Student Batch");
                batch.requestFocus();
                return;
            }
            if (student_cource.isEmpty()) {
               Toast.makeText(getApplicationContext(),"Select Course Item",Toast.LENGTH_LONG).show();
                showCourseItem.requestFocus();
                return;
            }
            if (student_courseFees.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Enter Course Fees",Toast.LENGTH_LONG).show();
                courseFees.requestFocus();
                return;
            }
            if (student_birthDate.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Select Birth Date", Toast.LENGTH_LONG).show();
                showBirthDate.requestFocus();
                return;
            }
            if (student_admissionDate.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Select Admission Date", Toast.LENGTH_LONG).show();
                showAdmissionDate.requestFocus();
                return;
            }
            if (selectedid <= 0) {

                Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                return;
            }
            if (student_country.isEmpty()) {

                Toast.makeText(getApplicationContext(), "Select Country", Toast.LENGTH_SHORT).show();
                return;
            }
            if (student_presentAddr.isEmpty()) {
                presentAddress.setError("Enter Present Address");
                presentAddress.requestFocus();
                return;
            }
            if (student_parmanentAddr.isEmpty()) {
                permanentAddress.setError("Enter Pernanent Address");
                permanentAddress.requestFocus();
                return;
            }


            student_gender = genderButton.getText().toString();

            progressDialog.setTitle("Student Admission");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            rf.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                            progressDialog.dismiss();

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();

                            String unicKey = databaseReference.push().getKey();

                            StudentAdd studentAdd;
                            studentAdd = new StudentAdd(
                                    downloadUrl.toString(),
                                    unicKey,
                                    student_name,
                                    student_institute,
                                    student_mobileNo,
                                    student_Fname,
                                    student_Mname,
                                    student_email,
                                    student_batch,
                                    student_birthDate,
                                    student_admissionDate,
                                    student_country,
                                    student_gender,
                                    student_cource,
                                    student_courseFees,
                                    student_presentAddr,
                                    student_parmanentAddr
                            );


                            databaseReference.child(unicKey).setValue(studentAdd);

                            Toast.makeText(getApplicationContext(), "Student Admission Successfully..", Toast.LENGTH_LONG).show();
                            cleareDate();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle unsuccessful uploads
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

    public void cleareDate(){

        circularImageView.setImageDrawable(null);
        studentName.setText("");
        currentInstitute.setText("");
        mobileNumber.setText("");
        fatherName.setText("");
        motherName.setText("");
        email.setText("");
        batch.setText("");
        showBirthDate.setText("");
        countryNameTextView.setText("");
        showAdmissionDate.setText("");
        presentAddress.setText("");
        showCourseItem.setText("");
        courseFees.setText("");
        radioGroup.setClickable(false);
        permanentAddress.setText("");

    }


}
