package com.zhongtianli.nebula.netutil;


//import com.bocai.goodeasy.bean.BaseBean;
//import com.bocai.goodeasy.bean.CityBean;
//import com.bocai.goodeasy.bean.CountBean;
//import com.bocai.goodeasy.bean.CourseBean;
//import com.bocai.goodeasy.bean.HotPostPhotoBean;
//import com.bocai.goodeasy.bean.InteralBean;
//import com.bocai.goodeasy.bean.NoticesBean;
//import com.bocai.goodeasy.bean.PostBean;
//import com.bocai.goodeasy.bean.PostListBean;
//import com.bocai.goodeasy.bean.SignListBean;
//import com.bocai.goodeasy.bean.UploadBean;
//import com.bocai.goodeasy.utils.UrlData;

import com.zhongtianli.nebula.bean.BaseBean;
import com.zhongtianli.nebula.utils.UrlData;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by admin on 2016/1/9.
 */
public interface TestApi {
    /**
     *
     * @param Password
     * @param MemberPhone
     * @param MemberName
     * @param Address
     * @param UserType
     * @param Province
     * @param City
     * @param District
     * @param Superior
     * @param Images
     * @return
     */
    /**
     * 登录
     * @param UserName
     * @param Password
     * @return
     */
    @FormUrlEncoded
    @POST(UrlData.LOGIN)
    Observable<BaseBean> login(@Field("UserName") String UserName, @Field("Password") String Password);

    /**
     * 找回密码-设置新密码
     * @param MemberPhone
     * @param Password
     * @return
     */
    @FormUrlEncoded
    @POST(UrlData.RESETPSD)
    Observable<BaseBean> resetPsd(@Field("MemberPhone") String MemberPhone, @Field("Password") String Password);
    /**
     * 获取短信验证码
     * @param phone
     * @param token
     * @return
     */

    @GET(UrlData.GETCODE)
    Observable<BaseBean> getCode(@Query("phone") String phone, @Query("token") String token);

    /**
     *获取短信验证码并验证是否已经注册
     * @param phone
     * @param token
     * @return
     */
    @GET(UrlData.GETCODEWITHCHECK)
    Observable<BaseBean> getCodeWithCheck(@Query("phone") String phone, @Query("token") String token);

    /**
     * 获取短信验证码并验证是否已经注册，未注册则报错
     * @param phone
     * @param token
     * @return
     */
    @GET(UrlData.GETCODEFORPSD)
    Observable<BaseBean> getCodeForPsd(@Query("phone") String phone, @Query("token") String token);

    /**
    *
     * @par
    /**
     *修改手机号
     * @param MemberId 会员Id，加密
     * @param MemberNewPhone 新手机号，加密
     * @return
     */
    @FormUrlEncoded
    @POST(UrlData.UPDATEMEMBERPHONE)
    Observable<BaseBean> UpdateMemberPhone(@Field("MemberId") String MemberId, @Field("MemberNewPhone") String MemberNewPhone);

    }
