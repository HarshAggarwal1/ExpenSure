package com.accenture.accpenture;

public class TableDataModel {
    public String getNumTableData() {
        return num;
    }

    public String getNameTableData() {
        return name;
    }

    public String getPriceTableData() {
        return price;
    }

    private String num;
    private String name;
    private String price;

    public TableDataModel(String num, String name, String price) {
        this.num = num;
        this.name = name;
        this.price = price;
    }
}
