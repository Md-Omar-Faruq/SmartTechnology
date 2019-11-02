package com.example.smarttechnology.Teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowAllTeacheClass extends RecyclerView.Adapter<ShowAllTeacheClass.MyViewHolder>{


    private Context context;
    private List<TeacherAdd> uploadlist;
    private OnItemClickListener listener;



    public ShowAllTeacheClass(Context context, List<TeacherAdd> uploadlist) {
        this.context = context;
        this.uploadlist = uploadlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.all_teacher,viewGroup,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        TeacherAdd teacherAdd = uploadlist.get(i);

        String userImage = uploadlist.get(i).getTeacherImageUri();



        try {
            Picasso.get().load(userImage).placeholder(R.drawable.loginicon2).into(myViewHolder.teachercircularImageView);
            //Picasso.get().load(userImage).placeholder(R.drawable.loginicon2).into(myViewHolder.teachercircularImageView);
        }catch (Exception e){}


        myViewHolder.teachertName.setText(teacherAdd.getTeacherName());
        myViewHolder.teacherEmail.setText(teacherAdd.getTeacherEmail());
        myViewHolder.teacherMobile.setText(teacherAdd.getTeacherMobileNo());

    }

    @Override
    public int getItemCount() {
        return uploadlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView teachercircularImageView;
        TextView teachertName, teacherEmail, teacherMobile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            teachercircularImageView = itemView.findViewById(R.id.allTeacherImage);
            teachertName = itemView.findViewById(R.id.allTeachertName);
            teacherEmail = itemView.findViewById(R.id.allTeacherEmail);
            teacherMobile = itemView.findViewById(R.id.allTeacherMobile);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(listener != null){
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }

        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }
}
