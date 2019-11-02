package com.example.smarttechnology;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
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

import com.example.smarttechnology.Teacher.TeacherAdd;
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

public class TeacherAddmission extends AppCompatActivity implements OnCountryPickerListener {


    String  teacher_Name, teacher_Mobile, teacher_Fname, teacher_Mname, teacher_Email, teacher_Subjent, teacher_Country,
            teacher_BirthDate, teacher_AddDate, teacher_PresentAddr, teacher_PermanentAddr, teacher_Gender;

    private Uri imageUri;

    private EditText teacherName, teacherMobile, teacherFname, teacherMname, teacherEmail, teacherSubject,
            teacherPresentAddr, teacherPermanentAddr;

    private TextView showAdmissionDate, showBirthDate, countryNameTextView;
    private ImageView countryFlagImageView;
    private DatePickerDialog datePickerDialog;
    private CircularImageView circularImageView;

    private CountryPicker countryPicker;

    private StorageTask uploadTask;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    private RadioButton genderButton;
    private RadioGroup radioGroup;

    ProgressDialog progress;

    private static final int IMAGE_PIC_COAD = 1000;
    private static final int PERMISSION_COAD = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_addmission);

        getSupportActionBar().setTitle("Teacher Admission");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showAdmissionDate = findViewById(R.id.teacheradmissionDate);
        showBirthDate = findViewById(R.id.teacherdateOFbirth);
        countryNameTextView = findViewById(R.id.teachercountryPickerTextView);
        countryFlagImageView = findViewById(R.id.teachercountryPickerImageview);
        circularImageView = findViewById(R.id.teachercircleImageView);


        teacherName = findViewById(R.id.teacherName);
        teacherMobile = findViewById(R.id.teachermobileNo);
        teacherFname = findViewById(R.id.teacherfatherName);
        teacherMname = findViewById(R.id.teachermotherName);
        teacherEmail = findViewById(R.id.teacherEmail);
        teacherSubject = findViewById(R.id.teacherSubject);
        countryNameTextView = findViewById(R.id.teachercountryPickerTextView);
        showBirthDate = findViewById(R.id.teacherdateOFbirth);
        showAdmissionDate = findViewById(R.id.teacheradmissionDate);
        teacherPresentAddr = findViewById(R.id.teacherpresentAddressEditText);
        teacherPermanentAddr = findViewById(R.id.teacherpermanenttAddressEditText);
        radioGroup = findViewById(R.id.teacherRadioGroup);

        progress = new ProgressDialog(TeacherAddmission.this);

        // set current Date in Admission dateView
        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        showAdmissionDate.setText(currentDay+"/"+(currentMonth)+"/"+currentYear);


        // Initial Firebase Database Refrence
        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher_Addmission");
        storageReference = FirebaseStorage.getInstance().getReference("Teacher_Addmission");

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

    public void CircleImageViewListener(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                // Permission Not Grented , if Request
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_COAD);
            } else {
                // Permission Aleartly Grenter
                PickImageFromGlarry();
            }

        } else {
            //System OS is less then marshmallow
            PickImageFromGlarry();
        }

    }

    // Handle Result of Run time Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_COAD:

                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    PickImageFromGlarry();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // Handle Result of Run time Pick Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PIC_COAD) {

            imageUri = data.getData();
            circularImageView.setImageURI(data.getData());
        }
    }

    // Image Load Form Glarry
    private void PickImageFromGlarry() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PIC_COAD);
    }

    // SetBirthDay Button Listener
    public void SetBirthDateButton(View view) {

        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth() + 1;
        int currentDay = datePicker.getDayOfMonth();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showBirthDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();

    }

    public void SetAdmissionDateButton(View view) {

        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth() + 1;
        int currentDay = datePicker.getDayOfMonth();

        // Teacher Admission Button
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                showAdmissionDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();

    }

    public void CountryPickerButtonListener(View view) {

        countryPicker.showDialog(getSupportFragmentManager());

    }


    @Override
    public void onSelectCountry(Country country) {

        // Get Country Name and Flag From CountryPicker
        countryFlagImageView.setImageResource(country.getFlag());
        countryNameTextView.setText(country.getName());

    }

    // take a Image Extention from user
    public String getFileExtenTion(Uri imageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    public void TeacherAddmission(View view) {

        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(getApplicationContext(), "Uploading in Progress ", Toast.LENGTH_LONG).show();
        }else {

        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderButton = findViewById(selectedId);

            if (imageUri == null) {
                Toast.makeText(getApplicationContext(), "Selected your Image", Toast.LENGTH_LONG).show();
                circularImageView.requestFocus();
                return;
            }

        // store image extention to StorageReference
        StorageReference rf = storageReference.child(System.currentTimeMillis() + "." + getFileExtenTion(imageUri));

        teacher_Name = teacherName.getText().toString().trim();
        teacher_Mobile = teacherMobile.getText().toString().trim();
        teacher_Fname = teacherFname.getText().toString().trim();
        teacher_Mname = teacherMname.getText().toString().trim();
        teacher_Email = teacherEmail.getText().toString().trim();
        teacher_Subjent = teacherSubject.getText().toString().trim();
        teacher_Country = countryNameTextView.getText().toString().trim();
        teacher_BirthDate = showBirthDate.getText().toString().trim();
        teacher_AddDate = showAdmissionDate.getText().toString().trim();
        teacher_PresentAddr = teacherPresentAddr.getText().toString().trim();
        teacher_PermanentAddr = teacherPermanentAddr.getText().toString().trim();




        if (teacher_Name.isEmpty()) {
            teacherName.setError("Enter Teacher Name");
            teacherName.requestFocus();
            return;
        }

        if (teacher_Mobile.isEmpty()) {
            teacherMobile.setError("Enter Mobile Number");
            teacherMobile.requestFocus();
            return;
        }

        if (teacher_Fname.isEmpty()) {
            teacherFname.setError("Enter Father Name");
            teacherFname.requestFocus();
            return;
        }
        if (teacher_Mname.isEmpty()) {
            teacherMname.setError("Enter Mother Name");
            teacherMname.requestFocus();
            return;
        }

        if (teacher_Email.isEmpty()) {
            teacherEmail.setError("Enter email");
            teacherEmail.requestFocus();
            return;
        }

        if (teacher_Subjent.isEmpty()) {
            teacherSubject.setError("Enter Teaching Subject");
            teacherSubject.requestFocus();
            return;
        }
        if (teacher_Country.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Select your country", Toast.LENGTH_LONG).show();
            return;
        }
        if (teacher_BirthDate.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Select Birth Date", Toast.LENGTH_LONG).show();
            return;
        }

        if (selectedId <= 0) {

            Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (teacher_AddDate.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Select Admission Date", Toast.LENGTH_LONG).show();
            return;
        }
        if (teacher_PresentAddr.isEmpty()) {
            teacherPresentAddr.setError("Enter Present Address");
            teacherPresentAddr.requestFocus();
            return;
        }
        if (teacher_PermanentAddr.isEmpty()) {
            teacherPermanentAddr.setError("Enter Permanent Address");
            teacherPermanentAddr.requestFocus();
            return;
        }


        teacher_Gender = genderButton.getText().toString();

            progress.setTitle("Teacher Admission");
            progress.setMessage("Please wait...");
            progress.show();

        rf.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content



                        progress.dismiss();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUrl = uriTask.getResult();

                        String unickey = databaseReference.push().getKey();

                        TeacherAdd teacherAdd;
                        teacherAdd = new TeacherAdd(
                                downloadUrl.toString(),
                                unickey,
                                teacher_Name,
                                teacher_Mobile,
                                teacher_Fname,
                                teacher_Mname,
                                teacher_Email,
                                teacher_Subjent,
                                teacher_Country,
                                teacher_BirthDate,
                                teacher_Gender,
                                teacher_AddDate,
                                teacher_PresentAddr,
                                teacher_PermanentAddr
                        );


                        databaseReference.child(unickey).setValue(teacherAdd);
                        Toast.makeText(getApplicationContext(), "Teacher Admission Successfully..", Toast.LENGTH_LONG).show();
                        cleareDate();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }
 }

    public void cleareDate(){

        circularImageView.setImageDrawable(null);
        teacherName.setText("");
        teacherMobile.setText("");
        teacherFname.setText("");
        teacherMname.setText("");
        teacherEmail.setText("");
        showBirthDate.setText("");
        countryNameTextView.setText("");
        showAdmissionDate.setText("");
        teacherPresentAddr.setText("");
        teacherSubject.setText("");
        radioGroup.setClickable(false);
        teacherPermanentAddr.setText("");

    }
}
