package com.zhongtianli.nebula.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhu on 2016/11/7.
 */

public class GetDevice implements Serializable{

//    "Result": "100",
//            "ProjectCode": "2016316081670119716643161672551",
//            "Devices": [
//    {
//        "DeviceMac": "41:4b:50:00:00:15",
//            "DeviceName": "硬件研发部",
//            "DeviceType": "2",
//            "DeviceStatus": "1"
//    },
     private  String Result;
    private  String ProjectCode;

    public ArrayList<GetDevice.Devices> getDevices() {
        return Devices;
    }

    public void setDevices(ArrayList<GetDevice.Devices> Devices) {
        this.Devices = Devices;
    }

    private ArrayList<Devices> Devices;

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getProjectCode() {
        return ProjectCode;
    }

    public void setProjectCode(String ProjectCode) {
        this.ProjectCode = ProjectCode;
    }

   public class Devices implements Serializable {
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
}
