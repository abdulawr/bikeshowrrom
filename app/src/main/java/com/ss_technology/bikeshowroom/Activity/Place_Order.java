package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.BaseURL;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Place_Order extends AppCompatActivity {

    TextView company,bike_cc,model,color,price,status;
    ApiCall apiCall;
    String id;
    JSONObject company_percentage;
    Spinner order_type,payment_typel;
    LinearLayout cas_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        getSupportActionBar().setTitle("Place Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getStringExtra("id");
        HashMap<String,String> map=new HashMap<>();
        apiCall = new ApiCall(this);
        map.put("id",id);
        cas_layout = findViewById(R.id.cas_layout);

        company = findViewById(R.id.company);
        order_type = findViewById(R.id.order_type);
        payment_typel = findViewById(R.id.payment_typel);
        bike_cc  = findViewById(R.id.bike_cc);
        model = findViewById(R.id.model);
        color = findViewById(R.id.color);
        price  = findViewById(R.id.price);
        status = findViewById(R.id.status);


        order_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1){
                    cas_layout.setVisibility(View.GONE);
                }
                else{
                    cas_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        apiCall.Insert(map, "getBike_Deatils.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject ob1 = new JSONObject(result);
                    JSONObject object = ob1.getJSONObject("bike");
                    String pr = ob1.getString("profit");
                    company_percentage = new JSONObject(pr);
                    company_percentage = new JSONObject(company_percentage.getString("profit_parameter"));

                    company.setText(object.getString("name"));
                    bike_cc.setText(object.getString("bike_cc"));
                    model.setText(object.getString("model"));
                    color.setText(object.getString("color"));
                    price.setText(object.getString("sell_price")+" PKR");

                    if (Integer.parseInt(object.getString("qty")) > 0){
                        status.setText("Available");
                        status.setTextColor(Color.parseColor("#4CAF50"));
                    }
                    else{
                        status.setText("Not available");
                        status.setTextColor(Color.parseColor("#E91E63"));
                    }

                }
                catch (Exception e){
                    Toast.makeText(Place_Order.this, "Error occurred while loading data!", Toast.LENGTH_SHORT).show();
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

    public void proceedd(View view) {
        // 0 cash && 1 installment
        int order_type_op = order_type.getSelectedItemPosition();
        // 0 wallet && 1 cash on deliver
        int payment_typel_op = payment_typel.getSelectedItemPosition();

        Intent intent = new Intent(getApplicationContext(),Order_confirmation.class);
        intent.putExtra("order_type",String.valueOf(order_type_op));
        intent.putExtra("payment_type",String.valueOf(payment_typel_op));
        intent.putExtra("company_percent",company_percentage.toString());
        intent.putExtra("bike_id",id);
        startActivity(intent);
    }
}