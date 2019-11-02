package com.example.smarttechnology;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.Massage.AdapterChat;
import com.example.smarttechnology.Massage.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView prifileIv;
    TextView nameTv,userStatusTv;
    EditText massageEt;
    ImageButton sendBtn;

    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelChat> chatList;
    AdapterChat chatAdapter;

    String hisUid;
    String myUid;
    String hisImage;
    String massage,name,onlineStatus,typingStatus;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.chadActivityToolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView = findViewById(R.id.chatRecyclerView);
        prifileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTv);
        massageEt = findViewById(R.id.massageEt);
        sendBtn = findViewById(R.id.sendBtn);

        // Layout for RecylearView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        hisUid = getIntent().getStringExtra("hisid");

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDbRef = firebaseDatabase.getReference("User_Registation");


        Query query = FirebaseDatabase.getInstance().getReference("User_Registation").orderByChild("uid").equalTo(hisUid);
        //Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    // get Date
                    name = ""+ds.child("fullName").getValue();
                    hisImage = ""+ds.child("image").getValue();
                    onlineStatus = ""+ds.child("onlineStatus").getValue();
                    typingStatus = ""+ds.child("typingTo").getValue();

                    if(typingStatus.equals(myUid)){
                        userStatusTv.setText("typing..");
                    }else {

                        if (onlineStatus.equals("online")) {
                            userStatusTv.setText(onlineStatus);
                        } else if (!onlineStatus.equals("online")) {
                            Calendar calendar = Calendar.getInstance(Locale.getDefault());
                            calendar.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime = DateFormat.format("dd-MM-yyyy hh:mm aa", calendar).toString();

                            userStatusTv.setText("Last seen at: " + dateTime);
                        }
                    }
                    // Set Data
                    nameTv.setText(name);
                    try{
                        Picasso.get().load(hisImage).placeholder(R.drawable.loginicon2).into(prifileIv);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.loginicon2).into(prifileIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                massage = massageEt.getText().toString().trim();

                if(massage.isEmpty()){
                    massageEt.setError("Empty massage cannot send..");
                    massageEt.requestFocus();
                    return;
                }

                sendMassage(massage);
            }
        });

        massageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.toString().trim().length() == 0){
                checkTypingStatus("onOne");
            }else {
                checkTypingStatus(hisUid);
            }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        readMassage();

        seenMassage();


    }

    private void seenMassage() {

        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                ModelChat chat = ds.getValue(ModelChat.class);
                if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                    HashMap<String, Object> hasSeenhashMap = new HashMap<>();
                    hasSeenhashMap.put("isSeen", true);
                    ds.getRef().updateChildren(hasSeenhashMap);
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMassage() {
        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                    chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)){
                        chatList.add(chat);
                    }
                    chatAdapter = new AdapterChat(ChatActivity.this, chatList, hisImage);
                    chatAdapter.notifyDataSetChanged();

                    recyclerView.setAdapter(chatAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMassage(String massage) {



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timeStamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();

        //String massage,receiver,sender,timestamp;
        //boolean isSeen;

        /*hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("massage",massage);
        hashMap.put("timeStamp",timeStamp);
        hashMap.put("isSeen",false);*/

        ModelChat modelChat = new ModelChat(
                massage,
                hisUid,
                myUid,
                timeStamp,
                false
        );
        databaseReference.child("Chats").push().setValue(modelChat);

        // Reset EditText
        massageEt.setText("");
    }

    public void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
        myUid = firebaseUser.getUid();
        }else {
            startActivity(new Intent(this,LoginPage.class));
            finish();
        }
    }

    private void checkOnlineStatus(String status){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User_Registation").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus",status);
        // update value of online status
        dbRef.updateChildren(hashMap);

    }

    private void checkTypingStatus(String typing){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User_Registation").child(myUid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo",typing);
        // update value of online status
        dbRef.updateChildren(hashMap);

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        //Set Online
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // get TimeStamp
        String timeStamp = String.valueOf(System.currentTimeMillis());

        // set ofline with last seen timeStamp
        checkOnlineStatus(timeStamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_layout_menu,menu);

        menu.findItem(R.id.userLoyoutSearchMenu).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logoutmenu:
                firebaseAuth.signOut();
                checkUserStatus();
        }


        return super.onOptionsItemSelected(item);
    }
}
