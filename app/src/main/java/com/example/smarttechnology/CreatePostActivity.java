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
import androidx.appcompat.widget.Toolbar;

import com.example.smarttechnology.Post.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePostActivity extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    private static final int IMAGE_PIC_COAD = 1000;
    private static final int PERMISSION_COAD = 1001;

    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    private Uri imageUri = null;
    private ProgressDialog progressDialog;

    private StorageReference postImagesReference;
    private DatabaseReference userDbRef,databaseReferencQuary;

    private Toolbar toolbar;
    private ImageView selectPostImage;
    private EditText postDiscription;
    private Button updatePostButton;
    private FirebaseAuth mAuth;

    private String User_Name,User_profile,User_email,sid;

    private String saveCurrentDate,saveCurrentTime,postRandomName,current_user_ud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Post");

        selectPostImage = findViewById(R.id.postimageButtonid);
        postDiscription = findViewById(R.id.postEditTextid);
        updatePostButton = findViewById(R.id.postUpdatettonid);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        current_user_ud = mAuth.getCurrentUser().getUid();
        User_email = mAuth.getCurrentUser().getEmail();

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        postImagesReference = FirebaseStorage.getInstance().getReference("Posts");
        userDbRef = FirebaseDatabase.getInstance().getReference().child("User_Registation");

        Query query = FirebaseDatabase.getInstance().getReference("User_Registation").orderByChild("uid").equalTo(current_user_ud);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //getUser_profile = dataSnapshot.child("image").getValue().toString();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                //RegistationUser teacherAdd = ds.getValue(RegistationUser.class);
                User_Name = ""+ds.child("fullName").getValue();
                User_profile = ""+ds.child("image").getValue();
                User_email = ""+ds.child("email").getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });// End query ValueEventListener
    }

    // Image Load Form Glarry
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
            selectPostImage.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.uploadpostimagemenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            case R.id.uploadPost:

                selectPostImage.setVisibility(View.VISIBLE);

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

    // Post Submit Button
    public void UpdatePostButton(View view) {

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                validatepostInfo();
            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }


    } // End Post Submit Button

    private void validatepostInfo() {

        String postDetails = postDiscription.getText().toString();

        if(postDetails.isEmpty()){
            postDiscription.setError("write your post discription...");
            postDiscription.requestFocus();
            return;
        }
        if(imageUri == null){
            // Post Without Image
            UploadData(postDetails,"noImage");
        }else {
            // Post With Image
            UploadData(postDetails, String.valueOf(imageUri));
        }
    }

    private void UploadData(final String imageDetails, String uri) {

        progressDialog.setTitle("Add New Post");
        progressDialog.setMessage("Please wait, while we are updating your new post...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathandName = "Posts/" + "post_" + timeStamp;

        if(!uri.equals("noImage")){
            // Post With Image
            StorageReference rf=FirebaseStorage.getInstance().getReference().child(filePathandName);

            rf.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUrl = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()){

                                Post postdata = new Post(
                                        timeStamp,
                                        imageDetails,
                                        downloadUrl,
                                        timeStamp,
                                        current_user_ud,
                                        User_Name,
                                        User_email,
                                        User_profile
                                );

                                /*HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("uid",current_user_ud);
                                hashMap.put("uName",User_Name);
                                hashMap.put("uEmail",User_email);
                                hashMap.put("profileImage",User_profile);
                                hashMap.put("postId",timeStamp);
                                hashMap.put("description",imageDetails);
                                hashMap.put("postImage",downloadUrl);
                                hashMap.put("pTime",timeStamp);*/


                                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

                                postsRef.child(timeStamp).setValue(postdata)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Data Add Success...
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Post is uploaded successfully ", Toast.LENGTH_LONG).show();

                                                // reset Information
                                                postDiscription.setText("");
                                                imageUri = null;
                                                selectPostImage.setImageURI(null);
                                                finish();
                                                // goto post Activity
                                                startActivity(new Intent(CreatePostActivity.this,PostLayout.class));

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
            // Post Without Image
            // public String postId,description,postimage,pTime,uid, uName, uEmail, profileImage;
            Post postdata = new Post(
                    timeStamp,
                    imageDetails,
                    "noImage",
                    timeStamp,
                    current_user_ud,
                    User_Name,
                    User_email,
                    User_profile
            );

            /*HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uid",current_user_ud);
            hashMap.put("uName",User_Name);
            hashMap.put("uEmail",User_email);
            hashMap.put("profileImage",User_profile);
            hashMap.put("postId",timeStamp);
            hashMap.put("description",imageDetails);
            hashMap.put("postImage","noImage");
            hashMap.put("pTime",timeStamp);*/


            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

            postsRef.child(timeStamp).setValue(postdata)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data Add Success...
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Post is uploaded successfully ", Toast.LENGTH_LONG).show();

                            // reset Information
                            postDiscription.setText("");
                            imageUri = null;
                            selectPostImage.setImageURI(null);
                            finish();
                            // goto post Activity
                            startActivity(new Intent(CreatePostActivity.this,PostLayout.class));

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

}



