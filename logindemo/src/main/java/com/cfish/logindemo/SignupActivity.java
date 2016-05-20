package com.cfish.logindemo;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "Sign up";
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button signButton;
    private TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEdit = (EditText) findViewById(R.id.input_name);
        emailEdit = (EditText) findViewById(R.id.input_email);
        passwordEdit = (EditText) findViewById(R.id.input_password);
        signButton = (Button) findViewById(R.id.btn_signup);
        login = (TextView) findViewById(R.id.link_login);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//return to LoginActivity
            }
        });
    }

    public void signup() {
        Log.d(TAG,"Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,android.R.style.Theme_Material_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    onSignupSuccess();
                    //onSignupFailed();
                    // progressDialog.dismiss();

            }
        }, 3000);
    }

    public void onSignupFailed() {
        Snackbar.make(login,"Signup failed",Snackbar.LENGTH_SHORT).show();
        signButton.setEnabled(true);
    }

    public void onSignupSuccess() {
        signButton.setEnabled(true);
        setResult(RESULT_OK,null);
        finish();
    }

    public Boolean validate() {
        Boolean valid = true;

        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (name.isEmpty() || name.length()< 3) {
            nameEdit.setError("at least 3 characters");
            valid = false;
        } else {
            nameEdit.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.setError("enter a valid email address");
            valid = false;
        } else {
            emailEdit.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() >10) {
            passwordEdit.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEdit.setError(null);
        }
        return valid;


    }
}
