package com.accenture.accpenture;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.util.Objects;

public class GoogleSignInActivity extends AppCompatActivity {

    private Dialog dialog;
    private Button cancel, register;
    private TextInputLayout username, password, confirmPassword, phone;
    private ImageView profileDP;
    private String email, fName, lName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_google_sign_in);

        cancel = findViewById(R.id.furtherGoogleCancelButton);
        register = findViewById(R.id.furtherGoogleRegisterButton);
        username = findViewById(R.id.furtherGoogleUsername);
        password = findViewById(R.id.furtherGooglePassword);
        confirmPassword = findViewById(R.id.furtherGooglePasswordConfirm);
        phone = findViewById(R.id.furtherGooglePhone);
        profileDP = findViewById(R.id.furtherGooglePickProfileDP);

        email = getIntent().getStringExtra("email");
        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        showProgressBar();
        boolean[] valid = {validateUsername(), validatePassword(), validateConfirmPassword(), validateMobile()};
        for (boolean b : valid) {
            if (!b) {
                hideProgressBar();
                return;
            }
        }

        // pre-registration
//        updateViewsBasedOnDataReceived();

        // Get all the values
        String _username = Objects.requireNonNull(username.getEditText()).getText().toString();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString();
        String _phone = Objects.requireNonNull(phone.getEditText()).getText().toString();


    }

    private void updateViewsBasedOnDataReceived() {
        return;
    }

    private void showProgressBar() {
        dialog = new Dialog(GoogleSignInActivity.this);
        dialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_bar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void hideProgressBar() {
        dialog.dismiss();
    }

    private void config() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        findViewById(android.R.id.content).setTransitionName("googleSignIn");
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        MaterialContainerTransform transform= new MaterialContainerTransform();
        transform.addTarget(android.R.id.content);
        transform.setDuration(300);
        getWindow().setSharedElementEnterTransition(transform);
        getWindow().setSharedElementExitTransition(transform);
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
