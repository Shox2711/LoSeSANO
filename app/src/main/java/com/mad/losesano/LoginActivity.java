package com.mad.losesano;

        import android.app.Activity;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.os.Bundle;
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

public class LoginActivity extends Activity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText et_email, et_password;
    Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        et_email = (EditText) findViewById(R.id.email_et_login);
        et_password = (EditText) findViewById(R.id.password_et_login);

        findViewById(R.id.register_tv).setOnClickListener(this);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

    }

    private void userLogin() {
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, LoggedInActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_tv:
                finish();
                Log.d("LoSeSANO", "Register Button Clicked");
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;

            case R.id.login_button:
                Log.d("LoSeSANO", "Login Button Clicked");
                userLogin();
                break;
        }
    }
}
