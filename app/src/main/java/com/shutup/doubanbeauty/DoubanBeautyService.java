package com.shutup.doubanbeauty;


import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by shutup on 16/8/9.
 */
public interface DoubanBeautyService {
    @GET("show.htm")
    Observable<Response<ResponseBody>> beautyList(@QueryMap Map<String, String> options);
}
