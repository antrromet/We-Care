package com.antrromet.wecare;

public class Constants {

    // App preferences stored in this file
    public static final String APP_PREFERENCES = "app_preferences";
    public static final String BASE_URL = "http://campaignindia-funlab.rhcloud.com";

    /**
     * Enum for holding the tags for each Volley Request
     */
    public enum VolleyTags {

        GET_ALL_CAMPAIGNS("get_all_campaigns"), GET_CAMPAIGN_DETAILS("get_campaign_details");

        public final String tag;

        VolleyTags(String tag) {
            this.tag = tag;
        }
    }

    /**
     * Enum for holding the tags for each Volley Request
     */
    public enum Urls {

        GET_ALL_CAMPAIGNS(BASE_URL + "/get/campaigns"), GET_CAMPAIGN_DETAILS(BASE_URL +
                "/get/campaign/%s");

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

    /**
     * Enum for holding the Request tags keys
     *
     * @author Antrromet
     */
    public enum ParamsKeys {

        _ID("_id"), NAME("name"), NGO("ngo"), URL("url"), SHORT_DESC("shortDesc"), IMG("img"),
        MISSION("mission"), ABOUT("about"), ACTIVITIES("activities"), DESC("desc"), TITLE
                ("title"), CONTACT("contact"), WEBSITE("website"), EMAIL("email"), FB_LINK
                ("fb_link"), TW_LINK("tw_link"), METADATA("metadata"), STARTS_ON("startsOn"),
        ENDS_ON("endsOn"), PROGRESS("progress"), SUB_TITLE("subTitle");

        public final String key;

        ParamsKeys(final String key) {

            this.key = key;
        }
    }


    /**
     * Enum for managing all the Loaders
     */
    public enum Loaders {

        CAMPAIGNS(100), CAMPAIGN_DETAILS(101), ACTIVITIES(102), CONTACTS(103);

        public final int id;

        Loaders(final int id) {

            this.id = id;
        }
    }

}
