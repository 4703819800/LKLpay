package com.lklpay.www.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**װ
 * 
 * @author Kevin
 * 
 */
public class PrefUtils {

	public static final String PREF_NAME = "loginInfo";//loginInfo //first_pref
	public static final String PREF_NAME_HELP = "helpInfo";//helpInfo

	public static boolean getBoolean(String key, boolean defaultValue) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(String key, boolean value) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}	
	
	public static String getString(String key, String defaultValue) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(String key, String value) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static void clear() {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
	
	/**
	 * 单独设置不会被清除的
	 *
	 */
	public static boolean getBoolean(String key, boolean defaultValue,String prefName) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(String key, boolean value,String prefName) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static String getString(String key, String defaultValue,String prefName) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(String key, String value,String prefName) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static void clear(String prefName) {
		SharedPreferences sp = MethodUtil.getContext().getSharedPreferences(
				prefName, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

}
