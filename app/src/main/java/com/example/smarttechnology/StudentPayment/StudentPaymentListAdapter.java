package com.example.smarttechnology.StudentPayment;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.R;
import com.example.smarttechnology.Student.StudentAdd;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentPaymentListAdapter extends RecyclerView.Adapter<StudentPaymentListAdapter.MyViewHolderPayment>{

    private Context context;
    private List<StudentAdd> studentList;
    private OnItemClickListener listener;

    public StudentPaymentListAdapter(Context context, List<StudentAdd> uploadlist) {
        this.context = context;
        this.studentList = uploadlist;
    }

    @NonNull
    @Override
    public StudentPaymentListAdapter.MyViewHolderPayment onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.studentpaymentlist,viewGroup,false);

        return new MyViewHolderPayment(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentPaymentListAdapter.MyViewHolderPayment myViewHolderPayment, int i) {

        StudentAdd studentAdd = studentList.get(i);

        String userImage = studentList.get(i).getStudentimageUri();


        try {
            Picasso.get().load(userImage).placeholder(R.drawable.loginicon2).into(myViewHolderPayment.paymentstudentcircularImageView);
        }catch (Exception e){}


        myViewHolderPayment.paymentstudentName.setText(studentAdd.getStudent_name());
        myViewHolderPayment.paymentstudentEmail.setText(studentAdd.getStudent_email());
        myViewHolderPayment.paymentstudentMobile.setText(studentAdd.getStudent_mobileNo());

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }




    public class MyViewHolderPayment extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {

        CircularImageView paymentstudentcircularImageView;
        TextView paymentstudentName, paymentstudentEmail, paymentstudentMobile;

        public MyViewHolderPayment(@NonNull View itemView) {
            super(itemView);

            paymentstudentcircularImageView = itemView.findViewById(R.id.paymentallStudentImage);
            paymentstudentName = itemView.findViewById(R.id.paymentallStudentName);
            paymentstudentEmail = itemView.findViewById(R.id.paymentallStudentEmail);
            paymentstudentMobile = itemView.findViewById(R.id.paymentallStudentMobile);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {

            if(listener!=null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.OnItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Choose an action");
            MenuItem feesPayment = menu.add(Menu.NONE,1,1,"Fees Payment");
            MenuItem paymentHistioy = menu.add(Menu.NONE,2,2,"Payment History");

            feesPayment.setOnMenuItemClickListener(this);
            paymentHistioy.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(listener!=null){
                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION){

                    switch (item.getItemId()){

                        case 1:

                            listener.OnFeesPayment(position);
                            return true;

                        case 2:

                            listener.OnPaymentHistory(position);
                            return true;
                    }
                }
            }

            return false;
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
        void OnFeesPayment(int position);
        void OnPaymentHistory(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }
}
