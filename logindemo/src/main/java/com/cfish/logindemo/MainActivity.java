package com.cfish.logindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private static final int  CODE_SIGNUP = 0;
    private EditText email;
    private EditText password;
    private Button loginButton;
    private TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signup = (TextView) findViewById(R.id.link_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, CODE_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG,"perform login");
        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, android.R.style.Theme_Material_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String emails = email.getText().toString();
        String passwords =  password.getText().toString();

        //my own authentication logic
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginFailed() {
        Snackbar.make(loginButton,"Login Failed", Snackbar.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public boolean validate() {
        boolean valid = true;
        String emails = email.getText().toString();
        String passwords = password.getText().toString();

        if (emails.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emails).matches()) {
            email.setError("please enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwords.isEmpty() || passwords.length() < 4 || passwords.length() >10) {
            password.setError("password is between 4 and 10 alphanumeric characers");;
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}
