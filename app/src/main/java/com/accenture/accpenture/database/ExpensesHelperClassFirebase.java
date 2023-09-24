package com.accenture.accpenture.database;

import java.util.Date;

public class ExpensesHelperClassFirebase {
    public String getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getCommName() {
        return commName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public String getDayName() {
        return dayName;
    }

    String id, userId, category, price, date, commName, quantity, dayName;

    public ExpensesHelperClassFirebase(String id, String userId, String category, String amount, String date, String commName, String quantity, String dayName) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.price = amount;
        this.date = date;
        this.commName = commName;
        this.quantity = quantity;
        this.dayName = dayName;
    }
}
