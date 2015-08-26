package com.rnfsoft.multimemo.db;

import com.rnfsoft.multimemo.BasicInfo;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rnfsoft.multimemo.BasicInfo;

/**
 * Created by taehee on 8/13/15.
 */
public class MemoDatabase {

    public static final String TAG = "MemoDatabase";

    private static MemoDatabase database;

    public static String TABLE_MEMO = "MEMO";

    public static String TABLE_PHOTO = "PHOTO";

    public static String TABLE_VIDEO = "VIDEO";

    public static String TABLE_VOICE = "VOICE";

    public static String TABLE_HANDWRITING = "HANDWRITING";

    public static int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private Context context;

    private MemoDatabase(Context context) {
        this.context = context;
    }


    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            println("creating database [" + BasicInfo.DATABASE_NAME + "].");

            println("creating table [" + TABLE_MEMO + "].");

            String DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_MEMO;

            try {
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            String CREATE_SQL = "create table " + TABLE_MEMO + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + " CONTENT_TEXT TEXT DEFAULT '', "
                    + " ID_PHOTO INTEGER, "
                    + " ID_VIDEO INTEGER, "
                    + " ID_VOICE INTEGER, "
                    + " ID_HANDWRITING INTEGER, "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";


            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception e) {
                Log.e(TAG, "Exception in CREATE_SQL", e);

            }


            println("creating table [" + TABLE_PHOTO + "].");

            DROP_SQL = "DROP TABLE IF EXISTS " + TABLE_PHOTO;

            try {
                db.execSQL(DROP_SQL);
            } catch (Exception e) {
                Log.e(TAG, "Exception in DROP_SQL", e);
            }

            CREATE_SQL = "CREATE TABLE " + TABLE_PHOTO + "("
                    + " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + " URI TEXT, "
                    + " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";


            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception e) {
                Log.e(TAG, "Exception in CREATE_SQL", e);

            }


            String CREATE_INDEX_SQL = "CREATE INDEX " + TABLE_PHOTO + "_INX ON " + TABLE_PHOTO + "("
                    + "URI"
                    + ")";

            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception e) {
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", e);
            }

            println("creating table [" + TABLE_VIDEO + "].");

            // drop existing table
            DROP_SQL = "drop table if exists " + TABLE_VIDEO;
            try {
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            // create table
            CREATE_SQL = "create table " + TABLE_VIDEO + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  URI TEXT, "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            CREATE_INDEX_SQL = "CREATE INDEX " + TABLE_VIDEO + "_INX ON " + TABLE_VIDEO + "("
                    + "URI"
                    + ")";

            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
            }


            println("creating table [" + TABLE_VOICE + "].");

            DROP_SQL = "drop table if exists " + TABLE_VOICE;
            try {
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            CREATE_SQL = "create table " + TABLE_VOICE + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  URI TEXT, "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            CREATE_INDEX_SQL = "create index " + TABLE_VOICE + "_IDX ON " + TABLE_VOICE + "("
                    + "URI"
                    + ")";
            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
            }

            println("creating table [" + TABLE_HANDWRITING + "].");

            DROP_SQL = "drop table if exists " + TABLE_HANDWRITING;
            try {
                db.execSQL(DROP_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            CREATE_SQL = "create table " + TABLE_HANDWRITING + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  URI TEXT, "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

            CREATE_INDEX_SQL = "create index " + TABLE_HANDWRITING + "_IDX ON " + TABLE_HANDWRITING + "("
                    + "URI"
                    + ")";
            try {
                db.execSQL(CREATE_INDEX_SQL);
            } catch (Exception ex) {
                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }


    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }

    public boolean open() {
        println("opening database [" + BasicInfo.DATABASE_NAME + "].");

        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();

        return true;
    }


    public void close() {
        println("closing database [" + BasicInfo.DATABASE_NAME + "].");
        db.close();
        database = null;
    }

    public static MemoDatabase getInstance(Context context) {
        if (database == null) {
            database = new MemoDatabase(context);
        }
        return database;
    }


    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch (Exception e) {
            Log.e(TAG, "Exception in excuteQuery", e);
        }
        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch (SQLException e) {
            Log.e(TAG, "Exception in executeQuery", e);
            return false;
        }

        return true;
    }
}
