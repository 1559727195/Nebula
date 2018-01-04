package com.zhongtianli.nebula.slidebottompanel;

import android.annotation.SuppressLint;
import android.view.View;

@SuppressLint("NewApi") public class ViewHelper {

	public static float getY(View view){
		return view.getY();
	}
	
	public static void setY(View view,float value){
		view.setY(value);
	}
}
