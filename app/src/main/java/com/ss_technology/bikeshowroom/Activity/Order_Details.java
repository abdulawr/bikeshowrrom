package com.ss_technology.bikeshowroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.Container.Order_Container;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Order_Details extends AppCompatActivity {

    Button bike_details;
    ApiCall apiCall;
    TextView order_id,date,amount,type,status,total_amt,paid_amt,remain_amt;
    ArrayList<Order_Container> list;
    RecyclerView rec;
    Button tranBTN;
    TextView advance_payment,payment_month,monthly_payment;
    TableLayout pp_layout;
    JSONObject install_order;
    long balance = 0;
    Transaction_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Order_Container data = (Order_Container) getIntent().getSerializableExtra("obj");
        list = new ArrayList<>();
        tranBTN = findViewById(R.id.tranBTN);
        bike_details = findViewById(R.id.bike_details);
        pp_layout = findViewById(R.id.pp_layout);
        advance_payment = findViewById(R.id.advance_payment);
        payment_month = findViewById(R.id.payment_month);
        monthly_payment = findViewById(R.id.monthly_payment);
        bike_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), View_Bike.class);
                intent.putExtra("id",data.getBikeID());
                startActivity(intent);
            }
        });

        order_id = findViewById(R.id.order_id);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        type = findViewById(R.id.type);
        status = findViewById(R.id.status);
        total_amt = findViewById(R.id.total_amt);
        paid_amt  = findViewById(R.id.paid_amt);
        remain_amt = findViewById(R.id.remain_amt);
        rec = findViewById(R.id.rec);

        apiCall = new ApiCall(this);
        HashMap<String,String> mp = new HashMap<>();
        mp.put("ID",data.getId());
        mp.put("userID", HelperFunctions.getUserID(Order_Details.this));
        mp.put("type","getViewInformation");
        apiCall.Insert(mp, "getBikeDetails.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("Basit",result);

                try {
                    JSONObject ob = new JSONObject(result);
                    balance = Long.parseLong(ob.getString("balance"));

                    total_amt.setText(ob.getString("total"));
                    paid_amt.setText(ob.getString("paid"));
                    remain_amt.setText(ob.getString("remain"));

                    JSONArray ar = ob.getJSONArray("tran");
                    for (int i = 0; i<ar.length(); i++){
                        Order_Container container = new Order_Container();
                        JSONObject sing = ar.getJSONObject(i);
                        container.setTotal(sing.getString("amount"));
                        container.setDate(sing.getString("date"));
                        container.setType(sing.getString("type"));
                        list.add(container);
                    }
                    if (!list.isEmpty())
                    {
                       rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                       adapter = new Transaction_Adapter(list,Order_Details.this);
                       rec.setAdapter(adapter);
                    }

                    JSONObject order = ob.getJSONObject("order");
                    order_id.setText("#"+order.getString("id"));
                    date.setText(order.getString("date"));
                    amount.setText(order.getString("sell_price")+" PKR");
                    String ty = order.getString("order_status");
                    if(ty.equals("0")){
                        status.setText("Pending");
                    }
                    else if(ty.equals("1")){
                        status.setText("Accepted");
                    }
                    else if(ty.equals("2")){
                        status.setText("Active");
                    }
                    else if(ty.equals("3")){
                        status.setText("Completed");
                    }

                    String st = order.getString("sell_type");
                    if(st.equals("2")){
                       type.setText("Installment");
                    }
                    else{
                        type.setText("Cash");
                    }


                    if(order.getString("sell_type").equals("2") && order.getString("order_status").equals("2")){
                        tranBTN.setVisibility(View.VISIBLE);
                    }
                    else{
                       tranBTN.setVisibility(View.GONE);
                    }

                    if(order.getString("sell_type").trim().equals("2") && !order.getString("order_status").trim().equals("0")){
                        pp_layout.setVisibility(View.VISIBLE);
                        install_order = ob.getJSONObject("install_order");
                        advance_payment.setText(install_order.getString("advance_payment")+" PKR");
                        payment_month.setText(install_order.getString("payment_months"));
                        monthly_payment.setText(install_order.getString("monthly_payment")+" PKR");

                    }
                }
                catch (Exception e){
                    Toast.makeText(Order_Details.this, "Json error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tranBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              HashMap<String,String> mp = new HashMap<>();
              mp.put("ID",data.getId());
              mp.put("type","insertMonthlyInstallment");
              try {
                  int amt = Integer.parseInt(install_order.getString("monthly_payment"));
                  mp.put("amount",String.valueOf(amt));

                  if(amt < balance){
                     apiCall.Insert(mp, "getBikeDetails.php", new VolleyCallback() {
                         @Override
                         public void onSuccess(String result) {
                             Log.e("Basit",result);
                             try {
                                 JSONObject object = new JSONObject(result);
                                 if(object.getString("status").trim().equals("1")){
                                     Toast.makeText(Order_Details.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                                     JSONArray ar = object.getJSONArray("transaction");
                                     for (int i = 0; i<ar.length(); i++){
                                         Order_Container container = new Order_Container();
                                         JSONObject sing = ar.getJSONObject(i);
                                         container.setTotal(sing.getString("amount"));
                                         container.setDate(sing.getString("date"));
                                         container.setType(sing.getString("type"));
                                         list.add(container);
                                     }
                                 }
                                 else{
                                     Toast.makeText(Order_Details.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                 }
                             }
                             catch (Exception e){
                                 Toast.makeText(Order_Details.this, "Json processing error", Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                  }
                  else{
                      Toast.makeText(Order_Details.this, "You don`t have enough balance to pay this transaction. Pleae recharge you account first", Toast.LENGTH_SHORT).show();
                  }
              }
              catch (Exception e)
              {
                  Toast.makeText(Order_Details.this, "Error occured try again!", Toast.LENGTH_SHORT).show();
              }

            }
        });


    }
}