package com.lklpay.www.updateutil;

import android.content.Context;

/*
 *  App  更新管理工具
 */
public class UpdateManager {

	private Context mContext;
	private String mType;
	private CheckForUpdateTask checkForUpdateTask;

	public UpdateManager(Context context, String type) {
		this.mContext = context;
		this.mType = type;
		checkForUpdateTask = new CheckForUpdateTask(mContext,mType);
	}

	public void checkUpdate() {
		checkForUpdateTask.execute();
	}

}
