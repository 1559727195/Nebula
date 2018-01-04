package com.zhongtianli.nebula.utils;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.entity.NavigationItem;


public class DatasUtil {
	private static DatasUtil mDatas;
	private List<NavigationItem> mList;

	private DatasUtil(Context context){
		mList=new ArrayList<NavigationItem>();
		mList.add(new NavigationItem("设备调试", context.getResources().getDrawable(R.drawable.ic_toggle_star),Style.DEFAULT));
		mList.add(new NavigationItem("设备管理", context.getResources().getDrawable(R.drawable.ic_toggle_star),Style.DEFAULT));
		mList.add(new NavigationItem("设置", context.getResources().getDrawable(R.drawable.ic_toggle_star),Style.DEFAULT));
		mList.add(new NavigationItem("消息", context.getResources().getDrawable(R.drawable.ic_toggle_star),Style.DEFAULT));
		mList.add(new NavigationItem("关于", context.getResources().getDrawable(R.drawable.ic_toggle_star),Style.DEFAULT));
	}
	
	public static DatasUtil getInstance(Context context){
		if(mDatas==null){
			synchronized (DatasUtil.class) {
				if(mDatas==null){
					mDatas=new DatasUtil(context);
				}
			}
		}
		return mDatas;
		
	}

	public  List<NavigationItem> getMenu(){
		 return new ArrayList<NavigationItem>(mList);
	}

}
