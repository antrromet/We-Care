package com.antrromet.wecare.adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antrromet.wecare.R;
import com.antrromet.wecare.models.NgoDetail;
import com.antrromet.wecare.widgets.MyLinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NgoDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CampaignsAdapter.OnCampaignClickListener mOnCampaignClickListener;
    private OnWebLinkClickListener mOnWebLinkClickListener;
    private NgoDetail mNgo;
    private Context mContext;

    public NgoDetailsAdapter(Context context) {
        mContext = context;
    }

    public void setData(NgoDetail data) {
        mNgo = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mNgo == null) {
            return 0;
        }
        return 3;
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
                        .inflate(R.layout.item_type_tile, parent, false);
                return new CampaignsViewHolder(view);
            case 2:
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

                    Date startDate = dateFormatter.parse(mNgo.getJoined());

                    dateFormatter = new SimpleDateFormat("MMM dd", Locale
                            .getDefault());
                    ((AboutViewHolder) holder).durationText.setText(mContext.getString(R.string
                            .joined, dateFormatter.format(startDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                    ((AboutViewHolder) holder).durationText.setText("");
                }
                ((AboutViewHolder) holder).aboutText.setText(mNgo.getAbout());
                ((AboutViewHolder) holder).titleText.setText(mNgo.getName());
                ((AboutViewHolder) holder).founderText.setText(mContext.getString(R.string
                        .founder, mNgo.getFounder()));
                ((AboutViewHolder) holder).founderText.setVisibility(View.VISIBLE);
                ((AboutViewHolder) holder).missionText.setText(mContext.getString(R.string
                        .mission, mNgo.getMission()));
                ((AboutViewHolder) holder).missionText.setVisibility(View.VISIBLE);
                break;
            }

            case 1: {
                ((CampaignsViewHolder) holder).titleText.setText(mContext.getString(R.string
                        .campaigns));
                ((CampaignsViewHolder) holder).campaignAdapter.setData(mNgo.getCampaigns());
                break;
            }

            case 2: {
                ((ContactViewHolder) holder).websiteText.setText(mContext.getString(R.string
                        .website, mNgo.getContact().getWebsite()));
                ((ContactViewHolder) holder).emailText.setText(mContext.getString(R.string
                        .email, mNgo.getContact().getEmail()));
                ((ContactViewHolder) holder).facebookText.setText(mContext.getString(R.string
                        .facebook, mNgo.getContact().getFbLink()));
                ((ContactViewHolder) holder).twitterText.setText(mContext.getString(R.string
                        .twitter, mNgo.getContact().getTwitterLink()));
                break;
            }
        }
    }

    public void setOnWebLinkClickListener(OnWebLinkClickListener onWebLinkClickListener) {
        mOnWebLinkClickListener = onWebLinkClickListener;
    }

    public void setOnCampaignClickListener(CampaignsAdapter.OnCampaignClickListener
                                                   onCampaignClickListener) {
        mOnCampaignClickListener = onCampaignClickListener;
    }

    public interface OnWebLinkClickListener {
        void onWebLinkClick(View view, String link);
    }

    public class AboutViewHolder extends RecyclerView.ViewHolder {

        private TextView durationText;
        private TextView founderText;
        private TextView titleText;
        private TextView aboutText;
        private TextView missionText;

        public AboutViewHolder(View view) {
            super(view);
            durationText = (TextView) view.findViewById(R.id.duration_text);
            founderText = (TextView) view.findViewById(R.id.founder_text);
            titleText = (TextView) view.findViewById(R.id.title_text);
            aboutText = (TextView) view.findViewById(R.id.about_text);
            missionText = (TextView) view.findViewById(R.id.mission_text);
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
            mOnWebLinkClickListener.onWebLinkClick(v, mNgo.getContact().getWebsite());
        }
    }

    public class CampaignsViewHolder extends RecyclerView.ViewHolder{

        private CampaignsAdapter campaignAdapter;
        private TextView titleText;
        private RecyclerView campaignRecyclerView;

        public CampaignsViewHolder(View view) {
            super(view);
            campaignAdapter = new CampaignsAdapter();
            campaignAdapter.setOnCampaignClickListener(mOnCampaignClickListener);

            campaignRecyclerView = (RecyclerView) view.findViewById(R.id.tile_recyclerview);

            campaignRecyclerView.setHasFixedSize(true);
            MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(mContext,
                    LinearLayoutManager.HORIZONTAL, false);
            campaignRecyclerView.setLayoutManager(mLayoutManager);
            campaignRecyclerView.setItemAnimator(new DefaultItemAnimator());
            campaignRecyclerView.setAdapter(campaignAdapter);

            titleText = (TextView) view.findViewById(R.id.title_text);
        }
    }

}
