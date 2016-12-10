package com.shutup.doubanbeauty;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements Constants {

    String TAG = this.getClass().getSimpleName();

    BeautyRecyclerViewAdapter mBeautyRecyclerViewAdapter;
    ArrayList<BeautyModel> mBeautyModels;
    static boolean isLastLoadMoreOk = false;
    int pager_offset = 1;
    protected DoubanBeautyService doubanBeautyService;
    protected Subscription mSubscription;
    protected Context mContext;

    public BaseFragment() {
        BeautyApplication beautyApplication = BeautyApplication.getInstance();
        doubanBeautyService = beautyApplication.getDoubanBeautyService();
    }

    public Map<String, String> getQueryMap(String cid, int pager_offset) {
        if (BuildConfig.DEBUG) Log.d(TAG, "pager_offset:" + pager_offset);
        Map<String, String> option = new HashMap<>();
        if (!cid.equals(CID_1)) {
            option.put("cid", cid);
        }
        option.put("pager_offset", pager_offset + "");
        return option;
    }

    public abstract void loadBeautyData(Map<String, String> option);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription!=null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
