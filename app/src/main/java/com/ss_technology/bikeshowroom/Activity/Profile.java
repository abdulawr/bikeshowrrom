package com.ss_technology.bikeshowroom.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.BaseURL;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.KeepMeLogin;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class Profile extends AppCompatActivity {

    EditText name,mobile,email,cnic,add,pass;
    Button update;
    ApiCall apiCall;
    ImageView profile,change_img;
    Bitmap bitmap=null;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        apiCall = new ApiCall(this);
        update = findViewById(R.id.update);
        change_img = findViewById(R.id.change_img);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        cnic = findViewById(R.id.cnic);
        add = findViewById(R.id.add);
        pass = findViewById(R.id.pass);
        profile =findViewById(R.id.profile);

        HashMap<String,String> map = new HashMap<>();
        map.put("type","getCustomerProfile");
        map.put("userID", HelperFunctions.getUserID(this));
        apiCall.Insert(map, "Messages_Api.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
               try{
                   setValues(result);
               }
               catch (Exception e){
                   Toast.makeText(Profile.this, "Error occurred in json parsing", Toast.LENGTH_SHORT).show();
               }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String na = name.getText().toString(), em = email.getText().toString(),
                        mob = mobile.getText().toString(), cn = cnic.getText().toString(),
                        ad = add.getText().toString(),pa = pass.getText().toString();
                if(HelperFunctions.verify(na) && HelperFunctions.verify(em) && HelperFunctions.verify(mob)
                && HelperFunctions.verify(ad) && HelperFunctions.verify(pa) && HelperFunctions.verify(cn)){

                    HashMap<String,String> map = new HashMap<>();
                    map.put("name",na);
                    map.put("email",em);
                    map.put("mobile",mob);
                    map.put("cnic",cn);
                    map.put("add",ad);
                    map.put("pass",pa);
                    map.put("type","updateCustomerProfile");
                    map.put("userID",HelperFunctions.getUserID(Profile.this));
                    apiCall.Insert(map, "Messages_Api.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                          try {
                             JSONObject object = new JSONObject(result);
                             if(object.getString("status").trim().equals("1")){
                                 setValues(object.getString("data"));
                             }
                              Toast.makeText(Profile.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                          }
                          catch (Exception e){
                              Toast.makeText(Profile.this, "Error occured in json parsing!", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });

                }
                else{
                    Toast.makeText(Profile.this, "Fill the form correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });


        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = Profile.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.update_profile_image, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                builder.setView(v);
                Dialog dialog = builder.create();
                dialog.setCancelable(false);

                ImageButton close_btn = v.findViewById(R.id.close_btn);
                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                profile_image = v.findViewById(R.id.profile_image);
                profile_image.setImageResource(R.drawable.chooseimage);
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                        }
                        else
                        {
                            SelectImage();
                        }
                    }
                });

                Button update_image = v.findViewById(R.id.update_image);
                update_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bitmap != null){
                            HashMap<String,String> map = new HashMap<>();
                            map.put("type","update_Customer_Profile_Image");
                            map.put("userID",HelperFunctions.getUserID(Profile.this));
                            map.put("image",HelperFunctions.BitmapToString(bitmap,80));
                            apiCall.Insert(map, "Messages_Api.php", new VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    try{
                                       JSONObject object = new JSONObject(result);
                                       if(object.getString("status").trim().equals("1")){
                                           Picasso.get().load(BaseURL.ImagePath("customer")+object.getString("img")).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(profile);
                                           KeepMeLogin keepMeLogin = new KeepMeLogin(Profile.this);
                                           JSONObject obj = new JSONObject(keepMeLogin.getData());
                                           obj.put("image",object.getString("img"));
                                           keepMeLogin.setData(obj.toString());

                                           dialog.dismiss();
                                       }
                                        Toast.makeText(Profile.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                    catch (Exception e){
                                        Toast.makeText(Profile.this,"Error occured in json parsing",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(Profile.this, "Select image first", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();
            }
        });

    }

    void setValues(String result){
       try{
           JSONObject object = new JSONObject(result);
           Picasso.get().load(BaseURL.ImagePath("customer")+object.getString("image")).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(profile);
           name.setText(object.getString("name"));
           mobile.setText(object.getString("mobile"));
           email.setText(object.getString("email"));
           cnic.setText(object.getString("cnic"));
           add.setText(object.getString("address"));
           pass.setText(object.getString("pass"));
       }
       catch (Exception e){
           Toast.makeText(Profile.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
       }
    }

    public void SelectImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUST_CODE_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE && permissions.length >0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //SelectImage();
            }
            else {
                ActivityCompat.requestPermissions(Profile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUST_CODE_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                Uri uri=data.getData();
                if (uri != null)
                {
                    try {
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                        Bitmap original = BitmapFactory.decodeStream(stream);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        original.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        bitmap=Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                        profile_image.setImageBitmap(original);

                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}