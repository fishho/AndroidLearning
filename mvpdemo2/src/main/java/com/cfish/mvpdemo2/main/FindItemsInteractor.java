package com.cfish.mvpdemo2.main;

import java.util.List;

/**
 * Created by GKX100217 on 2016/4/15.
 */
public interface FindItemsInteractor {

    interface OnFinishedListenter {
        void onFinished(List<String> items);
    }

    void findItems(OnFinishedListenter listenter);
}
