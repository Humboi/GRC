package com.example.gh_train_app.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gh_train_app.OthersFiles.Tools;
import com.example.gh_train_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    EditText _email, _password;
    Button sign_in;
    TextView sign_up, reset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Tools.setSystemBarColor(this);

        _email = (EditText) findViewById(R.id.email);
        _password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.btn_sign);
        sign_up = (TextView) findViewById(R.id.btn_sign_up);
        reset = (TextView) findViewById(R.id.reset);

        //Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        // Checking if user already logged in before and not logged out properly.
        if (firebaseAuth.getCurrentUser() != null) {
            // Opening DashboardActivity .
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            // Closing the current Login Activity.
            finish();
        }

//        to move to signup activity
        sign_up.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        // to move to dashboard activity
        sign_in.setOnClickListener(v -> login());

        //        to move to reset password activity
        reset.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class)));

    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        sign_in.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _email.getText().toString();
        String password = _password.getText().toString();


        // Calling  signInWithEmailAndPassword function with firebase object and passing EmailHolder and PasswordHolder inside it.
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    // If task done Successful.
                    if (task.isSuccessful()) {

                        // Hiding the progress dialog.
                        progressDialog.dismiss();

                        // Opening the DashboardActivity.
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        // Closing the current Login Activity.
                        finish();
                    } else {

                        // Hiding the progress dialog.
                        progressDialog.dismiss();
                        // Showing toast message when email or password not found in Firebase Online database.
//                            Toast.makeText(LoginActivity.this, "Email or Password Not found, Please Try Again", Toast.LENGTH_LONG).show();
                        sign_in.setEnabled(true);
                    }
                }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(LoginActivity.this, "Incorrect Password.", Toast.LENGTH_LONG).show();
            }
            if (e instanceof FirebaseAuthInvalidUserException) {
                String errorCode =
                        ((FirebaseAuthInvalidUserException) e).getErrorCode();

                if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                    Toast.makeText(LoginActivity.this, "User with this email doesn't exist.", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                    Toast.makeText(LoginActivity.this, "User with this email has been disabled.", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                    Toast.makeText(LoginActivity.this, "Your email address appears to be malformed.", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_WRONG_PASSWORD")) {
                    Toast.makeText(LoginActivity.this, "Your password is wrong.", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_TOO_MANY_REQUESTS")) {
                    Toast.makeText(LoginActivity.this, "Too many requests. Try again later.", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_OPERATION_NOT_ALLOWED")) {
                    Toast.makeText(LoginActivity.this, "Signing in with Email and Password is not enabled.", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL")) {
                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("Credential instance is already in use by another account")) {
                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("ERROR_CREDENTIAL_ALREADY_IN_USE")) {
                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                } else if (errorCode.equals("Account is already linked to another account")) {
                    Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Internet connection is weak", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        sign_in.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _email.getText().toString();
        String password = _password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("Enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            _password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        return valid;
    }


}