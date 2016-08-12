package com.shutup.doubanbeauty;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by shutup on 16/8/12.
 */
public class BeautyApplication extends Application{
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.dbmeinv.com/dbgroup/").addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    DoubanBeautyService doubanBeautyService = retrofit.create(DoubanBeautyService.class);

    static BeautyApplication sBeautyApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sBeautyApplication = this;
    }

    public static BeautyApplication getInstance() {
        return sBeautyApplication;
    }

    public DoubanBeautyService getDoubanBeautyService() {
        return doubanBeautyService;
    }

}
