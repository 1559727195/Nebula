package com.zhongtianli.nebula.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhu on 2016/11/7.
 */

public class SetDeviceBean  implements Serializable {
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setProjectCode(String ProjectCode) {
        this.ProjectCode = ProjectCode;
    }


    public String getProjectCode() {
        return ProjectCode;
    }

    private String UserName;
    private String ProjectCode;

    public ArrayList<Devices> getDevices() {
        return Devices;
    }

    public void setDevices(ArrayList<Devices> Devices) {
        this.Devices = Devices;
    }
    private ArrayList<Devices> Devices = new ArrayList<>();
}
