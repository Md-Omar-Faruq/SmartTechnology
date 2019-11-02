package com.example.smarttechnology.Student;

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

public class ShowAllStudentClass extends RecyclerView.Adapter<ShowAllStudentClass.MyViewHolder> {

    private Context context;
    private List<StudentAdd> uploadlist;
    private OnItemClickListener listener;

    public ShowAllStudentClass(Context context, List<StudentAdd> uploadlist) {
        this.context = context;
        this.uploadlist = uploadlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.all_student,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        StudentAdd studentAdd = uploadlist.get(i);

        String userImage = uploadlist.get(i).getStudentimageUri();


        try {
            Picasso.get().load(userImage).placeholder(R.drawable.loginicon2).into(myViewHolder.studentcircularImageView);
        }catch (Exception e){}


        myViewHolder.studentName.setText(studentAdd.getStudent_name());
        myViewHolder.studentEmail.setText(studentAdd.getStudent_email());
        myViewHolder.studentMobile.setText(studentAdd.getStudent_mobileNo());

    }

    @Override
    public int getItemCount() {
        return uploadlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircularImageView studentcircularImageView;
        TextView studentName, studentEmail, studentMobile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            studentcircularImageView = itemView.findViewById(R.id.allStudentImage);
            studentName = itemView.findViewById(R.id.allStudentName);
            studentEmail = itemView.findViewById(R.id.allStudentEmail);
            studentMobile = itemView.findViewById(R.id.allStudentMobile);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(listener != null){
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){
                    listener.OnItemClick(position);
                }
            }

        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);

    }

   public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
   }
}
