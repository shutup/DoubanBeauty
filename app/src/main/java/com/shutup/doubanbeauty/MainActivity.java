package com.shutup.doubanbeauty;

import android.os.Bundle;
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
        mTabTitles.add("全部");
        mTabTitles.add("2");

        mFragmentList = new ArrayList<>();
        BeautyTypeAllFragment beautyTypeAllFragment = new BeautyTypeAllFragment();
        mFragmentList.add(beautyTypeAllFragment);
        BeautyType2Fragment beautyType2Fragment = new BeautyType2Fragment();
        mFragmentList.add(beautyType2Fragment);

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
}
