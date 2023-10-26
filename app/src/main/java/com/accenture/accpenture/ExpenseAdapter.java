package com.accenture.accpenture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        String price = "Total Price: " + dataHolder.get(position).commodityPrice;
        holder.price.setText(price);
        String quantity = "Quantity: " + dataHolder.get(position).commodityQuantity;
        holder.commodityQuantity.setText(quantity);
        String category = "Category: " + dataHolder.get(position).category;
        holder.category.setText(category);
        String timeStamp = String.valueOf(dataHolder.get(position).getTimestamp());
        String date = "Date: " + getDayFromTimestamp(timeStamp) + "/" + getMonthFromTimestamp(timeStamp) + "/" + getYearFromTimestamp(timeStamp);
        holder.date.setText(date);
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
        TextView date;
        ImageButton deleteCategory;
        public ExpenseViewHolder(@NonNull View view) {
            super(view);
            commodityName = view.findViewById(R.id.textViewCommodityNameCard);
            price = view.findViewById(R.id.textViewPricePerPieceCard);
            commodityQuantity = view.findViewById(R.id.textViewQuantityCard);
            category = view.findViewById(R.id.textViewCategoryCard);
            deleteCategory = view.findViewById(R.id.imageButtonCategoryDelete);
            date = view.findViewById(R.id.textViewDateCard);
        }
    }
    private String getMonthFromTimestamp(String timeStamp) {
        Timestamp ts = new Timestamp(Long.parseLong(timeStamp));
        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return String.valueOf(month + 1);
    }
    private String getYearFromTimestamp(String timeStamp) {
        Timestamp ts = new Timestamp(Long.parseLong(timeStamp));
        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return String.valueOf(year);
    }
    private String getDayFromTimestamp(String timeStamp) {
        Timestamp ts = new Timestamp(Long.parseLong(timeStamp));
        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }
}
