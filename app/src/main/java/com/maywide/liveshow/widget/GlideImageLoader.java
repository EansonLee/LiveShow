package com.maywide.liveshow.widget;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by user on 2018/11/15.
 */

public class GlideImageLoader extends ImageLoader {
	@Override
	public void displayImage(Context context, Object path, ImageView imageView) {
		Glide.with(context.getApplicationContext())
				.load(path)
				.into(imageView);
	}
}
