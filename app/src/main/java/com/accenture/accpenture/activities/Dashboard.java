package com.accenture.accpenture.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.accenture.accpenture.fragments.DashboardFragment;
import com.accenture.accpenture.R;
import com.accenture.accpenture.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton addExpense;
    private String activeFrag = "dashboard";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);
         // set bottom navigation to first option
        bottomNavigationView.setSelectedItemId(R.id.dashboard_bottom_bar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dashboard_fragment_container_view, new DashboardFragment())
                .commit();

        addExpense = findViewById(R.id.fab_dashboard);

        addExpense.setOnClickListener(v -> {
            // Transition to AddExpense activity with animation
            Intent intent = new Intent(Dashboard.this, AddExpense.class);
            startActivity(intent);
        });

        // show dashboard fragment on bottom navigation item click

        bottomNavigationView.setOnItemSelectedListener(v -> {
            if (v.getItemId() == R.id.dashboard_bottom_bar) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dashboard_fragment_container_view, new DashboardFragment())
                        .commit();
                activeFrag = "dashboard";
                return true;
            }
            else if (v.getItemId() == R.id.user_settings_bottom_bar) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dashboard_fragment_container_view, new SettingsFragment())
                        .commit();
                activeFrag = "settings";
                return true;
            }
            return false;
        });

    }

    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

//        bottomSheet = findViewById(R.id.bottom_sheet);
//
//        // applying peek height of bottom sheet
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        bottomSheetBehavior.setPeekHeight(400);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);