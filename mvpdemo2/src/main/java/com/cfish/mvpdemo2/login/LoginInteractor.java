package com.cfish.mvpdemo2.login;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public interface LoginInteractor {

    interface  OnLoginFinishedListener {

        void onUsernameError();

        void onPasswordError();

        void onSuccess();

    }

    void login(String username,String password,OnLoginFinishedListener listener) ;
}
