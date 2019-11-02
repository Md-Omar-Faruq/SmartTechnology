package com.example.smarttechnology.Finance;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static com.example.smarttechnology.R.id;
import static com.example.smarttechnology.R.layout;

public class FinanceAdapter extends ArrayAdapter<FinanceDataEntryClass> {

    private Activity context;
    private List<FinanceDataEntryClass> statementList;

    public static double income ;
    public static double totalIncome;
    public static double expense;
    public static double totalExpense;
    public static double currentAmmount;
    public static String getincomeexpense;


    public FinanceAdapter(Activity context,List<FinanceDataEntryClass> statementList) {
        super(context, layout.statement,statementList);
        this.context = context;
        this.statementList = statementList;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(layout.statement,null,true);

        FinanceDataEntryClass StatementList = statementList.get(position);

        TextView txnDate = view.findViewById(id.txnDateId);
        TextView inorex = view.findViewById(id.inorexId);
        TextView ammount = view.findViewById(id.ammountId);
        TextView narration = view.findViewById(id.narrationId);
        TextView txnid = view.findViewById(id.txnId);
        LinearLayout layout = view.findViewById(id.layoutId);

        txnDate.setText(StatementList.getTrnastionDate());
        inorex.setText(StatementList.getInorEx());
        ammount.setText(StatementList.getAmmount());
        narration.setText(StatementList.getNarration());
        txnid.setText(StatementList.getTranstionId());

        getincomeexpense = StatementList.getInorEx();



        if(getincomeexpense.equals("Expense")){

            layout.setBackgroundColor(-500081);

            }

        return view;
    }



}
