package com.ss_technology.bikeshowroom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.bikeshowroom.Container.Message_Container;
import com.ss_technology.bikeshowroom.R;

import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.View_Holder> {

    Context context;
    List<Message_Container> list;

    public Message_Adapter(Context context, List<Message_Container> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter_veiw,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
       Message_Container data = list.get(position);
       if(data.getReplay_status().trim().equals("1")){
           holder.replay_Row.setVisibility(View.VISIBLE);
       }
       holder.subject.setText(data.getSubject());
       holder.message.setText(data.getMessage());
       holder.date.setText(data.getDate());
       holder.replay.setText(data.getReplay());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {

        TextView subject,message,date,replay;
        TableRow replay_Row;

        public View_Holder(@NonNull View itemView) {
            super(itemView);

            replay_Row = itemView.findViewById(R.id.replay_Row);
            subject = itemView.findViewById(R.id.subject);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
            replay = itemView.findViewById(R.id.replay);

        }
    }
}
