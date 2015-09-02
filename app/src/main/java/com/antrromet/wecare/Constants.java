package com.antrromet.wecare;

public class Constants {

    // App preferences stored in this file
    public static final String APP_PREFERENCES = "app_preferences";

    /**
     * Enum for holding the tags for each Volley Request
     */
    public enum VolleyTags {

        NINE_GAG_FEEDS("nine_gag_feeds");

        public final String tag;

        VolleyTags(String tag) {
            this.tag = tag;
        }
    }

    /**
     * Enum for holding the tags for each Volley Request
     */
    public enum Urls {

        NINE_GAG_FEEDS("http://infinigag.eu01.aws.af.cm/hot/%s");

        public final String link;

        Urls(String link) {
            this.link = link;
        }
    }

    /**
     * Enum for holding the shared preference keys
     */
    public enum SharedPreferenceKeys {

        NINE_GAG_NEXT_PAGE_ID("nine_gag_next_page_id");

        public final String key;

        SharedPreferenceKeys(final String key) {
            this.key = key;
        }

    }

}
