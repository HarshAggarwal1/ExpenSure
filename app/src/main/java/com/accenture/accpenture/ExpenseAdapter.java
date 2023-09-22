package com.accenture.accpenture;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>{
    private ArrayList<FragmentDataModel> dataHolder;
    public ExpenseAdapter(ArrayList<FragmentDataModel> dataHolder) {
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
        holder.textView.setText(dataHolder.get(position).text);
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

        TextView textView;
        ImageButton deleteCategory;
        public ExpenseViewHolder(@NonNull View view) {
            super(view);
            textView = view.findViewById(R.id.editTextCategory);
            deleteCategory = view.findViewById(R.id.imageButtonCategoryDelete);
        }
    }
}
