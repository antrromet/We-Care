package com.antrromet.wecare.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * DB Helper class that creates the SQLIte tables that will be used in this
 * application
 *
 * @author antriksh
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String CAMPAIGNS_TABLE_NAME = "campaigns";
    public static final String NGOS_TABLE_NAME = "ngos";
    public static final String CAMPAIGN_DETAILS_TABLE_NAME = "campaign_details";
    public static final String NGO_DETAILS_TABLE_NAME = "ngo_details";
    public static final String ACTIVITIES_TABLE_NAME = "activities";
    public static final String CONTACTS_TABLE_NAME = "contacts";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SHORT_DESC = "shortDesc";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_IMG = "img";
    public static final String COLUMN_NGO_ID = "ngo_id";
    public static final String COLUMN_NGO_NAME = "ngo_name";
    public static final String COLUMN_NGO_SHORT_DESC = "ngo_short_desc";
    public static final String COLUMN_ABOUT = "about";
    public static final String COLUMN_STARTS_ON = "starts_on";
    public static final String COLUMN_ENDS_ON = "ends_on";
    public static final String COLUMN_MISSION = "mission";
    public static final String COLUMN_PROGRESS = "progress";
    public static final String COLUMN_SUB_TITLE = "sub_title";
    public static final String COLUMN_CAMPAIGN_ID = "campaign_id";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FB_LINK = "fb_link";
    public static final String COLUMN_TWITTER_LINK = "twitter_link";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CAMPAIGN_COUNT = "campaign_count";
    public static final String COLUMN_JOINED = "joined";
    public static final String COLUMN_FOUNDER = "founder";

    private static final String TEXT = " text, ";
    private static final String INTEGER = " integer, ";

    private static final String CAMPAIGNS_TABLE_BUILDER = "create table " + CAMPAIGNS_TABLE_NAME +
            "(" + BaseColumns._ID + " integer primary key, " + COLUMN_ID + TEXT +
            COLUMN_IMG + TEXT + COLUMN_MISSION + TEXT + COLUMN_NAME + TEXT + COLUMN_NGO_ID + TEXT +
            COLUMN_NGO_NAME + TEXT + COLUMN_NGO_SHORT_DESC + TEXT +
            TEXT + COLUMN_SHORT_DESC + TEXT + COLUMN_URL + TEXT + "unique" +
            "(" + COLUMN_ID + ") on conflict " + "replace);";
    private static final String CAMPAIGNS_TABLE_DESTROYER = "drop table " + DBProvider.DB_NAME +
            "." + CAMPAIGNS_TABLE_NAME + ";";

    private static final String CAMPAIGN_DETAILS_TABLE_BUILDER = "create table " +
            CAMPAIGN_DETAILS_TABLE_NAME + "(" + BaseColumns._ID + " integer primary key, " +
            COLUMN_ID + TEXT + COLUMN_ABOUT + TEXT + COLUMN_IMG + TEXT +
            COLUMN_STARTS_ON + TEXT + COLUMN_ENDS_ON + TEXT + COLUMN_MISSION + TEXT +
            COLUMN_NAME + TEXT + COLUMN_NGO_ID + TEXT + COLUMN_NGO_NAME + TEXT +
            COLUMN_NGO_SHORT_DESC + TEXT + COLUMN_PROGRESS + INTEGER + COLUMN_SHORT_DESC + TEXT
            + COLUMN_URL + TEXT + COLUMN_SUB_TITLE + TEXT + "unique" + "(" + COLUMN_ID + ")" +
            " on conflict replace);";
    private static final String CAMPAIGN_DETAILS_TABLE_DESTROYER = "drop table " + DBProvider
            .DB_NAME + "." + CAMPAIGN_DETAILS_TABLE_NAME + ";";

    private static final String ACTIVITIES_TABLE_BUILDER = "create table " +
            ACTIVITIES_TABLE_NAME + "(" + BaseColumns._ID + " integer primary key, " +
            COLUMN_ID + TEXT + COLUMN_DESC + TEXT + COLUMN_TITLE + TEXT +
            COLUMN_CAMPAIGN_ID + INTEGER + COLUMN_IMG + TEXT + "unique" + "(" + COLUMN_ID + ")" +
            " on conflict replace);";
    private static final String ACTIVITIES_TABLE_DESTROYER = "drop table " + DBProvider
            .DB_NAME + "." + ACTIVITIES_TABLE_NAME + ";";

    private static final String CONTACTS_TABLE_BUILDER = "create table " +
            CONTACTS_TABLE_NAME + "(" + BaseColumns._ID + " integer primary key, " +
            COLUMN_ID + INTEGER + COLUMN_WEBSITE + TEXT + COLUMN_EMAIL + TEXT +
            COLUMN_FB_LINK + TEXT + COLUMN_TWITTER_LINK + TEXT + "unique" + "(" +
            COLUMN_ID + ")" + " on conflict replace);";
    private static final String CONTACTS_TABLE_DESTROYER = "drop table " + DBProvider
            .DB_NAME + "." + ACTIVITIES_TABLE_NAME + ";";

    private static final String NGOS_TABLE_BUILDER = "create table " + NGOS_TABLE_NAME +
            "(" + BaseColumns._ID + " integer primary key, " + COLUMN_ID + TEXT +
            COLUMN_IMG + TEXT + COLUMN_MISSION + TEXT + COLUMN_NAME + TEXT + COLUMN_SHORT_DESC +
            TEXT + COLUMN_CAMPAIGN_COUNT + INTEGER + "unique" +
            "(" + COLUMN_ID + ") on conflict " + "replace);";
    private static final String NGOS_TABLE_DESTROYER = "drop table " + DBProvider.DB_NAME +
            "." + NGOS_TABLE_NAME + ";";

    private static final String NGO_DETAILS_TABLE_BUILDER = "create table " +
            NGO_DETAILS_TABLE_NAME + "(" + BaseColumns._ID + " integer primary key, " +
            COLUMN_ID + TEXT + COLUMN_ABOUT + TEXT + COLUMN_IMG + TEXT +
            COLUMN_JOINED + TEXT + COLUMN_FOUNDER + TEXT + COLUMN_MISSION + TEXT +
            COLUMN_NAME + TEXT + COLUMN_SHORT_DESC + TEXT + COLUMN_URL + TEXT + "unique" + "(" +
            COLUMN_ID + ") on conflict replace);";
    private static final String NGO_DETAILS_TABLE_DESTROYER = "drop table " + DBProvider
            .DB_NAME + "." + CAMPAIGN_DETAILS_TABLE_NAME + ";";


    public DBOpenHelper(final Context context, final String name,
                        final CursorFactory factory, final int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CAMPAIGNS_TABLE_BUILDER);
        db.execSQL(CAMPAIGN_DETAILS_TABLE_BUILDER);
        db.execSQL(ACTIVITIES_TABLE_BUILDER);
        db.execSQL(CONTACTS_TABLE_BUILDER);
        db.execSQL(NGOS_TABLE_BUILDER);
        db.execSQL(NGO_DETAILS_TABLE_BUILDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CAMPAIGNS_TABLE_DESTROYER);
        db.execSQL(CAMPAIGN_DETAILS_TABLE_DESTROYER);
        db.execSQL(ACTIVITIES_TABLE_DESTROYER);
        db.execSQL(CONTACTS_TABLE_DESTROYER);
        db.execSQL(NGOS_TABLE_DESTROYER);
        db.execSQL(NGO_DETAILS_TABLE_DESTROYER);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
