package com.antrromet.wecare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    private static final String TAG = Application.class.getSimpleName();

    private static RequestQueue mRequestQueue;
    private static ArrayList<Constants.VolleyTags> mRequestTags;
    private Tracker mTracker;

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static <T> void addToRequestQueue(Request<T> req, Constants.VolleyTags tag) {
        // set the default tag if tag is empty
        req.setTag(tag);
        getRequestQueue().add(req);
        mRequestTags.add(tag);
    }

    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static boolean isQueueEmpty() {
        return mRequestTags.size() == 0;
    }

    public static void removeFromQueue(Constants.VolleyTags tag) {
        mRequestTags.remove(tag);
    }

    public static boolean containsRequest(Constants.VolleyTags tag) {
        return mRequestTags.contains(tag);
    }

    public static void initImageLoader(Context context) {

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();


        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheSize(calculateAvailableCacheSize());
        config.defaultDisplayImageOptions(defaultOptions);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private
    @SuppressLint("NewApi")
    static int calculateAvailableCacheSize() {
        int size = 0;
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            int sdkInt = Build.VERSION.SDK_INT;
            long totalBytes;
            long availableBytes;
            if (sdkInt < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int blockSize = statFs.getBlockSize();
                availableBytes = ((long) statFs.getAvailableBlocks()) * blockSize;
                totalBytes = ((long) statFs.getBlockCount()) * blockSize;
            } else {
                availableBytes = statFs.getAvailableBytes();
                totalBytes = statFs.getTotalBytes();
            }
            // Target at least 50% of available or 25% of total space
            size = (int) Math.min(availableBytes * 0.50f, totalBytes *
                    0.25f);
        } catch (IllegalArgumentException ignored) {
            // ignored
        }
        return size;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestTags = new ArrayList<>();
        initImageLoader(getApplicationContext());
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
