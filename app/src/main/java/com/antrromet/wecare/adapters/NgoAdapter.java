package com.antrromet.wecare.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antrromet.wecare.R;
import com.antrromet.wecare.models.Ngo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class NgoAdapter extends BaseAdapter {

    private final Context mContext;
    private List<Ngo> mNgos;
    private ImageLoader mImageLoader;

    public NgoAdapter(Context context) {
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        if (mNgos == null) {
            return 0;
        }
        return mNgos.size();
    }

    @Override
    public Object getItem(int position) {
        return mNgos.get(position);
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
            view = View.inflate(mContext, R.layout.grid_item_ngo, null);
            holder = new ViewHolder();
            holder.titleText = (TextView) view.findViewById(R.id.title_text);
            holder.descText = (TextView) view.findViewById(R.id.desc_text);
            holder.posterImage = (ImageView) view.findViewById(R.id.poster_image);
            holder.countText = (TextView) view.findViewById(R.id.count_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.titleText.setText(((Ngo) getItem(position)).getName());
        holder.descText.setText(((Ngo) getItem(position)).getShortDesc());
        holder.countText.setText(((Ngo) getItem(position)).getCampaignCount()+"");
        mImageLoader.displayImage(((Ngo) getItem(position)).getImg(), holder.posterImage);
        return view;
    }

    public void setNgos(List<Ngo> ngos) {
        mNgos = ngos;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView titleText;
        private TextView descText;
        private TextView countText;
        private ImageView posterImage;
    }

}
