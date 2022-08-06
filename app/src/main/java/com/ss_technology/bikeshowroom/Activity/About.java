package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONObject;

public class About extends AppCompatActivity {

    TextView name,email,mobile,address;
    ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);

        apiCall = new ApiCall(this);
        apiCall.get("getCompanyInfo.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
               try {
                   JSONObject object = new JSONObject(result);
                   name.setText("Name: "+object.getString("name"));
                   email.setText("Email: "+object.getString("email"));
                   mobile.setText("Mobile: "+object.getString("mobile"));
                   address.setText("Address: "+object.getString("add"));
               }
               catch (Exception e){
                   Toast.makeText(About.this, "Error occured in json parsing!", Toast.LENGTH_SHORT).show();
               }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}