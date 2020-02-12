package com.projet.easybusiness;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnnonceDbOpener extends SQLiteOpenHelper {

    public static  int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "annonces.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AnnoceContract.FeedEntry.TABLE_NAME + " (" +
                    AnnoceContract.FeedEntry._ID + " INTEGER PRIMARY KEY autoincrement," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_ID + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_TITRE + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_PRIX +"INTEGER," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_PSEUDO + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_TELEPHONE + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_VILLE + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_CODE_POSTAL + " TEXT," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_IMAGES + "BLOB," +
                    AnnoceContract.FeedEntry.COLUMN_NAME_DATE + "INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AnnoceContract.FeedEntry.TABLE_NAME;

    public AnnonceDbOpener(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void setDatabaseVersion(int version){
        this.DATABASE_VERSION = version;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        //setDatabaseVersion(newVersion);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
