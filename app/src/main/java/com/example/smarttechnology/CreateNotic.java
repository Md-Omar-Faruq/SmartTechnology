package com.example.smarttechnology;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttechnology.Notic.Notic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateNotic extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    private static final int IMAGE_PIC_COAD = 1000;
    private static final int PERMISSION_COAD = 1001;

    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference noticImagesReference;
    private DatabaseReference userDbRef,databaseReferencQuary;

    private ImageView selectNoticImage;
    private EditText noticTitle,noticDiscription;
    private Button updateNoticButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notic);

        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Notice");

        selectNoticImage = findViewById(R.id.noticimageButtonid);
        noticDiscription = findViewById(R.id.noticEditTextid);
        updateNoticButton = findViewById(R.id.noticUpdatettonid);
        noticTitle = findViewById(R.id.noticTitleEditTextid);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

        progressDialog = new ProgressDialog(this);


    }

    public void UpdateNoticButton(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                validatenoticInfo();
            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }

    }

    private void validatenoticInfo() {
        String notictitle = noticTitle.getText().toString();
        String noticDetails = noticDiscription.getText().toString();


        if(notictitle.isEmpty()){
            noticTitle.setError("write your notic Title");
            noticTitle.requestFocus();
            return;
        }

        if(noticDetails.isEmpty()){
            noticDiscription.setError("write your notic discription...");
            noticDiscription.requestFocus();
            return;
        }

        if(imageUri == null){
            // Post Without Image
            UploadData(notictitle,noticDetails,"noImage");
        }else {
            // Post With Image
            UploadData(notictitle,noticDetails, String.valueOf(imageUri));
        }
    }

    private void UploadData(final String title, final String noticDetails, String uri) {

        progressDialog.setTitle("Add New Notice");
        progressDialog.setMessage("Please wait, while we are uploding your new Notice..");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathandName = "Notic/" + "notic_" + timeStamp;

        if(!uri.equals("noImage")){
            // Notic With Image
            StorageReference rf=FirebaseStorage.getInstance().getReference().child(filePathandName);

            rf.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUrl = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()){

                                //public String noticId,noticTitle,description,noticimage,noticTime;
                                Notic noticdata = new Notic(
                                        timeStamp,
                                        title,
                                        noticDetails,
                                        downloadUrl,
                                        timeStamp
                                );


                                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Notic");

                                postsRef.child(timeStamp).setValue(noticdata)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data Add Success...
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Notice is uploaded successfully ", Toast.LENGTH_LONG).show();

                                                // reset Information
                                                noticTitle.setText("");
                                                noticDiscription.setText("");
                                                imageUri = null;
                                                selectNoticImage.setImageURI(null);
                                                finish();
                                                // goto post Activity
                                                startActivity(new Intent(CreateNotic.this,NoticBord.class));

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Faid Adding data in Database
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Faid Uploading Image
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

        }else if(uri.equals("noImage")){
            // Notic Without Image
            Notic noticdata = new Notic(
                    timeStamp,
                    title,
                    noticDetails,
                    "noImage",
                    timeStamp
            );


            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Notic");

            postsRef.child(timeStamp).setValue(noticdata)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data Add Success...
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Notice is uploaded successfully ", Toast.LENGTH_LONG).show();

                            // reset Information
                            noticTitle.setText("");
                            noticDiscription.setText("");
                            imageUri = null;
                            selectNoticImage.setImageURI(null);
                            finish();
                            // goto post Activity
                            startActivity(new Intent(CreateNotic.this,NoticBord.class));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Faid Adding data in Database
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }

    }


    private void PickImageFromGlarry() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PIC_COAD);
    }

    // Handle Result of Run time Pick Image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PIC_COAD){

            imageUri = data.getData();
            selectNoticImage.setImageURI(imageUri);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.uploadnotic_image_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                break;

            case R.id.uploadnotic:

                selectNoticImage.setVisibility(View.VISIBLE);

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

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
