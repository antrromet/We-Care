package com.antrromet.wecare.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antrromet.wecare.R;
import com.antrromet.wecare.fragments.BaseFragment;

/**
 * Created by Antrromet on 9/1/15 9:02 PM
 */
public class FeaturedFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);
        return view;
    }

}
