package com.accenture.accpenture;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {

    // Variables
    private static final int SPLASH_SCREEN = 3000;
    Animation topAnim, bottomAnim;
    ImageView imageLogo;
    TextView textLogo;

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

        setContentView(R.layout.activity_main);

        // 4. Set Animations
        // Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.fade_top2bottom_animation_1000);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.fade_bottom2top_animation_1000);
        // Hooks
        imageLogo = findViewById(R.id.imageLogo);
        textLogo = findViewById(R.id.textLogo);
        String logoName = "AccpenSure";
        textLogo.setText(logoName.toLowerCase());
        imageLogo.setAnimation(topAnim);
        textLogo.setAnimation(bottomAnim);

        // Call next activity after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Call next activity
                Intent intent = new Intent(MainActivity.this, Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(imageLogo, "logo_image");
                pairs[1] = new Pair<View, String>(textLogo, "logo_string");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(intent, options.toBundle());
                finish();
            }
        }, SPLASH_SCREEN);
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
}
