package com.accenture.accpenture.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accenture.accpenture.ExpenseFragmentDataModel;
import com.accenture.accpenture.R;
import com.accenture.accpenture.TableAdapter;
import com.accenture.accpenture.TableDataModel;
import com.accenture.accpenture.database.Database;
import com.accenture.accpenture.database.ExpenseData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CategoryBasedAnalysis extends AppCompatActivity {
    private PieChart pieChart;
    private RecyclerView recyclerView;
    private int[] pricePerCategory = new int[10];
    private final String[] category = {"Food", "Travel", "Shopping", "Entertainment", "Health", "Education", "Appliances", "Grocery", "Crockery", "Other"};
    private Database database;
    private ArrayList<TableDataModel> tableDataModels;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();

        tableDataModels = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_based_analysis);

        pieChart = findViewById(R.id.pieChart);
        recyclerView = findViewById(R.id.recyclerViewCategoryBasedAnalysis);

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String timeStamp = String.valueOf(ts.getTime());

        String day = getDayFromTimestamp(timeStamp);
        String month = getMonthFromTimestamp(timeStamp);
        String year = getYearFromTimestamp(timeStamp);

        database = Database.getInstance(this);

        for (int i = 0; i < 10; i++) {
            ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameCategoryOnSameDay(category[i], day, month, year);
            for (ExpenseData expenseData1 : expenseData) {
                pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
            }
        }


        double totalExpense = 0;
        for (int price : pricePerCategory) {
            totalExpense += price;
        }

        if (totalExpense == 0) {
            Toast.makeText(this, "No expense data available for today", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.01f);

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(31f);


        ArrayList<PieEntry> yValues = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            if (pricePerCategory[i] != 0) {
                yValues.add(new PieEntry(pricePerCategory[i], category[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(new int[]{R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5,
                R.color.color6, R.color.color7, R.color.color8, R.color.color9, R.color.color10}, this);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(14f);
        pieData.setValueTextColor(Color.rgb(255, 0, 0));

        pieChart.setData(pieData);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setDataInRecyclerViewDataHolder(day, month, year);
        TableAdapter tableAdapter = new TableAdapter(tableDataModels);
        recyclerView.setAdapter(tableAdapter);
    }

    private void setDataInRecyclerViewDataHolder(String day, String month, String year) {
        ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameDay(day, month, year);
        int i = 1;
        for (ExpenseData expenseData1 : expenseData) {
            String commName = expenseData1.getCommName();
            String price = expenseData1.getPrice();
            String ii = String.valueOf(i);
            i++;
            tableDataModels.add(new TableDataModel(ii, commName, price));
        }
    }

    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
    private String getDayNameFromTimestamp(String timeStamp) {
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(Long.parseLong(timeStamp));
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
