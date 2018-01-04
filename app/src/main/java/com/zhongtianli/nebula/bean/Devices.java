package com.zhongtianli.nebula.bean;

import java.io.Serializable;

/**
 * Created by zhu on 2016/11/7.
 */

public class Devices implements Serializable{
    private String DeviceMac;

    public String getDeviceMac() {
        return DeviceMac;
    }

    public void setDeviceMac(String DeviceMac) {
        this.DeviceMac = DeviceMac;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public String getDeviceStatus() {
        return DeviceStatus;
    }

    public void setDeviceStatus(String DeviceStatus) {
        this.DeviceStatus = DeviceStatus;
    }

    private  String  DeviceName;
    private  String DeviceType;
    private  String DeviceStatus;
}
