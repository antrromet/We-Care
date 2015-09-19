package com.antrromet.wecare.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.antrromet.wecare.CampaignDetailActivity;
import com.antrromet.wecare.Constants;
import com.antrromet.wecare.R;
import com.antrromet.wecare.adapters.CampaignAdapter;
import com.antrromet.wecare.interfaces.OnVolleyResponseListener;
import com.antrromet.wecare.models.Campaign;
import com.antrromet.wecare.provider.DBOpenHelper;
import com.antrromet.wecare.provider.DBProvider;
import com.antrromet.wecare.utils.JSONUtils;
import com.etsy.android.grid.StaggeredGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antrromet on 9/1/15 9:02 PM
 */
public class CampaignsFragment extends BaseFragment implements OnVolleyResponseListener,
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private List<Campaign> mCampaigns;
    private CampaignAdapter mCampaignAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaigns, container, false);


        // Request for campaigns
        // TODO : Add cache time mechanism
        setVolleyListener(this);
        requestCampaigns();

        // Load the local cached data
        getActivity().getSupportLoaderManager().restartLoader(Constants.Loaders.CAMPAIGNS.id,
                null, this);

        // Setting up the staggered listview
        StaggeredGridView mCampaignGridView = (StaggeredGridView) view.findViewById(R.id.campaign_grid);
        mCampaignAdapter = new CampaignAdapter(getActivity());
        mCampaignGridView.setAdapter(mCampaignAdapter);
        mCampaignGridView.setOnItemClickListener(this);
        return view;
    }

    /**
     * Request the campaigns
     */
    private void requestCampaigns() {
        requestVolley(Constants.VolleyTags.GET_ALL_CAMPAIGNS, Constants.Urls.GET_ALL_CAMPAIGNS.link,
                null, false);
    }

    @Override
    public void OnSuccess(Constants.VolleyTags tag, Object responseObject) {
        // Check necessary because the response might come after the app is destroyed and the
        // activity would be null in that case
        if (getActivity() != null) {
            insertCampaignsInDB((JSONArray) responseObject);
            mCampaignAdapter.setCampaigns(mCampaigns);
        }
    }

    /**
     * Insert campaigns in DB
     *
     * @param responseArray the response that we get from the server
     */
    private void insertCampaignsInDB(JSONArray responseArray) {
        {
            mCampaigns = new ArrayList<>(responseArray.length());
            ContentValues[] values = new ContentValues[responseArray.length()];
            for (int i = 0; i < responseArray.length(); i++) {
                Campaign campaign = new Campaign();
                ContentValues value = new ContentValues();
                JSONObject jsonObject = JSONUtils.optJSONObject(responseArray, i);
                value.put(DBOpenHelper.COLUMN_ID,
                        JSONUtils.optString(jsonObject, Constants.ParamsKeys._ID.key));
                value.put(DBOpenHelper.COLUMN_IMG, Constants.BASE_URL + JSONUtils.optString
                        (jsonObject, Constants.ParamsKeys.IMG.key));
                value.put(DBOpenHelper.COLUMN_MISSION, JSONUtils.optString
                        (jsonObject, Constants.ParamsKeys.MISSION.key));
                value.put(DBOpenHelper.COLUMN_NAME,
                        JSONUtils.optString(jsonObject, Constants.ParamsKeys.NAME.key));
                JSONObject ngoObject = JSONUtils.optJSONObject(jsonObject, Constants.ParamsKeys
                        .NGO.key);
                if (ngoObject != null) {
                    value.put(DBOpenHelper.COLUMN_NGO_ID,
                            JSONUtils.optString(ngoObject, Constants.ParamsKeys._ID.key));
                    value.put(DBOpenHelper.COLUMN_NGO_NAME,
                            JSONUtils.optString(ngoObject, Constants.ParamsKeys.NAME.key));
                    value.put(DBOpenHelper.COLUMN_NGO_SHORT_DESC,
                            JSONUtils.optString(ngoObject, Constants.ParamsKeys.SHORT_DESC.key));
                }
                value.put(DBOpenHelper.COLUMN_SHORT_DESC,
                        JSONUtils.optString(jsonObject, Constants.ParamsKeys.SHORT_DESC.key));
                value.put(DBOpenHelper.COLUMN_URL, Constants.BASE_URL +
                        JSONUtils.optString(jsonObject, Constants.ParamsKeys.URL.key));
                values[i] = value;

                setCampaignValues(campaign, value);
                mCampaigns.add(campaign);
            }
            getActivity().getContentResolver().bulkInsert(DBProvider.URI_CAMPAIGNS,
                    values);
        }
    }

    /**
     * Fill in the data to the campaign object, so that the loader need not be restarted again to
     * reload the updated data from the API
     *
     * @param campaign empty campaign object
     * @param value    ContentValues from which the data is to be filled in
     */
    private void setCampaignValues(Campaign campaign, ContentValues value) {
        campaign.setId(value.getAsString(DBOpenHelper.COLUMN_ID));
        campaign.setImg(value.getAsString(DBOpenHelper.COLUMN_IMG));
        campaign.setMission(value.getAsString(DBOpenHelper.COLUMN_MISSION));
        campaign.setName(value.getAsString(DBOpenHelper.COLUMN_NAME));
        campaign.setNgoId(value.getAsString(DBOpenHelper.COLUMN_NGO_ID));
        campaign.setNgoName(value.getAsString(DBOpenHelper.COLUMN_NGO_NAME));
        campaign.setNgoShortDesc(value.getAsString(DBOpenHelper.COLUMN_NGO_SHORT_DESC));
        campaign.setShortDesc(value.getAsString(DBOpenHelper.COLUMN_SHORT_DESC));
        campaign.setUrl(value.getAsString(DBOpenHelper.COLUMN_URL));
    }

    @Override
    public void OnError(Constants.VolleyTags tag, VolleyError error) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), DBProvider.URI_CAMPAIGNS,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == Constants.Loaders.CAMPAIGNS.id) {
            if (data != null) {
                if (data.moveToFirst()) {
                    mCampaigns = new ArrayList<>(data.getCount());
                    do {
                        Campaign campaign = new Campaign();
                        campaign.setId(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_ID)));
                        campaign.setImg(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_IMG)));
                        campaign.setName(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_NAME)));
                        campaign.setNgoId(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_NGO_ID)));
                        campaign.setNgoName(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_NGO_NAME)));
                        campaign.setNgoShortDesc(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_NGO_SHORT_DESC)));
                        campaign.setShortDesc(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_SHORT_DESC)));
                        campaign.setUrl(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_URL)));
                        mCampaigns.add(campaign);
                    } while (data.moveToNext());
                }
            }
            mCampaignAdapter.setCampaigns(mCampaigns);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), CampaignDetailActivity.class).putExtra(Constants
                .ParamsKeys._ID.key, mCampaigns.get(position).getId()).putExtra(Constants
                .ParamsKeys.NAME.key, mCampaigns.get(position).getName()).putExtra(Constants
                .ParamsKeys.IMG.key, mCampaigns.get(position).getImg()));
    }
}
