package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Wallet extends AppCompatActivity {

   TextView balance;
   ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        getSupportActionBar().setTitle("Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        balance = findViewById(R.id.balance);
        apiCall = new ApiCall(this);

        HashMap<String,String> map = new HashMap<>();
        map.put("id", HelperFunctions.getUserID(this));
        map.put("type","getBalance");

        apiCall.Insert(map, "getCustomerInfo.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
               try {
                   JSONObject res = new JSONObject(result);
                   balance.setText(res.getString("balance")+" PKR");
               }
               catch (Exception e){
                   Toast.makeText(Wallet.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
               }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        else if(item.getItemId() == R.id.jazzcash)
            startActivity(new Intent(getApplicationContext(),JazzCash_Testing.class));
        return true;
    }

    public void Recharge(View view) {
        startActivity(new Intent(getApplicationContext(),JazzCash.class));
    }
}