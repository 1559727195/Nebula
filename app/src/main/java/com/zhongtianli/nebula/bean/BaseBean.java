package com.zhongtianli.nebula.bean;

import java.io.Serializable;

/**
 * Created by admin on 2016/4/18.
 */
public class BaseBean implements Serializable{
    private String Content;
    private int Secure;
    private String ReturnInfo;
    private String ReturnNo;

    public int getSecure() {
        return Secure;
    }

    public void setSecure(int secure) {
        Secure = secure;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }


    public String getReturnInfo() {
        return ReturnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        ReturnInfo = returnInfo;
    }

    public String getReturnNo() {
        return ReturnNo;
    }

    public void setReturnNo(String returnNo) {
        ReturnNo = returnNo;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "Content='" + Content + '\'' +
                ", Secure=" + Secure +
                ", ReturnInfo='" + ReturnInfo + '\'' +
                ", ReturnNo='" + ReturnNo + '\'' +
                '}';
    }
}
