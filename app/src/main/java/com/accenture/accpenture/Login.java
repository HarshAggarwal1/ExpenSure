package com.accenture.accpenture;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class Login extends AppCompatActivity {

    // Variables
    Animation left2right;
    ImageView logo;
    Button btnRegister;

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

        setContentView(R.layout.activity_login);

        logo = findViewById(R.id.imageLogoLogin);

        btnRegister = findViewById(R.id.loginPageNewUser);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });



    }

//    private void moveLogo() { // To move the logo to the right permanently (Just Call this method in onCreate to use)
//        LinearLayout parent = (LinearLayout) logo.getParent();
//
//        ViewTreeObserver vto = parent.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                // Calculate the distance to the rightmost point of the parent
//                float parentWidth = (float) parent.getWidth();
//                float logoWidth = (float) logo.getWidth();
//                float translationX = parentWidth - logoWidth;
//                ObjectAnimator animator = ObjectAnimator.ofFloat(logo, "translationX", (translationX) / 2);
//                animator.setDuration(500);
//                animator.start();
//            }
//        });
//    }

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
        finishAffinity();
    }
}
