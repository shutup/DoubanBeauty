package com.shutup.doubanbeauty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.viewPager)
    ViewPager mViewPager;

    private List<String> mTabTitles;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mTabTitles = new ArrayList<>();
        mTabTitles.add(getString(R.string.cid_type_all));
        mTabTitles.add(getString(R.string.cid_type_2));
        mTabTitles.add(getString(R.string.cid_type_3));
        mTabTitles.add(getString(R.string.cid_type_4));
        mTabTitles.add(getString(R.string.cid_type_5));
        mTabTitles.add(getString(R.string.cid_type_6));
        mTabTitles.add(getString(R.string.cid_type_7));

        mFragmentList = new ArrayList<>();
        BeautyTypeAllFragment beautyTypeAllFragment = new BeautyTypeAllFragment();
        mFragmentList.add(beautyTypeAllFragment);
        BeautyType2Fragment beautyType2Fragment = new BeautyType2Fragment();
        mFragmentList.add(beautyType2Fragment);
        BeautyType3Fragment beautyType3Fragment = new BeautyType3Fragment();
        mFragmentList.add(beautyType3Fragment);
        BeautyType4Fragment beautyType4Fragment = new BeautyType4Fragment();
        mFragmentList.add(beautyType4Fragment);
        BeautyType5Fragment beautyType5Fragment = new BeautyType5Fragment();
        mFragmentList.add(beautyType5Fragment);
        BeautyType6Fragment beautyType6Fragment = new BeautyType6Fragment();
        mFragmentList.add(beautyType6Fragment);
        BeautyType7Fragment beautyType7Fragment = new BeautyType7Fragment();
        mFragmentList.add(beautyType7Fragment);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTabTitles.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        XPermissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
