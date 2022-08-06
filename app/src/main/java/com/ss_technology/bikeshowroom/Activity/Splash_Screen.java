package com.ss_technology.bikeshowroom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.ss_technology.bikeshowroom.Config.KeepMeLogin;
import com.ss_technology.bikeshowroom.R;

public class Splash_Screen extends AppCompatActivity {

    KeepMeLogin keepMeLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        keepMeLogin=new KeepMeLogin(this);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(keepMeLogin.checkData()){
                    //startActivity(new Intent(getApplicationContext(),Home.class));
                    startActivity(new Intent(getApplicationContext(),MainScreen_Activity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }

            }
        },1000);
    }
}