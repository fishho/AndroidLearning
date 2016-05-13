package com.cfish.mvpdemo2.main;

import android.os.Handler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public class FindItemsInteractorImpl implements FindItemsInteractor {

    @Override
    public void findItems(final OnFinishedListenter listenter) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listenter.onFinished(createArrayList());
            }
        },2000);
    }

    private List<String> createArrayList() {
        return Arrays.asList(
                "Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7",
                "Item 8",
                "Item 9",
                "Item 10"
        );
    }
}
