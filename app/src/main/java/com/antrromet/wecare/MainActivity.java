package com.antrromet.wecare;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.antrromet.wecare.adapters.MainActivityPagerAdapter;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.actionbar_layout);
        toolbar.setTitle(getString(R.string.app_name));

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainActivityPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Hack for flickering titles of the tab layout
        // https://code.google.com/p/android/issues/detail?id=180454
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            int mScrollState;
            int mScrollPosition;
            float mScrollOffset;

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                mScrollState = state;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                mScrollPosition = position;
                mScrollOffset = positionOffset;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    setScreenName(getString(R.string.campaigns));
                } else if (position == 1) {
                    setScreenName(getString(R.string.ngos));
                }
                if (mScrollState != ViewPager.SCROLL_STATE_IDLE) {
                    tabLayout.setScrollPosition(mScrollPosition, mScrollOffset, true);
                }
            }
        });
    }

}
