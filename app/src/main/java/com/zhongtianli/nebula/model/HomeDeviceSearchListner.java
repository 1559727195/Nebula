package com.zhongtianli.nebula.model;

import java.util.List;

/**
 * Created by robin on 2016/5/25.
 */
public interface HomeDeviceSearchListner {
    void getDeviceSearchSuccess(List<Flight> flightList);
    void getDeviceSearchFailed();
}
