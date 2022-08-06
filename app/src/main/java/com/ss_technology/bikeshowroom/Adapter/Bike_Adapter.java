package com.ss_technology.bikeshowroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ss_technology.bikeshowroom.Activity.View_Bike;
import com.ss_technology.bikeshowroom.Config.BaseURL;
import com.ss_technology.bikeshowroom.Container.BIke_Container;
import com.ss_technology.bikeshowroom.R;

import java.util.List;

public class Bike_Adapter extends RecyclerView.Adapter<Bike_Adapter.View_HOlder> {

    Context context;
    List<BIke_Container> list;

    public Bike_Adapter(Context context, List<BIke_Container> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View_HOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_HOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bike_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_HOlder holder, int position) {
        BIke_Container data = list.get(position);
        Picasso.get().load(BaseURL.ImagePath("bike")+data.getImage()).into(holder.bike_img);
        holder.name.setText(data.getCompnay()+" "+data.getBike_cc());
        holder.model.setText(data.getModel());
        holder.price.setText(data.getPrice()+" PKR");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_HOlder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView bike_img;
        TextView name,model,price;

        public View_HOlder(@NonNull View itemView) {
            super(itemView);
            bike_img = itemView.findViewById(R.id.bik_img);
            name = itemView.findViewById(R.id.name);
            model = itemView.findViewById(R.id.model);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, View_Bike.class);
            intent.putExtra("id",list.get(getLayoutPosition()).getId());
            context.startActivity(intent);
        }
    }
}
