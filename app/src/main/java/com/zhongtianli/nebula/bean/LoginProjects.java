package com.zhongtianli.nebula.bean;

import java.io.Serializable;

/**
 * Created by zhu on 2016/10/26.
 */

public class LoginProjects  implements Serializable{
    //    {
//        "Result": "100",
//            "Projects":
//        [{
//        "ProjectCode":"001",
//                "ProjectName":"新康花园",
//                "DeviceAdd":"1",
//                "DeviceEdit":"1",
//                "DeviceTest":"1",
//                "DeviceDebug":"1"
//    },{
//        "ProjectCode":"002",
//                "ProjectName":"同泾花园",
//                "DeviceAdd":"1",
//                "DeviceEdit":"1",
//                "DeviceTest":"1",
//                "DeviceDebug":"1"
//    }]
//    }
    private  String ProjectCode;
    private  String ProjectName;
    private  String DeviceAdd;
    private  String DeviceEdit;

    public void setProjectCode(String projectCode) {
        ProjectCode = projectCode;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public void setDeviceAdd(String deviceAdd) {
        DeviceAdd = deviceAdd;
    }

    public void setDeviceEdit(String deviceEdit) {
        DeviceEdit = deviceEdit;
    }

    public void setDeviceTest(String deviceTest) {
        DeviceTest = deviceTest;
    }

    public void setDeviceDebug(String deviceDebug) {
        DeviceDebug = deviceDebug;
    }

    private  String DeviceTest;
    private  String DeviceDebug;

    public String getProjectCode() {
        return ProjectCode;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public String getDeviceAdd() {
        return DeviceAdd;
    }

    public String getDeviceEdit() {
        return DeviceEdit;
    }

    public String getDeviceTest() {
        return DeviceTest;
    }

    public String getDeviceDebug() {
        return DeviceDebug;
    }
}
