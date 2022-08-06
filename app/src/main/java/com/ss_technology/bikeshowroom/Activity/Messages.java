package com.ss_technology.bikeshowroom.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ss_technology.bikeshowroom.Adapter.Message_Adapter;
import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.Container.Message_Container;
import com.ss_technology.bikeshowroom.R;
import com.ss_technology.bikeshowroom.databinding.ActivityMessagesBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Messages extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMessagesBinding binding;
    RecyclerView rec;
    ApiCall apiCall;
    ArrayList<Message_Container> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        apiCall = new ApiCall(this);
        list = new ArrayList<>();

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),Contact_US.class));
            }
        });

        rec = findViewById(R.id.rec);
        HashMap<String,String> map = new HashMap<>();
        map.put("userID", HelperFunctions.getUserID(this));
        map.put("type","getMessageList");
        apiCall.Insert(map, "Messages_Api.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for(int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        Message_Container container = new Message_Container();
                        container.setId(object.getString("id"));
                        container.setSubject(object.getString("subject"));
                        container.setDate(object.getString("dd"));
                        container.setMessage(object.getString("message"));
                        container.setReplay(object.getString("replay"));
                        container.setReplay_status(object.getString("replayStatus"));
                        list.add(container);
                    }

                    if(!list.isEmpty()){
                        rec.setLayoutManager(new LinearLayoutManager(Messages.this));
                        rec.setHasFixedSize(true);
                        Message_Adapter adapter = new Message_Adapter(Messages.this,list);
                        rec.setAdapter(adapter);
                    }
                }
                catch (Exception e){
                    Toast.makeText(Messages.this, "Error occured in json parsing", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}