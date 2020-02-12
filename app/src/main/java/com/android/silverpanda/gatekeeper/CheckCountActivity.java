package com.android.silverpanda.gatekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.os.Bundle;

public class CheckCountActivity extends AppCompatActivity {
    public static TextView totalAdultGuests;
    public static TextView totalChildGuests;
    public static TextView totalAdultGuestsArrived;
    public static TextView totalChildGuestsArrived;
    public static Button home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_count);
        totalAdultGuests = findViewById(R.id.totalAdultGuestsTextViewCount);
        totalChildGuests = findViewById(R.id.totalChildGuestsTextViewCount);
        totalAdultGuestsArrived = findViewById(R.id.totalAdultGuestsArrivedTextViewCount);
        totalChildGuestsArrived = findViewById(R.id.totalChildGuestsArrivedTextViewCount);
        home = findViewById(R.id.btnHomeCount);
        totalAdultGuests.setText(getIntent().getExtras().getString("totalAdults"));
        totalChildGuests.setText(getIntent().getExtras().getString("totalKids"));
        totalAdultGuestsArrived.setText(getIntent().getExtras().getString("totalAdultsArrived"));
        totalChildGuestsArrived.setText(getIntent().getExtras().getString("totalKidsArrived"));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckCountActivity.this,HomeActivity.class));
                finish();
            }
        });
    }
}