package com.example.smarttechnology.Massage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<ModelChat> chatList;
    String imageUri;

    FirebaseUser fuser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUri) {
        this.context = context;
        this.chatList = chatList;
        this.imageUri = imageUri;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(i==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.right_user_chat,viewGroup,false);
            return new MyHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_user_chat,viewGroup,false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {

        ModelChat chat = chatList.get(i);

        String massage = chatList.get(i).getMassage();
        String timeStamp = chat.getTimestamp();

        // convert  timeStamp to dd/mm/yyyy hh:mm aa
        /*Calendar calendarA = Calendar.getInstance(Locale.ENGLISH);
        calendarA.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendarA).toString();*/

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String postTime = DateFormat.format("dd-MM-yyyy hh:mm aa",calendar).toString();

        //set date
        myHolder.massageTv.setText(massage);
        myHolder.timeTv.setText(postTime);

        try {
            Picasso.get().load(imageUri).into(myHolder.prifileIv);
        }catch (Exception e){

        }

        // click to show Delete Diloge
        myHolder.massageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("you want to delete this massage");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMassage(i);
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        // set seen status of massage
        if(i==chatList.size()-1){

            if(chatList.get(i).isSeen()){
                myHolder.isSeenTv.setText("Seen");
            }else {
                myHolder.isSeenTv.setText("Delivered");
            }

        }else {
            myHolder.isSeenTv.setVisibility(View.GONE);
        }
    }

    private void deleteMassage(int position) {

        final String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String msgTimeStamp = chatList.get(position).getTimestamp();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        Query query = dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot ds : dataSnapshot.getChildren()){

                if(ds.child("sender").getValue().equals(myUID)){
                    // Remove the massage form the chat
                    //ds.getRef().removeValue(); // Delete Massage Parmanently

                    // Set the value of massage " This message was deleted.."
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("massage","This message was deleted..");
                    ds.getRef().updateChildren(hashMap);

                    Toast.makeText(context,"massage deleted..",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context, "You delete only your own massage ",Toast.LENGTH_LONG).show();
                }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        // get currently singed user
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView prifileIv;
        TextView massageTv,timeTv,isSeenTv;
        LinearLayout massageLayout;// for click layout ther show delete option

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            prifileIv = itemView.findViewById(R.id.profileIv);
            massageTv = itemView.findViewById(R.id.massageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);
            massageLayout = itemView.findViewById(R.id.massageLayoutId);
        }
    }

}
