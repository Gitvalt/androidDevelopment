package company.supernice.k1967.shoppinglistapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by K1967 on 28.9.2017.
 * Testing content-provider
 */

public class ShoppingListProvider extends ContentProvider {

    //what is provided and by whom
    private static final String AUTHORITY   = "fi.shoppinglistapp.contentprovider";
    private static final String BASE_PATH   = "Products";

    //provider address
    public static final Uri CONTENT_URI     = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


    private static final int Products       = 1;
    private static final int Product_ID     = 2;


    //what type of data
    public static final String CONTENT_TYPE         = ContentResolver.CURSOR_DIR_BASE_TYPE+"/highscores";
    public static final String CONTENT_ITEM_TYPE    = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/highscore";

    private DatabaseHelper database;
    private static final UriMatcher mUriMatcher     = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, BASE_PATH, Product_ID);
        mUriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", Products);
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseHelper.DataTable);

        int UriType = mUriMatcher.match(uri);

        try {
            switch (UriType) {
                case Product_ID:
                    break;
                case Products:
                    queryBuilder.appendWhere("_id =" + uri.getLastPathSegment());
                    break;
                default:
                    Log.e("Provider_ERROR", "Unkown uri in Shoppinglistprovider");
                    throw new IllegalArgumentException("Unknown uri in shoppinglistprovider");
            }
        }
        catch (IllegalArgumentException argEx)
        {

        }

        SQLiteDatabase db = database.getWritableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);


        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        int count = 0;

        switch (mUriMatcher.match(uri)){

            case Product_ID:
                count = sqLiteDatabase.update(DatabaseHelper.DataTable, contentValues, selection, selectionArgs);
                break;

            case Products:

                String productID = uri.getPathSegments().get(1);

                count = sqLiteDatabase.update(DatabaseHelper.DataTable, contentValues, selection, selectionArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int uriType = mUriMatcher.match(uri);
        long id     = 0;

        SQLiteDatabase db = database.getWritableDatabase();

        try
        {
            switch (uriType) {
                case Products:
                    id = db.insert(DatabaseHelper.DataTable, null, contentValues);
                    break;
                default:
                    Log.e("Provider_ERROR", "Unknown insert set. '" + uri.getLastPathSegment() + "' is not a valid table");
                    throw new IllegalArgumentException("Error!, Unknown insert uri");
            }
        }
        catch (IllegalArgumentException argEx)
        {

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase db = database.getWritableDatabase();

        int count = 0;

        switch (mUriMatcher.match(uri))
        {
            case Products:
                db.delete(DatabaseHelper.DataTable, s, strings);
                break;

            case Product_ID:
                String productID = uri.getLastPathSegment();

                db.delete(DatabaseHelper.DataTable, "_id" + productID, strings);

                break;

            default:
                throw new IllegalArgumentException("Unknown URI in content provider");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
