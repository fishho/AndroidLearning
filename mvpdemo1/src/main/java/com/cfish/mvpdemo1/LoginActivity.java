package com.cfish.mvpdemo1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cfish.mvpdemo1.R;
import com.cfish.mvpdemo1.presenter.ILoginPresenter;
import com.cfish.mvpdemo1.presenter.LoginPresenterCompl;
import com.cfish.mvpdemo1.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {
    private EditText editUser; //用户名
    private EditText editPass; //密码
    private Button btnLogin;  //登录
    private Button btnClear;  //清空
    ILoginPresenter loginPresenter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init view
        editUser = (EditText) this.findViewById(R.id.edit_user);
        editPass = (EditText) this.findViewById(R.id.edit_pass);
        btnLogin = (Button) this.findViewById(R.id.btn_login);
        btnClear = (Button) this.findViewById(R.id.btn_clear);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        //set listener
        btnLogin.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        //init
        loginPresenter = new LoginPresenterCompl(this);
        loginPresenter.setProgressBarVisiblity(View.VISIBLE);

    }

    @Override
    public void onClearText() {
        editUser.setText("");
        editPass.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        loginPresenter.setProgressBarVisiblity(View.INVISIBLE);
        btnLogin.setEnabled(true);
        btnClear.setEnabled(true);
        if (result) {
            Toast.makeText(this,"Login Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Login Fail,code = " + code, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                loginPresenter.clear();
                break;
            case R.id.btn_login:
                loginPresenter.setProgressBarVisiblity(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnClear.setEnabled(false);
                loginPresenter.doLogin(editUser.getText().toString(),editPass.getText().toString());
                break;

        }
    }
}
