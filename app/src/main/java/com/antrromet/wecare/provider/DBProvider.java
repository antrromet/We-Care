package com.antrromet.wecare.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class DBProvider extends ContentProvider {

    public static final String DB_NAME = "wecare.db";
    public static final int DB_VERSION = 1;
    private static final UriMatcher URI_MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    private static final String AUTHORITY = "com.antrromet.wecare.provider";
    private static final String URI_PREFIX = "content://com.antrromet.wecare.provider/";

    public static final Uri URI_CAMPAIGNS = Uri.parse(URI_PREFIX + DBOpenHelper
            .CAMPAIGNS_TABLE_NAME);
    public static final Uri URI_CAMPAIGN_DETAILS = Uri.parse(URI_PREFIX + DBOpenHelper
            .CAMPAIGN_DETAILS_TABLE_NAME);
    public static final Uri URI_ACTIVITIES = Uri.parse(URI_PREFIX + DBOpenHelper
            .ACTIVITIES_TABLE_NAME);
    public static final Uri URI_CONTACTS = Uri.parse(URI_PREFIX + DBOpenHelper
            .CONTACTS_TABLE_NAME);
    public static final Uri URI_NGOS = Uri.parse(URI_PREFIX + DBOpenHelper
            .NGOS_TABLE_NAME);

    private static final int CONTENT_CAMPAIGNS = 101;
    private static final int CONTENT_CAMPAIGN_DETAILS = 102;
    private static final int CONTENT_ACTIVITIES = 103;
    private static final int CONTENT_CONTACTS = 104;
    private static final int CONTENT_NGOS = 105;

    static {
        URI_MATCHER.addURI(AUTHORITY, DBOpenHelper.CAMPAIGNS_TABLE_NAME,
                CONTENT_CAMPAIGNS);
        URI_MATCHER.addURI(AUTHORITY, DBOpenHelper.CAMPAIGN_DETAILS_TABLE_NAME,
                CONTENT_CAMPAIGN_DETAILS);
        URI_MATCHER.addURI(AUTHORITY, DBOpenHelper.ACTIVITIES_TABLE_NAME,
                CONTENT_ACTIVITIES);
        URI_MATCHER.addURI(AUTHORITY, DBOpenHelper.CONTACTS_TABLE_NAME,
                CONTENT_CONTACTS);
        URI_MATCHER.addURI(AUTHORITY, DBOpenHelper.NGOS_TABLE_NAME,
                CONTENT_NGOS);
    }

    private DBOpenHelper dbHelper;

    /**
     * Call to get the table name associated to given contentType
     *
     * @param contentType type of the content
     * @return the table name
     */
    private static String getTableName(final int contentType) {

        if (contentType == CONTENT_CAMPAIGNS) {
            return DBOpenHelper.CAMPAIGNS_TABLE_NAME;
        } else if (contentType == CONTENT_CAMPAIGN_DETAILS) {
            return DBOpenHelper.CAMPAIGN_DETAILS_TABLE_NAME;
        } else if (contentType == CONTENT_ACTIVITIES) {
            return DBOpenHelper.ACTIVITIES_TABLE_NAME;
        } else if (contentType == CONTENT_CONTACTS) {
            return DBOpenHelper.CONTACTS_TABLE_NAME;
        }else if (contentType == CONTENT_NGOS) {
            return DBOpenHelper.NGOS_TABLE_NAME;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBOpenHelper(getContext(), DB_NAME, null, DB_VERSION);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        final SQLiteDatabase database = dbHelper.getReadableDatabase();

        final String limit = uri.getQueryParameter("limit");
        final String tableName = getTableName(URI_MATCHER.match(uri));

        cursor = database.query(false, tableName, projection, selection,
                selectionArgs, null, null, sortOrder, limit);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final String tableName = getTableName(URI_MATCHER.match(uri));
        long rowId = database.insert(tableName, null, values);
        if (rowId != -1) {
            final Uri insertUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(uri, null);
            return insertUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final String tableName = getTableName(URI_MATCHER.match(uri));
        int count = database.delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final String tableName = getTableName(URI_MATCHER.match(uri));

        int count = database.update(tableName, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int bulkInsert(final Uri uri, @NonNull final ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        final String tableName = getTableName(URI_MATCHER.match(uri));
        int numInserted = 0;
        db.beginTransaction();

        try {
            for (final ContentValues aValue : values) {
                if (aValue == null || aValue.size() == 0) {
                    break;
                }
                db.insert(tableName, null, aValue);
            }
            numInserted = values.length;
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
        return numInserted;
    }

}
