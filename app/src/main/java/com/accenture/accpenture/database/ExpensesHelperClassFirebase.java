package com.accenture.accpenture.database;

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

    public String getCommName() {
        return commName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    String id, userId, category, price, commName, quantity, date;

    public ExpensesHelperClassFirebase(String id, String userId, String category, String amount, String commName, String quantity, String date) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.price = amount;
        this.commName = commName;
        this.quantity = quantity;
        this.date = date;
    }
}
