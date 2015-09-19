package com.antrromet.wecare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.antrromet.wecare.adapters.CampaignsAdapter;
import com.antrromet.wecare.adapters.NgoDetailsAdapter;
import com.antrromet.wecare.interfaces.OnVolleyResponseListener;
import com.antrromet.wecare.models.Campaign;
import com.antrromet.wecare.models.Contact;
import com.antrromet.wecare.models.NgoDetail;
import com.antrromet.wecare.provider.DBOpenHelper;
import com.antrromet.wecare.provider.DBProvider;
import com.antrromet.wecare.utils.JSONUtils;
import com.antrromet.wecare.utils.Logger;
import com.antrromet.wecare.widgets.MyLinearLayoutManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NgoDetailActivity extends BaseActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, OnVolleyResponseListener, OnClickListener, NgoDetailsAdapter
        .OnWebLinkClickListener, CampaignsAdapter.OnCampaignClickListener {

    private String mNgoId;
    private NgoDetail mNgo;
    private ImageLoader mImageLoader;
    private NgoDetailsAdapter mNgoDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_detail);

        mNgoId = getIntent().getStringExtra(Constants.ParamsKeys._ID.key);
        // Precaution : Just in case the id is null
        if (TextUtils.isEmpty(mNgoId)) {
            showToast(getString(R.string.unknown_problem));
            finish();
        }

        mImageLoader = ImageLoader.getInstance();

        setAppBarLayoutHeight();

        // Setup the toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.actionbar_layout);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbar.setTitle(getIntent().getStringExtra(Constants.ParamsKeys.NAME.key));
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color
                .transparent));

        // Load the local cached data
        getSupportLoaderManager().restartLoader(Constants.Loaders.NGO_DETAILS.id,
                null, this);

        // Request for the latest campaign details
        setVolleyListener(this);
        requestNgoDetail();

        mNgoDetailsAdapter = new NgoDetailsAdapter(this);
        mNgoDetailsAdapter.setOnWebLinkClickListener(this);
        mNgoDetailsAdapter.setOnCampaignClickListener(this);
        RecyclerView ngoRecyclerView = (RecyclerView) findViewById(R.id
                .ngo_detail_recycler_view);
        ngoRecyclerView.setHasFixedSize(true);
        MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        ngoRecyclerView.setLayoutManager(mLayoutManager);
        ngoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ngoRecyclerView.setAdapter(mNgoDetailsAdapter);
    }

    private void setAppBarLayoutHeight() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.height = size.x;
        appBarLayout.setLayoutParams(params);
    }

    /**
     * Request ngo details
     */
    private void requestNgoDetail() {
        requestVolley(Constants.VolleyTags.GET_NGO_DETAILS, String.format(Constants.Urls
                .GET_NGO_DETAILS.link, mNgoId), Request.Method.GET, null, null, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == Constants.Loaders.NGO_DETAILS.id) {
            return new CursorLoader(this, DBProvider.URI_NGO_DETAILS,
                    null, DBOpenHelper.COLUMN_ID + " = ?", new String[]{mNgoId}, null);
        } else if (id == Constants.Loaders.CAMPAIGNS.id) {
            return new CursorLoader(this, DBProvider.URI_CAMPAIGNS,
                    null, DBOpenHelper.COLUMN_NGO_ID + " = ?", new String[]{mNgoId},
                    null);
        } else if (id == Constants.Loaders.CONTACTS.id) {
            return new CursorLoader(this, DBProvider.URI_CONTACTS,
                    null, DBOpenHelper.COLUMN_ID + " = ?", new String[]{mNgoId},
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == Constants.Loaders.NGO_DETAILS.id) {
            if (data != null && data.moveToFirst()) {
                mNgo = new NgoDetail();
                mNgo.setId(mNgoId);
                mNgo.setAbout(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_ABOUT)));
                getSupportLoaderManager().restartLoader(Constants.Loaders.CAMPAIGNS.id,
                        null, this);
                mNgo.setImg(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_IMG)));
                mNgo.setMission(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_MISSION)));
                mNgo.setName(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
                mNgo.setShortDesc(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_SHORT_DESC)));
                mNgo.setJoined(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_JOINED)));
                mNgo.setFounder(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_FOUNDER)));
                mNgo.setUrl(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_URL)));
                Logger.d(TAG, "Loaded ngo object");
            }
        } else if (loader.getId() == Constants.Loaders.CAMPAIGNS.id) {
            if (data != null && data.moveToFirst()) {
                List<Campaign> campaigns = new ArrayList<>(data.getCount());
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
                            .COLUMN_NAME)));
                    campaign.setShortDesc(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_SHORT_DESC)));
                    campaign.setUrl(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_URL)));
                    campaigns.add(campaign);
                } while (data.moveToNext());
                mNgo.setCampaigns(campaigns);
            }
            Logger.d(TAG, "Loaded campaigns");
            getSupportLoaderManager().restartLoader(Constants.Loaders.CONTACTS.id,
                    null, this);
        } else if (loader.getId() == Constants.Loaders.CONTACTS.id) {
            if (data != null && data.moveToFirst()) {
                Contact contact = new Contact();
                contact.setId(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_ID)));
                contact.setWebsite(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_WEBSITE)));
                contact.setEmail(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_EMAIL)));
                contact.setTwitterLink(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_TWITTER_LINK)));
                contact.setFbLink(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_FB_LINK)));
                mNgo.setContact(contact);
            }
            Logger.d(TAG, "Loaded contacts");
            if (mNgo != null) {
                setDataToViews();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void OnSuccess(Constants.VolleyTags tag, Object responseObject) {
        insertNgoDetailsInDB((JSONObject) responseObject);
        setDataToViews();
    }

    /**
     * Set the data to the views
     */
    private void setDataToViews() {
        mImageLoader.displayImage(mNgo.getImg(), (ImageView) findViewById(R.id
                .poster_image));
        mNgoDetailsAdapter.setData(mNgo);
        findViewById(R.id.fab).setOnClickListener(this);
    }

    /**
     * Insert the campaign details in the DB
     *
     * @param responseObject response from the Server
     */
    private void insertNgoDetailsInDB(JSONObject responseObject) {
        mNgo = new NgoDetail();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_ID, JSONUtils.optString(responseObject, Constants.ParamsKeys
                ._ID.key));
        values.put(DBOpenHelper.COLUMN_ABOUT, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.ABOUT.key));
        values.put(DBOpenHelper.COLUMN_NAME, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.NAME.key));
        values.put(DBOpenHelper.COLUMN_SHORT_DESC, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.SHORT_DESC.key));
        JSONArray campaignsJsonArray = JSONUtils.optJSONArray(responseObject, Constants
                .ParamsKeys.CAMPAIGNS.key);
        if (campaignsJsonArray != null) {
            ArrayList<Campaign> mCampaigns = new ArrayList<>(campaignsJsonArray.length());
            for (int i = 0; i < campaignsJsonArray.length(); i++) {
                Campaign campaign = new Campaign();
                ContentValues campaignValues = new ContentValues();
                JSONObject campaignJsonObject = JSONUtils.optJSONObject(campaignsJsonArray, i);
                if (campaignJsonObject != null) {
                    campaignValues.put(DBOpenHelper.COLUMN_ID, JSONUtils.optString
                            (campaignJsonObject, Constants.ParamsKeys._ID.key));
                    campaignValues.put(DBOpenHelper.COLUMN_IMG, Constants.BASE_URL + JSONUtils
                            .optString(campaignJsonObject, Constants.ParamsKeys.IMG.key));
                    campaignValues.put(DBOpenHelper.COLUMN_MISSION, Constants.BASE_URL + JSONUtils
                            .optString(campaignJsonObject, Constants.ParamsKeys.MISSION.key));
                    campaignValues.put(DBOpenHelper.COLUMN_NAME, JSONUtils.optString
                            (campaignJsonObject, Constants.ParamsKeys.NAME.key));
                    campaignValues.put(DBOpenHelper.COLUMN_SHORT_DESC, JSONUtils.optString
                            (campaignJsonObject, Constants.ParamsKeys.SHORT_DESC.key));
                    campaignValues.put(DBOpenHelper.COLUMN_URL, JSONUtils.optString
                            (campaignJsonObject, Constants.ParamsKeys.URL.key));
                    campaignValues.put(DBOpenHelper.COLUMN_NGO_ID, values.getAsString(DBOpenHelper.COLUMN_ID));
                    campaignValues.put(DBOpenHelper.COLUMN_NGO_NAME, values.getAsString
                            (DBOpenHelper.COLUMN_NAME));
                    campaignValues.put(DBOpenHelper.COLUMN_NGO_SHORT_DESC, values.getAsString
                            (DBOpenHelper.COLUMN_SHORT_DESC));
                    setCampaignValues(campaign, campaignValues);
                    mCampaigns.add(campaign);
                    getContentResolver().insert(DBProvider.URI_CAMPAIGNS, campaignValues);
                }
            }
            mNgo.setCampaigns(mCampaigns);
        }

        JSONObject contactObject = JSONUtils.optJSONObject(responseObject, Constants.ParamsKeys
                .CONTACT.key);
        if (contactObject != null) {
            Contact contact = new Contact();
            ContentValues contactValues = new ContentValues();
            contactValues.put(DBOpenHelper.COLUMN_ID, values.getAsString(DBOpenHelper
                    .COLUMN_ID));
            contactValues.put(DBOpenHelper.COLUMN_WEBSITE, JSONUtils.optString(contactObject,
                    Constants.ParamsKeys.WEBSITE.key));
            contactValues.put(DBOpenHelper.COLUMN_EMAIL, JSONUtils.optString(contactObject,
                    Constants.ParamsKeys.EMAIL.key));
            contactValues.put(DBOpenHelper.COLUMN_FB_LINK, JSONUtils.optString(contactObject,
                    Constants.ParamsKeys.FB_LINK.key));
            contactValues.put(DBOpenHelper.COLUMN_TWITTER_LINK, JSONUtils.optString(contactObject,
                    Constants.ParamsKeys.TW_LINK.key));
            getContentResolver().insert(DBProvider.URI_CONTACTS, contactValues);
            setContactValues(contact, contactValues);
            mNgo.setContact(contact);
        }
        values.put(DBOpenHelper.COLUMN_IMG, Constants.BASE_URL + JSONUtils.optString
                (responseObject, Constants.ParamsKeys.IMG.key));
        JSONObject metadataObject = JSONUtils.optJSONObject(responseObject, Constants.ParamsKeys
                .METADATA.key);
        if (metadataObject != null) {
            values.put(DBOpenHelper.COLUMN_JOINED, JSONUtils.optString(metadataObject, Constants
                    .ParamsKeys.JOINED.key));
            values.put(DBOpenHelper.COLUMN_FOUNDER, JSONUtils.optString(metadataObject, Constants
                    .ParamsKeys.FOUNDER.key));
        }
        values.put(DBOpenHelper.COLUMN_MISSION, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.MISSION.key));
        values.put(DBOpenHelper.COLUMN_URL, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.URL.key));
        getContentResolver().insert(DBProvider.URI_NGO_DETAILS, values);
        setNgoDetailsValues(values);
    }

    /**
     * Set the values to the ngo detail object of the activity
     *
     * @param values content values that contain the data for the
     */
    private void setNgoDetailsValues(ContentValues values) {
        mNgo.setId(values.getAsString(DBOpenHelper.COLUMN_ID));
        mNgo.setAbout(values.getAsString(DBOpenHelper.COLUMN_ABOUT));
        mNgo.setImg(values.getAsString(DBOpenHelper.COLUMN_IMG));
        mNgo.setFounder(values.getAsString(DBOpenHelper.COLUMN_FOUNDER));
        mNgo.setJoined(values.getAsString(DBOpenHelper.COLUMN_JOINED));
        mNgo.setMission(values.getAsString(DBOpenHelper.COLUMN_MISSION));
        mNgo.setName(values.getAsString(DBOpenHelper.COLUMN_NAME));
        mNgo.setShortDesc(values.getAsString(DBOpenHelper.COLUMN_DESC));
        mNgo.setUrl(values.getAsString(DBOpenHelper.COLUMN_URL));
    }

    /**
     * * Fill in the data to the contact object
     *
     * @param contact empty contact object with just the campaign id set
     * @param values  content values with the contact data
     */
    private void setContactValues(Contact contact, ContentValues values) {
        contact.setId(values.getAsString(DBOpenHelper.COLUMN_ID));
        contact.setWebsite(values.getAsString(DBOpenHelper.COLUMN_WEBSITE));
        contact.setEmail(values.getAsString(DBOpenHelper.COLUMN_EMAIL));
        contact.setFbLink(values.getAsString(DBOpenHelper.COLUMN_FB_LINK));
        contact.setTwitterLink(values.getAsString(DBOpenHelper.COLUMN_TWITTER_LINK));
    }


    /**
     * Fill in the data to the activity object
     *
     * @param campaign empty campaign object
     * @param values   content values with the activity data
     */
    private void setCampaignValues(Campaign campaign, ContentValues values) {
        campaign.setId(values.getAsString(DBOpenHelper.COLUMN_ID));
        campaign.setImg(values.getAsString(DBOpenHelper
                .COLUMN_IMG));
        campaign.setName(values.getAsString(DBOpenHelper
                .COLUMN_NAME));
        campaign.setNgoId(values.getAsString(DBOpenHelper
                .COLUMN_NGO_ID));
        campaign.setNgoName(values.getAsString(DBOpenHelper
                .COLUMN_NAME));
        campaign.setShortDesc(values.getAsString(DBOpenHelper
                .COLUMN_SHORT_DESC));
        campaign.setNgoId(values.getAsString(DBOpenHelper
                .COLUMN_NGO_ID));
        campaign.setNgoShortDesc(values.getAsString(DBOpenHelper
                .COLUMN_NGO_SHORT_DESC));
        campaign.setNgoName(values.getAsString(DBOpenHelper
                .COLUMN_NGO_NAME));
    }

    @Override
    public void OnError(Constants.VolleyTags tag, VolleyError error) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            shareNgo();
        }
    }

    private void shareNgo() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mNgo.getUrl());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    /**
     * Launching the webview activity in case of website
     *
     * @param url the url to be launched
     */
    private void launchWebViewActivity(String url) {
        startActivity(new Intent(this, WebViewActivity.class).putExtra(Constants.ParamsKeys.URL.key,
                url).putExtra(Constants.ParamsKeys.TITLE.key, mNgo.getName()));
    }

    @Override
    public void onWebLinkClick(View view, String link) {
        launchWebViewActivity(link);
    }

    @Override
    public void onCampaignClick(View view, String campaignId, String name, String img) {
        startActivity(new Intent(this, CampaignDetailActivity.class).putExtra(Constants
                .ParamsKeys._ID.key, campaignId).putExtra(Constants
                .ParamsKeys.NAME.key,name).putExtra(Constants
                .ParamsKeys.IMG.key, img));
    }
}
