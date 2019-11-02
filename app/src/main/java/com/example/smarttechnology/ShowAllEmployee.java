package com.example.smarttechnology;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.Employee.EmployeeAdd;
import com.example.smarttechnology.Employee.ShowAllEmployeeClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllEmployee extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShowAllEmployeeClass allemployee;
    private List<EmployeeAdd> employeeList;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_employee);

        getSupportActionBar().setTitle("All Employee");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.allEmployeeRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(ShowAllEmployee.this);

        employeeList = new ArrayList<>();

        getAllEmployee();
    }

    private void getAllEmployee() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Employee_Addmission");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                employeeList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    EmployeeAdd employeeAdd = ds.getValue(EmployeeAdd.class);
                    employeeList.add(employeeAdd);

                }
                allemployee = new ShowAllEmployeeClass(ShowAllEmployee.this,employeeList);
                recyclerView.setAdapter(allemployee);

                allemployee.SetOnItemClickListener(new ShowAllEmployeeClass.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        String email = employeeList.get(position).getEmployMobileNo();

                        Intent intent = new Intent(ShowAllEmployee.this,singleEmployeeDetails.class);
                        intent.putExtra("employMobileNo",email);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(getApplicationContext(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void searchUser(final String query) {

        databaseReference = FirebaseDatabase.getInstance().getReference("Employee_Addmission");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                employeeList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    EmployeeAdd employeeAdd = ds.getValue(EmployeeAdd.class);

                    if(employeeAdd.getEmployName().toLowerCase().contains(query.toLowerCase()) ||
                            employeeAdd.getEmployEmail().toLowerCase().contains(query.toLowerCase()) ||
                            employeeAdd.getEmployCountrt().toLowerCase().contains(query.toLowerCase()) ||
                            employeeAdd.getEmployMobileNo().toLowerCase().contains(query.toLowerCase())
                    ){

                        employeeList.add(employeeAdd);
                    }

                }
                allemployee = new ShowAllEmployeeClass(ShowAllEmployee.this,employeeList);
                recyclerView.setAdapter(allemployee);

                allemployee.SetOnItemClickListener(new ShowAllEmployeeClass.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {
                        String email = employeeList.get(position).getEmployMobileNo();

                        Intent intent = new Intent(ShowAllEmployee.this,singleEmployeeDetails.class);
                        intent.putExtra("employMobileNo",email);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(getApplicationContext(),"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.searchmenu,menu);

        MenuItem item = menu.findItem(R.id.searchId);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    getAllEmployee();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    getAllEmployee();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
