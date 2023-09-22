package com.accenture.accpenture.activities;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accenture.accpenture.ExpenseAdapter;
import com.accenture.accpenture.ExpenseFragmentDataModel;
import com.accenture.accpenture.FragmentDataModel;
import com.accenture.accpenture.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class AddExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // material ui top action bar
    private MaterialToolbar actionBar;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<ExpenseFragmentDataModel> dataHolder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        bottomSheetDialog = new BottomSheetDialog(this);
        createDialog();

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
                bottomSheetDialog.show();
                return true;
            }
            else if (item.getItemId() == R.id.expense_save) {
                return true;
            }
            return false;
        });

    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.add_expense_dialog, null, false);
        String[] category = getResources().getStringArray(R.array.category);
        spinner = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        bottomSheetDialog.setContentView(view);

        TextInputLayout categoryEditText = view.findViewById(R.id.editTextCommodityNameExpense);
        TextInputLayout amountEditText = view.findViewById(R.id.editTextPriceExpense);
        TextInputLayout quantityEditText = view.findViewById(R.id.editTextQuantityExpense);
        Button saveButton = view.findViewById(R.id.buttonSaveExpense);

        saveButton.setOnClickListener(v -> {
            String selectedCategory = spinner.getSelectedItem().toString();
            String categoryString = Objects.requireNonNull(categoryEditText.getEditText()).getText().toString();
            String amountString = Objects.requireNonNull(amountEditText.getEditText()).getText().toString();
            String quantityString = Objects.requireNonNull(quantityEditText.getEditText()).getText().toString();
            dataHolder.add(new ExpenseFragmentDataModel(categoryString, amountString, quantityString, selectedCategory));
            ExpenseAdapter expenseAdapter = new ExpenseAdapter(dataHolder);
            recyclerView.setAdapter(expenseAdapter);
            bottomSheetDialog.dismiss();
        });
    }

    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedCategory = parent.getItemAtPosition(position).toString();
        System.out.println(selectedCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
