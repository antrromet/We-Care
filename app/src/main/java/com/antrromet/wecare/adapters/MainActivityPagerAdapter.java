package com.antrromet.wecare.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.antrromet.wecare.fragments.BaseFragment;
import com.antrromet.wecare.fragments.CampaignsFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Campaigns", "NGOs"};
    private SparseArray<BaseFragment> fragments;

    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);
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
                    fragment = new CampaignsFragment();
                    fragments.put(0, fragment);
                    break;
                }

                case 1: {
                    fragment = PageFragment.newInstance(position);
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