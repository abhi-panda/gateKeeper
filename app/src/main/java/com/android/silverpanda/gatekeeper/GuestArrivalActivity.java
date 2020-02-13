package com.android.silverpanda.gatekeeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

    //XML Elements
    TextView guestNameTextView;
    TextView totalAdultGuestsTextView;
    TextView totalChildGuestsTextView;
    TextView totalAdultGuestsArrivedTextView;
    TextView totalChildGuestsArrivedTextView;

    Spinner adultGuestsArrivedSpinner;
    Spinner childGuestsArrivedSpinner;

    SharedPreferences sharedPref;

    RequestQueue queue;

    Guest newGuest =  new Guest();
    //POJO class Object
    final Guest guest = new Guest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_arrival);

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

        adultGuestsArrivedSpinner = findViewById(R.id.adultGuestsArrivedSpinner);
        childGuestsArrivedSpinner = findViewById(R.id.childGuestsArrivedSpinner);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // Generate remaining Guest values for spinner dropdown
    public String[] spinnerValueGenerator(int remainingPeople){
        if(remainingPeople == 0){
            String strArray[] = new String[1];
            strArray[0]="0";
            return  strArray;
        }
        String strArray[] = new String[remainingPeople+1];

        for(int i=0;i<=remainingPeople;i++){
            strArray[i]=String.valueOf(i);
        }
        return strArray;
    }

    // updates the Textfield in UI
    public void updateView(final Guest guest){
        guestNameTextView.setText(guest.getName());
        totalAdultGuestsTextView.setText(String.valueOf(guest.getTotalAdults()));
        totalChildGuestsTextView.setText(String.valueOf(guest.getTotalKids()));

        totalAdultGuestsArrivedTextView.setText(String.valueOf(guest.getAdultsArrived()));
        totalChildGuestsArrivedTextView.setText(String.valueOf(guest.getKidsArrived()));

        String remainingAdultGuests[] = spinnerValueGenerator(guest.getTotalAdults()- guest.getAdultsArrived());
        String remainingChildGuests[] = spinnerValueGenerator(guest.getTotalKids()- guest.getKidsArrived());


        //Dropdown adapter for Adult Guest
        ArrayAdapter adapterAdultGuest = new ArrayAdapter(this,android.R.layout.simple_spinner_item,remainingAdultGuests);
        adapterAdultGuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adultGuestsArrivedSpinner.setAdapter(adapterAdultGuest);
        adultGuestsArrivedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"Adult "+i,Toast.LENGTH_SHORT).show();
                newGuest.setAdultsArrived(guest.getAdultsArrived()+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                newGuest.setAdultsArrived(guest.getAdultsArrived());
            }
        });


        //Dropdown adapter for Child Guest
        ArrayAdapter adapterChildGuest = new ArrayAdapter(this,android.R.layout.simple_spinner_item,remainingChildGuests);
        adapterChildGuest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childGuestsArrivedSpinner.setAdapter(adapterChildGuest);
        childGuestsArrivedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),"Child "+i,Toast.LENGTH_SHORT).show();
                newGuest.setKidsArrived(guest.getKidsArrived()+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                newGuest.setKidsArrived(guest.getKidsArrived());
            }
        });

    }



    public void submitForm(View view){

        newGuest.setUid(guest.getUid());
        newGuest.setName(guest.getName());
        newGuest.setTotalAdults(guest.getTotalAdults());
        newGuest.setTotalKids(guest.getTotalKids());


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
                        updateView(guest);
                        Toast.makeText(getApplicationContext(),"Guest Count Updated!",Toast.LENGTH_SHORT).show();
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
