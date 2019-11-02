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

import com.example.smarttechnology.Teacher.ShowAllTeacheClass;
import com.example.smarttechnology.Teacher.TeacherAdd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllTeacher extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShowAllTeacheClass allTeacher;
    private List<TeacherAdd> teacherList;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_teacher);

        getSupportActionBar().setTitle("All Teacher");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.allTeacherRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(ShowAllTeacher.this);

        teacherList = new ArrayList<>();

        getAllTeacher();
    }

    private void getAllTeacher() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher_Addmission");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teacherList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    TeacherAdd teacherAdd = ds.getValue(TeacherAdd.class);
                    teacherList.add(teacherAdd);

                }

                allTeacher = new ShowAllTeacheClass(ShowAllTeacher.this,teacherList);
                recyclerView.setAdapter(allTeacher);

                allTeacher.setOnItemClickListener(new ShowAllTeacheClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        String email = teacherList.get(position).getTeacherId();
                        //String funall = teacherList.get(position).getTeacherName();

                        Intent intent = new Intent(ShowAllTeacher.this,singleTeacherDetails.class);
                        intent.putExtra("teacherId",email);
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher_Addmission");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teacherList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    TeacherAdd teacherAdd = ds.getValue(TeacherAdd.class);

                    if(teacherAdd.getTeacherName().toLowerCase().contains(query.toLowerCase()) ||
                            teacherAdd.getTeacherEmail().toLowerCase().contains(query.toLowerCase()) ||
                            teacherAdd.getTeacherCountry().toLowerCase().contains(query.toLowerCase()) ||
                            teacherAdd.getTeacherMobileNo().toLowerCase().contains(query.toLowerCase())
                            ){

                        teacherList.add(teacherAdd);
                    }

                }

                allTeacher = new ShowAllTeacheClass(ShowAllTeacher.this,teacherList);
                recyclerView.setAdapter(allTeacher);

                allTeacher.setOnItemClickListener(new ShowAllTeacheClass.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        String email = teacherList.get(position).getTeacherId();
                        //String funall = teacherList.get(position).getTeacherName();

                        Intent intent = new Intent(ShowAllTeacher.this,singleTeacherDetails.class);
                        intent.putExtra("teacherId",email);
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
                    getAllTeacher();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    getAllTeacher();
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
