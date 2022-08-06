package com.ss_technology.bikeshowroom.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.ss_technology.bikeshowroom.Adapter.Bike_Adapter;
import com.ss_technology.bikeshowroom.Config.ApiCall;
import com.ss_technology.bikeshowroom.Config.BaseURL;
import com.ss_technology.bikeshowroom.Config.HelperFunctions;
import com.ss_technology.bikeshowroom.Config.KeepMeLogin;
import com.ss_technology.bikeshowroom.Config.VolleyCallback;
import com.ss_technology.bikeshowroom.Container.BIke_Container;
import com.ss_technology.bikeshowroom.R;
import com.ss_technology.bikeshowroom.databinding.ActivityHomeBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;
    DrawerLayout drawer;
    NavigationView navigationView;
    KeepMeLogin keepMeLogin;
    TextView name,email;
    ImageView profile;
    RecyclerView recyclerView;
    ArrayList<BIke_Container> list;
    Bike_Adapter adapter;
    ApiCall apiCall;
    JSONArray bike_company = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        keepMeLogin=new KeepMeLogin(Home.this);
        Toolbar toolbar=binding.appBarHome.toolbar;
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.rec);
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        list = new ArrayList<>();
        apiCall = new ApiCall(this);

        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        profile = headerView.findViewById(R.id.profile_image);

       HelperFunctions.Network(this);

       apiCall.get("getBike_List.php", new VolleyCallback() {
           @Override
           public void onSuccess(String result) {
             setList(result);
           }
       });

        try {
            JSONObject object = new JSONObject(keepMeLogin.getData());
            name.setText(object.getString("name"));
            email.setText(object.getString("email"));
            Picasso.get().load(BaseURL.ImagePath("customer")+object.getString("image")).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(profile);
        }
        catch (Exception e){

            Toast.makeText(this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.signout){
            keepMeLogin.Clear();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else if(item.getItemId() == R.id.search){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View view1 = LayoutInflater.from(Home.this).inflate(R.layout.filter_data_view, viewGroup, false);
            builder.setView(view1);

            AutoCompleteTextView bike_cc = view1.findViewById(R.id.bike_cc);
            AutoCompleteTextView company_drp = view1.findViewById(R.id.company_drp);

            String [] bike_cc_ar = {
              "70",
              "100",
              "125",
              "150"
            };

            ArrayAdapter<String> bike_cc_adapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_list_item_1,bike_cc_ar);
            bike_cc.setAdapter(bike_cc_adapter);

            ArrayList<String> lst = new ArrayList<>();
            HashMap<String,String> lstID = new HashMap<>();
            if(bike_company != null){
                try{
                   for(int i = 0; i<bike_company.length(); i++){
                       JSONObject ob = bike_company.getJSONObject(i);
                       lst.add(ob.getString("name"));
                       lstID.put(ob.getString("name"),ob.getString("id"));
                   }
                }
                catch (Exception e){
                    Toast.makeText(Home.this, "Error in occurred in json parsing", Toast.LENGTH_SHORT).show();
                }
            }

            ArrayAdapter<String> cmp_adapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_list_item_1,lst);
            company_drp.setAdapter(cmp_adapter);

            AlertDialog alertDialog = builder.create();

            Button searchBike = view1.findViewById(R.id.searchBike);
            searchBike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextInputEditText model = view1.findViewById(R.id.model);
                    String mod = model.getText().toString();
                    String cmp = company_drp.getText().toString();
                    String bik_CC = bike_cc.getText().toString();

                    if(!TextUtils.isEmpty(cmp)){
                      cmp = lstID.get(cmp);
                    }
                    else{
                        cmp = "";
                    }

                    if(TextUtils.isEmpty(bik_CC)){
                        bik_CC = "";
                    }

                    HashMap<String,String> cmap = new HashMap<>();
                    cmap.put("company",cmp);
                    cmap.put("bike_cc",bik_CC);
                    cmap.put("model",mod);
                    cmap.put("type","searchBike");
                    apiCall.Insert(cmap, "getBike_List.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            setList(result);
                            alertDialog.dismiss();
                        }
                    });

                }
            });

           ;
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.contact_us){
         startActivity(new Intent(getApplicationContext(),Messages.class));
        }
        else if(id == R.id.about){
            startActivity(new Intent(getApplicationContext(),About.class));
        }
        else if(id == R.id.profile){
            startActivity(new Intent(getApplicationContext(),Profile.class));
        }
        else if(id == R.id.mechanics){
            startActivity(new Intent(getApplicationContext(),Mechanic_Request.class));
        }
        else if(id == R.id.wallet){
            startActivity(new Intent(getApplicationContext(),Wallet.class));
        }
        else if(id == R.id.a_order){
            Intent intent = new Intent(getApplicationContext(),Orders.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
        else if(id == R.id.history){
            Intent intent = new Intent(getApplicationContext(),Orders.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }
        else if(id == R.id.p_order){
            Intent intent = new Intent(getApplicationContext(),Orders.class);
            intent.putExtra("type","3");
            startActivity(intent);
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setList(String result){

        if(!list.isEmpty()){
            list.clear();
        }
        try {

            JSONObject ob = new JSONObject(result);
            JSONArray array = ob.getJSONArray("bike");
            bike_company = ob.getJSONArray("cmp");

            for (int i=0; i < array.length(); i++){
                JSONObject itm = array.getJSONObject(i);
                BIke_Container con = new BIke_Container();
                con.setId(itm.getString("id"));
                con.setCompnay(itm.getString("name"));
                con.setBike_cc(itm.getString("bike_cc"));
                con.setModel(itm.getString("model"));
                con.setImage(itm.getString("main_image"));
                con.setPrice(itm.getString("sell_price"));
                list.add(con);
            }

            if(!list.isEmpty()){
                adapter = new Bike_Adapter(Home.this,list);
                recyclerView.setLayoutManager(new LinearLayoutManager(Home.this));
                recyclerView.setAdapter(adapter);
            }
        }
        catch (Exception e){
            Toast.makeText(Home.this, "Json error", Toast.LENGTH_SHORT).show();
        }
    }
}