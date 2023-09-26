package com.accenture.accpenture.database;

import static java.util.Objects.requireNonNull;

import android.app.Dialog;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FetchExpenseDataFromFirebase {

    public FetchExpenseDataFromFirebase() {
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
