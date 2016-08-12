package com.shutup.doubanbeauty;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shutup on 16/8/9.
 */
public class BeautyRecyclerViewAdapter extends RecyclerView.Adapter implements Constants {

    private ArrayList<BeautyModel> mBeautyModels = null;
    private List<Integer> heights = null;
    RecyclerView.LayoutManager mLayoutManager;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status = LoadingStatusNormal;
    private int visibleThreshold = 1;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private OnLoadMoreListener mOnLoadMoreListener = null;

    public ArrayList<BeautyModel> getBeautyModels() {
        return mBeautyModels;
    }

    public void setBeautyModels(ArrayList<BeautyModel> beautyModels) {
        mBeautyModels = beautyModels;
    }


    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public BeautyRecyclerViewAdapter(ArrayList<BeautyModel> beautyModels, RecyclerView recyclerview) {
        mBeautyModels = beautyModels;
        heights = new ArrayList<>();
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLayoutManager = recyclerView.getLayoutManager();
                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItemPosition = 0;
                if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                    // get maximum element within the list
                    lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
                } else if (mLayoutManager instanceof LinearLayoutManager) {
                    lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                } else if (mLayoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                }
                if (status != LoadingStatusLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                    if (mOnLoadMoreListener != null)
                        mOnLoadMoreListener.onLoadMore();
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item_layout, parent, false);
        return new BeautyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mLayoutManager instanceof LinearLayoutManager) {

            BeautyViewHolder beautyViewHolder = (BeautyViewHolder) holder;
            BeautyModel beautyModel = mBeautyModels.get(position);
            beautyViewHolder.mBeautyItemTitle.setText(beautyModel.getTitle());
            Picasso.with(beautyViewHolder.mBeautyItemImage.getContext()).load(beautyModel.getImageUrl()).placeholder(R.drawable.image_placeholder).into(beautyViewHolder.mBeautyItemImage);

        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            BeautyViewHolder beautyViewHolder = (BeautyViewHolder) holder;
            if (heights.size() <= position) {
                heights.add((int) (300 + Math.random() * 300));
            }

            ViewGroup.LayoutParams layoutParams = beautyViewHolder.mBeautyItemImage.getLayoutParams();
            layoutParams.height = heights.get(position);
            beautyViewHolder.mBeautyItemImage.setLayoutParams(layoutParams);

            BeautyModel beautyModel = mBeautyModels.get(position);
            beautyViewHolder.mBeautyItemTitle.setText(beautyModel.getTitle());
            Picasso.with(beautyViewHolder.mBeautyItemImage.getContext()).load(beautyModel.getImageUrl()).placeholder(R.drawable.image_placeholder).into(beautyViewHolder.mBeautyItemImage);

        } else if (mLayoutManager instanceof GridLayoutManager) {
            BeautyViewHolder beautyViewHolder = (BeautyViewHolder) holder;
            BeautyModel beautyModel = mBeautyModels.get(position);
            beautyViewHolder.mBeautyItemTitle.setText(beautyModel.getTitle());
            Picasso.with(beautyViewHolder.mBeautyItemImage.getContext()).load(beautyModel.getImageUrl()).placeholder(R.drawable.image_placeholder).into(beautyViewHolder.mBeautyItemImage);
        }

    }

    @Override
    public int getItemCount() {
        return mBeautyModels.size();
    }

    public void setLoadingStatus(int status) {
        this.status = status;
    }
}
