package com.cfish.mvpdemo2.login;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public interface LoginView {

    void showProgress();

    void hideProgress();

    void setPasswordError();

    void setUsernameError();

    void navigateToHome();
}
