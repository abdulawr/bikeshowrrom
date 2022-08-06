package com.ss_technology.bikeshowroom.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Order_confirmation extends AppCompatActivity {

    TextView company,bike_cc,model,color;
    ApiCall apiCall;
    JSONObject company_percentage;

    TextView amount,ex_charges,total_amount;
    TableRow ex_charges_layout;
    String order_type,payment_type,bike_id,company_percent,balance;
    long t_amount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

         order_type = getIntent().getStringExtra("order_type");
         payment_type = getIntent().getStringExtra("payment_type");
         bike_id = getIntent().getStringExtra("bike_id");
         company_percent = getIntent().getStringExtra("company_percent");

        HashMap<String,String> map=new HashMap<>();
        apiCall = new ApiCall(this);
        map.put("id",bike_id);
        map.put("cus", HelperFunctions.getUserID(this));
        map.put("getBalance","getBalance");
        amount= findViewById(R.id.amount);
        ex_charges= findViewById(R.id.ex_charges);
        total_amount= findViewById(R.id.total_amount);
        ex_charges_layout = findViewById(R.id.ex_charges_layout);

        company = findViewById(R.id.company);
        bike_cc  = findViewById(R.id.bike_cc);
        model = findViewById(R.id.model);
        color = findViewById(R.id.color);


        apiCall.Insert(map, "getBike_Deatils.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject ob1 = new JSONObject(result);
                    JSONObject object = ob1.getJSONObject("bike");
                    String pr = ob1.getString("profit");
                    company_percentage = new JSONObject(pr);
                    company_percentage = new JSONObject(company_percentage.getString("profit_parameter"));

                    balance = ob1.getString("balance");
                    company.setText(object.getString("name"));
                    bike_cc.setText(object.getString("bike_cc"));
                    model.setText(object.getString("model"));
                    color.setText(object.getString("color"));

                    if(order_type.trim().equals("0")){
                      ex_charges_layout.setVisibility(View.GONE);
                      amount.setText(object.getString("sell_price")+" PKR");
                      total_amount.setText(object.getString("sell_price")+" PKR");
                      t_amount = Long.parseLong(object.getString("sell_price"));
                    }
                    else{
                       ex_charges_layout.setVisibility(View.VISIBLE);
                       long advance_percent = Long.parseLong(company_percentage.getString("advance_percent"));
                       long profit_percent = Long.parseLong(company_percentage.getString("profit_percent"));
                       long sell_price = Long.parseLong(object.getString("sell_price"));
                       long charges = (sell_price / 100) * profit_percent;
                        amount.setText(object.getString("sell_price")+" PKR");
                       ex_charges.setText(String.valueOf(charges)+" PKR");
                       long t_amount11 = sell_price + charges;
                       total_amount.setText(String.valueOf(t_amount11+" PKR"));

                        t_amount = t_amount11;

                    }

                }
                catch (Exception e){
                    Log.e("Basit",e.getMessage());
                    Toast.makeText(Order_confirmation.this, "Error occurred while loading data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void Submit(View view) {

        HashMap<String,String> mp = new HashMap<>();
        mp.put("total",String.valueOf(t_amount));
        mp.put("bike_id",bike_id);
        mp.put("cusID",HelperFunctions.getUserID(Order_confirmation.this));
        mp.put("order_type",String.valueOf(Integer.parseInt(order_type)+1));
        mp.put("payment_type",payment_type);
        mp.put("type","placed_new_order");

        if(order_type.trim().equals("0")){
            // cash
            if(payment_type.trim().equals("0")){
                // wallet
                if(t_amount > Long.parseLong(balance)){
                    Toast.makeText(Order_confirmation.this, "Please recharge you account, You don`t have enough balance to purchase this product!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            // installment
        }

        apiCall.Insert(mp, "bike_orders.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
             try {
                 JSONObject object = new JSONObject(result);
                 if(object.getString("status").trim().equals("1")){
                     Intent intent = new Intent(getApplicationContext(),Home.class);
                     startActivity(intent);
                     finish();
                 }
                 Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
             }
             catch (Exception e){
                 Toast.makeText(getApplicationContext(), "Json error", Toast.LENGTH_SHORT).show();
             }
            }
        });

    }
}