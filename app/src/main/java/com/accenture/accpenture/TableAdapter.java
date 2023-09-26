package com.accenture.accpenture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder>{

    private ArrayList<TableDataModel> dataHolder;

    public TableAdapter(ArrayList<TableDataModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public TableAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_data_item, parent, false);
        return new TableAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.MyViewHolder holder, int position) {
        if (dataHolder != null && dataHolder.size() > 0) {
            holder.num.setText(dataHolder.get(position).getNumTableData());
            holder.name.setText(dataHolder.get(position).getNameTableData());
            holder.price.setText(dataHolder.get(position).getPriceTableData());
        }
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView num, name, price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            num = itemView.findViewById(R.id.textViewSNo);
            name = itemView.findViewById(R.id.textViewItemName);
            price = itemView.findViewById(R.id.textViewPriceTableData);
        }
    }
}
