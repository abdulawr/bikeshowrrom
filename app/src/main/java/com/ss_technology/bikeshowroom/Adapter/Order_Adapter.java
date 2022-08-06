package com.ss_technology.bikeshowroom.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.bikeshowroom.Activity.Order_Details;
import com.ss_technology.bikeshowroom.Container.Order_Container;
import com.ss_technology.bikeshowroom.R;

import java.util.List;

public class Order_Adapter extends RecyclerView.Adapter<Order_Adapter.View_Holder> {

    Context context;
    List<Order_Container> list;

    public Order_Adapter(Context context, List<Order_Container> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
      Order_Container data = list.get(position);
      holder.order_id.setText("#"+data.getId());
      holder.date.setText(data.getDate());
      holder.amount.setText(data.getTotal()+" PKR");

      if(data.getSell_type().equals("2")){
          holder.type.setText("Installment");
      }
      else{
          holder.type.setText("Cash");
      }

      String sst = "";
      if(data.getType().equals("0")){
        sst = "Pending";
      }
      else if(data.getType().equals("1"))
      {
          sst = "Accepted";
      }else if(data.getType().equals("2"))
      {
          sst = "Active";
      }else if(data.getType().equals("3"))
      {
          sst = "Completed";
      }
      holder.status.setText(sst);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView order_id,date,amount,type,status;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            order_id = itemView.findViewById(R.id.order_id);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            type = itemView.findViewById(R.id.type);
            status = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, Order_Details.class);
            intent.putExtra("obj",list.get(getLayoutPosition()));
            context.startActivity(intent);
        }
    }
}
