package com.android.silverpanda.gatekeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IPMenuActivity  extends AppCompatActivity {
    private static EditText ip_address;
    private static Button save;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipmenu);
        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        ip_address=findViewById(R.id.ip);
        ImageButton save=findViewById(R.id.btn_save);
        //Setting the IP address
        ip_address.setText(sharedPref.getString("IP",null));
        editor = sharedPref.edit();
        //Context context = getApplicationContext();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value= ip_address.getText().toString();
                editor.putString("IP",value);
                editor.commit();
                Intent intent = new Intent(IPMenuActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
