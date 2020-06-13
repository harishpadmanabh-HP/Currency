package com.hp.ocr_googlevisionapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hp.ocr_googlevisionapi.models.HistoryModel;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    HistoryModel historyModel;

    public Adapter(HistoryModel historyModel) {
        this.historyModel = historyModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.amount.setText(historyModel.getAmount_details().get(position).getAmount()+" Rs note Scanned");

    }

    @Override
    public int getItemCount() {
        return historyModel.getAmount_details().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amount=itemView.findViewById(R.id.amount);
        }
    }
}
