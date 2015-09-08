package com.antrromet.wecare.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antrromet.wecare.R;
import com.antrromet.wecare.models.Campaign;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CampaignAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Campaign> mCampaigns;
    private com.nostra13.universalimageloader.core.ImageLoader mImageLoader;

    public CampaignAdapter(Context context) {
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        if (mCampaigns == null) {
            return 0;
        }
        return mCampaigns.size();
    }

    @Override
    public Object getItem(int position) {
        return mCampaigns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, R.layout.grid_item_campaign, null);
            holder = new ViewHolder();
            holder.titleText = (TextView) view.findViewById(R.id.title_text);
            holder.descText = (TextView) view.findViewById(R.id.desc_text);
            holder.posterImage = (ImageView) view.findViewById(R.id.poster_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.titleText.setText(((Campaign) getItem(position)).getName());
        holder.descText.setText(((Campaign) getItem(position)).getShortDesc());
        mImageLoader.displayImage(((Campaign) getItem(position)).getImg(), holder.posterImage);
        return view;
    }

    public void setCampaigns(List<Campaign> campaigns) {
        mCampaigns = campaigns;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView titleText;
        private TextView descText;
        private ImageView posterImage;
    }

}
