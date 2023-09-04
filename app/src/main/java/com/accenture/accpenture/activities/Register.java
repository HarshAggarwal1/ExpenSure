package com.accenture.accpenture.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.accenture.accpenture.R;
import com.accenture.accpenture.database.UserHelperClassFirebase;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Objects;

public class Register extends AppCompatActivity {

    // Variables
    private Button btnLogin, btnRegister;
    private TextInputLayout username, password, email, confirmPassword, fName, lName, phone;
    private Dialog dialog;
    private ImageView profileDP;
    private static boolean isImageSelected = false;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private UserHelperClassFirebase helperClass;
    private static Uri imageUri, dpUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        profileDP = findViewById(R.id.pickProfileDP);

        // Pick Profile Picture
        profileDP.setOnClickListener(v -> {
            // only images
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            pickMedia.launch(intent);
        });

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

        showProgressBar();

        // Check if all fields are valid
        boolean[] valid = {validateUsername(), validatePassword(), validateEmail(), validateConfirmPassword(), validateFName(), validateLName(), validateMobile()};
        for (boolean b : valid) {
            if (!b) {
                hideProgressBar();
                return;
            }
        }

        // Get all the values
        String _username = Objects.requireNonNull(username.getEditText()).getText().toString();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString();
        String _email = Objects.requireNonNull(email.getEditText()).getText().toString();
        String _fName = Objects.requireNonNull(fName.getEditText()).getText().toString();
        String _lName = Objects.requireNonNull(lName.getEditText()).getText().toString();
        String _phone = Objects.requireNonNull(phone.getEditText()).getText().toString();

        // Register Logic Below ...............
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        // Capitalize first letter of first name and last name and remove trailing spaces
        _fName = _fName.substring(0, 1).toUpperCase().trim() + _fName.substring(1).toLowerCase().trim();
        _lName = _lName.substring(0, 1).toUpperCase().trim() + _lName.substring(1).toLowerCase().trim();

        // Create UserHelperClassFirebase object
        helperClass = new UserHelperClassFirebase(_username, _password, _email, _fName, _lName, _phone);

        reference.child(_username).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                uploadImage(_username);
            }
            else {
                username.setError("Username already exists");
            }
        });
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
                        Toast.makeText(Register.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
                        hideProgressBar();
                        Toast.makeText(Register.this, "Failed to Upload the Image.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
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

    private void showProgressBar() {
        dialog = new Dialog(Register.this);
        dialog.requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_bar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void hideProgressBar() {
        dialog.dismiss();
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
                        Toast.makeText(Register.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//        pickMedia.launch(new PickVisualMediaRequest.Builder()
//                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
//                .build());
//
//        String mimeType = "image/*";
//        pickMedia.launch(new PickVisualMediaRequest.Builder()
//                .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType))
//                .build());

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
