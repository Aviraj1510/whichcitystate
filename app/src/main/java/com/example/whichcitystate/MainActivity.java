package com.example.whichcitystate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText pinCodeEdt;
    private Button getDataBtn;
    private TextView pinCodeDetailsTV;

    String pinCode;
    private RecyclerView postOfficesRV;
    private ArrayList<JSONObject> postOfficeList;
    private PostOfficeAdapter postOfficeAdapter;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinCodeEdt = findViewById(R.id.idedtPinCode);
        getDataBtn = findViewById(R.id.idBtnGetCityandState);
        pinCodeDetailsTV = findViewById(R.id.idTVPinCodeDetails);
        postOfficesRV = findViewById(R.id.pincodedetailList);
        
        mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        postOfficeList = new ArrayList<>();
        postOfficeAdapter = new PostOfficeAdapter(postOfficeList);
        postOfficesRV.setLayoutManager(new LinearLayoutManager(this));
        postOfficesRV.setAdapter(postOfficeAdapter);

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinCode = pinCodeEdt.getText().toString().trim();

                if(TextUtils.isEmpty(pinCode)){
                    Toast.makeText(MainActivity.this, "Please Enter Valid Pin Code.", Toast.LENGTH_SHORT).show();
                }else {
                    getDataFromPinCode(pinCode);
                }
            }
        });

    }

    private void getDataFromPinCode(String pinCode) {
        String url = "https://api.postalpincode.in/pincode/" + pinCode;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject responseObject = response.getJSONObject(0);
                    String status = responseObject.getString("Status");

                    if (status.equals("Success")) {
                        JSONArray postOfficeArray = responseObject.getJSONArray("PostOffice");
                        postOfficeList.clear();
                        if (postOfficeArray.length() > 0) {
                            for (int i = 0; i < postOfficeArray.length(); i++) {
                                postOfficeList.add(postOfficeArray.getJSONObject(i));

                            }
                            Log.d("detail", "postOffice: " +  postOfficeArray );
                            postOfficeAdapter.notifyDataSetChanged();
                            pinCodeDetailsTV.setVisibility(View.GONE);
                        } else {
                            showNoDataMessage("No details available for this pin code.");
                        }
                    } else {
                        pinCodeDetailsTV.setText("Invalid pin code");
                        showNoDataMessage("Invalid pin code");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    showError("Error parsing response: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                showError("Error fetching data: " + error.getMessage());
            }
        });

        queue.add(request);
    }
    private void showNoDataMessage(String message) {
        pinCodeDetailsTV.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        pinCodeDetailsTV.setText(message);
    }
    private void showError(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}