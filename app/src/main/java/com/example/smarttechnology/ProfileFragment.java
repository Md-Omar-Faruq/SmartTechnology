package com.example.smarttechnology;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.Post.Post;
import com.example.smarttechnology.Post.PostAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // Post Material
    private RecyclerView postRecylerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference PostRef;

    StorageReference storageReference;
    String storagePath = "User_Profile_Cover_Imagd/";

    private CircularImageView imageView;
    ImageView coverPic;
    TextView nameText,emailText;
    FloatingActionButton fab;

    ProgressDialog progressDialog;

    private static final int CEMERA_REQUEST_COAD = 100;
    private static final int STORAGE_REQUEST_COAD = 200;
    private static final int IMAGE_PIC_GALLERY_REQUEST_COAD = 300;
    private static final int IMAGE_PIC_CEMERA_REQUEST_COAD = 400;


    String cameraPermission[];
    String storagePermission[];

    Uri image_uri;
    String ProfileOrCoverPhoto;

    String userId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Initial FireBase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User_Registation");
        storageReference = getInstance().getReference();

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageView = view.findViewById(R.id.avatarIv);
        nameText = view.findViewById(R.id.nemeTextView);
        emailText = view.findViewById(R.id.emailTextView);
        coverPic = view.findViewById(R.id.coverPhotoId);
        fab = view.findViewById(R.id.floatingAB);

        // Post Meterial
        userId = firebaseAuth.getCurrentUser().getUid();
        postRecylerView = view.findViewById(R.id.userPersonalPost);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postRecylerView.setLayoutManager(layoutManager);
        postList = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    // Get Data From FireBase
                    String name = ""+ ds.child("fullName").getValue();
                    String email = ""+ ds.child("email").getValue();
                    String image = ""+ ds.child("image").getValue();
                    String cover = ""+ ds.child("cover").getValue();

                    // Set Data to Profile

                    nameText.setText(name);
                    emailText.setText(email);
                    try {
                        Picasso.get().load(image).into(imageView);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.loginicon2).into(imageView);
                    }

                    try {
                        Picasso.get().load(cover).into(coverPic);
                    }catch (Exception e){
                        //Picasso.get().load(R.drawable.add_image).into(coverPic);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query queryPost = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("uid").equalTo(userId);
        queryPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        Post post = ds.getValue(Post.class);
                        postList.add(post);
                    }

                    //postAdapter = new PostAdapter(getActivity(), postList);
                    postAdapter = new PostAdapter(getActivity(),postList);
                    postRecylerView.setAdapter(postAdapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(getActivity(),"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                showEditProfileDiaglog();
                
            }
        });

        return view;
    }


    public boolean checkStoragePermission(){

        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestCemeraPermission(){
        requestPermissions(storagePermission,STORAGE_REQUEST_COAD);
    }


    public boolean checkCemeraPermission(){

        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestStoragePermission(){
        requestPermissions(cameraPermission,CEMERA_REQUEST_COAD);
    }



    private void showEditProfileDiaglog() {

        String option[] = {"Edit Profile Picture","Edit Cover Photo","Edit Name"};
        // Aleart Diaglog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Diaglog item Listener
                if(which == 0){
                    // Edit Profile
                    progressDialog.setMessage("Updating Profile Picture...");
                    ProfileOrCoverPhoto = "image";
                    showImagePicDiaglog();
                }else if(which == 1){
                    // Edit Cover pic
                    progressDialog.setMessage("Updating Cover Picture...");
                    ProfileOrCoverPhoto = "cover";
                    showImagePicDiaglog();

                }else if(which == 2){
                    // Edit Name
                    progressDialog.setMessage("Updating User Name...");
                    showNameUpdateDiaglog();
                }

            }
        });
        builder.create().show();
    }

    private void showNameUpdateDiaglog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Updating Name");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        // Add Edit Text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter Name");
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        // Add Button in Dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String value = editText.getText().toString().trim();

                if(!TextUtils.isEmpty(value)){
                    progressDialog.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("fullName",value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),"Updated..",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });

                }else {
                    String massage = "Please Enter";
                    Toast.makeText(getContext(),massage,Toast.LENGTH_LONG).show();
                }

            }
        });

        // Add Button in Dialog to cancle
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // create and show dialog
        builder.create().show();

    }

    private void showImagePicDiaglog() {

        String option1[] = {"Gallery","Cemera"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From");

        builder.setItems(option1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Diaglog item Listener
                if(which == 0){
                    // Gallery Click
                    //progressDialog.setMessage("Updating Cover Picture...");

                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else {
                        picFromGallery();
                    }

                }else if(which == 1){
                    // Cemera Click
                    //progressDialog.setMessage("Updating Profile Picture...");
                    if(!checkCemeraPermission()){
                        requestCemeraPermission();

                    }else {
                        picFromCamera();
                    }

                }

            }
        });
        builder.create().show();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            // Piking form camera, frist check if camera and storage permission allowd or not
            case CEMERA_REQUEST_COAD:{

                if(grantResults.length > 0){

            boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if(cameraAccepted && writeStorageAccepted){
                // Permission enable
                picFromCamera();
            }else {
                Toast.makeText(getActivity(),"Please Enable Camera & Storage Permission",Toast.LENGTH_LONG).show();
            }
            }
            }
            break;

            case STORAGE_REQUEST_COAD:{

                // Piking form Gallery, frist check if storage permission allowd or not
                if(grantResults.length > 0){

                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(writeStorageAccepted){
                        // Permission enable
                        picFromGallery();
                    }else {
                        Toast.makeText(getActivity(),"Please Enable Storage Permission",Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){

            if(requestCode == IMAGE_PIC_GALLERY_REQUEST_COAD){
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if(requestCode == IMAGE_PIC_CEMERA_REQUEST_COAD){
                uploadProfileCoverPhoto(image_uri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {

        progressDialog.show();
        String filePathAndName = storagePath+ ""+ProfileOrCoverPhoto+"_"+user.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);

        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                            Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){

                            HashMap<String, Object> result = new HashMap<>();

                            result.put(ProfileOrCoverPhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(result)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            String massage = "Image Updated...";
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),massage,Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            String massage = "Error..";
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(),massage,Toast.LENGTH_LONG).show();
                                        }
                                    });

                            }else {
                            // Error
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Some Error occured",Toast.LENGTH_LONG).show();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void picFromCamera() {
        // Intent of Picking image form device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        // Put Image Uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PIC_CEMERA_REQUEST_COAD);

    }

    private void picFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PIC_GALLERY_REQUEST_COAD);
    }

    public void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){

        }else {
            startActivity(new Intent(getActivity(),LoginPage.class));
            getActivity().finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.user_layout_menu,menu);
        menu.findItem(R.id.userLoyoutSearchMenu).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.userLoyoutHomeMenu){
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        }else if(id == R.id.logoutmenu){

        }

        return super.onOptionsItemSelected(item);
    }
}
