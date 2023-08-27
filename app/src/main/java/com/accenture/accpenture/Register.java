package com.accenture.accpenture;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.accenture.accpenture.database.UserHelperClassFirebase;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Register extends AppCompatActivity {

    // Variables
    private Button btnLogin, btnRegister;
    private TextInputLayout username, password, email, confirmPassword, fName, lName, phone;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    UserHelperClassFirebase helperClass;

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
        btnRegister = findViewById(R.id.registerButton);
        username = findViewById(R.id.registerUsername);
        password = findViewById(R.id.registerPassword);
        email = findViewById(R.id.registerEmail);
        confirmPassword = findViewById(R.id.checkRegisterPassword);
        fName = findViewById(R.id.registerFirstName);
        lName = findViewById(R.id.registerLastName);
        phone = findViewById(R.id.registerContact);


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

    public void registerUser(View v) {
        // Get all the values
        String _username = Objects.requireNonNull(username.getEditText()).getText().toString();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString();
        String _email = Objects.requireNonNull(email.getEditText()).getText().toString();
        String _confirmPassword = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString();
        String _fName = Objects.requireNonNull(fName.getEditText()).getText().toString();
        String _lName = Objects.requireNonNull(lName.getEditText()).getText().toString();
        String _phone = Objects.requireNonNull(phone.getEditText()).getText().toString();

        // Check if all fields are valid
        boolean[] valid = {validateUsername(), validatePassword(), validateEmail(), validateConfirmPassword(), validateFName(), validateLName(), validateMobile()};
        for (boolean b : valid) {
            if (!b) {
                return;
            }
        }

        // Register Logic Below ...............
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        // Capitalize first letter of first name and last name and remove trailing spaces
        _fName = _fName.substring(0, 1).toUpperCase() + _fName.substring(1).toLowerCase().trim();
        _lName = _lName.substring(0, 1).toUpperCase() + _lName.substring(1).toLowerCase().trim();

        // Create UserHelperClassFirebase object
        helperClass = new UserHelperClassFirebase(_username, _password, _email, _fName, _lName, _phone);

        reference.child(_username).setValue(helperClass);
    }

    private boolean validateUsername() {
        String _username = Objects.requireNonNull(username.getEditText()).getText().toString();
        String noWhiteSpace = "(\\A\\w{8,15}\\z)";
        if (_username.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        }
        else if (_username.length() > 15) {
            username.setError("Username too long");
            return false;
        } else if (!_username.matches(noWhiteSpace)) {
            username.setError("Invalid Username");
            username.setHelperText("Username must be 8-15 characters long");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateFName() {
        String _fName = Objects.requireNonNull(fName.getEditText()).getText().toString();
        if (_fName.isEmpty()) {
            fName.setError("Field cannot be empty");
            return false;
        } else if (!_fName.matches("[ a-zA-Z ]+")) {
            fName.setError("Invalid");
            return false;
        } else {
            fName.setError(null);
            return true;
        }
    }
    private boolean validateLName() {
        String _lName = Objects.requireNonNull(lName.getEditText()).getText().toString();
        if (_lName.isEmpty()) {
            lName.setError("Field cannot be empty");
            return false;
        } else if (!_lName.matches("[ a-zA-Z ]+")) {
            lName.setError("Invalid");
            return false;
        } else {
            lName.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String _email = Objects.requireNonNull(email.getEditText()).getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (_email.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        }
        else if (!_email.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        }
        else {
            email.setError(null);
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
    private boolean validateConfirmPassword() {
        String _confirmPass = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString();

        if (_password.isEmpty()) {
            confirmPassword.setError("Please fill previous field first");
            return false;
        }
        else if (_confirmPass.isEmpty()) {
            confirmPassword.setError("Field cannot be empty");
            return false;
        }
        else if (!_confirmPass.equals(_password)) {
            confirmPassword.setError("Password does not match");
            return false;
        }
        else {
            confirmPassword.setError(null);
            return true;
        }
    }
    private boolean validateMobile() {
        String _phone = Objects.requireNonNull(phone.getEditText()).getText().toString();
        if (_phone.isEmpty()) {
            phone.setError("Field cannot be empty");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
