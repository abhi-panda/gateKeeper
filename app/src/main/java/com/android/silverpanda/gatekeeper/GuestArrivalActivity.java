package com.android.silverpanda.gatekeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class GuestArrivalActivity extends AppCompatActivity{

    TextView guestNameTextView;
    TextView totalAdultGuestsTextView;
    TextView totalChildGuestsTextView;
    TextView totalAdultGuestsArrivedTextView;
    TextView totalChildGuestsArrivedTextView;
    RadioGroup adultsArrivedNowRadioGroup;
    RadioGroup KidsArrivedNowRadioGroup;

    int check = 0;

    SharedPreferences sharedPref;

    RequestQueue queue;

    Guest newGuest =  new Guest();
    //POJO class Object
    final Guest guest = new Guest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_arrival);
        adultsArrivedNowRadioGroup = findViewById(R.id.radiogroupAdultArrived);
        KidsArrivedNowRadioGroup = findViewById(R.id.radiogroupChildArrived);
        Button home_btn = findViewById(R.id.btnHome);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuestArrivalActivity.this, HomeActivity.class));
                finish();
            }
        });

        //Getting reference to XML objects
        guestNameTextView = findViewById(R.id.guestNameTextView);
        totalAdultGuestsTextView = findViewById(R.id.totalAdultGuestsTextView);
        totalChildGuestsTextView =findViewById(R.id.totalChildGuestsTextView);

        totalAdultGuestsArrivedTextView = findViewById(R.id.totalAdultGuestsArrivedTextView);
        totalChildGuestsArrivedTextView = findViewById(R.id.totalChildGuestsArrivedTextView);


        // To get BarcodeId from Intent
        Bundle extras = getIntent().getExtras();
        String guestBarcodeId = (extras.get("scanned_code").toString());

        //Setting up the URl
        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        String url ="http://"+sharedPref.getString("IP",null)+":3000/transcend/"+guestBarcodeId;
        Log.d("IP ",url);


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

        // Request a json response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            //Adding all the properties of scanned Guest
                            guest.setUid(response.getString("uid"));
                            guest.setName(response.getString("name"));
                            guest.setTotalAdults(Integer.parseInt(response.getString("totalAdults")));
                            guest.setTotalKids(Integer.parseInt(response.getString("totalKids")));
                            guest.setAdultsArrived(Integer.parseInt(response.getString("adultsArrived")));
                            guest.setKidsArrived(Integer.parseInt(response.getString("kidsArrived")));
                            updateView(guest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }




    public void addRadioButtons(int adultsArrivedNumber,int childsArrivedNumber) {
        adultsArrivedNowRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(800,80);
        params.gravity= Gravity.CENTER;
        adultsArrivedNowRadioGroup.setLayoutParams(params);

        for (int i = 1; i <= adultsArrivedNumber; i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(String.valueOf(i));
            rdbtn.setPadding(10,0,30,0);
            rdbtn.setTextAppearance(R.style.TextAppearance_AppCompat_Display1);
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedId = adultsArrivedNowRadioGroup.getCheckedRadioButtonId();

                    // find the radio button by returned id
                    RadioButton radioButton = findViewById(selectedId);
                    newGuest.setAdultsArrived(guest.getAdultsArrived()+ Integer.parseInt((String)(radioButton.getText())));

                }
            });
            adultsArrivedNowRadioGroup.addView(rdbtn);
        }
        KidsArrivedNowRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
        RadioGroup.LayoutParams params1 = new RadioGroup.LayoutParams(800,80);
        params.gravity= Gravity.CENTER;
        KidsArrivedNowRadioGroup.setLayoutParams(params1);
        //
        for (int i = 1; i <= childsArrivedNumber; i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(String.valueOf(i));
            rdbtn.setPadding(10,0,30,0);
            rdbtn.setTextAppearance(R.style.TextAppearance_AppCompat_Display1);
            rdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedId = KidsArrivedNowRadioGroup.getCheckedRadioButtonId();

                    // find the radio button by returned id
                    RadioButton radioButton = findViewById(selectedId);
                    newGuest.setKidsArrived(guest.getKidsArrived()+ Integer.parseInt((String)(radioButton.getText())));
                }
            });
            KidsArrivedNowRadioGroup.addView(rdbtn);
        }

    }

    // updates the Textfield in UI
    public void updateView(final Guest guest){
        guestNameTextView.setText(guest.getName());
        totalAdultGuestsTextView.setText(String.valueOf(guest.getTotalAdults()));
        totalChildGuestsTextView.setText(String.valueOf(guest.getTotalKids()));

        totalAdultGuestsArrivedTextView.setText(String.valueOf(guest.getAdultsArrived()));
        totalChildGuestsArrivedTextView.setText(String.valueOf(guest.getKidsArrived()));

        addRadioButtons(guest.getTotalAdults()- guest.getAdultsArrived(),guest.getTotalKids()-guest.getKidsArrived());


    }



    public void submitForm(View view){

        newGuest.setUid(guest.getUid());
        newGuest.setName(guest.getName());
        newGuest.setTotalAdults(guest.getTotalAdults());
        newGuest.setTotalKids(guest.getTotalKids());

        if(KidsArrivedNowRadioGroup.getCheckedRadioButtonId() == -1){
            check++;
            newGuest.setKidsArrived(guest.getKidsArrived());
        }
        if(adultsArrivedNowRadioGroup.getCheckedRadioButtonId() == -1){
            check++;
            newGuest.setAdultsArrived(guest.getAdultsArrived());
        }

        sharedPref = getSharedPreferences("A", Context.MODE_PRIVATE);
        String url ="http://"+sharedPref.getString("IP",null)+":3000/transcend/"+newGuest.getUid();


        JSONObject reqObject = null;

        try {
            reqObject = new JSONObject(newGuest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Request a json response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                reqObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Adding all the properties of scanned Guest
                        try {
                            guest.setUid(response.getString("uid"));
                            guest.setName(response.getString("name"));
                            guest.setTotalAdults(Integer.parseInt(response.getString("totalAdults")));
                            guest.setTotalKids(Integer.parseInt(response.getString("totalKids")));
                            guest.setAdultsArrived(Integer.parseInt(response.getString("adultsArrived")));
                            guest.setKidsArrived(Integer.parseInt(response.getString("kidsArrived")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(check==2){
                            Toast.makeText(getApplicationContext(),"Submit pressed w/o info",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Guest Count Updated!",Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(GuestArrivalActivity.this , HomeActivity.class));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Update Response",error.toString());

                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }

        );

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
