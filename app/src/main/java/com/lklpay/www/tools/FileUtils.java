package com.lklpay.www.tools;

import android.os.Environment;

import com.lklpay.www.application.MyApplication;

import java.io.File;

public class FileUtils {
	public static final String CACHE = "cache";
	public static final String USERINFO = "userinfo";
	public static final String RECORD = "record";
	public static final String ICON = "icon";

	/**
	 * 获取图片的缓存的路径
	 * 
	 * @return
	 */
	public static File getIconDir() {
		return getDir(ICON);

	}

	/**
	 * 获取录音保存路径
	 * 
	 * @return
	 */
	public static File getRecordDir() {
		return getDir(RECORD);
	}

	/**
	 * 获取缓存路径
	 * 
	 * @return
	 */
	public static File getCacheDir() {
		return getDir(CACHE);
	}

	/**
	 * 获取用户资料的缓存的路径
	 * 
	 * @return
	 */
	public static File getUserInfoDir() {
		return getDir(USERINFO);

	}

	public static File getDirGen() {
		StringBuilder path = new StringBuilder();
		if (isSDAvailable()) {
			path.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			path.append(File.separator);// '/'
			path.append(MyApplication.ROOT);// /mnt/sdcard/yxt

		} else {
			File filesDir = MethodUtil.getContext().getCacheDir();
			path.append(filesDir.getAbsolutePath());
			path.append(File.separator);

		}
		File file = new File(path.toString());
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();// 创建文件夹
		}
		return file;

	}

	public static File getDir(String cache) {
		StringBuilder path = new StringBuilder();
		if (isSDAvailable()) {
			path.append(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			path.append(File.separator);// '/'
			path.append(MyApplication.ROOT);// /mnt/sdcard/yxt
			path.append(File.separator);
			path.append(cache);// /mnt/sdcard/yxt/cache

		} else {
			File filesDir = MethodUtil.getContext().getCacheDir(); // cache
			// getFileDir
			// file
			path.append(filesDir.getAbsolutePath());// /data/data/com.itheima.googleplay/cache
			path.append(File.separator);// /data/data/com.itheima.googleplay/cache/
			path.append(cache);// /data/data/com.itheima.googleplay/cache/cache
		}
		File file = new File(path.toString());
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();// 创建文件夹
		}
		return file;

	}

	private static boolean isSDAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

}
