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

import com.example.smarttechnology.Employee.EmployeeAdd;
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

public class EmployAdmission extends AppCompatActivity implements OnCountryPickerListener {


    String employ_Name,employ_MobileNo,employ_Fname,employ_Mname,employ_Email,employ_BirthDate,employ_AddDate
            ,employ_Gender,employ_PresentAddr,employ_PermanentAddr,employ_Country;

    private Uri imageUri;
    private TextView showAdmissionDate,showBirthDate,countryNameTextView;
    private ImageView countryFlagImageView;
    private DatePickerDialog datePickerDialog;
    private CircularImageView circularImageView;

    private StorageTask progressTask;
    private ProgressDialog progressDialog;

    private RadioButton genderButton;
    private RadioGroup radioGroup;



    // Dicleration Firebase DatabaseRefrence
    DatabaseReference databaseReference;
    StorageReference storageReference;

    private EditText employName,employMobileNo,employFname,employMname,employEmail,employPresentAddr,employPermanentAddr;


    private CountryPicker countryPicker;

    private static final int IMAGE_PIC_COAD = 1000;
    private static final int PERMISSION_COAD = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employ_admission);

        getSupportActionBar().setTitle("Employ Admission");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showAdmissionDate = findViewById(R.id.employadmissionDate);
        showBirthDate = findViewById(R.id.employdateOFbirth);
        countryFlagImageView = findViewById(R.id.employcountryPickerImageview);
        countryNameTextView = findViewById(R.id.employcountryPickerTextView);
        circularImageView = findViewById(R.id.employcircleImageView);
        radioGroup = findViewById(R.id.employRadioGroup);

        employName = findViewById(R.id.employName);
        employMobileNo = findViewById(R.id.employmobileNo);
        employFname = findViewById(R.id.employfatherName);
        employMname = findViewById(R.id.employmotherName);
        employEmail = findViewById(R.id.employEmail);
        employPresentAddr = findViewById(R.id.employpresentAddressEditText);
        employPermanentAddr = findViewById(R.id.employpermanenttAddressEditText);


        progressDialog = new ProgressDialog(EmployAdmission.this);

        // set current Date in Admission dateView
        DatePicker datePicker = new DatePicker(this);
        int currentYear = datePicker.getYear();
        int currentMonth = datePicker.getMonth()+1;
        int currentDay = datePicker.getDayOfMonth();

        showAdmissionDate.setText(currentDay+"/"+(currentMonth)+"/"+currentYear);

        // Initial Firebase Database Refrence
        databaseReference = FirebaseDatabase.getInstance().getReference("Employee_Addmission");
        storageReference = FirebaseStorage.getInstance().getReference("Employee_Addmission");

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

    public void CountryPickerButtonListener(View view) {

        countryPicker.showDialog(getSupportFragmentManager());

    }

    // Image Load Form Glarry
    private void PickImageFromGlarry() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PIC_COAD);
    }

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

    // Handle Result of Run time Pick Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIC_COAD){

            imageUri = data.getData();
            circularImageView.setImageURI(data.getData());
        }
    }




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

    public void EmployAddmissionMethode(View view) {

        if(progressTask != null && progressTask.isInProgress()){
            Toast.makeText(getApplicationContext(), "Uploading in Progress ", Toast.LENGTH_LONG).show();
        }else {

            if(imageUri == null){
                Toast.makeText(getApplicationContext(),"Select image",Toast.LENGTH_LONG).show();
                circularImageView.requestFocus();
                return;
            }
            // store image extention to StorageReference
            StorageReference rf=storageReference.child(System.currentTimeMillis()+"."+getFileExtenTion(imageUri));

            // Gender Selection Coad
            int selectedid = radioGroup.getCheckedRadioButtonId();
            genderButton = findViewById(selectedid);

            employ_Name = employName.getText().toString().trim();
            employ_MobileNo = employMobileNo.getText().toString().trim();
            employ_Fname = employFname.getText().toString().trim();
            employ_Mname = employMname.getText().toString().trim();
            employ_Email = employEmail.getText().toString().trim();
            employ_BirthDate = showBirthDate.getText().toString().trim();
            employ_AddDate = showAdmissionDate.getText().toString().trim();
            employ_Country = countryNameTextView.getText().toString().trim();
            employ_PresentAddr = employPresentAddr.getText().toString().trim();
            employ_PermanentAddr = employPermanentAddr.getText().toString().trim();



            if(employ_Name.isEmpty()){
                employName.setError("Enter employee Name");
                employName.requestFocus();
                return;
            }
            if(employ_MobileNo.isEmpty()){
                employMobileNo.setError("Enter Mobil Number");
                employMobileNo.requestFocus();
                return;
            }
            if(employ_Fname.isEmpty()){
                employFname.setError("Enter employee Father Name");
                employFname.requestFocus();
                return;
            }
            if(employ_Mname.isEmpty()){
                employMname.setError("Enter employee Mother Name");
                employMname.requestFocus();
                return;
            }
            if(employ_Email.isEmpty()){
                employEmail.setError("Enter Email");
                employEmail.requestFocus();
                return;
            }
            if(employ_BirthDate.isEmpty()){
                Toast.makeText(getApplicationContext(),"Select Birth Date",Toast.LENGTH_LONG).show();
                showBirthDate.requestFocus();
                return;
            }
            if (selectedid <= 0) {

                Toast.makeText(getApplicationContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                return;
            }
            if(employ_AddDate.isEmpty()){
                Toast.makeText(getApplicationContext(), "Select Admission Date", Toast.LENGTH_SHORT).show();
                return;
            }
            if(employ_Country.isEmpty()){
                Toast.makeText(getApplicationContext(), "Select Country", Toast.LENGTH_SHORT).show();
                return;
            }
            if(employ_PresentAddr.isEmpty()){
                employPresentAddr.setError("Enter your present Address");
                employPresentAddr.requestFocus();
                return;
            }
            if(employ_PermanentAddr.isEmpty()){
                employPermanentAddr.setError("Enter your permanent Address");
                employPermanentAddr.requestFocus();
                return;
            }

            employ_Gender = genderButton.getText().toString();

            progressDialog.setTitle("Employee Admission");
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

                            String unickey = databaseReference.push().getKey();

                            EmployeeAdd employeeAdd;
                            employeeAdd = new EmployeeAdd(
                                    downloadUrl.toString(),
                                    unickey,
                                    employ_Name,
                                    employ_MobileNo,
                                    employ_Fname,
                                    employ_Mname,
                                    employ_Email,
                                    employ_BirthDate,
                                    employ_Gender,
                                    employ_AddDate,
                                    employ_Country,
                                    employ_PresentAddr,
                                    employ_PermanentAddr
                            );


                            databaseReference.child(unickey).setValue(employeeAdd);

                            Toast.makeText(getApplicationContext(), "Employee Admission Successfully..", Toast.LENGTH_LONG).show();

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
        employName.setText("");
        employMobileNo.setText("");
        employFname.setText("");
        employMname.setText("");
        employEmail.setText("");
        showBirthDate.setText("");
        countryNameTextView.setText("");
        showAdmissionDate.setText("");
        employPresentAddr.setText("");
        employPermanentAddr.setText("");
        radioGroup.setClickable(false);

    }
}
