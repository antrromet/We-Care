package com.antrromet.wecare.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.antrromet.wecare.fragments.BaseFragment;
import com.antrromet.wecare.fragments.FeaturedFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Featured", "Tab2", "Tab3"};
    private Context context;
    private SparseArray<BaseFragment> fragments;

    public MainActivityPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        fragments = new SparseArray<>(PAGE_COUNT);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        BaseFragment fragment = fragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 0: {
                    fragment = new FeaturedFragment();
                    fragments.put(0, fragment);
                    break;
                }

                case 1:
                case 2: {
                    fragment =  PageFragment.newInstance(position);
                    fragments.put(1, fragment);
                    break;
                }


                default:
                    fragment = null;
            }
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}