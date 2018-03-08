package com.mad.losesano;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

/**
 * Created by oshau on 01/03/2018.
 */

public class SignupActivity extends Activity implements View.OnClickListener {

    EditText et_email, et_password;
    Button register_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_email = (EditText) findViewById(R.id.email_et_login);
        et_password = (EditText) findViewById(R.id.password_et_login);

        mAuth = FirebaseAuth.getInstance();

        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(this);
        findViewById(R.id.login_tv).setOnClickListener(this);

    }

    private void registerUser() {
        Log.d("LoSeSANO", "Email Address: " + et_email.getText().toString());
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (email.isEmpty()) {
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Please enter a valid email");
            et_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }

        if (password.length() < 6) {
            et_password.setError("Minimum lenght of password should be 6");
            et_password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:
                registerUser();
                break;

            case R.id.login_tv:
                finish();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                break;
        }
    }
}
