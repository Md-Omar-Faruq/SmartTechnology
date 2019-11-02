package com.example.smarttechnology;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentConsultancy extends AppCompatActivity {

    private TextView textautho,textautho2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_consultancy);

        getSupportActionBar().setTitle("Student Consultancy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textautho = findViewById(R.id.textautho);
        textautho2 = findViewById(R.id.textautho2);
        textautho.setSelected(true);
        textautho2.setSelected(true);
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
