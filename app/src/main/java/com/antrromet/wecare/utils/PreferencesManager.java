package com.antrromet.wecare.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.antrromet.wecare.Constants;


/**
 * Class for managing Shared Preferences
 * 
 * @author Antrromet
 * 
 */
public class PreferencesManager {

	private PreferencesManager() {
		// Nothing to do, adding since the utility classes should not have a
		// public constructor
	}

	/**
	 * Set String value for a particular key. Convert non-Strings to appropriate
	 * Strings before storing
	 *
	 * @param c Context
	 * @param key SharedPreference Key
	 * @param value to be stored in the preferences
	 */
	public static void set(final Context c, final String file,
						   final Constants.SharedPreferenceKeys key, final String value) {

		final SharedPreferences preferences = c.getSharedPreferences(file,
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = preferences.edit();
		// If an empty string is passed then store null instead
		editor.putString(key.key, TextUtils.isEmpty(value) ? null : value);
		editor.commit();
	}

	public static String getString(final Context c, final String file,
								   final Constants.SharedPreferenceKeys key) {
		return getString(c, file, key, null);
	}

	/**
	 *
	 * @param c Context
	 * @param file Preference file stored in the Constants class
	 * @param key SharedPreference key
	 * @param defaultString Default string to return if the value is not found in the SharedPreference
	 * @return the String associated with the key in the SharedPreferences file
	 */
	public static String getString(final Context c, final String file,
			final Constants.SharedPreferenceKeys key, final String defaultString) {

		final SharedPreferences preferences = c.getSharedPreferences(file,
				Context.MODE_PRIVATE);
		return preferences.getString(key.key, defaultString);
	}


	/**
	 * @param c Context
	 * @param file Clear the preferences stored in this file
	 */
	public static void clearPreferences(final Context c, final String file) {

		final SharedPreferences preferences = c.getSharedPreferences(file,
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

}
