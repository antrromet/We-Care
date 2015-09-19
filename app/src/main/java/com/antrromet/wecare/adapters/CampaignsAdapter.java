package com.antrromet.wecare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.antrromet.wecare.R;
import com.antrromet.wecare.models.Campaign;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CampaignsAdapter extends RecyclerView.Adapter<CampaignsAdapter
        .ViewHolder> {

    private final ImageLoader mImageLoader;
    private List<Campaign> mCampaigns;
    private OnCampaignClickListener mOnCampaignClickListener;

    public CampaignsAdapter() {
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
        holder.titleTextView.setText(mCampaigns.get(position).getName());
        holder.descTextView.setText(mCampaigns.get(position).getShortDesc());
        mImageLoader.displayImage(mCampaigns.get(position).getImg(), holder.imageView);
    }

    public void setData(List<Campaign> data) {
        mCampaigns = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCampaigns == null) {
            return 0;
        }
        return mCampaigns.size();
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView titleTextView;
        private TextView descTextView;
        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.title_textview);
            descTextView = (TextView) view.findViewById(R.id.desc_textview);
            imageView = (ImageView) view.findViewById(R.id.activity_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnCampaignClickListener.onCampaignClick(view, mCampaigns.get(getAdapterPosition())
                    .getId(), mCampaigns.get(getAdapterPosition()).getName(), mCampaigns.get
                    (getAdapterPosition()).getImg());
        }
    }

    public void setOnCampaignClickListener(OnCampaignClickListener onCampaignClickListener) {
        mOnCampaignClickListener = onCampaignClickListener;
    }

    public interface OnCampaignClickListener {
        void onCampaignClick(View view, String campaignId, String name, String img);
    }

}
