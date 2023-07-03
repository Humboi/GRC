package com.example.gh_train_app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gh_train_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText reset_email;
    Button btn_send;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        reset_email=(EditText)findViewById(R.id.email);
        btn_send=(Button)findViewById(R.id.btn_send);

        auth = FirebaseAuth.getInstance();

        btn_send.setOnClickListener(v -> {
            String email = reset_email.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                return;
            }

            final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Sending Reset Link...");
            progressDialog.show();

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Hiding the progress dialog.
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hiding the progress dialog.
                                progressDialog.dismiss();
                                Toast.makeText(ForgetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        });

    }
}
