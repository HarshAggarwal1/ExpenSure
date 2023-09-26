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
    private TextView timeOfView, totalSpent, totalSpentDesc;
    private String categoryIntent;
    private int[] pricePerCategory = new int[10];
    private final String[] category = {"Food", "Travel", "Shopping", "Entertainment", "Health", "Education", "Appliances", "Grocery", "Crockery", "Other"};
    private Database database;
    private ArrayList<TableDataModel> tableDataModels;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_based_analysis);

        // common part
        tableDataModels = new ArrayList<>();

        pieChart = findViewById(R.id.pieChart);
        recyclerView = findViewById(R.id.recyclerViewCategoryBasedAnalysis);
        timeOfView =   findViewById(R.id.textViewTimeOfViewCategoryBasedAnalysis);
        totalSpent = findViewById(R.id.textViewTotalSpentToday);
        totalSpentDesc = findViewById(R.id.textViewTotalSpentTodayDescriptor);


        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String timeStamp = String.valueOf(ts.getTime());
        String day = getDayFromTimestamp(timeStamp);
        String month = getMonthFromTimestamp(timeStamp);
        String year = getYearFromTimestamp(timeStamp);

        database = Database.getInstance(this);

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // -----------------------------------------------------------------------------------------

         categoryIntent = getIntent().getStringExtra("category");

        if (categoryIntent.equals("Today")) {
            String timeOfViewText = getDayNameFromTimestamp(timeStamp) + ", " + day + "/" + month + "/" + year;
            timeOfView.setText(timeOfViewText);
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
            String totalSpentText = "Total Spent Today: ";
            totalSpentDesc.setText(totalSpentText);
            String totalSpentText1 = "₹" + String.valueOf(totalExpense);
            totalSpent.setText(totalSpentText1);

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
            setDataInRecyclerViewDataHolder(day, month, year);
            TableAdapter tableAdapter = new TableAdapter(tableDataModels);
            recyclerView.setAdapter(tableAdapter);
        }
        else if (categoryIntent.equals("Weekly")) {
            String weekNum = getWeekNumOfMonth(day);
            String timeOfViewText = "Week " + weekNum + " of " + getMonthNameFromMonthVal(month);
            timeOfView.setText(timeOfViewText);

            for (int i = 0; i < 10; i++) {
                ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameCategoryOnSameMonth(category[i], month, year);
                makePricePerCategoryForWeek(expenseData, day, i);
            }

            double totalExpense = 0;
            for (int price : pricePerCategory) {
                totalExpense += price;
            }
            if (totalExpense == 0) {
                Toast.makeText(this, "No expense data available for this week", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            String totalSpentText = "Total Spent This Week: ";
            totalSpentDesc.setText(totalSpentText);
            String totalSpentText1 = "₹" + String.valueOf(totalExpense);
            totalSpent.setText(totalSpentText1);

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
            setDataInRecyclerViewDataHolderOnSameWeek(month, year, day);
            TableAdapter tableAdapter = new TableAdapter(tableDataModels);
            recyclerView.setAdapter(tableAdapter);
        }
        else if (categoryIntent.equals("Monthly")) {
            String timeOfViewText = getMonthNameFromMonthVal(month) + ", " + year;
            timeOfView.setText(timeOfViewText);

            for (int i = 0; i < 10; i++) {
                ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameCategoryOnSameMonth(category[i], month, year);
                for (ExpenseData expenseData1 : expenseData) {
                    pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                }
            }

            double totalExpense = 0;
            for (int price : pricePerCategory) {
                totalExpense += price;
            }
            if (totalExpense == 0) {
                Toast.makeText(this, "No expense data available for this month", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            String totalSpentText = "Total Spent This Month: ";
            totalSpentDesc.setText(totalSpentText);
            String totalSpentText1 = "₹" + String.valueOf(totalExpense);
            totalSpent.setText(totalSpentText1);

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
            setDataInRecyclerViewDataHolderOnSameMonth(month, year);
            TableAdapter tableAdapter = new TableAdapter(tableDataModels);
            recyclerView.setAdapter(tableAdapter);
        }
        else if (categoryIntent.equals("Yearly")) {
            String timeOfViewText = year;
            timeOfView.setText(timeOfViewText);

            for (int i = 0; i < 10; i++) {
                ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameCategoryOnSameYear(category[i], year);
                for (ExpenseData expenseData1 : expenseData) {
                    pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                }
            }

            double totalExpense = 0;
            for (int price : pricePerCategory) {
                totalExpense += price;
            }
            if (totalExpense == 0) {
                Toast.makeText(this, "No expense data available for this year", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            String totalSpentText = "Total Spent This Year: ";
            totalSpentDesc.setText(totalSpentText);
            String totalSpentText1 = "₹" + String.valueOf(totalExpense);
            totalSpent.setText(totalSpentText1);

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
            setDataInRecyclerViewDataHolderOnSameYear(year);
            TableAdapter tableAdapter = new TableAdapter(tableDataModels);
            recyclerView.setAdapter(tableAdapter);
        }
        else {
            Toast.makeText(this, "BACK!!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    private void setDataInRecyclerViewDataHolderOnSameYear(String year) {
        ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameYear(year);
        int i = 1;
        for (ExpenseData expenseData1 : expenseData) {
            String commName = expenseData1.getCommName();
            String price = expenseData1.getPrice();
            String ii = String.valueOf(i);
            i++;
            tableDataModels.add(new TableDataModel(ii, commName, price));
        }
    }

    private void setDataInRecyclerViewDataHolderOnSameMonth(String month, String year) {
        ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameMonth(month, year);
        int i = 1;
        for (ExpenseData expenseData1 : expenseData) {
            String commName = expenseData1.getCommName();
            String price = expenseData1.getPrice();
            String ii = String.valueOf(i);
            i++;
            tableDataModels.add(new TableDataModel(ii, commName, price));
        }
    }

    private void setDataInRecyclerViewDataHolderOnSameWeek(String month, String year, String day) {
        if (tableDataModels.size() != 0) {
            tableDataModels.clear();
        }
        ExpenseData[] expenseData = database.expenseDao().getExpenseDataFromSameMonth(month, year);
        int dayVal = Integer.parseInt(day);
        if (dayVal % 7 == 1) {
            int i = 0;
            int dayStart = dayVal;
            int dayEnd = dayVal + 6;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
        else if (dayVal % 7 == 2) {
            int i = 0;
            int dayStart = dayVal - 1;
            int dayEnd = dayVal + 5;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
        else if (dayVal % 7 == 3) {
            int i = 0;
            int dayStart = dayVal - 2;
            int dayEnd = dayVal + 4;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
        else if (dayVal % 7 == 4) {
            int i = 0;
            int dayStart = dayVal - 3;
            int dayEnd = dayVal + 3;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
        else if (dayVal % 7 == 5) {
            int i = 0;
            int dayStart = dayVal - 4;
            int dayEnd = dayVal + 2;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
        else if (dayVal % 7 == 6) {
            int i = 0;
            int dayStart = dayVal - 5;
            int dayEnd = dayVal + 1;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
        else {
            int i = 0;
            int dayStart = dayVal - 6;
            int dayEnd = dayVal;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        String commName = expenseData1.getCommName();
                        String price = expenseData1.getPrice();
                        String ii = String.valueOf(i);
                        i++;
                        tableDataModels.add(new TableDataModel(ii, commName, price));
                    }
                }
            }
        }
    }

    private void makePricePerCategoryForWeek(ExpenseData[] expenseData, String day, int i) {
        if (expenseData.length == 0) {
            return;
        }
        int dayVal = Integer.parseInt(day);
        if (dayVal % 7 == 1) {
            int dayStart = dayVal;
            int dayEnd = dayVal + 6;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
        else if (dayVal % 7 == 2) {
            int dayStart = dayVal - 1;
            int dayEnd = dayVal + 5;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
        else if (dayVal % 7 == 3) {
            int dayStart = dayVal - 2;
            int dayEnd = dayVal + 4;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
        else if (dayVal % 7 == 4) {
            int dayStart = dayVal - 3;
            int dayEnd = dayVal + 3;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
        else if (dayVal % 7 == 5) {
            int dayStart = dayVal - 4;
            int dayEnd = dayVal + 2;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
        else if (dayVal % 7 == 6) {
            int dayStart = dayVal - 5;
            int dayEnd = dayVal + 1;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
        else {
            int dayStart = dayVal - 6;
            int dayEnd = dayVal;
            for (int dayVal1 = dayStart; dayVal1 <= dayEnd; dayVal1++) {
                String dayVal1String = String.valueOf(dayVal1);
                for (ExpenseData expenseData1 : expenseData) {
                    if (expenseData1.getDay().equals(dayVal1String)) {
                        pricePerCategory[i] += Integer.parseInt(expenseData1.getPrice());
                    }
                }
            }
        }
    }


    private String getWeekNumOfMonth(String day) {
        int dayVal = Integer.parseInt(day);

        if (dayVal <= 7) {
            return "1";
        }
        else if (dayVal <= 14) {
            return "2";
        }
        else if (dayVal <= 21) {
            return "3";
        }
        else if (dayVal <= 28) {
            return "4";
        }
        else {
            return "5";
        }
    }

    private String getMonthNameFromMonthVal(String month) {
        if (month.equals("1")) {
            return "January";
        }
        else if (month.equals("2")) {
            return "February";
        }
        else if (month.equals("3")) {
            return "March";
        }
        else if (month.equals("4")) {
            return "April";
        }
        else if (month.equals("5")) {
            return "May";
        }
        else if (month.equals("6")) {
            return "June";
        }
        else if (month.equals("7")) {
            return "July";
        }
        else if (month.equals("8")) {
            return "August";
        }
        else if (month.equals("9")) {
            return "September";
        }
        else if (month.equals("10")) {
            return "October";
        }
        else if (month.equals("11")) {
            return "November";
        }
        else {
            return "December";
        }
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
