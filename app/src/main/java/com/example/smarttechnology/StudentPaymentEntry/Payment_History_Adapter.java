package com.example.smarttechnology.StudentPaymentEntry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.smarttechnology.R;

import java.util.List;

public class Payment_History_Adapter extends ArrayAdapter<FeesPaymentEntryClass> {

    private Activity context;
    private List<FeesPaymentEntryClass> paymentHistoryList;

    public Payment_History_Adapter(Activity context, List<FeesPaymentEntryClass> paymentHistoryList) {
        super(context, R.layout.payment_history, paymentHistoryList);
        this.context = context;
        this.paymentHistoryList = paymentHistoryList;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.payment_history,null,false);

        FeesPaymentEntryClass paymentHistory = paymentHistoryList.get(position);

        TextView id = view.findViewById(R.id.paymentHid);
        TextView name = view.findViewById(R.id.paymentHname);
        TextView number = view.findViewById(R.id.paymentHmobile);
        TextView date = view.findViewById(R.id.paymentHpDate);
        TextView total = view.findViewById(R.id.paymentHTotalFees);
        TextView discount = view.findViewById(R.id.paymentHDiscount);
        TextView after = view.findViewById(R.id.paymentHafterTotal);
        TextView paid = view.findViewById(R.id.paymentHpaid);
        TextView due = view.findViewById(R.id.paymentHdue);

        id.setText(paymentHistory.getStudentPid());
        name.setText(paymentHistory.getStudentPname());
        number.setText(paymentHistory.getStudentPmobileNo());
        date.setText(paymentHistory.getPaymentDate());
        total.setText(paymentHistory.getStudentPcourseFees());
        discount.setText(paymentHistory.getDiscountAmmount());
        after.setText(paymentHistory.getAfterTotal());
        paid.setText(paymentHistory.getPaidAmmount());
        due.setText(paymentHistory.getDueAmmount());


        return view;
    }
}
