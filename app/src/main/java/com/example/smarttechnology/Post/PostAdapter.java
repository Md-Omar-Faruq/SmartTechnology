package com.example.smarttechnology.Post;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private Context context;
    private List<Post> postList;

    public static String imageStatus;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_post_layout, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myHolder, int i) {

        Post post = postList.get(i);

        String uid = post.getUid();
        String uName = post.getuName();
        String uEmail = post.getuEmail();
        String profileImage = post.getProfileImage();
        String postId = post.getPostId();
        String description = post.getDescription();
        String postimage = post.getPostimage();
        String pTimeStamp = post.getpTime();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String postTime = DateFormat.format("dd-MM-yyyy hh:mm aa",calendar).toString();

        // set Date
        myHolder.uNameTv.setText(uName);
        myHolder.pTimeTv.setText(postTime);
        myHolder.pDescriptionTv.setText(description);


        // if there is no image then imageview is hidden
        imageStatus = post.getPostimage();

            if(imageStatus.equals("noImage")){
                myHolder.postImageIv.setVisibility(View.GONE);
            }else {
                try {
                    Picasso.get().load(imageStatus).into(myHolder.postImageIv);
                }catch (Exception ee){
                }
            }
            // set user profile image
            try {
                Picasso.get().load(profileImage).placeholder(R.drawable.loginicon2).into(myHolder.userProfileImageIv);
            }catch (Exception ee){

            }
            // get post image

            myHolder.moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"More",Toast.LENGTH_LONG).show();
                }
            });
            myHolder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Like",Toast.LENGTH_LONG).show();
                }
            });
            myHolder.commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Comment",Toast.LENGTH_LONG).show();
                }
            });
            myHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Share",Toast.LENGTH_LONG).show();
                }
            });
        }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    // View Holder Class
    class MyViewHolder extends RecyclerView.ViewHolder{

        // View All Post xml layout

        ImageView postImageIv,userProfileImageIv;
        TextView uNameTv,pTimeTv,pDescriptionTv,pLikeTv;
        ImageButton moreButton;
        Button likeButton,commentButton,shareButton;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postImageIv = itemView.findViewById(R.id.post_imageIv);
            userProfileImageIv = itemView.findViewById(R.id.post_profile_imageIv);
            uNameTv = itemView.findViewById(R.id.post_user_nameTv);
            pDescriptionTv = itemView.findViewById(R.id.post_descriptionTv);
            pLikeTv = itemView.findViewById(R.id.postLikeTv);
            pTimeTv = itemView.findViewById(R.id.post_TimeTv);
            moreButton = itemView.findViewById(R.id.post_MoreBt);
            likeButton = itemView.findViewById(R.id.likeButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            cardView = itemView.findViewById(R.id.postCardView);


        }
    }
}

