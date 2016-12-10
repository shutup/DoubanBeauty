package com.shutup.doubanbeauty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class BeautyDetailActivity extends AppCompatActivity implements Constants {

    @InjectView(R.id.detailGirlImg)
    ImageView mDetailGirlImg;

    private PhotoViewAttacher mPhotoViewAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);
        ButterKnife.inject(this);
        loadImage();
    }

    private void loadImage() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(GirlDetailUrl);
        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.image_placeholder).into(mDetailGirlImg);
        mPhotoViewAttacher = new PhotoViewAttacher(mDetailGirlImg);
    }
}
