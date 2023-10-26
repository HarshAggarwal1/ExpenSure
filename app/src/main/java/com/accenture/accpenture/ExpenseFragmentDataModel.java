package com.accenture.accpenture;

public class ExpenseFragmentDataModel{
    String commodityName;
    String commodityPrice;

    public String getCommodityName() {
        return commodityName;
    }

    public String getCommodityPrice() {
        return commodityPrice;
    }

    public String getCommodityQuantity() {
        return commodityQuantity;
    }

    public String getCategory() {
        return category;
    }
    public long getTimestamp() {
        return timestamp;
    }

    String commodityQuantity;
    String category;
    long timestamp;

    public ExpenseFragmentDataModel(String commodityName, String commodityPrice, String commodityQuantity, String category, long timestamp) {
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityQuantity = commodityQuantity;
        this.category = category;
        this.timestamp = timestamp;
    }
}