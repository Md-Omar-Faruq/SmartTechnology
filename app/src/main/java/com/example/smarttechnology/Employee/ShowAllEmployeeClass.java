package com.example.smarttechnology.Employee;

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

public class ShowAllEmployeeClass extends RecyclerView.Adapter<ShowAllEmployeeClass.MyViewHolder> {


    private Context context;
    private List<EmployeeAdd> uploadlist;
    private OnItemClickListener listener;

    public ShowAllEmployeeClass(Context context, List<EmployeeAdd> uploadlist) {
        this.context = context;
        this.uploadlist = uploadlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.all_employee,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        EmployeeAdd employeeAdd = uploadlist.get(i);

        String userImage = uploadlist.get(i).getEmployimageUri();



        try {
            Picasso.get().load(userImage).placeholder(R.drawable.loginicon2).into(myViewHolder.employeecircularImageView);
        }catch (Exception e){}



        myViewHolder.employeeName.setText(employeeAdd.getEmployName());
        myViewHolder.employeeEmail.setText(employeeAdd.getEmployEmail());
        myViewHolder.employeeMobile.setText(employeeAdd.getEmployMobileNo());

    }

    @Override
    public int getItemCount() {
        return uploadlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircularImageView employeecircularImageView;
        TextView employeeName, employeeEmail, employeeMobile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            employeecircularImageView = itemView.findViewById(R.id.allEmployeeImage);
            employeeName = itemView.findViewById(R.id.allEmployeeName);
            employeeEmail = itemView.findViewById(R.id.allEmployeeEmail);
            employeeMobile = itemView.findViewById(R.id.allEmployeeMobile);

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

    public void SetOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }


}
