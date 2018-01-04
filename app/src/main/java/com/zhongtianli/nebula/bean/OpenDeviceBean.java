package com.zhongtianli.nebula.bean;

/**
 * Created by zhu on 2016/11/9.
 */

public class OpenDeviceBean {
   private String   macAddress;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamFloor() {
        return paramFloor;
    }

    public void setParamFloor(String paramFloor) {
        this.paramFloor = paramFloor;
    }

    public String getParamInout() {
        return paramInout;
    }

    public void setParamInout(String paramInout) {
        this.paramInout = paramInout;
    }

    private String paramId;
    private String paramFloor;
    private  String paramInout;
}
