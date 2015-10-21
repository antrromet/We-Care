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
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.antrromet.wecare.adapters.CampaignDetailsAdapter;
import com.antrromet.wecare.interfaces.OnVolleyResponseListener;
import com.antrromet.wecare.models.Activity;
import com.antrromet.wecare.models.CampaignDetail;
import com.antrromet.wecare.models.Contact;
import com.antrromet.wecare.provider.DBOpenHelper;
import com.antrromet.wecare.provider.DBProvider;
import com.antrromet.wecare.utils.JSONUtils;
import com.antrromet.wecare.widgets.MyLinearLayoutManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CampaignDetailActivity extends BaseActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, OnVolleyResponseListener, OnClickListener, CampaignDetailsAdapter.OnWebLinkClickListener, SwipeRefreshLayout.OnRefreshListener, CampaignDetailsAdapter.OnNgoClickListener {

    private String mCampaignId;
    private CampaignDetail mCampaign;
    private ImageLoader mImageLoader;
    private CampaignDetailsAdapter mCampaignDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_detail);

        mCampaignId = getIntent().getStringExtra(Constants.ParamsKeys._ID.key);
        // Precaution : Just in case the id is null
        if (TextUtils.isEmpty(mCampaignId)) {
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
        String title = getIntent().getStringExtra(Constants.ParamsKeys.NAME.key);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color
                .transparent));
        setScreenName(getString(R.string.campaigns) + " - " + title);
        // Load the local cached data
        getSupportLoaderManager().restartLoader(Constants.Loaders.CAMPAIGN_DETAILS.id,
                null, this);

        // Request for the latest campaign details
        setVolleyListener(this);
        requestCampaignDetail();

        mCampaignDetailsAdapter = new CampaignDetailsAdapter(this);
        mCampaignDetailsAdapter.setOnWebLinkClickListener(this);
        mCampaignDetailsAdapter.setOnNgoClickListener(this);
        RecyclerView campaignRecyclerView = (RecyclerView) findViewById(R.id
                .campaign_detail_recycler_view);
        campaignRecyclerView.setHasFixedSize(true);
        MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        campaignRecyclerView.setLayoutManager(mLayoutManager);
        campaignRecyclerView.setItemAnimator(new DefaultItemAnimator());
        campaignRecyclerView.setAdapter(mCampaignDetailsAdapter);
    }

    private void setAppBarLayoutHeight() {
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        params.height = size.x;
        appBarLayout.setLayoutParams(params);
    }

    /**
     * Request campaign details
     */
    private void requestCampaignDetail() {
        requestVolley(Constants.VolleyTags.GET_CAMPAIGN_DETAILS, String.format(Constants.Urls
                .GET_CAMPAIGN_DETAILS.link, mCampaignId), Request.Method.GET, null, null, false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == Constants.Loaders.CAMPAIGN_DETAILS.id) {
            return new CursorLoader(this, DBProvider.URI_CAMPAIGN_DETAILS,
                    null, DBOpenHelper.COLUMN_ID + " = ?", new String[]{mCampaignId}, null);
        } else if (id == Constants.Loaders.ACTIVITIES.id) {
            return new CursorLoader(this, DBProvider.URI_ACTIVITIES,
                    null, DBOpenHelper.COLUMN_CAMPAIGN_ID + " = ?", new String[]{mCampaignId},
                    null);
        } else if (id == Constants.Loaders.CONTACTS.id) {
            return new CursorLoader(this, DBProvider.URI_CONTACTS,
                    null, DBOpenHelper.COLUMN_ID + " = ?", new String[]{mCampaignId},
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == Constants.Loaders.CAMPAIGN_DETAILS.id) {
            if (data != null && data.moveToFirst()) {
                mCampaign = new CampaignDetail();
                mCampaign.setId(mCampaignId);
                mCampaign.setAbout(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_ABOUT)));
                getSupportLoaderManager().restartLoader(Constants.Loaders.ACTIVITIES.id,
                        null, this);
                mCampaign.setImg(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_IMG)));
                mCampaign.setStartsOn(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_STARTS_ON)));
                mCampaign.setEndsOn(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_ENDS_ON)));
                mCampaign.setMission(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_MISSION)));
                mCampaign.setName(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_NAME)));
                mCampaign.setNgoId(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_NGO_ID)));
                mCampaign.setNgoName(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_NGO_NAME)));
                mCampaign.setNgoShortDesc(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_NGO_SHORT_DESC)));
                mCampaign.setProgress(data.getInt(data.getColumnIndex(DBOpenHelper.COLUMN_PROGRESS)));
                mCampaign.setShortDesc(data.getString(data.getColumnIndex(DBOpenHelper
                        .COLUMN_SHORT_DESC)));
                mCampaign.setSubTitle(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_SUB_TITLE)));
                mCampaign.setUrl(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_URL)));
            }
        } else if (loader.getId() == Constants.Loaders.ACTIVITIES.id) {
            if (data != null && data.moveToFirst()) {
                List<Activity> activities = new ArrayList<>(data.getCount());
                do {
                    Activity activity = new Activity();
                    activity.setCampaignId(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_CAMPAIGN_ID)));
                    activity.setId(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_ID)));
                    activity.setDesc(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_DESC)));
                    activity.setTitle(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_TITLE)));
                    activity.setImg(data.getString(data.getColumnIndex(DBOpenHelper
                            .COLUMN_IMG)));
                    activities.add(activity);
                } while (data.moveToNext());
                mCampaign.setActivities(activities);
            }
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
                mCampaign.setContact(contact);
            }
            if (mCampaign != null) {
                setDataToViews();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void OnSuccess(Constants.VolleyTags tag, Object responseObject) {
        insertCampaignDetailsInDB((JSONObject) responseObject);
        setDataToViews();
    }

    /**
     * Set the data to the views
     */
    private void setDataToViews() {
        mImageLoader.displayImage(mCampaign.getImg(), (ImageView) findViewById(R.id
                .poster_image));
        mCampaignDetailsAdapter.setData(mCampaign);
        findViewById(R.id.fab).setOnClickListener(this);
    }

    /**
     * Insert the campaign details in the DB
     *
     * @param responseObject response from the Server
     */
    private void insertCampaignDetailsInDB(JSONObject responseObject) {
        mCampaign = new CampaignDetail();
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_ID, JSONUtils.optString(responseObject, Constants.ParamsKeys
                ._ID.key));
        values.put(DBOpenHelper.COLUMN_ABOUT, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.ABOUT.key));
        JSONArray activitiesJsonArray = JSONUtils.optJSONArray(responseObject, Constants
                .ParamsKeys.ACTIVITIES.key);
        if (activitiesJsonArray != null) {
            ArrayList<Activity> mCampaignActivities = new ArrayList<>(activitiesJsonArray.length());
            for (int i = 0; i < activitiesJsonArray.length(); i++) {
                Activity activity = new Activity();
                activity.setCampaignId(values.getAsString(DBOpenHelper.COLUMN_ID));
                ContentValues activityValues = new ContentValues();
                JSONObject activityJsonObject = JSONUtils.optJSONObject(activitiesJsonArray, i);
                if (activityJsonObject != null) {
                    activityValues.put(DBOpenHelper.COLUMN_CAMPAIGN_ID, values.getAsString
                            (DBOpenHelper.COLUMN_ID));
                    activityValues.put(DBOpenHelper.COLUMN_ID, JSONUtils.optString
                            (activityJsonObject, Constants.ParamsKeys._ID.key));
                    activityValues.put(DBOpenHelper.COLUMN_DESC, JSONUtils.optString
                            (activityJsonObject, Constants.ParamsKeys.DESC.key));
                    activityValues.put(DBOpenHelper.COLUMN_IMG, Constants.BASE_URL + JSONUtils
                            .optString(activityJsonObject, Constants.ParamsKeys.IMG.key));
                    activityValues.put(DBOpenHelper.COLUMN_TITLE, JSONUtils.optString
                            (activityJsonObject, Constants.ParamsKeys.TITLE.key));
                    setActivityValues(activity, activityValues);
                    mCampaignActivities.add(activity);
                    getContentResolver().delete(DBProvider.URI_ACTIVITIES, DBOpenHelper
                            .COLUMN_CAMPAIGN_ID + " = ?", new String[]{values.getAsString
                            (DBOpenHelper.COLUMN_ID)});
                    getContentResolver().insert(DBProvider.URI_ACTIVITIES, activityValues);
                }
            }
            mCampaign.setActivities(mCampaignActivities);
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
            mCampaign.setContact(contact);
        }
        values.put(DBOpenHelper.COLUMN_IMG, Constants.BASE_URL + JSONUtils.optString
                (responseObject, Constants.ParamsKeys.IMG.key));
        JSONObject metadataObject = JSONUtils.optJSONObject(responseObject, Constants.ParamsKeys
                .METADATA.key);
        if (metadataObject != null) {
            values.put(DBOpenHelper.COLUMN_STARTS_ON, JSONUtils.optString(metadataObject, Constants
                    .ParamsKeys.STARTS_ON.key));
            values.put(DBOpenHelper.COLUMN_ENDS_ON, JSONUtils.optString(metadataObject, Constants
                    .ParamsKeys.ENDS_ON.key));
        }
        values.put(DBOpenHelper.COLUMN_MISSION, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.MISSION.key));
        values.put(DBOpenHelper.COLUMN_NAME, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.NAME.key));

        JSONObject ngoObject = JSONUtils.optJSONObject(responseObject, Constants.ParamsKeys.NGO
                .key);
        if (ngoObject != null) {
            values.put(DBOpenHelper.COLUMN_NGO_ID, JSONUtils.optString(ngoObject, Constants
                    .ParamsKeys._ID.key));
            values.put(DBOpenHelper.COLUMN_NGO_NAME, JSONUtils.optString(ngoObject, Constants
                    .ParamsKeys.NAME.key));
            values.put(DBOpenHelper.COLUMN_NGO_SHORT_DESC, JSONUtils.optString(ngoObject, Constants
                    .ParamsKeys.SHORT_DESC.key));
        }

        values.put(DBOpenHelper.COLUMN_PROGRESS, JSONUtils.optInt(responseObject, Constants
                .ParamsKeys.PROGRESS.key));
        values.put(DBOpenHelper.COLUMN_SHORT_DESC, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.SHORT_DESC.key));
        values.put(DBOpenHelper.COLUMN_SUB_TITLE, JSONUtils.optString(responseObject, Constants
                .ParamsKeys.SUB_TITLE.key));
        values.put(DBOpenHelper.COLUMN_URL, Constants.BASE_URL + JSONUtils.optString(responseObject,
                Constants.ParamsKeys.URL.key));
        getContentResolver().insert(DBProvider.URI_CAMPAIGN_DETAILS, values);
        setCampaignDetailsValues(values);
    }

    /**
     * Set the values to the campgin detail object of the activity
     *
     * @param values content values that contain the data for the
     */
    private void setCampaignDetailsValues(ContentValues values) {
        mCampaign.setId(values.getAsString(DBOpenHelper.COLUMN_ID));
        mCampaign.setAbout(values.getAsString(DBOpenHelper.COLUMN_ABOUT));
        mCampaign.setImg(values.getAsString(DBOpenHelper.COLUMN_IMG));
        mCampaign.setStartsOn(values.getAsString(DBOpenHelper.COLUMN_STARTS_ON));
        mCampaign.setEndsOn(values.getAsString(DBOpenHelper.COLUMN_ENDS_ON));
        mCampaign.setMission(values.getAsString(DBOpenHelper.COLUMN_MISSION));
        mCampaign.setName(values.getAsString(DBOpenHelper.COLUMN_NAME));
        mCampaign.setNgoId(values.getAsString(DBOpenHelper.COLUMN_NGO_ID));
        mCampaign.setNgoName(values.getAsString(DBOpenHelper.COLUMN_NGO_NAME));
        mCampaign.setNgoShortDesc(values.getAsString(DBOpenHelper.COLUMN_NGO_SHORT_DESC));
        mCampaign.setProgress(values.getAsInteger(DBOpenHelper.COLUMN_PROGRESS));
        mCampaign.setShortDesc(values.getAsString(DBOpenHelper.COLUMN_DESC));
        mCampaign.setSubTitle(values.getAsString(DBOpenHelper.COLUMN_SUB_TITLE));
        mCampaign.setUrl(values.getAsString(DBOpenHelper.COLUMN_URL));
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
     * @param activity empty activity object
     * @param values   content values with the activity data
     */
    private void setActivityValues(Activity activity, ContentValues values) {
        activity.setCampaignId(values.getAsString(DBOpenHelper.COLUMN_CAMPAIGN_ID));
        activity.setId(values.getAsString(DBOpenHelper.COLUMN_ID));
        activity.setDesc(values.getAsString(DBOpenHelper.COLUMN_DESC));
        activity.setImg(values.getAsString(DBOpenHelper.COLUMN_IMG));
        activity.setTitle(values.getAsString(DBOpenHelper.COLUMN_TITLE));
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
            shareCampaign();
        }
    }

    private void shareCampaign() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mCampaign.getUrl());
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
                url).putExtra(Constants.ParamsKeys.TITLE.key, mCampaign.getName()));
    }

    @Override
    public void onWebLinkClick(View view, String link) {
        launchWebViewActivity(link);
    }

    @Override
    public void onRefresh() {
        requestCampaignDetail();
    }

    @Override
    public void onNgoClick(View view, String id, String name) {
        startActivity(new Intent(this, NgoDetailActivity.class).putExtra(Constants
                .ParamsKeys._ID.key, id).putExtra(Constants
                .ParamsKeys.NAME.key, name).putExtra(Constants
                .ParamsKeys.IMG.key, ""));
    }
}
