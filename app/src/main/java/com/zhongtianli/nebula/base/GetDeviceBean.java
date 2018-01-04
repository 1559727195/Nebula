package com.zhongtianli.nebula.base;

import java.io.Serializable;

/**
 * Created by zhu on 2016/10/27.
 */

public class GetDeviceBean  implements Serializable{
    //"DeviceMac": "41:4b:50:00:00:66",
//                                    "DeviceName": "massky gate",
//                                    "DeviceType": "1",
//                                    "DeviceStatus": "0"
    private String DeviceMac;
    private  String DeviceName;
    private  String DeviceType;

    public void setDeviceMac(String deviceMac) {
        DeviceMac = deviceMac;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public void setDeviceStatus(String deviceStatus) {
        DeviceStatus = deviceStatus;
    }

    public String getDeviceStatus() {
        return DeviceStatus;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public String getDeviceMac() {
        return DeviceMac;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    private  String DeviceStatus;
}
