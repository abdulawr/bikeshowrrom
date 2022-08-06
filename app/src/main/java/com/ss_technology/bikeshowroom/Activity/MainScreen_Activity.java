package com.ss_technology.bikeshowroom.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ss_technology.bikeshowroom.R;

public class MainScreen_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);



    }

    public void Buy_Bikes(View view){
        startActivity(new Intent(getApplicationContext(),Home.class));
    }

    public void Mechanis(View view){
        startActivity(new Intent(getApplicationContext(),Mechanic_Request.class));
    }
}