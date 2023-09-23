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

    String commodityQuantity;
    String category;

    public ExpenseFragmentDataModel(String commodityName, String commodityPrice, String commodityQuantity, String category) {
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityQuantity = commodityQuantity;
        this.category = category;
    }
}