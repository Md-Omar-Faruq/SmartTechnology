package com.example.smarttechnology;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.smarttechnology.Notic.Notic;
import com.example.smarttechnology.Notic.NoticAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoticBord extends AppCompatActivity {

    // Network Connection Status
    boolean wifiConnection,mobileConntectio;
    NetworkInfo activeInfo;

    RecyclerView noticerecyclerView;
    List<Notic> noticList;
    NoticAdapter noticAdapter;
    private DatabaseReference noticeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notic_bord);

        getSupportActionBar().setTitle("Notice Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Network Connection Status
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo = conManager.getActiveNetworkInfo();

        noticerecyclerView = findViewById(R.id.noticRecyclerViewId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        noticerecyclerView.setLayoutManager(layoutManager);

        noticList = new ArrayList<>();

        if(activeInfo != null && activeInfo.isConnected()){
            wifiConnection = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConntectio = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnection || mobileConntectio){

                loadNotice();
            }
        }else {
            String massage = "No Internet Connection Please Connect the Device to Internet";
            Toast.makeText(getApplicationContext(),massage,Toast.LENGTH_LONG).show();
        }
    }

    private void loadNotice() {
        noticeRef = FirebaseDatabase.getInstance().getReference("Notic");

        noticeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                noticList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    Notic notic = ds.getValue(Notic.class);
                    noticList.add(notic);

                }

                noticAdapter = new NoticAdapter(NoticBord.this, noticList);
                noticerecyclerView.setAdapter(noticAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void searchUser(final String query) {

        noticeRef = FirebaseDatabase.getInstance().getReference("Notic");

        noticeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                noticList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Notic notic = ds.getValue(Notic.class);

                    if(notic.getNoticTitle().toLowerCase().contains(query.toLowerCase()) ||
                            notic.getNoticTime().toLowerCase().contains(query.toLowerCase()) ||
                            notic.getDescription().toLowerCase().contains(query.toLowerCase())
                    ){

                        noticList.add(notic);
                    }



                }

                noticAdapter = new NoticAdapter(NoticBord.this, noticList);
                noticerecyclerView.setAdapter(noticAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.createnoticmenu,menu);

        MenuItem item = menu.findItem(R.id.notic_searchId);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    loadNotice();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    loadNotice();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.createNoticMenu:
                Intent intentstudent = new Intent(NoticBord.this, CreateNotic.class);
                startActivity(intentstudent);
                break;

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
