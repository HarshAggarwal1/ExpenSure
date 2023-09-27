package com.accenture.accpenture.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accenture.accpenture.R;
import com.accenture.accpenture.TableAdapter;
import com.accenture.accpenture.TableDataModel;
import com.accenture.accpenture.database.Database;
import com.accenture.accpenture.database.ExpenseData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class AllExpenses extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView imageViewDate;
    private TextView textViewDate;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_expenses);

        Toast.makeText(this, "Select a date to view expenses", Toast.LENGTH_LONG).show();

        database = Database.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewAllExpenses);
        imageViewDate = findViewById(R.id.imageViewCalendarIconAllExpenses);
        textViewDate = findViewById(R.id.textViewTimeOfViewAllExpenses);

        imageViewDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AllExpenses.this,
                    new DatePickerDialog.OnDateSetListener() {
                        private ArrayList<TableDataModel> tableDataModels = new ArrayList<>();
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String timeOfView = dayOfMonth + "-" + (month + 1) + "-" + year;
                            textViewDate.setText(timeOfView);
                            RecyclerView.LayoutManager layoutManager = new androidx.recyclerview.widget.LinearLayoutManager(AllExpenses.this, LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            setDataInRecyclerViewDataHolder(String.valueOf(dayOfMonth), String.valueOf(month + 1), String.valueOf(year));
                            TableAdapter tableAdapter = new TableAdapter(tableDataModels);
                            recyclerView.setAdapter(tableAdapter);
                        }
                        private void setDataInRecyclerViewDataHolder(String day, String month, String year) {
                            ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameDay(day, month, year);
                            if (expenseData.length == 0) {
                                Toast.makeText(AllExpenses.this, "No expenses on this day", Toast.LENGTH_SHORT).show();
                            }
                            int i = 1;
                            for (ExpenseData expenseData1 : expenseData) {
                                String commName = expenseData1.getCommName();
                                String price = expenseData1.getPrice();
                                String ii = String.valueOf(i);
                                i++;
                                tableDataModels.add(new TableDataModel(ii, commName, price));
                            }
                        }
                    },
                    year, month, dayOfMonth);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        });
    }
    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
}
