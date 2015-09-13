package com.antrromet.wecare.adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.antrromet.wecare.R;
import com.antrromet.wecare.models.CampaignDetail;
import com.antrromet.wecare.widgets.MyLinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CampaignDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnWebLinkClickListener mOnWebLinkClickListener;
    private CampaignDetail mCampaign;
    private Context mContext;

    public CampaignDetailsAdapter(Context context) {
        mContext = context;
    }

    public void setData(CampaignDetail data) {
        mCampaign = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCampaign == null) {
            return 0;
        }
        return 4;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_type_title, parent, false);
                return new AboutViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_type_activities, parent, false);
                return new ActivitiesViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_type_progress, parent, false);
                return new ProgressViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_type_contact, parent, false);
                return new ContactViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {

            case 0: {
                try {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale
                            .getDefault());

                    Date startDate = dateFormatter.parse(mCampaign.getStartsOn());
                    Date endDate = dateFormatter.parse(mCampaign.getEndsOn());

                    dateFormatter = new SimpleDateFormat("MMM dd", Locale
                            .getDefault());
                    ((AboutViewHolder) holder).durationText.setText(dateFormatter.format(startDate)
                            + " - " + dateFormatter.format(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                    ((AboutViewHolder) holder).durationText.setText("");
                }
                ((AboutViewHolder) holder).aboutText.setText(mCampaign.getAbout());
                ((AboutViewHolder) holder).titleText.setText(mCampaign.getName());
                break;
            }

            case 1: {
                ((ActivitiesViewHolder) holder).activityAdapter.setData(mCampaign.getActivities());
                break;
            }
            case 2: {
                ((ProgressViewHolder) holder).progress.setProgress(mCampaign.getProgress());
                ((ProgressViewHolder) holder).progressText.setText(mContext.getString(R.string
                        .progress, mCampaign.getProgress()));
                break;
            }
            case 3: {
                ((ContactViewHolder) holder).websiteText.setText(mContext.getString(R.string
                        .website, mCampaign.getContact().getWebsite()));
                ((ContactViewHolder) holder).emailText.setText(mContext.getString(R.string
                        .email, mCampaign.getContact().getEmail()));
                ((ContactViewHolder) holder).facebookText.setText(mContext.getString(R.string
                        .facebook, mCampaign.getContact().getFbLink()));
                ((ContactViewHolder) holder).twitterText.setText(mContext.getString(R.string
                        .twitter, mCampaign.getContact().getTwitterLink()));
                break;
            }
        }
    }

    public void setOnWebLinkClickListener(OnWebLinkClickListener onWebLinkClickListener) {
        mOnWebLinkClickListener = onWebLinkClickListener;
    }

    public interface OnWebLinkClickListener {
        void onWebLinkClick(View view, String link);
    }

    public class ActivitiesViewHolder extends RecyclerView.ViewHolder {

        private ActivitiesAdapter activityAdapter;
        private RecyclerView activitiesRecyclerView;

        public ActivitiesViewHolder(View view) {
            super(view);
            activityAdapter = new ActivitiesAdapter();
            activitiesRecyclerView = (RecyclerView) view.findViewById(R.id.activities_recyclerview);

            activitiesRecyclerView.setHasFixedSize(true);
            MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(mContext,
                    LinearLayoutManager.HORIZONTAL, false);
            activitiesRecyclerView.setLayoutManager(mLayoutManager);
            activitiesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            activitiesRecyclerView.setAdapter(activityAdapter);
        }

    }

    public class AboutViewHolder extends RecyclerView.ViewHolder {

        private TextView durationText;
        private TextView titleText;
        private TextView aboutText;

        public AboutViewHolder(View view) {
            super(view);
            durationText = (TextView) view.findViewById(R.id.duration_text);
            titleText = (TextView) view.findViewById(R.id.title_text);
            aboutText = (TextView) view.findViewById(R.id.about_text);
        }

    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        private TextView progressText;
        private ProgressBar progress;

        public ProgressViewHolder(View view) {
            super(view);
            progressText = (TextView) view.findViewById(R.id.progress_text);
            progress = (ProgressBar) view.findViewById(R.id.progress);
        }

    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView websiteText;
        private TextView emailText;
        private TextView facebookText;
        private TextView twitterText;

        public ContactViewHolder(View view) {
            super(view);
            websiteText = (TextView) view.findViewById(R.id.website_text);
            emailText = (TextView) view.findViewById(R.id.email_text);
            facebookText = (TextView) view.findViewById(R.id.facebook_text);
            twitterText = (TextView) view.findViewById(R.id.twitter_text);

            websiteText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnWebLinkClickListener.onWebLinkClick(v, mCampaign.getContact().getWebsite());
        }
    }

}
