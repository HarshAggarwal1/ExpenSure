package com.accenture.accpenture.activities;

import static java.util.Objects.requireNonNull;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.accenture.accpenture.ExpenseAdapter;
import com.accenture.accpenture.ExpenseFragmentDataModel;
import com.accenture.accpenture.R;
import com.accenture.accpenture.TableAdapter;
import com.accenture.accpenture.TableDataModel;
import com.accenture.accpenture.database.Database;
import com.accenture.accpenture.database.ExpenseData;
import com.accenture.accpenture.database.ExpensesHelperClassFirebase;
import com.accenture.accpenture.database.FetchExpenseDataFromFirebase;
import com.accenture.accpenture.database.UserHelperClassFirebase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // material ui top action bar
    private MaterialToolbar actionBar;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private BottomSheetDialog bottomSheetDialog;
    private ArrayList<ExpenseFragmentDataModel> dataHolder;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private ExpensesHelperClassFirebase helperClass;
    private Database database;
    private Dialog dialog;
    private long pickedTimeStamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();

        database = Database.getInstance(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        bottomSheetDialog = new BottomSheetDialog(this);
        createDialog();

        actionBar = findViewById(R.id.topAppBarExpense);
        recyclerView = findViewById(R.id.recycler_view_for_category_add_expense);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        dataHolder = new ArrayList<>();

        actionBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.expense_close) {
                onBackPressed();

                return true;
            }
            else if (item.getItemId() == R.id.expense_add_category) {
                bottomSheetDialog.show();
                return true;
            }
            else if (item.getItemId() == R.id.expense_save) {
                saveInDatabase();
                return true;
            }
            return false;
        });

    }

    private void saveInDatabase() {
        showProgressBar();
        if (dataHolder.size() == 0) {
            Toast.makeText(this, "No Expense Added!", Toast.LENGTH_SHORT).show();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
            String dateString = df.format(new Date());
            hideProgressBar();
            return;
        }
        int i = 0;
        for (; i < dataHolder.size(); i++) {
            ExpenseFragmentDataModel expenseFragmentDataModel = dataHolder.get(i);
            String commodityName = expenseFragmentDataModel.getCommodityName();
            String commodityPrice = expenseFragmentDataModel.getCommodityPrice();
            String commodityQuantity = expenseFragmentDataModel.getCommodityQuantity();
            String category = expenseFragmentDataModel.getCategory();
            long timeStamp = expenseFragmentDataModel.getTimestamp();

            int j = 0;
            while (j < 320) {
                j++;
            }
            saveInDatabaseItem(commodityName, commodityPrice, commodityQuantity, category, timeStamp);
        }
        if (dataHolder.size() == i) {
            hideProgressBar();
        }
        Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();

        onBackPressed();
    }

    private void saveInDatabaseItem(String commodityName, String commodityPrice, String commodityQuantity, String commodityCategory, long timeStamp) {

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("expenses");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
        String dateString = df.format(new Date());

        String userNameExpensesFirebase = database.appDao().getFirstRow().getUsername();

        String currTimeStamp = String.valueOf(timeStamp);

        helperClass = new ExpensesHelperClassFirebase(currTimeStamp, userNameExpensesFirebase, commodityCategory, commodityPrice, commodityName, commodityQuantity, dateString);

        String pricePerPiece = String.valueOf(Double.parseDouble(commodityPrice) / Double.parseDouble(commodityQuantity));
        String dayName = getDayNameFromTimestamp(currTimeStamp);
        String day = getDayFromTimestamp(currTimeStamp);
        String month = getMonthFromTimestamp(currTimeStamp);
        String year = getYearFromTimestamp(currTimeStamp);

        database.expenseDao().insert(new com.accenture.accpenture.database.ExpenseData(commodityName, commodityCategory, commodityPrice, commodityQuantity, pricePerPiece, dayName, day, month, year));

        reference.child(currTimeStamp).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Expense Added!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Expense Not Added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.add_expense_dialog, null, false);
        String[] category = getResources().getStringArray(R.array.category);
        spinner = view.findViewById(R.id.spinnerCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        bottomSheetDialog.setContentView(view);

        TextInputLayout commodityEditText = view.findViewById(R.id.editTextCommodityNameExpense);
        TextInputLayout amountEditText = view.findViewById(R.id.editTextPriceExpense);
        TextInputLayout quantityEditText = view.findViewById(R.id.editTextQuantityExpense);
        ImageView imageViewDatePicker = view.findViewById(R.id.imageViewCalendarIconAddExpense);

        Button saveButton = view.findViewById(R.id.buttonSaveExpense);
        pickedTimeStamp = System.currentTimeMillis();
        imageViewDatePicker.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddExpense.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            // timestamp convert from year month and dayOfMonth
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, dayOfMonth);
                            pickedTimeStamp = calendar.getTimeInMillis();
                        }
                    },
                    year, month, dayOfMonth);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        });


        saveButton.setOnClickListener(v -> {
            String selectedCategory = spinner.getSelectedItem().toString();
            String commodityString = Objects.requireNonNull(commodityEditText.getEditText()).getText().toString();
            String amountString = Objects.requireNonNull(amountEditText.getEditText()).getText().toString();
            String quantityString = Objects.requireNonNull(quantityEditText.getEditText()).getText().toString();
            if (selectedCategory.equals("Select Category…")) {
                Toast.makeText(this, "No Category Selected!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
                return;
            }
            if (commodityString.isEmpty()) {
                commodityEditText.setError("Field can't be empty");
                return;
            }
            else {
                commodityEditText.setError(null);
            }
            if (amountString.isEmpty()) {
                amountEditText.setError("Field can't be empty");
                return;
            }
            else {
                amountEditText.setError(null);
            }
            if (quantityString.isEmpty()) {
                quantityEditText.setError("Field can't be empty");
                return;
            }
            else {
                quantityEditText.setError(null);
            }
            dataHolder.add(new ExpenseFragmentDataModel(commodityString, amountString, quantityString, selectedCategory, pickedTimeStamp));
            ExpenseAdapter expenseAdapter = new ExpenseAdapter(dataHolder);
            recyclerView.setAdapter(expenseAdapter);
            bottomSheetDialog.dismiss();
        });
    }

    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedCategory = parent.getItemAtPosition(position).toString();
        System.out.println(selectedCategory);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showProgressBar() {
        dialog = new Dialog(AddExpense.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_bar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void hideProgressBar() {
        dialog.dismiss();
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
