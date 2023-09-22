package com.accenture.accpenture.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accenture.accpenture.ExpenseAdapter;
import com.accenture.accpenture.FragmentDataModel;
import com.accenture.accpenture.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AddExpense extends AppCompatActivity {
    // material ui top action bar
    private MaterialToolbar actionBar;
    private RecyclerView recyclerView;
    private ArrayList<FragmentDataModel> dataHolder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        actionBar = findViewById(R.id.topAppBarExpense);
        recyclerView = findViewById(R.id.recycler_view_for_category_add_expense);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        dataHolder = new ArrayList<>();

        actionBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.expense_close) {
                onBackPressed();

                return true;
            }
            else if (item.getItemId() == R.id.expense_add_category) {
                dataHolder.add(new FragmentDataModel("Category Added"));
                ExpenseAdapter expenseAdapter = new ExpenseAdapter(dataHolder);
                recyclerView.setAdapter(expenseAdapter);

                return true;
            }
            else if (item.getItemId() == R.id.expense_save) {
                return true;
            }
            return false;
        });

    }
    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
