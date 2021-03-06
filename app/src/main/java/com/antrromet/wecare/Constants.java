package com.antrromet.wecare;

public class Constants {

    // App preferences stored in this file
    public static final String APP_PREFERENCES = "app_preferences";
    public static final String BASE_URL = "https://wecare-ghci.rhcloud.com";

    /**
     * Enum for holding the tags for each Volley Request
     */
    public enum VolleyTags {

        GET_ALL_CAMPAIGNS("get_all_campaigns"), GET_CAMPAIGN_DETAILS("get_campaign_details"),
        GET_ALL_NGOS("get_all_ngos"), GET_NGO_DETAILS("get_ngo_details");

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
                "/get/campaign/%s"), GET_ALL_NGOS(BASE_URL + "/get/ngo"),
        GET_NGO_DETAILS(BASE_URL + "/get/ngo/%s");

        public final String link;

        Urls(String link) {
            this.link = link;
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
        ENDS_ON("endsOn"), PROGRESS("progress"), SUB_TITLE("subTitle"), CAMPAIGNS("campaigns"),
        JOINED("joined"), FOUNDER("founder");

        public final String key;

        ParamsKeys(final String key) {

            this.key = key;
        }
    }


    /**
     * Enum for managing all the Loaders
     */
    public enum Loaders {

        CAMPAIGNS(100), CAMPAIGN_DETAILS(101), ACTIVITIES(102), CONTACTS(103),
        NGOS(104), NGO_DETAILS(105);

        public final int id;

        Loaders(final int id) {

            this.id = id;
        }
    }

    /**
     * Enum for holding the shared preference keys
     */
    public enum SharedPreferenceKeys {

        TEST("test");

        public final String key;

        SharedPreferenceKeys(final String key) {
            this.key = key;
        }

    }

}
