package com.accenture.accpenture.fragments;

import static java.util.Objects.requireNonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.accpenture.R;
import com.accenture.accpenture.activities.Login;
import com.accenture.accpenture.database.Database;
import com.accenture.accpenture.database.ExpenseData;
import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialCardView logoutCardView, loadDatabaseCardView;
    private TextView userFullName;
    private ImageView userImage;
    private Database database;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceForImage;
    private Dialog dialog;
    private Context context;
    private Query query;
    private String timeStamp;
    private String date;
    private String userID;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        assert container != null;
        context = container.getContext();
        database = Database.getInstance(getContext());
        String fullName = database.appDao().getFirstRow().getFName() + " " + database.appDao().getFirstRow().getLName();
        userID = database.appDao().getFirstRow().getUsername();
        View view = inflater.inflate(
                R.layout.fragment_settings, container, false);
        logoutCardView = view.findViewById(R.id.logoutUserProfile);
        loadDatabaseCardView = view.findViewById(R.id.loadDatabaseUserProfile);
        userFullName = view.findViewById(R.id.userFullNameUserProfile);
        userFullName.setText(fullName);
        userImage = view.findViewById(R.id.userProfileImageUserProfile);

        // set user image
        setUserImage();

        logoutCardView.setOnClickListener(v -> {
            // Transition to Login activity with animation
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        });

        loadDatabaseCardView.setOnClickListener(v -> {
            loadDatabase();
        });

        return view;
    }

    private void loadDatabase() {
        showProgressBar();
        addExpenseData();
    }

    private void setUserImage() {
        showProgressBar();
        // Get image from firebase
        String userName = database.appDao().getFirstRow().getUsername();
        databaseReferenceForImage = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = databaseReferenceForImage.orderByChild("username").equalTo(userName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Uri imageUri = Uri.parse(snapshot.child(userName).child("profilePicture").getValue(String.class));
                    Glide.with(requireContext()).load(imageUri).into(userImage);
                }
                else {
                    Toast.makeText(getContext(), "No such user exists", Toast.LENGTH_SHORT).show();
                }
                hideProgressBar();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error in loading Profile Picture", Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });

    }

    private void showProgressBar() {
        dialog = new Dialog(this.context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_bar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void hideProgressBar() {
        dialog.dismiss();
    }

    private void addExpenseData() {

        database.expenseDao().deleteAll();

        databaseReference = FirebaseDatabase.getInstance().getReference("expenses");
        query = databaseReference.orderByChild("userId").equalTo(userID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("date").getValue() == null) {
                        continue;
                    }

                    String commName = requireNonNull(dataSnapshot.child("commName").getValue()).toString();
                    String category = requireNonNull(dataSnapshot.child("category").getValue()).toString();
                    String price = requireNonNull(dataSnapshot.child("price").getValue()).toString();
                    String quantity = requireNonNull(dataSnapshot.child("quantity").getValue()).toString();
                    String timeStamp = requireNonNull(dataSnapshot.child("id").getValue()).toString();
                    String dayName = getDayNameFromTimestamp(timeStamp);
                    String day = getDayFromTimestamp(timeStamp);
                    String month = getMonthFromTimestamp(timeStamp);
                    String year = getYearFromTimestamp(timeStamp);

                    int temp_price = Integer.parseInt(price);
                    int temp_quantity = Integer.parseInt(quantity);

                    double pricePerPiece = (double) temp_price / (double) temp_quantity;

                    String pricePerPieceString = String.valueOf(pricePerPiece);

                    // Add to ROOM database
                    database.expenseDao().insert(new ExpenseData(commName, category, price, quantity, pricePerPieceString, dayName, day, month, year));

                }
                Toast.makeText(getContext(), "Data Recovered Successfully", Toast.LENGTH_SHORT).show();
                query.removeEventListener(this);
                hideProgressBar();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error: " + error.getMessage());
                query.removeEventListener(this);
                hideProgressBar();
            }
        });
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

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setDate(String date) {
        this.date = date;
    }
}