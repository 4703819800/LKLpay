package com.lklpay.www.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.dialog.widget.MaterialDialog;
import com.lklpay.www.CouponsPayActivity;
import com.lklpay.www.R;

public class UiUtils {


	public static View inflate(int id) {
		return View.inflate(MethodUtil.getContext(), id, null);
	}

	public static Drawable getDrawalbe(int id) {
		return MethodUtil.getResource().getDrawable(id);
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = MethodUtil.getResource().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** px转换dip */

	public static int px2dip(int px) {
		final float scale = MethodUtil.getResource().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * 
	 * 获取屏幕宽高
	 * 
	 */
	public static int[] getWindowManager(Context context) {
		int[] s;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		s = new int[2];
		s[0] = width;
		s[1] = height;
		return s;
	}

	/**
	 * 创建一个自定义的loading对话框，在加载网络等待的时候显示
	 *
	 * @param context
	 * @return
	 */
	public static Dialog creatLoadingDialog(Context context) {
		if (context != null) {
			Dialog dialog = new Dialog(context, R.style.CustomProgressDialog);
			dialog.setContentView(R.layout.loading_dialog);
			dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
			ImageView lodingImageView = (ImageView) dialog
					.findViewById(R.id.loadingImageView);
			lodingImageView.setVisibility(View.VISIBLE);
			Animation operatingAnim = AnimationUtils.loadAnimation(context,
					R.anim.rotate);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			if (operatingAnim != null) {
				lodingImageView.startAnimation(operatingAnim);
			}
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		} else {
			return null;
		}

	}

	/**
	 * 弹出提示框
	 */
	public static BaseAnimatorSet mBasIn;
	public static BaseAnimatorSet mBasOut;
	public static MaterialDialog MaterialDialogDefault(Context context,String msg) {

		final MaterialDialog dialog = new MaterialDialog(context);
		dialog.content(msg)//
				.btnText("取消", "确定")//
				.showAnim(mBasIn)//
				.dismissAnim(mBasOut)//
				.show();
		return dialog;
	}



}
