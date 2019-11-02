package com.example.smarttechnology.Notic;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class NoticAdapter extends RecyclerView.Adapter<NoticAdapter.MyViewHolder>{

    Context context;
    List<Notic> noticList;

    public NoticAdapter(Context context, List<Notic> noticList) {
        this.context = context;
        this.noticList = noticList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.all_notice, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Notic notic = noticList.get(i);

        String noticeTitle = notic.getNoticTitle();
        String noticeId = notic.getNoticId();
        String noticeDescrif = notic.getDescription();
        String noticeTimeSpand = notic.getNoticTime();
        String noticeImage = notic.getNoticimage();

        // Convert noticeTime to dd-mm-yy
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(noticeTimeSpand));
        String noticeTime = DateFormat.format("dd-MM-yyyy hh:mm aa",calendar).toString();

        // Set Data
        myViewHolder.noticeTitle.setText(noticeTitle);
        myViewHolder.noticeTime.setText(noticeTime);
        myViewHolder.noticeDetailse.setText(noticeDescrif);

        if(noticeImage.equals("noImage")){
            myViewHolder.noticeImage.setVisibility(View.GONE);
        }else {
            try {
                Picasso.get().load(noticeImage).into(myViewHolder.noticeImage);
            }catch (Exception e){}
        }



    }

    @Override
    public int getItemCount() {
        return noticList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView noticeImage;
        TextView noticeTitle,noticeTime,noticeDetailse;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            noticeImage = itemView.findViewById(R.id.notic_imageIv);
            noticeTitle = itemView.findViewById(R.id.notic_TitleTv);
            noticeTime = itemView.findViewById(R.id.noticDateTv);
            noticeDetailse = itemView.findViewById(R.id.notic_descriptionTv);
        }
    }
}
