package com.cfish.mvpdemo2.login;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public interface LoginPresenter {

    void validateCredentials(String username, String password);

    void onDestroy();
}
