package com.ss_technology.bikeshowroom.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.bikeshowroom.Container.Order_Container;
import com.ss_technology.bikeshowroom.R;

import java.util.List;

public class Transaction_Adapter extends RecyclerView.Adapter<Transaction_Adapter.View_Holder> {

    List<Order_Container> list;
    Context context;

    public Transaction_Adapter(List<Order_Container> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
      Order_Container data = list.get(position);
      holder.amount.setText("Amount: "+data.getTotal());
      holder.date.setText("Date: "+data.getDate());

      if(data.getType().equals("a")){
          holder.type.setText("Advance");
      }
      else if(data.getType().equals("c")){
            holder.type.setText("Full payment");
        }
      else if(data.getType().equals("i")){
          holder.type.setText("Installment");
      }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView amount,date,type;
        public View_Holder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            type = itemView.findViewById(R.id.type);

        }
    }
}
