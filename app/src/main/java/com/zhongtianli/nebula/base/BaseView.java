package com.zhongtianli.nebula.base;

/**
 * Created by kaizen on 15/10/21.
 */
public interface BaseView {

    void showLoading();
    void hideLoading();
    void showError(String msg);
    void showToast(String msg);
    void setData(String json);
}
