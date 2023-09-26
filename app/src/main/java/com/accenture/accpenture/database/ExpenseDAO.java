package com.accenture.accpenture.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ExpenseDAO {
    @Insert
    void insert(ExpenseData expenseData);

    @Query("SELECT COUNT(*) FROM ExpenseData")
    int count();

    // select all fields from same day, month and year
    @Query("SELECT * FROM ExpenseData WHERE day = :day AND month = :month AND year = :year")
    ExpenseData[] getExpenseDataFromSameDay(String day, String month, String year);

    // select all fields from same month and year
    @Query("SELECT * FROM ExpenseData WHERE month = :month AND year = :year")
    ExpenseData[] getExpenseDataFromSameMonth(String month, String year);

    // select all fields from same year
    @Query("SELECT * FROM ExpenseData WHERE year = :year")
    ExpenseData[] getExpenseDataFromSameYear(String year);

    // select all fields from same category
    @Query("SELECT * FROM ExpenseData WHERE category = :category")
    ExpenseData[] getExpenseDataFromSameCategory(String category);

    // get all fields of a particular category on a particular day and month and year
    @Query("SELECT * FROM ExpenseData WHERE category = :category AND day = :day AND month = :month AND year = :year")
    ExpenseData[] getExpenseDataFromSameCategoryOnSameDay(String category, String day, String month, String year);

    // get all fields of a particular category on a particular month and year
    @Query("SELECT * FROM ExpenseData WHERE category = :category AND month = :month AND year = :year")
    ExpenseData[] getExpenseDataFromSameCategoryOnSameMonth(String category, String month, String year);

    // get all fields of a particular category on a particular year
    @Query("SELECT * FROM ExpenseData WHERE category = :category AND year = :year")
    ExpenseData[] getExpenseDataFromSameCategoryOnSameYear(String category, String year);

    // delete all fields from same day, month and year
    @Query("DELETE FROM ExpenseData WHERE day = :day AND month = :month AND year = :year")
    void deleteExpenseDataFromSameDay(String day, String month, String year);


    // delete all fields from same month and year
    @Query("DELETE FROM ExpenseData WHERE month = :month AND year = :year")
    void deleteExpenseDataFromSameMonth(String month, String year);

    // delete all fields from same year
    @Query("DELETE FROM ExpenseData WHERE year = :year")
    void deleteExpenseDataFromSameYear(String year);

    // delete all fields from same category
    @Query("DELETE FROM ExpenseData WHERE category = :category")
    void deleteExpenseDataFromSameCategory(String category);

    @Query("DELETE FROM ExpenseData")
    void deleteAll();
}
