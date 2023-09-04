package com.accenture.accpenture.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.accenture.accpenture.R;
import com.accenture.accpenture.database.AppData;
import com.accenture.accpenture.database.Database;
import com.accenture.accpenture.database.UserHelperClassFirebase;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Objects;

public class GoogleSignInActivity extends AppCompatActivity {

    private Dialog dialog;
    private Button cancel, register;
    private TextInputLayout username, password, confirmPassword, phone;
    private ImageView profileDP;
    private String email, fName, lName;
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private UserHelperClassFirebase helperClass;
    private static Uri imageUri, dpUri;
    private static boolean isImageSelected = false;
    private Database database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        database = Database.getInstance(getApplicationContext());
        database.appDao().deleteAll();

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
        profileDP.setOnClickListener(v -> {
            // only images
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            pickMedia.launch(intent);
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

        // Register Logic Below ...............
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        // Capitalize first letter of first name and last name and remove trailing spaces
        fName = fName.substring(0, 1).toUpperCase().trim() + fName.substring(1).toLowerCase().trim();
        lName = lName.substring(0, 1).toUpperCase().trim() + lName.substring(1).toLowerCase().trim();

        // Create UserHelperClassFirebase object
        helperClass = new UserHelperClassFirebase(_username, _password, email, fName, lName, _phone);

        reference.child(_username).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                uploadImage(_username);
            }
            else {
                username.setError("Username already exists");
            }
        });

        // App Database
        database.appDao().insert(new AppData(email, _password, fName, lName, _phone, _username));

        // Intent work here
    }

    private void uploadImage(String _username) {
        // Upload image to Firebase Storage
        imageUri = Uri.parse("android.resource://com.accenture.accpenture/drawable/profile_dp");
        if (isImageSelected) {
            imageUri = dpUri;
        }
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures/"+_username);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                // Update the profile picture URL in the database
                reference.child(_username).child("profilePicture").setValue(uri.toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hideProgressBar();
                        Toast.makeText(GoogleSignInActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
                        hideProgressBar();
                        Toast.makeText(GoogleSignInActivity.this, "Failed to Upload the Image.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
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

    private String getFilePathFromURI(Uri uri) {
        String path = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    ActivityResultLauncher<Intent> pickMedia = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        if (data != null) {
                            isImageSelected = true;
                            dpUri = data.getData();
                            final String path = getFilePathFromURI(dpUri);
                            if (path != null) {
                                File f = new File(path);
                                dpUri = Uri.fromFile(f);
                            }
                            profileDP.setPadding(0, 0, 0, 0);
                            profileDP.setImageURI(dpUri);
                        }
                    }
                    else {
                        Toast.makeText(GoogleSignInActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
