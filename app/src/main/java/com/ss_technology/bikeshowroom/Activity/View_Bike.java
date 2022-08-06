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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class View_Bike extends AppCompatActivity {

    ApiCall apiCall;
    ImageView mainImg;
    TextView company,bike_cc,model,color,price,status;
    LinearLayout sub_images;
    Button place_order;
    TextView out_of_stock;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bike);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = getIntent().getStringExtra("id");
        HashMap<String,String> map=new HashMap<>();
        apiCall = new ApiCall(this);
        place_order = findViewById(R.id.place_order);
        out_of_stock = findViewById(R.id.out_of_stock);
        mainImg = findViewById(R.id.mainImg);
        map.put("id",id);

        company = findViewById(R.id.company);
        sub_images = findViewById(R.id.sub_images);
        bike_cc  = findViewById(R.id.bike_cc);
        model = findViewById(R.id.model);
        color = findViewById(R.id.color);
        price  = findViewById(R.id.price);
        status = findViewById(R.id.status);

        apiCall.Insert(map, "getBike_Deatils.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject ob1 = new JSONObject(result);
                    JSONObject object = ob1.getJSONObject("bike");
                    Picasso.get().load(BaseURL.ImagePath("bike")+object.getString("main_image")).into(mainImg);
                    company.setText(object.getString("name"));
                    bike_cc.setText(object.getString("bike_cc"));
                    model.setText(object.getString("model"));
                    color.setText(object.getString("color"));
                    price.setText(object.getString("sell_price")+" PKR");

                    if (Integer.parseInt(object.getString("qty")) > 0){
                       status.setText("Available");
                       status.setTextColor(Color.parseColor("#4CAF50"));
                       place_order.setVisibility(View.VISIBLE);
                       out_of_stock.setVisibility(View.GONE);
                    }
                    else{
                        status.setText("Not available");
                        status.setTextColor(Color.parseColor("#E91E63"));
                        out_of_stock.setVisibility(View.VISIBLE);
                        place_order.setVisibility(View.GONE);
                    }

                    if(HelperFunctions.verify(object.getString("images"))){
                        sub_images.setVisibility(View.VISIBLE);
                        JSONArray array = new JSONArray(object.getString("images"));
                        for(int i = 0; i<array.length(); i++){
                            String url = array.getString(i);

                            CardView cardView = new CardView(View_Bike.this);
                            cardView.setElevation(8);
                            cardView.setRadius(8);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    180,
                                    180
                            );
                            params.setMargins(10, 15, 10, 15);
                            cardView.setLayoutParams(params);

                            ImageView img = new ImageView(View_Bike.this);
                            cardView.addView(img);
                            sub_images.addView(cardView);
                            img.setElevation(8);
                            img.setScaleType(ImageView.ScaleType.FIT_XY);

                            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                            );
                            img.setLayoutParams(params1);

                            Picasso.get().load(BaseURL.ImagePath("bike")+url).into(img);
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(View_Bike.this, "Error occurred while loading data!", Toast.LENGTH_SHORT).show();
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

    public void Place_order(View view) {
        Intent intent = new Intent(getApplicationContext(),Place_Order.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}