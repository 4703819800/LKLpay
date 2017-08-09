package com.lklpay.www.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.lklpay.www.R;
import com.lklpay.www.manager.ThreadManager;
import com.lklpay.www.tools.MethodUtil;
import com.lklpay.www.tools.UiUtils;


/***
 * 创建了自定义帧布局 把baseFragment 一部分代码 抽取到这个类中
 * 
 * @author itcast
 * 
 */
public abstract class LoadingPage extends FrameLayout {

	public static final int STATE_UNKOWN = 0;
	public static final int STATE_LOADING = 1;
	public static final int STATE_ERROR = 2;
	public static final int STATE_EMPTY = 3;
	public static final int STATE_SUCCESS = 4;
	public int state = STATE_UNKOWN;

	private View loadingView;// 加载中的界面
	private View errorView;// 错误界面
	private View emptyView;// 空界面
	private View successView;// 加载成功的界面

	public LoadingPage(Context context) {
		super(context);
		init();
	}

	public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		loadingView = createLoadingView(); // 创建了加载中的界面
		if (loadingView != null) {
			this.addView(loadingView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		errorView = createErrorView(); // 加载错误界面
		if (errorView != null) {
			this.addView(errorView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		emptyView = createEmptyView(); // 加载空的界面
		if (emptyView != null) {
			this.addView(emptyView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		showPage();// 根据不同的状态显示不同的界面
	}

	// 根据不同的状态显示不同的界面
	private void showPage() {
		if (loadingView != null) {
			loadingView.setVisibility(state == STATE_UNKOWN
					|| state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
		}
		if (errorView != null) {
			errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
					: View.INVISIBLE);
		}
		if (emptyView != null) {
			emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
					: View.INVISIBLE);
		}
		if (state == STATE_SUCCESS) {
			if (successView == null) {
				successView  = UiUtils.inflate(LayoutId());
				this.addView(successView, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			successView.setVisibility(View.VISIBLE);
			createSuccessView(state,successView);
		} else {
			if (successView != null) {
				successView.setVisibility(View.INVISIBLE);
			}
		}
	}

	/* 创建了空的界面 */
	private View createEmptyView() {
		View view = View.inflate(MethodUtil.getContext(),
				R.layout.page_empty, null);
		return view;
	}

	/* 创建了错误界面 */
	private View createErrorView() {
		final View view = View.inflate(MethodUtil.getContext(),
				R.layout.page_error, null);
		Button page_bt = (Button) view.findViewById(R.id.page_bt);
		page_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				show();
			}
		});

		// LinearLayout titlebar_left = (LinearLayout)
		// view.findViewById(R.id.titlebar_left);
		// titlebar_left.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// errorView.setVisibility(View.INVISIBLE);
		// }
		// });

		return view;
	}

	/* 创建加载中的界面 */
	private View createLoadingView() {
		View view = View.inflate(MethodUtil.getContext(),
				R.layout.page_loading, null);
		return view;
	}

	public enum LoadResult {
		loading(1),error(2), empty(3), success(4);

		int value;

		LoadResult(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}

	// 根据服务器的数据 切换状态
	public void show() {
		if (state == STATE_ERROR || state == STATE_EMPTY) {
			state = STATE_LOADING;
		}
		// 请求服务器 获取服务器上数据 进行判断
		// 请求服务器 返回一个结果
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {

			@Override
			public void run() {
				final LoadResult result = load();
				MethodUtil.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (result != null) {
							state = result.getValue();
							showPage(); // 状态改变了,重新判断当前应该显示哪个界面
						}
					}
				});
			}
		});

		showPage();

	}

	/***
	 * 获取界面Id
	 *
	 * @return
	 */
	public abstract int LayoutId();

	/***
	 * 创建成功的界面
	 * 
	 * @return
	 */
	public abstract void createSuccessView(int state,View successView);

	/**
	 * 请求服务器
	 * 
	 * @return
	 */
	protected abstract LoadResult load();
}