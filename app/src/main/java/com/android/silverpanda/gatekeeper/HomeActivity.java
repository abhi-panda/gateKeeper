package com.android.silverpanda.gatekeeper;

import androidx.appcompat.app.AppCompatActivity;
import org.json.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button scan_code= findViewById(R.id.scan_code);
        Button check_count= findViewById(R.id.check_count);
        Button verify_conn= findViewById(R.id.verify_conn);
        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);

        if(!sharedPref.contains("IP")){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("IP","192.168.0.123");
            editor.commit();
        }

        scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ScanActivity.class));
            }
        });
        check_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCount();
            }
        });
        verify_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyConn();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_ip_menu : Intent ip_settings=new Intent(this,IPMenuActivity.class);
                startActivity(ip_settings);
                finish();
                break;
            default: break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ip_menu, menu);
        return true;
    }

    private  void getCount(){
        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        String ip_address=sharedPref.getString("IP",null);
        final String parsedText="http://"+ip_address+":3000/transcend/guestCount/totals";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, parsedText,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseCountData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });

        stringRequest.setTag("count");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void parseCountData(String response) {
        Intent check_count_intent=new Intent(this,CheckCountActivity.class);

        try {
            JSONObject jsonObject = new JSONObject(response);
            String totalKids  = jsonObject.getString("totalKids");
            String totalAdults  = jsonObject.getString("totalAdults");
            String totalKidsArrived  = jsonObject.getString("totalKidsArrived");
            String totalAdultsArrived  = jsonObject.getString("totalAdultsArrived");

            check_count_intent.putExtra("totalKids",totalKids);
            check_count_intent.putExtra("totalAdults",totalAdults);
            check_count_intent.putExtra("totalKidsArrived",totalKidsArrived);
            check_count_intent.putExtra("totalAdultsArrived",totalAdultsArrived);
            startActivity(check_count_intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private  void verifyConn(){
        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        String ip_address=sharedPref.getString("IP",null);
        final String parsedText="http://"+ip_address+":3000/transcend/connection/verify";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, parsedText,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseVerifyData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this,"Could not verify Connection -> "+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
        stringRequest.setTag("verify");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseVerifyData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String connection  = jsonObject.getString("connection");

            if(connection.equals("successful")){
                Toast.makeText(HomeActivity.this,"Connection Successfully Verified! ",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(HomeActivity.this,"Could not verify Connection "+connection,Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
