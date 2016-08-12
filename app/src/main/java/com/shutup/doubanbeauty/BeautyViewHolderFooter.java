package com.shutup.doubanbeauty;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by shutup on 16/8/10.
 */
public class BeautyViewHolderFooter extends RecyclerView.ViewHolder {


    LinearLayout mLinearLayout;
    ProgressBar mProgressBar;
    TextView mTextview;

    public BeautyViewHolderFooter(View itemView) {
        super(itemView);
        mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        mTextview = (TextView) itemView.findViewById(R.id.textview);
    }
}
