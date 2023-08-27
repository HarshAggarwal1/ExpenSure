package com.accenture.accpenture;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.android.material.textfield.TextInputLayout;

import kotlin.io.LineReader;

public class Register extends AppCompatActivity {

    // Variables
    private Button btnLogin, btnRegister;
    private ImageView imageLogo;
    private TextInputLayout username, password;
    private LinearLayout tempLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Layout in Full-Screen
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // 2 Change colour of System Bars
        WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        windowInsetsController.setAppearanceLightNavigationBars(true);
        windowInsetsController.setAppearanceLightStatusBars(true);

        // 3. Handle Overlapping Insets
        handleOverlappingInsets();

        setContentView(R.layout.activity_register);

        btnLogin = findViewById(R.id.backToLoginButton);
        imageLogo = findViewById(R.id.imageLogoRegister);
        btnRegister = findViewById(R.id.registerButton);
        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        tempLayout = findViewById(R.id.registerTempLayout);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void handleOverlappingInsets() {
        View view = getWindow().getDecorView();
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            v.setPadding(insets.left, v.getPaddingTop(), insets.right, v.getPaddingBottom());

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
