package com.cfish.mvpdemo1.view;

/**
 * Created by GKX100217 on 2016/4/14.
 */
public interface ILoginView {
    public void onClearText();
    public void onLoginResult(Boolean result,int code);
    public void onSetProgressBarVisibility(int visibility);
}
