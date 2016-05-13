package com.cfish.mvpdemo2.main;

import java.util.List;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public interface MainView {

    void showProgress();
    void hideProgress();
    void setItems(List<String> items);
    void showMessage(String message);
}
