package com.accenture.accpenture.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ExpenseData")
public class ExpenseData {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "commName")
    private String commName;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "quantity")
    private String quantity;

    @ColumnInfo(name = "pricePerPiece")
    private String pricePerPiece;

    @ColumnInfo(name = "dayName")
    private String dayName;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "month")
    private String month;

    @ColumnInfo(name = "year")
    private String year;

    public ExpenseData(long id, String commName, String category, String price, String quantity, String pricePerPiece, String dayName, String day, String month, String year) {
        this.id = id;
        this.commName = commName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.pricePerPiece = pricePerPiece;
        this.dayName = dayName;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Ignore
    public ExpenseData(String commName, String category, String price, String quantity, String pricePerPiece, String dayName, String day, String month, String year) {
        this.commName = commName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.pricePerPiece = pricePerPiece;
        this.dayName = dayName;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(String commName) {
        this.commName = commName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(String pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
