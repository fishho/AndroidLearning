package com.cfish.mvpdemo1.presenter;

/**
 * Created by GKX100217 on 2016/4/14.
 */
public interface ILoginPresenter {
    void clear();
    void doLogin(String name,String passwd);
    void setProgressBarVisiblity(int visiblity);
}
