package com.accenture.accpenture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>{
    private ArrayList<ExpenseFragmentDataModel> dataHolder;
    public ExpenseAdapter(ArrayList<ExpenseFragmentDataModel> dataHolder) {
        this.dataHolder = dataHolder;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_expense_category, parent, false);
        return new ExpenseAdapter.ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ExpenseViewHolder holder, int position) {
        holder.commodityName.setText(dataHolder.get(position).commodityName);
        holder.price.setText(dataHolder.get(position).commodityPrice);
        holder.commodityQuantity.setText(dataHolder.get(position).commodityQuantity);
        holder.category.setText(dataHolder.get(position).category);
        holder.deleteCategory.setOnClickListener(v -> {
            dataHolder.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return dataHolder.size();
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder{

        TextView commodityName;
        TextView price;
        TextView commodityQuantity;
        TextView category;
        ImageButton deleteCategory;
        public ExpenseViewHolder(@NonNull View view) {
            super(view);
            commodityName = view.findViewById(R.id.textViewCommodityNameCard);
            price = view.findViewById(R.id.textViewPricePerPieceCard);
            commodityQuantity = view.findViewById(R.id.textViewQuantityCard);
            category = view.findViewById(R.id.textViewCategoryCard);
            deleteCategory = view.findViewById(R.id.imageButtonCategoryDelete);
        }
    }
}
