package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.KeepMeLogin;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Contact_US extends AppCompatActivity {

    KeepMeLogin keepMeLogin;
    EditText subject,message;
    Button send;
    ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        keepMeLogin = new KeepMeLogin(this);
        apiCall = new ApiCall(this);

        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sub = subject.getText().toString(), msg = message.getText().toString();
              if(HelperFunctions.verify(sub) && HelperFunctions.verify(msg)){
                  HashMap<String,String> map = new HashMap<>();
                  map.put("subject",sub);
                  map.put("message",msg);
                  map.put("type","sendMsgToAdmin");
                  map.put("userID",HelperFunctions.getUserID(Contact_US.this));
                  apiCall.Insert(map, "Messages_Api.php", new VolleyCallback() {
                      @Override
                      public void onSuccess(String result) {
                        try{
                            JSONObject object = new JSONObject(result);
                            if(object.getString("status").trim().equals("1")){
                                subject.getText().clear();
                                message.getText().clear();
                            }
                            Toast.makeText(Contact_US.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){
                            Toast.makeText(Contact_US.this, "Error occured in json parsing!", Toast.LENGTH_SHORT).show();
                        }
                      }
                  });
              }
              else {
                  HelperFunctions.Message(Contact_US.this,"Fill the form correctly");
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