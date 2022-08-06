package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Adapter.Order_Adapter;
import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.Container.Order_Container;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Orders extends AppCompatActivity {

    TextView empty;
    RecyclerView rec;
    ApiCall apiCall;
    ArrayList<Order_Container> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getStringExtra("type").equals("3")){
            getSupportActionBar().setTitle("Pending orders");
        }
        else if(getIntent().getStringExtra("type").equals("1")){
            getSupportActionBar().setTitle("Active orders");
        }
        else{
            getSupportActionBar().setTitle("Completed orders");
        }
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status = getIntent().getStringExtra("type");
        empty = findViewById(R.id.empty);
        rec = findViewById(R.id.rec);
        apiCall = new ApiCall(this);
        list = new ArrayList<>();

        HashMap<String,String> mp = new HashMap<>();
        mp.put("userID", HelperFunctions.getUserID(this));
        mp.put("status",status);
        mp.put("type","get_order_details");

        apiCall.Insert(mp, "getBikeDetails.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i<array.length(); i++){
                       Order_Container con = new Order_Container();
                        JSONObject ob = array.getJSONObject(i);
                       con.setId(ob.getString("id"));
                       con.setBikeID(ob.getString("bikeID"));
                       con.setDate(ob.getString("date"));
                       con.setC_payment_type(ob.getString("c_payment_type"));
                       con.setTotal(ob.getString("sell_price"));
                       con.setType(ob.getString("order_status"));
                       con.setSell_type(ob.getString("sell_type"));

                       list.add(con);
                    }

                    if(!list.isEmpty()){
                        empty.setVisibility(View.GONE);
                        rec.setVisibility(View.VISIBLE);
                        rec.setLayoutManager(new LinearLayoutManager(Orders.this));
                        Order_Adapter adapter = new Order_Adapter(Orders.this,list);
                        rec.setAdapter(adapter);
                    }
                    else{
                        rec.setVisibility(View.GONE);
                        empty.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e){
                    Toast.makeText(Orders.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
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