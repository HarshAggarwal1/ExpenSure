package com.accenture.accpenture;

import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.accenture.accpenture.database.AppData;
import com.accenture.accpenture.database.Database;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    // Variables
    private Button btnRegister, btnLogin;
    private TextInputLayout username, password;
    private Database database;

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

        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        btnRegister = findViewById(R.id.loginPageNewUser);
        btnLogin = findViewById(R.id.loginButton);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(username, "username_move");
                pairs[1] = new Pair<View, String>(password, "pass_move");
                pairs[2] = new Pair<View, String>(btnRegister, "cred_button");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
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

    public void loginUser(View view) {
        if (!validateUsername() | !validatePassword()) {
            return;
        }
        else {
            isUser();
        }
    }

    private void isUser() {
        String _username = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(_username).child("password").getValue(String.class);
                    if (passwordFromDB.equals(_password)) {

                        username.setError(null);
                        username.setErrorEnabled(false);

                        String fName = snapshot.child(_username).child("fname").getValue(String.class);
                        String lName = snapshot.child(_username).child("lname").getValue(String.class);
                        String phone = snapshot.child(_username).child("phone").getValue(String.class);
                        String email = snapshot.child(_username).child("email").getValue(String.class);
                        String username = snapshot.child(_username).child("username").getValue(String.class);
                        String name = fName + " " + lName;

                        database = Database.getInstance(getApplicationContext());
                        database.appDao().insert(new AppData(email, passwordFromDB, fName, lName, phone, username));

//                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                        intent.putExtra("name", name);
//                        intent.putExtra("username", username);
//                        intent.putExtra("email", email);
//                        intent.putExtra("phone", phone);
//                        intent.putExtra("password", passwordFromDB);
//                        startActivity(intent);

                    }
                    else {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else {
                    username.setError("No such User exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        String checkSpaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (val.length() > 15) {
            username.setError("Username is too large!");
            return false;
        } else if (!val.matches(checkSpaces)) {
            username.setError("No White spaces are allowed!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +        // at least 1 digit
                "(?=.*[a-z])" +        // at least 1 lower case letter
                "(?=.*[A-Z])" +        // at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      // any letter
                "(?=.*[@#$%^&+=])" +    // at least 1 special character
                "(?=\\S+$)" +           // no white spaces
                ".{8,}" +               // at least 8 characters
                "$";
        if (_password.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!_password.matches(passwordVal)) {
            password.setError("Invalid Password");
            password.setHelperText("Password must contain at least 1 digit, 1 upper case letter, 1 lower case letter, 1 special character, no white spaces, and at least 8 characters");
            return false;
        } else {
            password.setError(null);
            password.setHelperText("");
            return true;
        }
    }
}
