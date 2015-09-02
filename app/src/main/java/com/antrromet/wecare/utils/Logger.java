package com.antrromet.wecare.utils;

import android.text.TextUtils;

import com.antrromet.wecare.BuildConfig;


/**
 * Utility class to mask the Android Logging mechanism. Simply swap the DEBUG
 * parameter before compiling to block logs.
 *
 * @author Antrromet
 */
public class Logger {

    public static final boolean DEBUG = BuildConfig.DEBUG;

    private Logger() {
        // Nothing to do, adding since the utility classes should not have a
        // public constructor
    }

    public static void v(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.v(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable e) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.v(tag, message, e);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable e) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.w(tag, message, e);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable e) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.i(tag, message, e);
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {

            int maxLogSize = 2000;
            for (int i = 0; i <= message.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > message.length() ? message.length() : end;
                android.util.Log.d(tag, message.substring(start, end));
            }

        }
    }

    public static void d(String tag, String message, Throwable e) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.d(tag, message, e);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable e) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.e(tag, message, e);
        }
    }

    public static void wtf(String tag, String message) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.wtf(tag, message);
        }
    }

    public static void wtf(String tag, String message, Throwable e) {
        if (DEBUG && !TextUtils.isEmpty(message)) {
            android.util.Log.wtf(tag, message, e);
        }
    }
}
