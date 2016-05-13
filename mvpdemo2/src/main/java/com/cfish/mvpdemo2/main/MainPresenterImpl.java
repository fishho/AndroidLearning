package com.cfish.mvpdemo2.main;

import android.util.Log;

import java.util.List;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public class MainPresenterImpl implements MainPresenter,FindItemsInteractor.OnFinishedListenter {

    private MainView mainView;

    private FindItemsInteractor findItemsInteractor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        findItemsInteractor = new FindItemsInteractorImpl();
    }
    @Override
    public void onResume() {
        if (mainView != null) {
            mainView.showProgress();
        }
        findItemsInteractor.findItems(this);
    }

    @Override
    public void onItemClicked(int position) {
        if (mainView != null) {
            mainView.showMessage(String.format("Position %d clicked",position+1));
        }
        Log.d("Dfish","clicked");
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onFinished(List<String> items) {
        if (mainView != null) {
            mainView.setItems(items);
            mainView.hideProgress();
        }
    }
}
