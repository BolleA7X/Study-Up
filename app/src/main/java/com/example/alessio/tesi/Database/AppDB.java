package com.example.alessio.tesi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class AppDB {

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("Creating db","");
            db.execSQL(CREATE_SESSION_TABLE);
            db.execSQL(CREATE_LOCATION_TABLE);
            db.execSQL(CREATE_COURSE_TABLE);
            db.execSQL(CREATE_TROPHY_TABLE);

            //inserting all trophies
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (1, "+Trophy.BRONZE+", 1)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (2, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (3, "+Trophy.SILVER+", 1)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (4, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (5, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (6, "+Trophy.GOLD+", 1)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (7, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (8, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (9, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (10, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (11, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (12, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (13, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (14, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (15, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (16, "+Trophy.GOLD+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (17, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (18, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (19, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (20, "+Trophy.PLATINUM+", 1)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Dropping tables","");
            db.execSQL(DROP_SESSION_TABLE);
            db.execSQL(DROP_LOCATION_TABLE);
            db.execSQL(DROP_COURSE_TABLE);
            db.execSQL(DROP_TROPHY_TABLE);
            onCreate(db);
        }
    }

    //database constants
    public static final String DB_NAME = "appDB.db";
    public static final int DB_VERSION = 5;

    //session constants
    public static final String SESSION_TABLE = "session";

    public static final String SESSION_ID = "_id";
    public static final int SESSION_ID_COL = 1;

    public static final String SESSION_YEAR = "year";
    public static final int SESSION_YEAR_COL = 2;

    public static final String SESSION_MONTH = "month";
    public static final int SESSION_MONTH_COL = 3;

    public static final String SESSION_DAY = "day";
    public static final int SESSION_DAY_COL = 4;

    public static final String SESSION_DURATION = "duration";
    public static final int SESSION_DURATION_COL = 5;

    public static final String SESSION_THEORY = "theory";
    public static final int SESSION_THEORY_COL = 6;

    public static final String SESSION_EXERCISE = "exercise";
    public static final int SESSION_EXERCISE_COL = 7;

    public static final String SESSION_PROJECT = "project";
    public static final int SESSION_PROJECT_COL = 8;

    public static final String SESSION_LOCATION_NAME = "location_name";
    public static final int SESSION_LOCATION_NAME_COL = 9;

    public static final String SESSION_COURSE_ID = "course_id";
    public static final int SESSION_COURSE_ID_COL = 10;

    //location constants
    public static final String LOCATION_TABLE = "location";

    public static final String LOCATION_NAME = "_name";
    public static final int LOCATION_NAME_COL = 1;

    public static final String LOCATION_LATITUDE = "latitude";
    public static final int LOCATION_LATITUDE_COL = 2;

    public static final String LOCATION_LONGITUDE = "longitude";
    public static final int LOCATION_LONGITUDE_COL = 3;

    //course constants
    public static final String COURSE_TABLE = "course";

    public static final String COURSE_ID = "_id";
    public static final int COURSE_ID_COL = 1;

    public static final String COURSE_NAME = "name";
    public static final int COURSE_NAME_COL = 2;

    //trophy constants
    public static final String TROPHY_TABLE = "trophy";

    public static final String TROPHY_ID = "_id";
    public static final int TROPHY_ID_COL = 1;

    public static final String TROPHY_COLOR = "color";
    public static final int TROPHY_COLOR_COL = 2;

    public static final String TROPHY_UNLOCKED = "unlocked";
    public static final int TROPHY_UNLOCKED_COL = 3;

    //counter constants
    public static final String COUNTER_TABLE = "counter";

    public static final String COUNTER_NAME = "_name";
    public static final int COUNTER_NAME_COL = 1;

    public static final String COUNTER_VALUE = "value";
    public static final int COUNTER_VALUE_COL = 2;

    //CREATE TABLE statements
    public static final String CREATE_SESSION_TABLE =
            "CREATE TABLE " + SESSION_TABLE + " ( " +
                    SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SESSION_YEAR + " INTEGER, " +
                    SESSION_MONTH + " INTEGER, " +
                    SESSION_DAY + " INTEGER, " +
                    SESSION_DURATION + " INTEGER, " +
                    SESSION_THEORY + " INTEGER, " +
                    SESSION_EXERCISE + " INTEGER, " +
                    SESSION_PROJECT + " INTEGER, " +
                    SESSION_LOCATION_NAME + " TEXT, " +
                    SESSION_COURSE_ID + " INTEGER);";

    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + LOCATION_TABLE + " ( " +
                    LOCATION_NAME + " TEXT PRIMARY KEY, " +
                    LOCATION_LATITUDE + " REAL, " +
                    LOCATION_LONGITUDE + " REAL);";

    public static final String CREATE_COURSE_TABLE =
            "CREATE TABLE " + COURSE_TABLE + " ( " +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NAME + " TEXT);";

    public static final String CREATE_TROPHY_TABLE =
            "CREATE TABLE " + TROPHY_TABLE + " ( " +
                    TROPHY_ID + " INTEGER PRIMARY KEY, " +
                    TROPHY_COLOR + " TEXT, " +
                    TROPHY_UNLOCKED + " INTEGER);";

    public static final String CREATE_COUNTER_TABLE =
            "CREATE TABLE " + COUNTER_TABLE + " ( " +
                    COUNTER_NAME + " TEXT PRIMARY KEY, " +
                    COUNTER_VALUE + " INTEGER);";

    //DROP TABLE statements
    public static final String DROP_SESSION_TABLE =
            "DROP TABLE IF EXISTS " + SESSION_TABLE;

    public static final String DROP_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + LOCATION_TABLE;

    public static final String DROP_COURSE_TABLE =
            "DROP TABLE IF EXISTS " + COURSE_TABLE;

    public static final String DROP_TROPHY_TABLE =
            "DROP TABLE IF EXISTS " + TROPHY_TABLE;

    public static final String DROP_COUNTER_TABLE =
            "DROP TABLE IF EXISTS " + COUNTER_TABLE;

    //attributes and methods
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public AppDB(Context context) {
        dbHelper = new DBHelper(context,DB_NAME,null,DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if(db != null)
            db.close();
    }

    public void insertSubject(Course course) {
        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME,course.getName());
        this.openWriteableDB();
        long rowID = db.insert(COURSE_TABLE,null,cv);
        this.closeDB();
    }

    public ArrayList<String> getSubjects() {
        this.openReadableDB();
        String[] args = new String[] {COURSE_NAME};
        Cursor cursor = db.query(COURSE_TABLE,args,null,null,null,null,null);
        ArrayList<String> subjects = new ArrayList<String>();
        while(cursor.moveToNext())
           subjects.add(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
        if(cursor !=null)
            cursor.close();
        this.closeDB();
        return subjects;
    }

    public Trophy[] getTrophies() {
        this.openReadableDB();
        String[] args = new String[] {TROPHY_COLOR,TROPHY_UNLOCKED};
        Cursor cursor = db.query(TROPHY_TABLE,args,null,null,null,null,null);
        Trophy[] trophies = new Trophy[20];
        int i = 0;
        while(cursor.moveToNext()) {
            Trophy temp = new Trophy(i+1,cursor.getString(cursor.getColumnIndex(TROPHY_COLOR)),cursor.getInt(cursor.getColumnIndex(TROPHY_UNLOCKED)));
            trophies[i] = temp;
            ++i;
        }
        if(cursor !=null)
            cursor.close();
        this.closeDB();
        return trophies;
    }
}