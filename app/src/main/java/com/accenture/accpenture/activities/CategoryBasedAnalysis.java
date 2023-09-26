package com.accenture.accpenture.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.accenture.accpenture.ExpenseFragmentDataModel;
import com.accenture.accpenture.R;
import com.accenture.accpenture.database.Database;
import com.accenture.accpenture.database.ExpenseData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
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
    private TextView date, expense;
    private int[] pricePerCategory = new int[10];
    private final String[] category = {"Food", "Travel", "Shopping", "Entertainment", "Health", "Education", "Appliances", "Grocery", "Crockery", "Other"};
    private Database database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_based_analysis);

        pieChart = findViewById(R.id.pieChart);
        date = findViewById(R.id.dateCategoryBasedAnalysis);
        expense = findViewById(R.id.totalExpenseCategoryBasedAnalysis);

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String timeStamp = String.valueOf(ts.getTime());

        String day = getDayFromTimestamp(timeStamp);
        String month = getMonthFromTimestamp(timeStamp);
        String year = getYearFromTimestamp(timeStamp);

        String dateText = "Date: " + day + "-" + month + "-" + year;
        date.setText(dateText);

        database = Database.getInstance(this);

        for (int i = 0; i < 10; i++) {
            ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameCategory(category[i]);
            for (ExpenseData expenseData1 : expenseData) {
                if (expenseData1.getDay().equals(day) && expenseData1.getMonth().equals(month) && expenseData1.getYear().equals(year)) {
                    pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                }
            }
        }

        double totalExpense = 0;
        for (int price : pricePerCategory) {
            totalExpense += price;
        }

        String expenseText = "Total Expense: " + totalExpense;
        expense.setText(expenseText);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.50f);

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

        PieDataSet dataSet = new PieDataSet(yValues, "Category");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.rgb(156, 39, 176));

        pieChart.setData(pieData);
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
