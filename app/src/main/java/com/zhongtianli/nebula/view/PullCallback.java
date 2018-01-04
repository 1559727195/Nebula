package com.zhongtianli.nebula.view;

/**
 * @author bian.xd
 */
public interface PullCallback {

    void onLoadMore();

    void onRefresh();

    boolean isLoading();

    boolean hasLoadedAllItems();

}
