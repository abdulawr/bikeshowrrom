package com.ss_technology.bikeshowroom.Config;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HelperFunctions {

    public static String currentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String getUserID(Context context){
        KeepMeLogin keepMeLogin = new KeepMeLogin(context);
        try{
            JSONObject object = new JSONObject(keepMeLogin.getData());
            return object.getString("id");
        }
        catch (Exception e){
            return "-1";
        }
    }

    public static String BitmapToString(Bitmap bi,int quality)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bi.compress(Bitmap.CompressFormat.JPEG,quality,outputStream);
        byte[] imageByte=outputStream.toByteArray();
        String encodeimage= Base64.encodeToString(imageByte, Base64.DEFAULT);
        return  encodeimage;

    }

    public static void Network(Context context){
        if (!CheckInternetConnection.Connection(context)){
            Toast.makeText(context,"Check you internet connection",Toast.LENGTH_LONG).show();
        }
    }


    public static void Message(Activity context, String msg){
        Snackbar.make(context.findViewById(android.R.id.content), msg, Snackbar.LENGTH_SHORT)
                .show();
    }

    public static String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    public static  boolean verify(String value){
        return (value.trim().length() > 0 && !value.equals(""));
    }
}
