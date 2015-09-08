package com.antrromet.wecare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.antrromet.wecare.R;
import com.antrromet.wecare.models.Activity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter
        .ViewHolder> {

    private final ImageLoader mImageLoader;
    private List<Activity> mActivities;

    public ActivitiesAdapter() {
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(mActivities.get(position).getTitle());
        holder.descTextView.setText(mActivities.get(position).getDesc());
        mImageLoader.displayImage(mActivities.get(position).getImg(), holder.imageView);
    }

    public void setData(List<Activity> data) {
        mActivities = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mActivities == null) {
            return 0;
        }
        return mActivities.size();
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descTextView;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.title_textview);
            descTextView = (TextView) view.findViewById(R.id.desc_textview);
            imageView = (ImageView) view.findViewById(R.id.activity_image);
        }

    }

}
