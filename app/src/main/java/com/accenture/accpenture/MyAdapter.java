package com.accenture.accpenture;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.accenture.accpenture.activities.CategoryBasedAnalysis;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<FragmentDataModel> dataHolder;

    public MyAdapter(ArrayList<FragmentDataModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(dataHolder.get(position).getText());
        if (position % 2 == 0) {
            holder.cardView.setBackgroundColor(Color.rgb(22, 2, 78));
        }
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CategoryBasedAnalysis.class);
            intent.putExtra("category", dataHolder.get(position).getText());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        MaterialCardView cardView;
        public MyViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.pieChartCard);
            textView = view.findViewById(R.id.pieChartText);
        }
    }
}
