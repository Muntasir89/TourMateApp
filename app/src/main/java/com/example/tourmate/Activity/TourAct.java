package com.example.tourmate.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tourmate.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TourAct extends AppCompatActivity {
    String[] yesNoList = {"Yes", "No"};
    String[] hotelTypeList = {"***/****", "*****"};
    String[] transportList = {"Bus", "Taxi", "Local transport"};
    String[] placeList = {"Kuakata", "Rangamati", "Cox's bazar", "Nighum dwip", "Manpura dwip", "Sajek", "Sylhet", "Sundarbans", "Srimangal", "Saint-martin", "Dhaka", "Rajshahi", "Bandarban", "Chattogram", "Barishal", "Mymensingh", "Khulna"};
    String[] attractionsList = {"Natural", "Man-made"};

    TextInputEditText daysTIET, budgetTIET;

    AutoCompleteTextView seaLoverACTV, mountainLoverACTV, historyLoverACTV, availEntFaciACTV, hotelNeededACTV, hotelTypeACTV, transportACTV, placeACTV, travelGuideACTV, attractionsACTV, travellingPartnerACTV, safeACTV, foodStallsACTV, localFriendshipACTV, startingPointACTV;

    ArrayAdapter<String>yesNoAda, hotelTypeACTVAda, placeACTVAda;

    String seaLoverACTVStr, mountainLoverACTVStr, historyLoverACTVStr, availEntFaciACTVStr,
            hotelNeededACTVStr, hotelTypeACTVStr,  daysTIETStr, transportACTVStr, budgetTIETStr,
            placeACTVStr, travelGuideACTVStr, attractionsACTVStr, travellingPartnerACTVStr, safeACTVStr,
            foodStallsACTVStr, localFriendshipACTVStr, startingPointACTVStr;

    String url = "https://web-production-bbbd.up.railway.app/predict";
    Button predictBtn;

    FirebaseAuth FAuthObj; static FirebaseUser FUserObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tour);

        FAuthObj = FirebaseAuth.getInstance();

        //initializing ACTV
        seaLoverACTV = findViewById(R.id.seaLoverACTV);
        mountainLoverACTV = findViewById(R.id.mountainLoverACTV);
        historyLoverACTV = findViewById(R.id.historyLoverACTV);
        availEntFaciACTV = findViewById(R.id.availEntFaciACTV);
        hotelNeededACTV = findViewById(R.id.hotelNeededACTV);
        hotelTypeACTV = findViewById(R.id.hotelTypeACTV);
        transportACTV = findViewById(R.id.transportACTV);
        placeACTV = findViewById(R.id.placeACTV);
        travelGuideACTV = findViewById(R.id.travelGuideACTV);
        attractionsACTV = findViewById(R.id.attractionsACTV);
        travellingPartnerACTV = findViewById(R.id.travellingPartnerACTV);
        safeACTV = findViewById(R.id.safeACTV);
        foodStallsACTV = findViewById(R.id.foodStallsACTV);
        localFriendshipACTV = findViewById(R.id.localFriendshipACTV);
        startingPointACTV = findViewById(R.id.startingPointACTV);

        //TIET
        daysTIET = findViewById(R.id.daysTIET);
        budgetTIET = findViewById(R.id.budgetTIET);

        //Button
        predictBtn = findViewById(R.id.predictBtn);

        //Initializing ACTV adapters
        yesNoAda = new ArrayAdapter<>(this, R.layout.auto_comp_item, yesNoList);
        hotelTypeACTVAda = new ArrayAdapter<>(this, R.layout.auto_comp_item, hotelTypeList);
        placeACTVAda = new ArrayAdapter<>(this, R.layout.auto_comp_item, placeList);

        //Setting ACTV adapters
        seaLoverACTV.setAdapter(yesNoAda);
        mountainLoverACTV.setAdapter(yesNoAda);
        historyLoverACTV.setAdapter(yesNoAda);
        availEntFaciACTV.setAdapter(yesNoAda);
        hotelNeededACTV.setAdapter(yesNoAda);
        hotelTypeACTV.setAdapter(hotelTypeACTVAda);
        transportACTV.setAdapter(yesNoAda);
        placeACTV.setAdapter(placeACTVAda);
        travelGuideACTV.setAdapter(yesNoAda);
        attractionsACTV.setAdapter(yesNoAda);
        travellingPartnerACTV.setAdapter(yesNoAda);
        safeACTV.setAdapter(yesNoAda);
        foodStallsACTV.setAdapter(yesNoAda);
        localFriendshipACTV.setAdapter(yesNoAda);
        startingPointACTV.setAdapter(placeACTVAda);


        //ClickListener
        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessPrediction();
                //PredicitonResultNoDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ProcessPrediction() {
        LoadAllString();
        if(TextUtils.isEmpty(seaLoverACTVStr)  || TextUtils.isEmpty(mountainLoverACTVStr) || TextUtils.isEmpty(historyLoverACTVStr)
                || TextUtils.isEmpty(availEntFaciACTVStr) || TextUtils.isEmpty(hotelNeededACTVStr)
                || TextUtils.isEmpty(hotelTypeACTVStr) || TextUtils.isEmpty(daysTIETStr)
                || TextUtils.isEmpty(transportACTVStr) || TextUtils.isEmpty(budgetTIETStr)
                || TextUtils.isEmpty(placeACTVStr) || TextUtils.isEmpty(travelGuideACTVStr)
                || TextUtils.isEmpty(attractionsACTVStr) || TextUtils.isEmpty(travellingPartnerACTVStr)
                || TextUtils.isEmpty(safeACTVStr) || TextUtils.isEmpty(foodStallsACTVStr)
                || TextUtils.isEmpty(localFriendshipACTVStr) || TextUtils.isEmpty(startingPointACTVStr)){
            Toast.makeText(this, "Some field are empty", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(this, seaLoverACTVStr+" "+mountainLoverACTVStr+" "+historyLoverACTVStr+" "+availEntFaciACTVStr+" "+hotelNeededACTVStr+" "+hotelTypeACTVStr+" "+transportACTVStr+" "+daysTIETStr+" "+placeACTVStr+" "+budgetTIETStr+" "+travelGuideACTVStr+" "+attractionsACTVStr+" "+travellingPartnerACTVStr+" "+safeACTVStr+" "+foodStallsACTVStr+" "+localFriendshipACTVStr+" "+startingPointACTVStr, Toast.LENGTH_SHORT).show();
            SendRequest();
        }

    }

    private void SendRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(
                            response);
                    String data = jsonObject.getString("suitable trip");

                    if(data.equals("1")){
                        PredicitonResultYesDialog();
                        //Toast.makeText(TourAct.this, "equals", Toast.LENGTH_SHORT).show();
                    }else{
                        PredicitonResultNoDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TourAct", error.getMessage().toString());
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();

                params.put("sea_lover", seaLoverACTVStr);
                params.put("mountain_lover", mountainLoverACTVStr);
                params.put("history_lover", historyLoverACTVStr);
                params.put("entertainment_lover", availEntFaciACTVStr);
                params.put("need_hotel", hotelNeededACTVStr);
                params.put("hotel_type", hotelTypeACTVStr);
                params.put("need_transport", transportACTVStr);
                params.put("days", daysTIETStr);
                params.put("place", placeACTVStr);
                params.put("budget", budgetTIETStr);
                params.put("travel_guide", travelGuideACTVStr);
                params.put("prefer_attractions", attractionsACTVStr);
                params.put("traveling_partner", travellingPartnerACTVStr);
                params.put("prefer_safety", safeACTVStr);
                params.put("foodie",foodStallsACTVStr);
                params.put("tourist_friendly_place",localFriendshipACTVStr );
                params.put("starting_point", startingPointACTVStr);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(TourAct.this);
        queue.add(stringRequest);
    }

    private void LoadAllString() {
            //For Gender
            seaLoverACTVStr = seaLoverACTV.getText().toString();
            if(seaLoverACTVStr.equals("Yes"))
                seaLoverACTVStr = "1";
            else if(seaLoverACTVStr.equals("No"))
                seaLoverACTVStr = "0";

            //For /* seaLover
        mountainLoverACTVStr = mountainLoverACTV.getText().toString();
            if(mountainLoverACTVStr.equals("Yes"))
                mountainLoverACTVStr = "1";
            else
                mountainLoverACTVStr = "0";
            //For Housing
        historyLoverACTVStr = historyLoverACTV.getText().toString();
            if(historyLoverACTVStr.equals("Yes"))
                historyLoverACTVStr = "1";
            else
                historyLoverACTVStr = "0";
            //For Education
        availEntFaciACTVStr = availEntFaciACTV.getText().toString();
            if(availEntFaciACTVStr.equals("Yes"))
                availEntFaciACTVStr = "1";
            else
                availEntFaciACTVStr = "0";
            //For PropertyArea
        hotelNeededACTVStr = hotelNeededACTV.getText().toString();
            if(hotelNeededACTVStr.equals("Yes"))
                hotelNeededACTVStr = "1";
            else
                hotelNeededACTVStr = "0";
            //For Business Type
        hotelTypeACTVStr = hotelTypeACTV.getText().toString();
            if(hotelTypeACTVStr.equals("***/****"))
                hotelTypeACTVStr = "1";
            else if(hotelTypeACTVStr.equals("*****"))
                hotelTypeACTVStr = "0";
            //Days
        daysTIETStr = daysTIET.getText().toString();

        transportACTVStr = transportACTV.getText().toString();
        if(transportACTVStr.equals("Yes"))
            transportACTVStr = "1";
        else
            transportACTVStr = "0";
        budgetTIETStr = budgetTIET.getText().toString().trim();

        placeACTVStr = placeACTV.getText().toString().trim();
        if(placeACTVStr.equals("Kuakata"))
            placeACTVStr = "1";
        else if(placeACTVStr.equals("Rangamati"))
            placeACTVStr = "2";
        else if(placeACTVStr.equals("Cox's bazar"))
            placeACTVStr = "3";
        else if(placeACTVStr.equals("Nighum dwip"))
            placeACTVStr = "4";
        else if(placeACTVStr.equals("Manpura dwip"))
            placeACTVStr = "5";
        else if(placeACTVStr.equals("Sajek"))
            placeACTVStr = "6";
        else if(placeACTVStr.equals("Sylhet"))
            placeACTVStr = "7";
        else if(placeACTVStr.equals("Sundarbans"))
            placeACTVStr = "8";
        else if(placeACTVStr.equals("Srimangal"))
            placeACTVStr = "9";
        else if(placeACTVStr.equals("Saint-martin"))
            placeACTVStr = "10";
        else if(placeACTVStr.equals("Dhaka"))
            placeACTVStr = "11";
        else if(placeACTVStr.equals("Rajshahi"))
            placeACTVStr = "12";
        else if(placeACTVStr.equals("Bandarban"))
            placeACTVStr = "13";
        else if(placeACTVStr.equals("Chattogram"))
            placeACTVStr = "14";
        else if(placeACTVStr.equals("Barishal"))
            placeACTVStr = "15";
        else if(placeACTVStr.equals("Mymensingh"))
            placeACTVStr = "16";
        else if(placeACTVStr.equals("Khulna"))
            placeACTVStr = "17";

        travelGuideACTVStr = travelGuideACTV.getText().toString().trim();
        if(travelGuideACTVStr.equals("Yes"))
            travelGuideACTVStr = "1";
        else
            travelGuideACTVStr = "0";

        attractionsACTVStr = attractionsACTV.getText().toString().trim();
        if(attractionsACTVStr.equals("Yes"))
            attractionsACTVStr = "1";
        else
            attractionsACTVStr = "0";

        travellingPartnerACTVStr = travellingPartnerACTV.getText().toString().trim();
        if(travellingPartnerACTVStr.equals("Yes"))
            travellingPartnerACTVStr = "1";
        else
            travellingPartnerACTVStr = "0";

        safeACTVStr = safeACTV.getText().toString().trim();
        if(safeACTVStr.equals("Yes"))
            safeACTVStr = "1";
        else
            safeACTVStr = "0";

        foodStallsACTVStr = foodStallsACTV.getText().toString().trim();
        if(foodStallsACTVStr.equals("Yes"))
            foodStallsACTVStr = "1";
        else
            foodStallsACTVStr = "0";

        localFriendshipACTVStr = localFriendshipACTV.getText().toString().trim();
        if(localFriendshipACTVStr.equals("Yes"))
            localFriendshipACTVStr = "1";
        else
            localFriendshipACTVStr = "0";

        startingPointACTVStr = startingPointACTV.getText().toString().trim();
        if(startingPointACTVStr.equals("Kuakata"))
            startingPointACTVStr = "1";
        else if(startingPointACTVStr.equals("Rangamati"))
            startingPointACTVStr = "2";
        else if(startingPointACTVStr.equals("Cox's bazar"))
            startingPointACTVStr = "3";
        else if(startingPointACTVStr.equals("Nighum dwip"))
            startingPointACTVStr = "4";
        else if(startingPointACTVStr.equals("Manpura dwip"))
            startingPointACTVStr = "5";
        else if(startingPointACTVStr.equals("Sajek"))
            startingPointACTVStr = "6";
        else if(startingPointACTVStr.equals("Sylhet"))
            startingPointACTVStr = "7";
        else if(startingPointACTVStr.equals("Sundarbans"))
            startingPointACTVStr = "8";
        else if(startingPointACTVStr.equals("Srimangal"))
            startingPointACTVStr = "9";
        else if(startingPointACTVStr.equals("Saint-martin"))
            startingPointACTVStr = "10";
        else if(startingPointACTVStr.equals("Dhaka"))
            startingPointACTVStr = "11";
        else if(startingPointACTVStr.equals("Rajshahi"))
            startingPointACTVStr = "12";
        else if(startingPointACTVStr.equals("Bandarban"))
            startingPointACTVStr = "13";
        else if(startingPointACTVStr.equals("Chattogram"))
            startingPointACTVStr = "14";
        else if(startingPointACTVStr.equals("Barishal"))
            startingPointACTVStr = "15";
        else if(startingPointACTVStr.equals("Mymensingh"))
            startingPointACTVStr = "16";
        else if(startingPointACTVStr.equals("Khulna"))
            startingPointACTVStr = "17";
        //Temporary data
  /*      seaLoverACTVStr="1";
        mountainLoverACTVStr="0";
        historyLoverACTVStr="0";
        availEntFaciACTVStr="1";
        hotelNeededACTVStr="1";
        hotelTypeACTVStr="1";
        transportACTVStr="1";
        daysTIETStr="2";
        placeACTVStr="3";
        budgetTIETStr="5500";
        travelGuideACTVStr="0";
        attractionsACTVStr="1";
        travellingPartnerACTVStr="1";
        safeACTVStr="1";
        foodStallsACTVStr="1";
        localFriendshipACTVStr="1";
        startingPointACTVStr="14";*/
    }
    private void PredicitonResultYesDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.result_yes_layout);

        Button btnTrAg =    dialog.findViewById(R.id.TrAg);
        Button btnOk =    dialog.findViewById(R.id.Ok);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Reset();
            }
        });
        btnTrAg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TourAct.this, "All info has been reset", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                Reset();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TourAct.this, "Back to home", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private void Reset() {
        seaLoverACTV.setText("");
        mountainLoverACTV.setText("");
        historyLoverACTV.setText("");
        availEntFaciACTV.setText("");
        hotelNeededACTV.setText("");
        hotelTypeACTV.setText("");
        daysTIET.setText("");
        transportACTV.setText("");
        budgetTIET.setText("");
        placeACTV.setText("");
        travelGuideACTV.setText("");
        attractionsACTV.setText("");
        travellingPartnerACTV.setText("");
        safeACTV.setText("");
        foodStallsACTV.setText("");
        localFriendshipACTV.setText("");
        startingPointACTV.setText("");
    }

    private void PredicitonResultNoDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.result_no_layout);

        Button btnTrAg =    dialog.findViewById(R.id.TrAg);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnTrAg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TourAct.this, "All info has been reset", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}