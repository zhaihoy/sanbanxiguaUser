package com.hbbc.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Administrator on 2017/8/17.
 */

//@com.bumptech.glide.annotation.GlideModule
public class Mshare_App_Glide_Module implements GlideModule{

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
//        builder.setDiskCache(DiskLruCacheFactory.DEFAULT_DISK_CACHE_DIR);
//        builder.setDiskCache(new DiskCache.Factory() {
//            @Override
//            public DiskCache build() {
//
//                return new DiskCache() {
//                    @Override
//                    public File get(Key key) {
//
//                        return null;
//                    }
//
//
//
//                    @Override
//                    public void put(Key key, Writer writer) {
//
//                    }
//
//
//
//                    @Override
//                    public void delete(Key key) {
//
//                    }
//
//
//
//                    @Override
//                    public void clear() {
//
//                    }
//                };
//            }
//        });
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,"custom_disk_cache_dir",50));

    }



    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
