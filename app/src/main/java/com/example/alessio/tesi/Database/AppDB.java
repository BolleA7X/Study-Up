package com.example.alessio.tesi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.renderer.scatter.ChevronUpShapeRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

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
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (1, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (2, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (3, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (4, "+Trophy.BRONZE+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (5, "+Trophy.SILVER+", 0)");
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (6, "+Trophy.GOLD+", 0)");
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
            db.execSQL("INSERT INTO "+TROPHY_TABLE+" VALUES (20, "+Trophy.PLATINUM+", 0)");
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
    public static final int DB_VERSION = 8;

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

    public static final String SESSION_COURSE_NAME = "course_name";
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

    public static final String COURSE_NAME = "_name";
    public static final int COURSE_NAME_COL = 1;

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
                    SESSION_COURSE_NAME + " TEXT);";

    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + LOCATION_TABLE + " ( " +
                    LOCATION_NAME + " TEXT PRIMARY KEY, " +
                    LOCATION_LATITUDE + " REAL, " +
                    LOCATION_LONGITUDE + " REAL);";

    public static final String CREATE_COURSE_TABLE =
            "CREATE TABLE " + COURSE_TABLE + " ( " +
                    COURSE_NAME + " TEXT PRIMARY KEY);";

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

    //METODI PER ESEGUIRE SPECIFICHE QUERY: NON SPECIFICO I DETTAGLI DI OGNI SINGOLO METODO
    //query per inserire nuova materia
    public void insertSubject(Course course) {
        ContentValues cv = new ContentValues();
        cv.put(COURSE_NAME,course.getName());
        this.openWriteableDB();
        long rowID = db.insert(COURSE_TABLE,null,cv);
        this.closeDB();
    }

    //query per inserire nuovo posto
    public void insertLocation(Location location) {
        ContentValues cv = new ContentValues();
        cv.put(LOCATION_NAME,location.getName());
        cv.put(LOCATION_LATITUDE,location.getLatitude());
        cv.put(LOCATION_LONGITUDE,location.getLongitude());
        this.openWriteableDB();
        long rowID = db.insert(LOCATION_TABLE,null,cv);
        this.closeDB();
    }

    //query per inserire dati di sessione appena finita
    public void insertSession(Session session) {
        ContentValues cv = new ContentValues();
        cv.put(SESSION_YEAR,session.getYear());
        cv.put(SESSION_MONTH,session.getMonth());
        cv.put(SESSION_DAY,session.getDay());
        cv.put(SESSION_DURATION,session.getDuration());
        cv.put(SESSION_THEORY,session.getTheory());
        cv.put(SESSION_EXERCISE,session.getExercise());
        cv.put(SESSION_PROJECT,session.getProject());
        cv.put(SESSION_LOCATION_NAME,session.getLocation_name());
        cv.put(SESSION_COURSE_NAME,session.getCourse_name());
        this.openWriteableDB();
        long rowId = db.insert(SESSION_TABLE,null,cv);
        this.closeDB();
    }

    //query per ottenere le materie (per relativo spinner)
    public ArrayList<String> getSubjects() {
        this.openReadableDB();
        String[] args = new String[] {COURSE_NAME};
        Cursor cursor = db.query(COURSE_TABLE,args,null,null,null,null,null);
        ArrayList<String> subjects = new ArrayList<String>();
        while(cursor.moveToNext())
           subjects.add(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return subjects;
    }

    //query per ottenere i posti (per relativo spinner)
    public ArrayList<String> getLocations() {
        this.openReadableDB();
        String[] args = new String[] {LOCATION_NAME};
        Cursor cursor = db.query(LOCATION_TABLE,args,null,null,null,null,null);
        ArrayList<String> locations = new ArrayList<String>();
        while(cursor.moveToNext())
            locations.add(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return locations;
    }

    //query per ottenere i trofei (per avere i dati nel relatico fragment)
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

    //query per ottenere i dati da inserire nel grafico a torta dei corsi
    public ArrayList<PieEntry> getSubjectsPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        this.openReadableDB();
        String[] args = new String[] {"SUM("+SESSION_DURATION+")"};
        //1) ottengo la somma delle durate totali
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,null,null,null);
        cursor.moveToFirst();
        int totalDuration = cursor.getInt(0);
        //2) ottengo la lista dei corsi, uso un iteratore per comodità
        ArrayList<String> subjects = this.getSubjects();
        Iterator<String> iterator = subjects.iterator();
        //3)itero per ottenere la somma delle durate dei singoli corsi e calcolare le singole percentuali
        while(iterator.hasNext()) {
            String course = iterator.next();
            String where = SESSION_COURSE_NAME + "= ?";
            String[] whereArgs = {course};
            if(!db.isOpen())
                this.openReadableDB();
            cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
            cursor.moveToFirst();
            float percent = (float)cursor.getInt(0)/(float)totalDuration*100;
            //4)aggiungo ogni percentuale calcolata e il nome del corso ai dati del grafico
            entries.add(new PieEntry(percent,course));
        }
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return entries;
    }

    //query per ottenere i dati da inserire nel grafico a torta dei tipi
    public ArrayList<PieEntry> getTypesPieChartData() {
        float[] percents = new float[3];
        ArrayList<PieEntry> entries = new ArrayList<>();
        this.openReadableDB();
        String[] args = new String[] {"COUNT("+SESSION_ID+")"};
        //1) ottengo il numero di sessioni
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,null,null,null);
        cursor.moveToFirst();
        float numberOfSessions = (float)cursor.getInt(0);
        Log.d("Sessioni totali",String.valueOf(numberOfSessions));
        //2) itero sui tre tipi di sessione e conto le occorrenze per ognuno
        String[] types = {SESSION_THEORY,SESSION_EXERCISE,SESSION_PROJECT};
        for(int i=0;i<3;i++) {
            String where = types[i] + "= ?";
            String[] whereArgs = {"1"};
            if(!db.isOpen())
                this.openReadableDB();
            cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
            cursor.moveToFirst();
            //3) calcolo le percentuali assolute per ogni tipo
            percents[i] = ((float)cursor.getInt(0))/numberOfSessions;
            Log.d(types[i],String.valueOf(percents[i]));
        }
        //4) trovo il coefficiente di normalizzazione
        float k = 1/(percents[0] + percents[1] + percents[2]);
        Log.d("k",String.valueOf(k));
        //5) calcolo le percentuali relative per ogni tipo e le inserisco nell'ArrayList
        entries.add(new PieEntry(k*percents[0]*100,SESSION_THEORY));
        entries.add(new PieEntry(k*percents[1]*100,SESSION_EXERCISE));
        entries.add(new PieEntry(k*percents[2]*100,SESSION_PROJECT));
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return entries;
    }

    //query per ottenere il luogo più frequente
    public String getMostFrequentLocation() {
        String result = "";
        this.openReadableDB();
        String[] args = {SESSION_LOCATION_NAME,"COUNT("+SESSION_LOCATION_NAME+") AS cont"};
        String order = "cont DESC";
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,SESSION_LOCATION_NAME,null,order,"1");
        while(cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex(SESSION_LOCATION_NAME));
            Log.println(Log.DEBUG, "cursor: ", result);
        }
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    public int getTotalTime() {
        int totalDuration = 0;
        this.openReadableDB();
        String[] args = new String[] {"SUM("+SESSION_DURATION+")"};
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,null,null,null);
        while(cursor.moveToNext())
            totalDuration = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return totalDuration;
    }

    public float[] getInfoForAdvice() {
        int cont = 0;
        float[] result = new float[3];
        String select;
        String[] whereArgs;
        String query;
        Cursor cursor1,cursor2;
        this.openReadableDB();

        //1) MEDIA DEI CORSI STUDIATI IN 10 GIORNI
        //ottengo la conta dei giorni
        select = "DISTINCT "+SESSION_DAY+","+SESSION_MONTH+","+SESSION_YEAR;
        query = "SELECT "+select+" FROM "+SESSION_TABLE+" ORDER BY "+SESSION_ID+" DESC LIMIT 10";
        cursor1 = db.rawQuery(query,null);
        cursor1.moveToFirst();
        cont = cursor1.getInt(0);
        //ottengo la conta dei corsi diversi studiati ogni giorno per 10 giorni
        select = "DISTINCT COUNT("+SESSION_COURSE_NAME+")";
        query = "SELECT "+select+" FROM "+SESSION_TABLE+" ORDER BY "+SESSION_ID+" DESC LIMIT 10";
        cursor2 = db.rawQuery(query,null);
        cursor2.moveToFirst();
        if(cont != 0)
            result[0] = (float)cursor2.getInt(0) / (float)cont;
        else
            result[0] = 0;

        //2) MEDIA DELLA DURATA TOTALE GIORNALIERA
        //ottengo la durata totale
        cont = this.getTotalTime();
        //ottengo i giorni
        select = "DISTINCT "+SESSION_DAY+","+SESSION_MONTH+","+SESSION_YEAR;
        query = "SELECT "+select+" FROM "+SESSION_TABLE;
        if(!db.isOpen())
            this.openReadableDB();
        cursor1 = db.rawQuery(query,null);
        //calcolo il secondo risultato
        if(cont != 0)
            result[1] = (float)cont / (float)cursor1.getCount();
        else
            result[1] = 0;

        //3) MEDIA DELLE DISTANZE DI TEMPO TRA SESSIONI
        //ottengo il numero di sessioni
        select = "COUNT(*)";
        query = "SELECT "+select+" FROM "+SESSION_TABLE;
        cursor1 = db.rawQuery(query,null);
        cursor1.moveToFirst();
        cont = cursor1.getInt(0);
        if(cont > 1) {
            //ottengo la somma delle differenze di tempo tra sessioni
            long diffSum = 0;
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            select = SESSION_DAY + "," + SESSION_MONTH + "," + SESSION_YEAR;
            query = "SELECT " + select + " FROM " + SESSION_TABLE + " WHERE " + SESSION_ID + "= ?";
            whereArgs = new String[]{"1"};
            cursor2 = db.rawQuery(query, whereArgs);
            cursor2.moveToFirst();
            cal1.set(cursor2.getInt(cursor2.getColumnIndex(SESSION_YEAR)),
                    cursor2.getInt(cursor2.getColumnIndex(SESSION_MONTH)),
                    cursor2.getInt(cursor2.getColumnIndex(SESSION_DAY)));
            //considero come se le sessioni fossero tutte svolte alle 00:00
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            for (int i = 2; i <= cont; i++) {
                whereArgs = new String[]{String.valueOf(i)};
                if (!db.isOpen())
                    this.openReadableDB();
                cursor2 = db.rawQuery(query, whereArgs);
                cursor2.moveToFirst();
                cal2.set(cursor2.getInt(cursor2.getColumnIndex(SESSION_YEAR)),
                        cursor2.getInt(cursor2.getColumnIndex(SESSION_MONTH)),
                        cursor2.getInt(cursor2.getColumnIndex(SESSION_DAY)));
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);
                diffSum += TimeUnit.MILLISECONDS.toDays(Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis()));
                cal1 = cal2;
            }
            //calcolo il terzo risultato
            result[2] = (float) diffSum / (float) cont;

            if(cursor2 != null)
                cursor2.close();
        }
        else
            result[2] = 0;

        if(cursor1 != null)
            cursor1.close();
        this.closeDB();

        return result;
    }

    public int unlockTrophy(int id) {
        ContentValues cv = new ContentValues();
        cv.put(TROPHY_UNLOCKED,1);
        String where = TROPHY_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};
        this.openWriteableDB();
        int rowCount = db.update(TROPHY_TABLE,cv,where,whereArgs);
        this.closeDB();
        return rowCount;
    }

    public void deleteAll() {
        this.openWriteableDB();
        db.execSQL(DROP_SESSION_TABLE);
        db.execSQL(DROP_LOCATION_TABLE);
        db.execSQL(DROP_COURSE_TABLE);
        db.execSQL(CREATE_SESSION_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        this.closeDB();
    }

    public int deleteCourse(String name) {
        String where = COURSE_NAME + "= ?";
        String[] whereArgs = {name};
        this.openWriteableDB();
        int rowCount = db.delete(COURSE_TABLE,where,whereArgs);
        this.closeDB();
        return rowCount;
    }

    public int deleteLocation(String name) {
        String where = LOCATION_NAME + "= ?";
        String[] whereArgs = {name};
        this.openWriteableDB();
        int rowCount = db.delete(LOCATION_TABLE,where,whereArgs);
        this.closeDB();
        return rowCount;
    }
}