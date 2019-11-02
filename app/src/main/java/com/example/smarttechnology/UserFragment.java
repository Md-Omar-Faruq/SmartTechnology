package com.example.smarttechnology;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttechnology.ModelUserRecyclerView.AdapterUsears;
import com.example.smarttechnology.ModelUserRecyclerView.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterUsears adapterUsears;
    List<ModelUser> userList;

    FirebaseAuth firebaseAuth;
    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // init recyclerView
        recyclerView = view.findViewById(R.id.userRecyclerView);
        // set RecyclerView Properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth = FirebaseAuth.getInstance();

        userList = new ArrayList<>();

        getAllusers();

        return view;
    }

    private void getAllusers() {
        // get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // get path of database named "User_Registation" containing user info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User_Registation");

        // get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            userList.clear();
            for(DataSnapshot ds: dataSnapshot.getChildren()){

                ModelUser modelUser = ds.getValue(ModelUser.class);

                // get all user except currently signed in user
                if(!modelUser.getuid().equals(firebaseUser.getUid())){
                    userList.add(modelUser);

                    // initial adapter
                    adapterUsears = new AdapterUsears(getActivity(), userList);
                    // set adapter to recycle view
                    recyclerView.setAdapter(adapterUsears);
                }
            }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUser(final String query) {

        // get current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // get path of database named "User_Registation" containing user info
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User_Registation");

        // get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelUser modelUser = ds.getValue(ModelUser.class);

                    // get all user except currently signed in user
                    if(!modelUser.getuid().equals(firebaseUser.getUid())){

                        if(modelUser.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                        modelUser.getEmail().toLowerCase().contains(query.toLowerCase())){
                            userList.add(modelUser);
                        }


                        // initial adapter
                        adapterUsears = new AdapterUsears(getActivity(), userList);
                        // refresh adapter
                        adapterUsears.notifyDataSetChanged();
                        // set adapter to recycle view
                        recyclerView.setAdapter(adapterUsears);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

   public void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){

        }else {
            startActivity(new Intent(getActivity(),LoginPage.class));
            getActivity().finish();
        }
   }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.user_layout_menu,menu);

        MenuItem item = menu.findItem(R.id.userLoyoutSearchMenu);
        SearchView searchView =(SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    getAllusers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    getAllusers();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.userLoyoutHomeMenu:
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();

            case R.id.logoutmenu:
                firebaseAuth.signOut();
                getActivity().finish();
                checkUserStatus();
        }


        return super.onOptionsItemSelected(item);
    }
}
