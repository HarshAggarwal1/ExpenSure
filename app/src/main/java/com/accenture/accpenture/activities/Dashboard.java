package com.accenture.accpenture.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.accenture.accpenture.DashboardFragment;
import com.accenture.accpenture.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Dashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetDialog bottomSheetDialog;
    FrameLayout bottomSheet;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dashboard_fragment_container_view, new DashboardFragment())
                .commit();

        bottomSheet = findViewById(R.id.bottom_sheet);

        // applying peek height of bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(400);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);




    }

    private void config() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
