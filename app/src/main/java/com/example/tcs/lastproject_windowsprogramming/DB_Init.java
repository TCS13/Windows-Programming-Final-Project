package com.example.tcs.lastproject_windowsprogramming;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DB_Init {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DB_Init() {}

    /* Inner class that defines the table contents */
    public static abstract class FactionTable implements BaseColumns {
        public static final String FACTIONS_TABLE_NAME = "factions";
        public static final String FACTION_SWC_ID = "SWC_ID";
        public static final String FACTION_NAME = "faction_name";
        public static final String FACTION_LEADER = "faction_leader";
        public static final String FACTION_2IC = "faction_2iC";
        public static final String FACTION_LOGO = "faction_logo";
        public static final String FACTION_CAT_AND_TYPE = "faction_cat_and_type";
        public static final String FACTION_FOUNDING_DATE = "faction_founding_date";
        public static final String FACTION_DESCRIPTION = "faction_description";
        public static final String FACTION_COLUMN_NULLABLE = "NULL";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FactionTable.FACTIONS_TABLE_NAME + " (" +
                    FactionTable._ID + " INTEGER PRIMARY KEY," +
                    FactionTable.FACTION_SWC_ID + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_NAME + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_LEADER + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_2IC + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_LOGO + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_CAT_AND_TYPE + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_FOUNDING_DATE + TEXT_TYPE + COMMA_SEP +
                    FactionTable.FACTION_DESCRIPTION + TEXT_TYPE +
            " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FactionTable.FACTIONS_TABLE_NAME;

    public static class FactionDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Factions.db";
        private Context mContext;

        public FactionDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        public synchronized void insertFactionDataToDB(Faction factionData) {
            //Insert a faction's records to the DB
            // Gets the data repository in write mode
            FactionDbHelper helper = new FactionDbHelper(mContext);
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(DB_Init.FactionTable.FACTION_SWC_ID, factionData.getSWC_uid());
            values.put(DB_Init.FactionTable.FACTION_NAME, factionData.getName());
            values.put(DB_Init.FactionTable.FACTION_LEADER, factionData.getLeader());
            values.put(DB_Init.FactionTable.FACTION_2IC, factionData.getM2iC());
            values.put(DB_Init.FactionTable.FACTION_CAT_AND_TYPE, factionData.getCatAndType());
            values.put(DB_Init.FactionTable.FACTION_FOUNDING_DATE, factionData.getFoundationDate());
            values.put(DB_Init.FactionTable.FACTION_DESCRIPTION, factionData.getDescription());
            values.put(DB_Init.FactionTable.FACTION_LOGO, factionData.getLogo().toString());

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(
                    DB_Init.FactionTable.FACTIONS_TABLE_NAME,
                    DB_Init.FactionTable.FACTION_COLUMN_NULLABLE,
                    values);
            db.close();
            close();
            return;
        }
    }
}