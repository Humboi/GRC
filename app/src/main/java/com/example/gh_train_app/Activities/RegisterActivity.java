package com.example.gh_train_app.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.R;
import com.example.gh_train_app.models.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.gh_train_app.OthersFiles.Tools.validateEmail;
import static com.example.gh_train_app.OthersFiles.Tools.validateMobile;
import static com.example.gh_train_app.OthersFiles.Tools.validatePassword;

public class RegisterActivity extends AppCompatActivity {
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    EditText fullName, userName, editMobile, editEmail, editAddress, editPass, editConfirmPass;
    Button btnRegister, btnupload;
    ImageView ivimage;
    Uri pickedImgUri;
    ProgressDialog progressDialog;
    String full_name, user_Name, phone_Number, Email, Address, User_Pass, ConfirmPass;
    int flag = 0;
    DatabaseReference mdb;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Tools.setSystemBarColor(this);

        fullName = (EditText) findViewById(R.id.fullname);
        userName = (EditText) findViewById(R.id.username);
        editMobile = (EditText) findViewById(R.id.phonenumber);
        editEmail = (EditText) findViewById(R.id.email);
        editAddress = (EditText) findViewById(R.id.address);
        editPass = (EditText) findViewById(R.id.password);
        editConfirmPass = (EditText) findViewById(R.id.confirmpassword);
        btnupload = (Button) findViewById(R.id.btnupload);
        btnRegister = (Button) findViewById(R.id.btn_register);
        ivimage = (ImageView) findViewById(R.id.regUserPhoto);
        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference().child("Users");

        btnupload.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 22) {
                checkAndRequestForPermission();
            } else {
                openGallery();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_name = fullName.getText().toString().trim();
                user_Name = userName.getText().toString().trim();
                phone_Number = editMobile.getText().toString().trim();
                Email = editEmail.getText().toString().trim();
                Address = editAddress.getText().toString().trim();
                User_Pass = editPass.getText().toString().trim();
                ConfirmPass = editConfirmPass.getText().toString().trim();
                flag = 0;

                if (fullName.getText().toString().trim().length() == 0) {
                    fullName.setError("First Name cannot be Empty");
                    fullName.requestFocus();
                    flag = 1;
                }
                if (userName.getText().toString().trim().length() == 0) {
                    userName.setError("Last Name cannot be Empty");
                    userName.requestFocus();
                    flag = 1;
                }
                if (editAddress.getText().toString().trim().length() == 0) {
                    editAddress.setError("Address cannot be Empty");
                    editAddress.requestFocus();
                    flag = 1;
                }
                if (!validateMobile(editMobile.getText().toString().trim())) {
                    editMobile.setError("Invalid Mobile number");
                    editMobile.requestFocus();
                    flag = 1;
                }
                if (editMobile.getText().toString().trim().length() == 0) {
                    editMobile.setError("Mobile Number cannot be empty");
                    editMobile.requestFocus();
                    flag = 1;
                }

                if (!validateEmail(editEmail.getText().toString().trim())) {
                    editEmail.setError("Invalid Email");
                    editEmail.requestFocus();
                    flag = 1;
                }
                if (editEmail.getText().toString().trim().length() == 0) {
                    editEmail.setError("Email cannot be empty");
                    editEmail.requestFocus();
                    flag = 1;
                }
                if (!validatePassword(editPass.getText().toString().trim())) {
                    editPass.setError("Password must be atleast 6 to 8 characters or digits");
                    editPass.requestFocus();
                    flag = 1;
                }
                if (editPass.getText().toString().trim().length() == 0) {
                    editPass.setError("Password cannot be empty");
                    editPass.requestFocus();
                    flag = 1;
                }
                if (!editPass.getText().toString().equals(editConfirmPass.getText().toString().trim())) {
                    editConfirmPass.setError("Password Does Not Match");
                    editConfirmPass.requestFocus();
                    flag = 1;
                }
                if (editConfirmPass.getText().toString().trim().length() == 0) {
                    editConfirmPass.setError("Confirm Password cannot be empty");
                    editConfirmPass.requestFocus();
                    flag = 1;
                }
                if (flag == 0) {
                    fullName.setError(null);
                    userName.setError(null);
                    editMobile.setError(null);
                    editEmail.setError(null);
                    editAddress.setError(null);
                    editPass.setError(null);
                    editConfirmPass.setError(null);
                    progressDialog = new ProgressDialog(RegisterActivity.this,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Registering...");
                    progressDialog.show();
                    CreateUserAccount(full_name, phone_Number, user_Name, Email, Address, ConfirmPass);
//                    clearInput();
                }
            }
        });

    }

    public void clearInput() {
        fullName.setText("");
        userName.setText("");
        editMobile.setText("");
        editEmail.setText("");
        editPass.setText("");
        editConfirmPass.setText("");
    }

    private void CreateUserAccount(final String fullname, final String phonenumber, final String name, final String email, final String address, final String password) {
        // this method create user account with specific email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userid = mAuth.getCurrentUser().getUid();
                        // user account created successfully
                        Register workout = new Register(userid, fullname, phonenumber, name, email, address, password);
                        mdb.child(userid).setValue(workout);
                        showMessage("Account created");
                        progressDialog.dismiss();
                        // after we created user account we need to update his profile picture and name
                        updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());
                    } else {
                        // account creation failed
                        progressDialog.dismiss();
                        showMessage("Account creation failed" + task.getException().getMessage());
                    }
                });
    }


    // update user photo and name
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // first we need to upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            // image uploaded succesfully
            // now we can get our image url
            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                // uri contain user image url
                UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();
                currentUser.updateProfile(profleUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // user info updated successfully
                                showMessage("Registration Successful");
                                updateUI();
                            }
                        });
            });
        });
    }

    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(homeActivity);
        finish();
    }

    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }


    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            ivimage.setImageURI(pickedImgUri);
        }
    }


}
