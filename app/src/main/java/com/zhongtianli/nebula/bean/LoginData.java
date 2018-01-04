package com.zhongtianli.nebula.bean;

/**
 * Created by zhu on 2016/10/26.
 */

public class LoginData {
    public String UserName;
    public String Password;
    public LoginData(String UserName,String Password){
        this.UserName=UserName;
        this.Password=Password;
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


}
