package com.example.smarttechnology.ModelUserRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.ChatActivity;
import com.example.smarttechnology.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsears extends RecyclerView.Adapter<AdapterUsears.MyHolder>{

    Context context;
    List<ModelUser> userList;

    // Constractor
    public AdapterUsears(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            // Infleting layour (raw_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        // get Data
        final String hisUID = userList.get(i).getuid();
        String userImage = userList.get(i).getImage();
        String userName = userList.get(i).getFullName();
        String userEmail = userList.get(i).getEmail();

        // set Data
        myHolder.mNameTv.setText(userName);
        myHolder.mEmailTv.setText(userEmail);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.loginicon2).into(myHolder.mAvatarIv);
        }catch (Exception e){}

        // handle item view
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,""+userEmail,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("hisid",hisUID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        CircularImageView mAvatarIv;
        TextView mNameTv,mEmailTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            // item view
            mAvatarIv = itemView.findViewById(R.id.avatarIvAllUser);
            mNameTv = itemView.findViewById(R.id.nameTextAllUser);
            mEmailTv = itemView.findViewById(R.id.emailTextAllUser);
        }
    }
}
