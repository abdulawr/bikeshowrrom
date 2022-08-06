package com.ss_technology.bikeshowroom.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    TextInputEditText name,mobile,email,add,pass,cnic;
    Button choose_image,signUp;
    ApiCall apiCall;
    Bitmap bitmap=null;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    ImageView profileimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_up_activity);

        init();

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SignUp.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(SignUp.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {

                    SelectImage();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nn=name.getText().toString();
                String mo=mobile.getText().toString();
                String em=email.getText().toString();
                String ad=add.getText().toString();
                String pa=pass.getText().toString();
                String cn=cnic.getText().toString();
                if (HelperFunctions.verify(cn) && HelperFunctions.verify(nn) && HelperFunctions.verify(mo) && HelperFunctions.verify(em)
                        && HelperFunctions.verify(ad) && HelperFunctions.verify(pa)){
                    if (bitmap != null){
                        HashMap<String,String> map=new HashMap<>();
                        map.put("name",nn);
                        map.put("mobile",mo);
                        map.put("email",em);
                        map.put("add",ad);
                        map.put("pass",pa);
                        map.put("cnic",cn);
                        map.put("image",HelperFunctions.BitmapToString(bitmap,90));
                        apiCall.Insert(map, "createCustomerAccount.php", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                try{
                                    JSONObject object=new JSONObject(result);
                                    if (object.getString("status").trim().equals("1")){
                                       startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                       finish();
                                        Toast.makeText(SignUp.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(SignUp.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception exception){
                                    Toast.makeText(SignUp.this, "Error occurred in Json Parsing!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SignUp.this, "Select Image first!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SignUp.this, "Fill the form correctly!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SelectImage()
    {
        profileimage.setVisibility(View.GONE);
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUST_CODE_IMAGE);
//        if(intent.resolveActivity(getPackageManager()) != null)
//        {
//            Log.e("Basit","TEsting");
//
//        }
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
                ActivityCompat.requestPermissions(SignUp.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                        profileimage.setVisibility(View.VISIBLE);
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                        Bitmap original = BitmapFactory.decodeStream(stream);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        original.compress(Bitmap.CompressFormat.JPEG, 80, out);
                        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        bitmap=Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                        profileimage.setImageBitmap(original);
                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }


    private void init(){
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        add=findViewById(R.id.add);
        cnic=findViewById(R.id.cnic);
        profileimage=findViewById(R.id.profileimage);
        pass=findViewById(R.id.pass);
        choose_image=findViewById(R.id.choose_image);
        signUp=findViewById(R.id.signUp);
        apiCall=new ApiCall(this);

        try{
            if (!getIntent().getStringExtra("mobile").trim().equals("")){
                mobile.setText(getIntent().getStringExtra("mobile"));
                mobile.setEnabled(false);
                email.setEnabled(true);
            }
        }
        catch (Exception e){
        }

        try {
            if (!getIntent().getStringExtra("email").trim().equals("")){
                name.setText(getIntent().getStringExtra("name"));
                email.setText(getIntent().getStringExtra("email"));
                email.setEnabled(false);
                mobile.setEnabled(true);

            }
        }
        catch (Exception e){
        }

    }
}