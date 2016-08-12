package com.shutup.doubanbeauty;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shutup on 16/8/9.
 */
public class BeautyViewHolder extends RecyclerView.ViewHolder {
    ImageView mBeautyItemImage;
    TextView mBeautyItemTitle;

    public BeautyViewHolder(View itemView) {
        super(itemView);
        mBeautyItemImage = (ImageView) itemView.findViewById(R.id.beauty_item_image);
        mBeautyItemTitle = (TextView) itemView.findViewById(R.id.beauty_item_title);
    }
}
