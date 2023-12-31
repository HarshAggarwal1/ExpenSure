package com.accenture.accpenture.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.accenture.accpenture.R;
import com.accenture.accpenture.database.AppData;
import com.accenture.accpenture.database.Database;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    // Variables
    private static final int RC_SIGN_IN = 9001;
    private Button btnRegister, btnLogin;
    private TextInputLayout username, password;
    private Database database;
    private Dialog dialog;
    private ImageView googleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        database = Database.getInstance(getApplicationContext());
        database.appDao().deleteAll();
        database.expenseDao().deleteAll();

        config();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        username = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        btnRegister = findViewById(R.id.loginPageNewUser);
        btnLogin = findViewById(R.id.loginButton);
        googleSignIn = findViewById(R.id.loginGoogle);

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
        showProgressBar();
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

                        database.appDao().insert(new AppData(email, passwordFromDB, fName, lName, phone, username));
                        hideProgressBar();

                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                        intent.putExtra("name", name);
//                        intent.putExtra("username", username);
//                        intent.putExtra("email", email);
//                        intent.putExtra("phone", phone);
//                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        hideProgressBar();
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                    hideProgressBar();
                }
                else {
                    hideProgressBar();
                    username.setError("No such User exists");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressBar();
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

    private void showProgressBar() {
        dialog = new Dialog(Login.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_bar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void hideProgressBar() {
        dialog.dismiss();
    }

    public void loginRegisterWithGoogle(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        googleSignInClient.signOut();
        googleSignInLauncher.launch(signInIntent);

    }

    ActivityResultLauncher<Intent> googleSignInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            String email = account.getEmail();
                            String fName = account.getGivenName();
                            String lName = account.getFamilyName();
                            // get photo url
                            Uri dp_uri = account.getPhotoUrl();
                            System.out.println("Email: " + email);
                            System.out.println("Name: " + fName + " " + lName);
                            System.out.println("Uri: " + dp_uri);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startMaterialContainerTransform(email, fName, lName);
                                }
                            }, 200);
                        }
                        catch (ApiException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Unable to login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void startMaterialContainerTransform(String _e, String _fN, String _lN) {
        Intent intent = new Intent(Login.this, GoogleSignInActivity.class);
        intent.putExtra("email", _e);
        intent.putExtra("fName", _fN);
        intent.putExtra("lName", _lN);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, googleSignIn, "googleSignIn").toBundle();
        startActivity(intent, bundle);
    }

    private void config() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementsUseOverlay(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
