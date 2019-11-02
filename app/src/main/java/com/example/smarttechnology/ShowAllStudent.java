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

import com.example.smarttechnology.Student.ShowAllStudentClass;
import com.example.smarttechnology.Student.StudentAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllStudent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShowAllStudentClass allStudent;
    private List<StudentAdd> studentList;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_student);

        getSupportActionBar().setTitle("All Student");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.allStudentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(ShowAllStudent.this);

        studentList = new ArrayList<>();

        getAllStudent();
    }

    private void getAllStudent(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Student_Addmission");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    StudentAdd studentAdd = ds.getValue(StudentAdd.class);
                    studentList.add(studentAdd);

                }

                allStudent = new ShowAllStudentClass(ShowAllStudent.this,studentList);
                recyclerView.setAdapter(allStudent);

                allStudent.setOnItemClickListener(new ShowAllStudentClass.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {

                        String email = studentList.get(position).getStudent_mobileNo();

                        Intent intent = new Intent(ShowAllStudent.this, singleStudentDetails.class);
                        intent.putExtra("student_mobileNo",email);
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Student_Addmission");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    StudentAdd studentAdd = ds.getValue(StudentAdd.class);

                    if(studentAdd.getStudent_name().toLowerCase().contains(query.toLowerCase()) ||
                    studentAdd.getStudent_email().toLowerCase().contains(query.toLowerCase()) ||
                    studentAdd.getStudent_batch().toLowerCase().contains(query.toLowerCase()) ||
                    studentAdd.getStudent_institute().toLowerCase().contains(query.toLowerCase()) ||
                    studentAdd.getStudent_country().toLowerCase().contains(query.toLowerCase()) ||
                    studentAdd.getStudent_mobileNo().toLowerCase().contains(query.toLowerCase())){

                        studentList.add(studentAdd);

                    }



                }

                allStudent = new ShowAllStudentClass(ShowAllStudent.this,studentList);
                recyclerView.setAdapter(allStudent);

                allStudent.setOnItemClickListener(new ShowAllStudentClass.OnItemClickListener() {
                    @Override
                    public void OnItemClick(int position) {

                        String email = studentList.get(position).getStudent_mobileNo();

                        Intent intent = new Intent(ShowAllStudent.this, singleStudentDetails.class);
                        intent.putExtra("student_mobileNo",email);
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
                    getAllStudent();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    getAllStudent();
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
