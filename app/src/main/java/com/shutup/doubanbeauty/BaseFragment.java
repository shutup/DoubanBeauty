package com.shutup.doubanbeauty;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.attr.path;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements Constants {

    String TAG = this.getClass().getSimpleName();

    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    BeautyRecyclerViewAdapter mBeautyRecyclerViewAdapter;
    ArrayList<BeautyModel> mBeautyModels;
    static boolean isLastLoadMoreOk = false;
    int pager_offset = 1;
    protected DoubanBeautyService doubanBeautyService;
    protected Subscription mSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        ButterKnife.inject(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String, String> option = getQueryMap(getCID(), 1);
                loadBeautyData(option);
            }
        });

        initRecyclerView();

        Map<String, String> option = getQueryMap(getCID(), pager_offset);
        loadBeautyData(option);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public BaseFragment() {
        BeautyApplication beautyApplication = BeautyApplication.getInstance();
        doubanBeautyService = beautyApplication.getDoubanBeautyService();
    }

    public abstract String getCID();

    public Map<String, String> getQueryMap(String cid, int pager_offset) {
        if (BuildConfig.DEBUG) Log.d(TAG, "pager_offset:" + pager_offset);
        Map<String, String> option = new HashMap<>();
        if (!cid.equals(CID_1)) {
            option.put("cid", cid);
        }
        option.put("pager_offset", pager_offset + "");
        return option;
    }

    public void loadBeautyData(Map<String, String> option) {
        mSubscription = doubanBeautyService.beautyList(option).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Func1<Response<ResponseBody>, ArrayList<BeautyModel>>() {
                    ArrayList<BeautyModel> result = new ArrayList<>();

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
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLastLoadMoreOk = true;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (BuildConfig.DEBUG) Log.d(TAG, throwable.toString());
                        mBeautyRecyclerViewAdapter.setLoadingStatus(LoadingStatusCompleted);
                        mBeautyRecyclerViewAdapter.notifyDataSetChanged();
                        isLastLoadMoreOk = false;
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initRecyclerView() {
        mBeautyModels = new ArrayList<>();
        mBeautyRecyclerViewAdapter = new BeautyRecyclerViewAdapter(mBeautyModels, mRecyclerView);
        mBeautyRecyclerViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "mBeautyRecyclerViewAdapter.getStatus():" + mBeautyRecyclerViewAdapter.getStatus());
                if (mBeautyRecyclerViewAdapter.getStatus() == LoadingStatusLoading) {
                    return;
                }
                mBeautyRecyclerViewAdapter.setLoadingStatus(LoadingStatusLoading);
                if (isLastLoadMoreOk) {
                    pager_offset++;
                }
                Map<String, String> option = getQueryMap(getCID(), pager_offset);
                loadBeautyData(option);
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(this.getContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (BuildConfig.DEBUG) Log.d("BeautyType2Fragment", "position:" + position);
                BeautyModel beautyModel = mBeautyRecyclerViewAdapter.getBeautyModels().get(position);
                Intent intent = new Intent(getActivity(), BeautyDetailActivity.class);
                intent.putExtra(GirlDetailUrl,beautyModel.getImageUrl());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                BeautyViewHolder beautyViewHolder = (BeautyViewHolder) mRecyclerView.getChildViewHolder(view);
                ImageUtils.saveImageToGallery(getActivity(),beautyViewHolder.mBeautyItemImage);
                Toast.makeText(getActivity(), R.string.image_saved_str, Toast.LENGTH_SHORT).show();
            }
        }));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBeautyRecyclerViewAdapter);
    }

}
