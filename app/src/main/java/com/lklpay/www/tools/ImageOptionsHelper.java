package com.lklpay.www.tools;

import android.widget.ImageView;

import com.lklpay.www.R;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;


/**
 * Created by Administrator on 2017/6/19.
 */

public class ImageOptionsHelper {

    private static ImageOptions options;

    public static ImageOptions getOptions() {

        if (options == null) {
            options = new ImageOptions.Builder()
                    //                    .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))//图片大小
                    .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                    //                    .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setUseMemCache(true)//设置使用缓存
                    // 是否忽略GIF格式的图片
                    .setIgnoreGif(false)
                    // 图片缩放模式
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    // 下载中显示的图片
                    .setLoadingDrawableId(R.drawable.icon_stub)
                    // 下载失败显示的图片
                    .setFailureDrawableId(R.drawable.icon_error)
                    // 得到ImageOptions对象
                    .build();
        }
        return options;
    }

    public void setImage(final ImageView imageView, final String url) {
        x.image().bind(imageView, url, getOptions());
    }

    public void getImageFile(String url, final Boolean cache) {
        x.image().loadFile(url, getOptions(), new Callback.CacheCallback<File>() {
            @Override
            public boolean onCache(File result) {
                return cache;
            }

            @Override
            public void onSuccess(File result) {
                //设置图片保存到本地

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
