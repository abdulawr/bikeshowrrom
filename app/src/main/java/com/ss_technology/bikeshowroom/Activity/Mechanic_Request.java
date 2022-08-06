package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Mechanic_Request extends AppCompatActivity {

    EditText mobile,address,problem;
    Button submit;
    ApiCall apiCall;
    Spinner spinner;

    ArrayList<String> area_ID;
    ArrayList<String> area_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Mechanic Request");
        setContentView(R.layout.activity_mechanic_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        problem = findViewById(R.id.problem);
        submit = findViewById(R.id.submit);
        apiCall = new ApiCall(this);
        spinner = findViewById(R.id.spinner);
        area_ID = new ArrayList<>();
        area_Name = new ArrayList<>();

        apiCall.getWithOutAlert("getMechanicArea.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
               try {
                   JSONArray array = new JSONArray(result);
                   for (int i=0; i<array.length(); i++){
                       JSONObject object = array.getJSONObject(i);
                       area_ID.add(object.getString("id"));
                       area_Name.add(object.getString("name"));
                   }

                   if(!area_ID.isEmpty()){
                       ArrayAdapter<String> adapter = new ArrayAdapter<>(Mechanic_Request.this, android.R.layout.simple_list_item_1,area_Name);
                       spinner.setAdapter(adapter);
                   }
               }
               catch (Exception e){
                   finish();
                   Toast.makeText(Mechanic_Request.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
               }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spr = area_ID.get(spinner.getSelectedItemPosition());


                String mob = mobile.getText().toString(), add = address.getText().toString(),
                        prob = problem.getText().toString();
                if(HelperFunctions.verify(mob) && HelperFunctions.verify(add)){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("mobile",mob);
                    map.put("address",add);
                    map.put("problem",prob);
                    map.put("areaID",spr);
                    map.put("type","submit_Mechanic_Request");
                    map.put("userID",HelperFunctions.getUserID(Mechanic_Request.this));
                    apiCall.Insert(map, "Messages_Api.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {

                          try {
                              JSONObject object = new JSONObject(result);
                              if(object.getString("status").trim().equals("1")){
                                  mobile.getText().clear();
                                  address.getText().clear();
                                  problem.getText().clear();
                              }
                              Toast.makeText(Mechanic_Request.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                          }
                          catch (Exception e){
                              Toast.makeText(Mechanic_Request.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }
                else{
                    Toast.makeText(Mechanic_Request.this, "Enter mobile no and address first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}