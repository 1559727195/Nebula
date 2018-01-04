package com.zhongtianli.nebula.utils;

/**
 * Created by win7 on 2015/12/3.
 */
public class UrlData
{
    public static final String BASEURL="http://nebula.inebula.cn:22711/";//baseURL
    public static final String GETCODE="api/AppApi/GetVerificationCode";//获取短信验证码
    public static final String GETCODEWITHCHECK="api/AppApi/GetVerificationCodeWithExistsVerify";//获取短信验证码并验证是否已经注册
    public static final String GETCODEFORPSD="api/AppApi/GetVerificationCodeForPassword";//获取短信验证码并验证是否已经注册，未注册则报错
    public static final String REGIST="api/AppApi/Register";//注册
    public static final String LOGIN="api/Nebula/Nebula_SetLogin";//登录
    public static final String RESETPSD="api/AppApi/UpdateMemberPassword";//忘记密码设置新密码

    //我的
    public static final String UPLOADPHOTO="api/AppApi/UploadMemberAvatar";//上传图像
    public static final String SIGN="api/AppApi/Checkin";//签到
    public static final String SIGNLIST="api/AppApi/GetCheckinList";//签到记录
    public static final String PHONEBOOK="api/AppApi/GetPhonebook";//通讯记录
    public static final String UPDATEMEMBERPHONE="";//修改手机号
    //提升


}
