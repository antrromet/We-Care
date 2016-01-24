package com.antrromet.wecare.provider;

import android.provider.BaseColumns;

/**
 * @author harshita.k
 */
public class WeCareContracts {
    public static final class NGO_ENTRY implements BaseColumns {
        public static final String TABLE_NAME = "ngos";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHORT_DESC = "shortDesc";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_MISSION = "mission";
        public static final String COLUMN_CAMPAIGN_COUNT = "campaign_count";
    }

    public static final class CAMPAIGN_ENTRY implements BaseColumns {
        public static final String TABLE_NAME = "campaigns";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHORT_DESC = "shortDesc";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_MISSION = "mission";
        public static final String COLUMN_CAMPAIGN_COUNT = "campaign_count";
        public static final String COLUMN_NGO_ID = "ngo_id";
        public static final String COLUMN_NGO_NAME = "ngo_name";
        public static final String COLUMN_NGO_SHORT_DESC = "ngo_short_desc";
        public static final String COLUMN_URL = "url";
    }

    public static final class CAMPAIGN_DETAIL_ENTRY implements BaseColumns {
        public static final String TABLE_NAME = "campaign_details";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ABOUT = "about";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_STARTS_ON = "starts_on";
        public static final String COLUMN_ENDS_ON = "ends_on";
        public static final String COLUMN_MISSION = "mission";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NGO_ID = "ngo_id";
        public static final String COLUMN_NGO_NAME = "ngo_name";
        public static final String COLUMN_NGO_SHORT_DESC = "ngo_short_desc";
        public static final String COLUMN_PROGRESS = "progress";
        public static final String COLUMN_SHORT_DESC = "shortDesc";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_SUB_TITLE = "sub_title";
    }

    public static final class NGO_DETAIL_ENTRY implements BaseColumns {
        public static final String TABLE_NAME = "ngo_details";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ABOUT = "about";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_JOINED = "joined";
        public static final String COLUMN_FOUNDER = "founder";
        public static final String COLUMN_MISSION = "mission";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SHORT_DESC = "shortDesc";
        public static final String COLUMN_URL = "url";

    }

    public static final class CONTACT_ENTRY implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_WEBSITE = "website";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FB_LINK = "fb_link";
        public static final String COLUMN_TWITTER_LINK = "twitter_link";
    }

    public static final class ACTIVITIY_ENTRY implements BaseColumns {
        public static final String TABLE_NAME = "activities";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DESC = "desc";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CAMPAIGN_ID = "campaign_id";
        public static final String COLUMN_IMG = "img";
    }
}
