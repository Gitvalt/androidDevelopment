package company.supernice.k1967.shoppinglistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Valtteri on 9.9.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public final String DataTable = "phoneStore";
    public static final String DataBaseName = "NiceData";
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + DataTable + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Quantity INT, Price DOUBLE)");
        db = sqLiteDatabase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ DataTable);
        onCreate(db);
    }

}
