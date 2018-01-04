package com.zhongtianli.nebula.presenter;
import android.content.Context;

import com.zhongtianli.nebula.bean.GetDevice;
import com.zhongtianli.nebula.bean.LoginProjects;
import com.zhongtianli.nebula.bean.SetDeviceBean;
import com.zhongtianli.nebula.model.GetFlightInfo;
import com.zhongtianli.nebula.model.GetLoginProjects;
import com.zhongtianli.nebula.model.IGetFlight;
import com.zhongtianli.nebula.model.IGetFragment;
import com.zhongtianli.nebula.model.INebula_GetDevice;
import com.zhongtianli.nebula.model.ISetDevice;
import com.zhongtianli.nebula.model.ISetPassword;
import com.zhongtianli.nebula.model.ISetVisitor;
import com.zhongtianli.nebula.model.IShowNebula_GetLoginProjects;
import com.zhongtianli.nebula.model.OnNebula_GetDeviceListener;
import com.zhongtianli.nebula.model.OnNebula_GetLoginListener;
import com.zhongtianli.nebula.model.OnNebula_SetDeviceListener;
import com.zhongtianli.nebula.model.OnNebula_SetPasswordListener;
import com.zhongtianli.nebula.model.OnNebula_SetVisitorListener;
import com.zhongtianli.nebula.model.SetDeviceInfo;
import com.zhongtianli.nebula.model.SetPassWordInfo;
import com.zhongtianli.nebula.model.SetVisitorInfo;
import com.zhongtianli.nebula.view.IShowFlightView;
import com.zhongtianli.nebula.view.IShowFragmentView;
import com.zhongtianli.nebula.view.IShowNebula_GetDeviceView;
import com.zhongtianli.nebula.view.IShowNebula_SetDeviceView;
import com.zhongtianli.nebula.view.IShowNebula_SetPasswordView;
import com.zhongtianli.nebula.view.IShowNebula_VisitorView;

import java.util.ArrayList;
import java.util.Map;

import static com.zhongtianli.nebula.R.drawable.control;

/**
 * Created by robin on 2016/5/25.
 */
public class FlightInfoPresenter {
    private  ISetVisitor iSetVistor;//远程控制设备
    private IShowNebula_SetDeviceView iShowNebula_setDeviceView;//上传设备信息
    private IGetFlight iGetFlight;//获取设备信息
    private ISetDevice iSetDevice;//设置设备信息
    private ISetPassword iSetPassword;//设置新密码
    private  IShowNebula_SetPasswordView iShowNebula_setPasswordView;//设置新密码后用来展示新密码
    private IShowFlightView iShowFlightView;
    private IShowFragmentView iShowFragmentView;
    private IGetFragment iGetFragment;
    private INebula_GetDevice iNebula_getDevice;//拿到设备信息接口
    private IShowNebula_GetDeviceView iShowNebula_getDeviceView;
    private  IShowNebula_GetLoginProjects iShowNebula_getLoginProjects;//拉取登录下的项目列表信息
    private  GetLoginProjects getLoginProjects;
    private IShowNebula_VisitorView iShowNebula_visitorView;//远程设备控制

    //拉取登录下的项目列表
    public  FlightInfoPresenter(IShowNebula_GetLoginProjects iShowNebula_getLoginProjects){
        this.iShowNebula_getLoginProjects = iShowNebula_getLoginProjects;
        this.getLoginProjects = new GetLoginProjects();
    }

    //下载设备信息
    public  FlightInfoPresenter(IShowNebula_GetDeviceView iShowNebula_getDeviceView){
        this.iShowNebula_getDeviceView = iShowNebula_getDeviceView;
        this.iGetFlight = new GetFlightInfo();
    }

      //上传设备信息
      public  FlightInfoPresenter(IShowNebula_SetDeviceView iShowNebula_setDeviceView){
          this.iShowNebula_setDeviceView = iShowNebula_setDeviceView;
          this.iSetDevice = new SetDeviceInfo();
      }

    //修改密码
    public  FlightInfoPresenter(IShowNebula_SetPasswordView iShowNebula_setPasswordView){
        this.iShowNebula_setPasswordView = iShowNebula_setPasswordView;
        this.iSetPassword = new SetPassWordInfo();
    }

    //远程控制设备开关
    public  FlightInfoPresenter (IShowNebula_VisitorView iShowNebula_visitorView) {
         this.iShowNebula_visitorView = iShowNebula_visitorView;
        this.iSetVistor = new SetVisitorInfo();
    }

    //远程控制设备开关
    public void nebula_remote_control(Context context, Map<String, String> map){
        iSetVistor.nebula_SetVisitor(context, map, new OnNebula_SetVisitorListener() {
            @Override
            public void setVisitorInfoSuccess(String list) {
                iShowNebula_visitorView.showNebula_visitorView_success(list);
            }

            @Override
            public void setVisitorInfoFailed() {
                iShowNebula_visitorView.showNebula_visitorView_Error();
            }
        });
    }

    //拉取登录下的项目列表信息
    public void getNebula_GetLoginProjects(Context context, Map<String, String> map, boolean isFirst){

        getLoginProjects.nebula_LoginProjects(isFirst,context,map,new OnNebula_GetLoginListener(){

            @Override
            public void getLoginInfoSuccess(ArrayList<LoginProjects> list, boolean isFirst) {
                iShowNebula_getLoginProjects.showNebula_GetLoginProjects(list,isFirst);
            }

            @Override
            public void getLoginInfoFailed(boolean isFirst) {
                iShowNebula_getLoginProjects.showNebula_LoginProjectsError(isFirst);
            }
        });
    }

    //下载设备信息
    public void getNebula_GetDevice(Context context, Map<String, String> map){
        iGetFlight.nebula_GetDevice(context,map,new OnNebula_GetDeviceListener(){

            @Override
            public void getDeviceInfoSuccess(GetDevice list) {
                iShowNebula_getDeviceView.showNebula_GetDevice(list);
            }

            @Override
            public void getDeviceInfoFailed() {
                     iShowNebula_getDeviceView.showNebula_FailedError();
            }
        });
     }

    //上传设备信息-修改设备名称后
    public void setNebula_GetDevice(Context context, SetDeviceBean setDeviceBean){
        iSetDevice.nebula_SetDevice(context,setDeviceBean,new OnNebula_SetDeviceListener(){

            @Override
            public void setDeviceInfoSuccess(String Result) {//上传设备信息-后返回的编码-100表示成功
                iShowNebula_setDeviceView.showNebula_SetDevice(Result);
            }

            @Override
            public void setDeviceInfoFailed() {
             iShowNebula_setDeviceView.showNebula_FailedError();//上传失败
            }
        });
    }

    //修改密码 -》
    public void setNebula_password(Context context, Map map){
        iSetPassword.nebula_SetPassword(context,map,new OnNebula_SetPasswordListener(){

            @Override
            public void setPasswordInfoSuccess(String success) {
                iShowNebula_setPasswordView.showNebula_setPassword_success(success);//设置新密码成功
            }

            @Override
            public void setPasswordInfoFailed() {
                iShowNebula_setPasswordView.showNebula_setPasswordError();//设置新密码失败
            }
        });
    }
}


