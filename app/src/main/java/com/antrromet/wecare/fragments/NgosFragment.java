package com.antrromet.wecare.fragments;

/**
 * Created by Antrromet on 9/14/15 12:35 AM
 */

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
import com.antrromet.wecare.adapters.NgoAdapter;
import com.antrromet.wecare.interfaces.OnVolleyResponseListener;
import com.antrromet.wecare.models.Ngo;
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
public class NgosFragment extends BaseFragment implements OnVolleyResponseListener,
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private List<Ngo> mNgos;
    private NgoAdapter mNgoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ngos, container, false);


        // Request for ngos
        // TODO : Add cache time mechanism
        setVolleyListener(this);
        requestNGOs();

        // Load the local cached data
        getActivity().getSupportLoaderManager().restartLoader(Constants.Loaders.NGOS.id,
                null, this);

        // Setting up the staggered listview
        StaggeredGridView mNgosGridView = (StaggeredGridView) view.findViewById(R.id.ngos_grid);
        mNgoAdapter = new NgoAdapter(getActivity());
        mNgosGridView.setAdapter(mNgoAdapter);
        mNgosGridView.setOnItemClickListener(this);
        return view;
    }

    /**
     * Request the ngos
     */
    private void requestNGOs() {
        requestVolley(Constants.VolleyTags.GET_ALL_NGOS, Constants.Urls.GET_ALL_NGOS.link,
                null, false);
    }

    @Override
    public void OnSuccess(Constants.VolleyTags tag, Object responseObject) {
        // Check necessary because the response might come after the app is destroyed and the
        // activity would be null in that case
        if (getActivity() != null) {
            insertNgosInDB((JSONArray) responseObject);
            mNgoAdapter.setNgos(mNgos);
        }
    }

    /**
     * Insert ngos in DB
     *
     * @param responseArray the response that we get from the server
     */
    private void insertNgosInDB(JSONArray responseArray) {
        {
            mNgos = new ArrayList<>(responseArray.length());
            ContentValues[] values = new ContentValues[responseArray.length()];
            for (int i = 0; i < responseArray.length(); i++) {
                Ngo ngo = new Ngo();
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
                JSONArray campaignArray = JSONUtils.optJSONArray(jsonObject, Constants.ParamsKeys
                        .CAMPAIGNS.key);
                if (campaignArray != null) {
                    value.put(DBOpenHelper.COLUMN_CAMPAIGN_COUNT,
                            campaignArray.length());
                }
                value.put(DBOpenHelper.COLUMN_SHORT_DESC,
                        JSONUtils.optString(jsonObject, Constants.ParamsKeys.SHORT_DESC.key));
                values[i] = value;

                setNgoValues(ngo, value);
                mNgos.add(ngo);
            }
            getActivity().getContentResolver().bulkInsert(DBProvider.URI_NGOS,
                    values);
        }
    }

    /**
     * Fill in the data to the ngo object, so that the loader need not be restarted again to
     * reload the updated data from the API
     *
     * @param ngo empty campaign object
     * @param value    ContentValues from which the data is to be filled in
     */
    private void setNgoValues(Ngo ngo, ContentValues value) {
        ngo.setId(value.getAsString(DBOpenHelper.COLUMN_ID));
        ngo.setImg(value.getAsString(DBOpenHelper.COLUMN_IMG));
        ngo.setMission(value.getAsString(DBOpenHelper.COLUMN_MISSION));
        ngo.setName(value.getAsString(DBOpenHelper.COLUMN_NAME));
        ngo.setShortDesc(value.getAsString(DBOpenHelper.COLUMN_SHORT_DESC));
        ngo.setCampaignCount(value.getAsInteger(DBOpenHelper.COLUMN_CAMPAIGN_COUNT));
    }

    @Override
    public void OnError(Constants.VolleyTags tag, VolleyError error) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), DBProvider.URI_NGOS,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == Constants.Loaders.NGOS.id) {
            if (data != null) {
                if (data.moveToFirst()) {
                    mNgos = new ArrayList<>(data.getCount());
                    do {
                        Ngo ngo = new Ngo();
                        ngo.setId(data.getString(data.getColumnIndex(DBOpenHelper.COLUMN_ID)));
                        ngo.setImg(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_IMG)));
                        ngo.setName(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_NAME)));
                        ngo.setShortDesc(data.getString(data.getColumnIndex(DBOpenHelper
                                .COLUMN_SHORT_DESC)));
                        ngo.setCampaignCount(data.getInt(data.getColumnIndex(DBOpenHelper
                                .COLUMN_CAMPAIGN_COUNT)));
                        mNgos.add(ngo);
                    } while (data.moveToNext());
                }
            }
            mNgoAdapter.setNgos(mNgos);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getActivity(), CampaignDetailActivity.class).putExtra(Constants
                .ParamsKeys._ID.key, mNgos.get(position).getId()).putExtra(Constants
                .ParamsKeys.NAME.key, mNgos.get(position).getName()).putExtra(Constants
                .ParamsKeys.IMG.key, mNgos.get(position).getImg()));
    }
}
