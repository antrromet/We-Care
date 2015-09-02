package com.antrromet.wecare.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtils {

	/**
	 *
	 * @param jsonObject the object from which the JSONObject is to be fetched
	 * @param key key for the JSONObject
	 * @return value mapped by the given key
	 */
	public static JSONObject optJSONObject(JSONObject jsonObject, String key) {
		// http://code.google.com/p/android/issues/detail?id=13830
		if (jsonObject.isNull(key))
			return null;
		else
			return jsonObject.optJSONObject(key);
	}

	/**
	 *
	 * @param jsonArray the array from which the JSONObject is to be fetched
	 * @param pos position for the JSONObject
	 * @return value at the given position
	 */
	public static JSONObject optJSONObject(JSONArray jsonArray, int pos) {
		if (jsonArray.isNull(pos))
			return null;
		else
			return jsonArray.optJSONObject(pos);
	}

	/**
	 *
	 * @param jsonObject the object from which the JSONArray is to be fetched
	 * @param key key for the JSONArray
	 * @return value mapped by the given key
	 */
	public static JSONArray optJSONArray(JSONObject jsonObject, String key) {
		if (jsonObject.isNull(key))
			return null;
		else
			return jsonObject.optJSONArray(key);
	}

	/**
	 *
	 * @param jsonObject the object from which the String is to be fetched
	 * @param key key for the String
	 * @return value mapped by the given key, or {@code null} if not present or null.
	 */
	public static String optString(JSONObject jsonObject, String key) {
		if (jsonObject.isNull(key))
			return null;
		else
			return jsonObject.optString(key, null);
	}

	/**
	 *
	 * @param jsonObject the object from which the int is to be fetched
	 * @param key key for the int
	 * @return value mapped by the given key, or {@code -1} if not present or -1.
	 */
	public static int optInt(JSONObject jsonObject, String key) {
		if (jsonObject.isNull(key))
			return -1;
		else
			return jsonObject.optInt(key, -1);
	}

	/**
	 *
	 * @param jsonObject the object from which the long is to be fetched
	 * @param key key for the int
	 * @return value mapped by the given key, or {@code -1} if not present or -1.
	 */
	public static long optLong(JSONObject jsonObject, String key) {
		if (jsonObject.isNull(key))
			return -1;
		else
			return jsonObject.optLong(key, -1);
	}

}
