package com.shutup.doubanbeauty;


import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements Constants {
    String TAG = this.getClass().getSimpleName();
    BeautyRecyclerViewAdapter mBeautyRecyclerViewAdapter;
    ArrayList<BeautyModel> mBeautyModels;
    static boolean isLastLoadMoreOk = false;
    static int pager_offset = 1;
    protected DoubanBeautyService doubanBeautyService;

    public BaseFragment() {
        BeautyApplication beautyApplication = BeautyApplication.getInstance();
        doubanBeautyService = beautyApplication.getDoubanBeautyService();
    }

    public Map<String, String> getQueryMap(String cid, int pager_offset) {
        if (BuildConfig.DEBUG) Log.d(TAG, "pager_offset:" + pager_offset);
        Map<String, String> option = new HashMap();
        if (!cid.equals(CID_1)) {
            option.put("cid", cid);
        }
        option.put("pager_offset", pager_offset + "");
        return option;
    }

    public abstract void loadBeautyData(Map<String, String> option);
/*
    public void loadBeautyData(Map<String, String> option) {
        doubanBeautyService.beautyList(option).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Func1<Response<ResponseBody>, ArrayList<BeautyModel>>() {
            ArrayList<BeautyModel> result = new ArrayList<BeautyModel>();

            @Override
            public ArrayList<BeautyModel> call(Response<ResponseBody> responseBodyResponse) {
                if (BuildConfig.DEBUG) try {
                    Document document = Jsoup.parse(responseBodyResponse.body().string());
                    Elements elements = document.select("img");
                    BeautyModel beautyModel;
                    for (Element e : elements) {
                        String imageUrl = e.attr("src");
                        String imageTitle = e.attr("title");
                        beautyModel = new BeautyModel(imageUrl, imageTitle);
                        result.add(beautyModel);
                    }
                    Log.d(TAG, responseBodyResponse.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }).subscribe(new Action1<ArrayList<BeautyModel>>() {
            @Override
            public void call(ArrayList<BeautyModel> beautyModels) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "beautyModels.size():" + beautyModels.size());
                if (beautyModels.size() > 0) {
                    mBeautyRecyclerViewAdapter.setLoadingStatus(LoadingStatusCompleted);
                    mBeautyRecyclerViewAdapter.getBeautyModels().addAll(beautyModels);
                    mBeautyRecyclerViewAdapter.notifyDataSetChanged();
                } else {
                    mBeautyRecyclerViewAdapter.setLoadingStatus(LoadingStatusNoMoreData);
                    mBeautyRecyclerViewAdapter.notifyDataSetChanged();
                }
                isLastLoadMoreOk = true;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (BuildConfig.DEBUG) Log.d(TAG, throwable.toString());
                mBeautyRecyclerViewAdapter.setLoadingStatus(LoadingStatusCompleted);
                mBeautyRecyclerViewAdapter.notifyDataSetChanged();
                isLastLoadMoreOk = false;
            }
        });
    }
*/
}
