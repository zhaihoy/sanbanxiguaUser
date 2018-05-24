package com.hbbc.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/11/10.
 *
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(path).into(imageView);
    }
}
