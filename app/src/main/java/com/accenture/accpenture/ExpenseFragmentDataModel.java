package com.accenture.accpenture;

public class ExpenseFragmentDataModel{
    String commodityName;
    String commodityPrice;
    String commodityQuantity;
    String category;

    public ExpenseFragmentDataModel(String commodityName, String commodityPrice, String commodityQuantity, String category) {
        this.commodityName = commodityName;
        this.commodityPrice = commodityPrice;
        this.commodityQuantity = commodityQuantity;
        this.category = category;
    }
}