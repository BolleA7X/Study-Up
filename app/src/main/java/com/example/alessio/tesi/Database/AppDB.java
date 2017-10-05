package com.example.alessio.tesi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
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
            db.execSQL(CREATE_USER_TABLE);

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
            db.execSQL(DROP_USER_TABLE);
            onCreate(db);
        }
    }

    //database constants
    public static final String DB_NAME = "appDB.db";
    public static final int DB_VERSION = 12;

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

    public static final String SESSION_USER = "username";
    public static final int SESSION_USER_COL = 11;

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

    public static final String COURSE_USER = "user";
    public static final int COURSE_USER_COL = 3;

    //trophy constants
    public static final String TROPHY_TABLE = "trophy";

    public static final String TROPHY_ID = "_id";
    public static final int TROPHY_ID_COL = 1;

    public static final String TROPHY_COLOR = "color";
    public static final int TROPHY_COLOR_COL = 2;

    public static final String TROPHY_UNLOCKED = "unlocked";
    public static final int TROPHY_UNLOCKED_COL = 3;

    //user constants
    public static final String USER_TABLE = "user";

    public static final String USER_ID = "_id";
    public static final int USER_ID_COL = 1;

    public static final String USER_LASTID = "lastId";
    public static final int USER_LASTID_COL = 2;

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
                    SESSION_COURSE_NAME + " TEXT, " +
                    SESSION_USER + " TEXT);";

    public static final String CREATE_LOCATION_TABLE =
            "CREATE TABLE " + LOCATION_TABLE + " ( " +
                    LOCATION_NAME + " TEXT PRIMARY KEY, " +
                    LOCATION_LATITUDE + " REAL, " +
                    LOCATION_LONGITUDE + " REAL);";

    public static final String CREATE_COURSE_TABLE =
            "CREATE TABLE " + COURSE_TABLE + " ( " +
                    COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NAME + " TEXT, " +
                    COURSE_USER + " TEXT);";

    public static final String CREATE_TROPHY_TABLE =
            "CREATE TABLE " + TROPHY_TABLE + " ( " +
                    TROPHY_ID + " INTEGER PRIMARY KEY, " +
                    TROPHY_COLOR + " TEXT, " +
                    TROPHY_UNLOCKED + " INTEGER);";

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE " + USER_TABLE + " ( " +
                    USER_ID + " TEXT, " +
                    USER_LASTID + " INTEGER);";

    //DROP TABLE statements
    public static final String DROP_SESSION_TABLE =
            "DROP TABLE IF EXISTS " + SESSION_TABLE;

    public static final String DROP_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + LOCATION_TABLE;

    public static final String DROP_COURSE_TABLE =
            "DROP TABLE IF EXISTS " + COURSE_TABLE;

    public static final String DROP_TROPHY_TABLE =
            "DROP TABLE IF EXISTS " + TROPHY_TABLE;

    public static final String DROP_USER_TABLE =
            "DROP TABLE IF EXISTS " + USER_TABLE;

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
    public void insertSubject(Course course,String user) {
        String[] args = {"*"};
        String where = COURSE_NAME+"= ? AND "+COURSE_USER+"= ?";
        String[] whereArgs = {course.getName(),user};
        this.openWriteableDB();
        Cursor cursor = db.query(COURSE_TABLE,args,where,whereArgs,null,null,null);
        if(cursor.getCount() == 0) {
            ContentValues cv = new ContentValues();
            cv.put(COURSE_NAME, course.getName());
            cv.put(COURSE_USER, user);
            if (!db.isOpen())
                this.openWriteableDB();
            long rowID = db.insert(COURSE_TABLE, null, cv);
            this.closeDB();
        }
    }

    //query per inserire nuovo posto
    public void insertLocation(Location location) {
        String[] args = {"*"};
        String where = LOCATION_NAME+"= ?";
        String[] whereArgs = {location.getName()};
        this.openWriteableDB();
        Cursor cursor = db.query(LOCATION_TABLE,args,where,whereArgs,null,null,null);
        if(cursor.getCount() == 0) {
            ContentValues cv = new ContentValues();
            cv.put(LOCATION_NAME, location.getName());
            cv.put(LOCATION_LATITUDE, location.getLatitude());
            cv.put(LOCATION_LONGITUDE, location.getLongitude());
            if(!db.isOpen())
                this.openWriteableDB();
            long rowID = db.insert(LOCATION_TABLE, null, cv);
            this.closeDB();
        }
    }

    //query per inserire dati di sessione appena finita
    public void insertSession(Session session,String user) {
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
        cv.put(SESSION_USER,user);
        this.openWriteableDB();
        long rowId = db.insert(SESSION_TABLE,null,cv);
        this.closeDB();
    }

    //query per ottenere le materie (per relativo spinner)
    public ArrayList<String> getSubjects(String user) {
        this.openReadableDB();
        String[] args = new String[] {COURSE_NAME};
        String where = COURSE_USER + "= ?";
        String[] whereArgs = {user};
        Cursor cursor = db.query(COURSE_TABLE,args,where,whereArgs,null,null,null);
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
            locations.add(cursor.getString(cursor.getColumnIndex(LOCATION_NAME)));
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return locations;
    }

    //query per ottenere i trofei (per avere i dati nel relativo fragment)
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
    public ArrayList<PieEntry> getSubjectsPieChartData(String user) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        this.openReadableDB();
        String[] args = new String[] {"SUM("+SESSION_DURATION+")"};
        String where = SESSION_USER + "= ?";
        String[] whereArgs = {user};
        //1) ottengo la somma delle durate totali dell'utente loggato
        Cursor cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        int totalDuration = cursor.getInt(0);
        //2) ottengo la lista dei corsi, uso un iteratore per comodità
        ArrayList<String> subjects = this.getSubjects(user);
        Iterator<String> iterator = subjects.iterator();
        //3)itero per ottenere la somma delle durate dei singoli corsi e calcolare le singole percentuali
        while(iterator.hasNext()) {
            String course = iterator.next();
            where = SESSION_COURSE_NAME + "= ? AND " + SESSION_USER + "= ?";
            whereArgs = new String[] {course,user};
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
    public ArrayList<PieEntry> getTypesPieChartData(String user) {
        float[] percents = new float[3];
        ArrayList<PieEntry> entries = new ArrayList<>();
        this.openReadableDB();
        String[] args = new String[] {"COUNT("+SESSION_ID+")"};
        String where = SESSION_USER + "= ?";
        String[] whereArgs = {user};
        //1) ottengo il numero di sessioni
        Cursor cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        float numberOfSessions = (float)cursor.getInt(0);
        //2) itero sui tre tipi di sessione e conto le occorrenze per ognuno
        String[] types = {SESSION_THEORY,SESSION_EXERCISE,SESSION_PROJECT};
        for(int i=0;i<3;i++) {
            where = types[i] + "= ? AND " + SESSION_USER + "= ?";
            whereArgs = new String[] {"1",user};
            if(!db.isOpen())
                this.openReadableDB();
            cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
            cursor.moveToFirst();
            //3) calcolo le percentuali assolute per ogni tipo
            percents[i] = ((float)cursor.getInt(0))/numberOfSessions;
        }
        //4) trovo il coefficiente di normalizzazione
        float k = 1/(percents[0] + percents[1] + percents[2]);
        Log.d("k",String.valueOf(k));
        //5) calcolo le percentuali relative per ogni tipo e le inserisco nell'ArrayList
        entries.add(new PieEntry(k*percents[0]*100,"Teoria"));
        entries.add(new PieEntry(k*percents[1]*100,"Esercizi"));
        entries.add(new PieEntry(k*percents[2]*100,"Progetto"));
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

    public int getTotalTime(String user) {
        int totalDuration = 0;
        this.openReadableDB();
        String[] args = new String[] {"SUM("+SESSION_DURATION+")"};
        String where = SESSION_USER + "= ?";
        String[] whereArgs = {user};
        Cursor cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
        while(cursor.moveToNext())
            totalDuration = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return totalDuration;
    }

    public double[] getCoordinates(String name) {
        double[] result = new double[2];
        this.openReadableDB();
        String[] args = new String[] {LOCATION_LATITUDE,LOCATION_LONGITUDE};
        String where = LOCATION_NAME+"= ?";
        String[] whereArgs = {name};
        Cursor cursor = db.query(LOCATION_TABLE,args,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        result[0] = cursor.getDouble(cursor.getColumnIndex(LOCATION_LATITUDE));
        result[1] = cursor.getDouble(cursor.getColumnIndex(LOCATION_LONGITUDE));
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    public float[] getInfoForAdvice(String user) {
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
        query = "SELECT "+select+" FROM "+SESSION_TABLE+" WHERE "+SESSION_USER+"= ?"+" ORDER BY "+SESSION_ID+" DESC LIMIT 10";
        whereArgs = new String[] {user};
        cursor1 = db.rawQuery(query,whereArgs);
        if(cursor1.getCount() != 0) {
            while(cursor1.moveToNext()) {
                select = "COUNT(DISTINCT "+SESSION_COURSE_NAME+")";
                query = "SELECT "+select+" FROM "+SESSION_TABLE+" WHERE "+
                        SESSION_USER+"=? AND "+
                        SESSION_DAY+"=? AND "+
                        SESSION_MONTH+"=? AND "+
                        SESSION_YEAR+"=?";
                whereArgs = new String[] {user,
                                          String.valueOf(cursor1.getInt(cursor1.getColumnIndex(SESSION_DAY))),
                                          String.valueOf(cursor1.getInt(cursor1.getColumnIndex(SESSION_MONTH))),
                                          String.valueOf(cursor1.getInt(cursor1.getColumnIndex(SESSION_YEAR)))};
                if(!db.isOpen())
                    this.openReadableDB();
                cursor2 = db.rawQuery(query,whereArgs);
                cursor2.moveToFirst();
                cont += cursor2.getInt(0);
            }
            result[0] = (float)cont / (float)cursor1.getCount();
        }
        else
            result[0] = 0;

        //2) MEDIA DELLA DURATA TOTALE GIORNALIERA
        //ottengo la durata totale
        cont = this.getTotalTime(user);
        //ottengo i giorni
        select = "DISTINCT "+SESSION_DAY+","+SESSION_MONTH+","+SESSION_YEAR;
        query = "SELECT "+select+" FROM "+SESSION_TABLE+" WHERE "+SESSION_USER+"= ?";
        whereArgs = new String[] {user};
        if(!db.isOpen())
            this.openReadableDB();
        cursor1 = db.rawQuery(query,whereArgs);
        //calcolo il secondo risultato
        if(cont != 0)
            result[1] = (float)cont / (float)cursor1.getCount();
        else
            result[1] = 0;

        //3) MEDIA DELLE DISTANZE DI TEMPO TRA SESSIONI
        //ottengo il numero di sessioni
        select = "*";
        query = "SELECT "+select+" FROM "+SESSION_TABLE+" WHERE "+SESSION_USER+"= ?"+" GROUP BY "+SESSION_DAY+","+SESSION_MONTH+","+SESSION_YEAR;
        cursor1 = db.rawQuery(query,whereArgs);
        cont = cursor1.getCount();
        if(cont > 1) {
            //ottengo la somma delle differenze di tempo tra sessioni
            long diffSum = 0;
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            select = SESSION_DAY + "," + SESSION_MONTH + "," + SESSION_YEAR;
            query = "SELECT " + select + " FROM " + SESSION_TABLE + " WHERE " + SESSION_ID + "= ? AND "+SESSION_USER+"= ?";
            cursor1.moveToFirst();
            whereArgs = new String[] {String.valueOf(cursor1.getInt(cursor1.getColumnIndex(SESSION_ID))),user};
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
            while(cursor1.moveToNext()) {
                if (!db.isOpen())
                    this.openReadableDB();
                whereArgs = new String[] {String.valueOf(cursor1.getInt(cursor1.getColumnIndex(SESSION_ID))),user};
                cursor2 = db.rawQuery(query, whereArgs);
                cursor2.moveToFirst();
                cal2.set(cursor2.getInt(cursor2.getColumnIndex(SESSION_YEAR)),
                        cursor2.getInt(cursor2.getColumnIndex(SESSION_MONTH)),
                        cursor2.getInt(cursor2.getColumnIndex(SESSION_DAY)));
                cal2.set(Calendar.HOUR_OF_DAY, 0);
                cal2.set(Calendar.MINUTE, 0);
                cal2.set(Calendar.SECOND, 0);
                cal2.set(Calendar.MILLISECOND, 0);
                diffSum += (TimeUnit.MILLISECONDS.toDays(Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis())));
                cal1 = (Calendar)cal2.clone();
            }
            //calcolo il terzo risultato
            result[2] = (float) diffSum / (float) cont;

            if(cursor2 != null)
                cursor2.close();
        }
        else
            result[2] = 10;

        if(cursor1 != null)
            cursor1.close();
        this.closeDB();

        return result;
    }

    public ArrayList<Session> getAllSessions(int lastIndex,String user) {
        ArrayList<Session> result = new ArrayList<>();
        this.openReadableDB();
        String[] args = {"*"};
        String where = SESSION_ID + "> ? AND "+SESSION_USER + "= ?";
        String[] whereArgs = {String.valueOf(lastIndex),user};
        Cursor cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
        while(cursor.moveToNext()) {
            Session s = new Session();
            s.setId(cursor.getInt(cursor.getColumnIndex(SESSION_ID)));
            s.setYear(cursor.getInt(cursor.getColumnIndex(SESSION_YEAR)));
            s.setMonth(cursor.getInt(cursor.getColumnIndex(SESSION_MONTH)));
            s.setDay(cursor.getInt(cursor.getColumnIndex(SESSION_DAY)));
            s.setDuration(cursor.getInt(cursor.getColumnIndex(SESSION_DURATION)));
            s.setTheory(cursor.getInt(cursor.getColumnIndex(SESSION_THEORY)));
            s.setExercise(cursor.getInt(cursor.getColumnIndex(SESSION_EXERCISE)));
            s.setProject(cursor.getInt(cursor.getColumnIndex(SESSION_PROJECT)));
            s.setCourse_name(cursor.getString(cursor.getColumnIndex(SESSION_COURSE_NAME)));
            result.add(s);
        }
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

    public boolean searchUser(String user) {
        this.openReadableDB();
        String[] args = {"*"};
        String where = USER_ID+"= ?";
        String[] whereArgs = {user};
        Cursor cursor = db.query(USER_TABLE,args,where,whereArgs,null,null,null);
        boolean result;
        if(cursor.getCount() == 0)
            result = false;
        else
            result = true;
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    public boolean searchCourse(String user,String course) {
        this.openReadableDB();
        String[] args = {"*"};
        String where = COURSE_NAME+"= ? AND "+COURSE_USER+"= ?";
        String[] whereArgs = {course,user};
        Cursor cursor = db.query(COURSE_TABLE,args,where,whereArgs,null,null,null);
        boolean result;
        if(cursor.getCount() == 0)
            result = false;
        else
            result = true;
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    public void deleteUserSessions(String user) {
        this.openWriteableDB();
        String where = SESSION_USER+"= ?";
        String[] whereArgs = {user};
        db.delete(SESSION_TABLE,where,whereArgs);
        this.closeDB();
    }

    public void deleteAll(String user) {
        this.openWriteableDB();
        String where = COURSE_USER+"= ?";
        String[] whereArgs = {user};
        deleteUserSessions(user);
        if(!db.isOpen())
            this.openWriteableDB();
        db.delete(COURSE_TABLE,where,whereArgs);
        this.closeDB();
    }

    public int deleteCourse(String name,String user) {
        String where = COURSE_NAME + "= ? AND " + COURSE_USER + "= ?";
        String[] whereArgs = {name,user};
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

    //QUERY PER CONDIZIONI DI SBLOCCO TROFEI

    //trofei 7 e 8: 2 o 3 materie diverse al giorno
    //ritorna il numero di materie diverse studiate questo giorno
    public int differentCourses(Calendar calendar,String user) {
        this.openReadableDB();
        String[] args = {"COUNT(DISTINCT "+SESSION_COURSE_NAME+")"};
        String where = SESSION_DAY+"= ? AND "+SESSION_MONTH+"= ? AND "+SESSION_YEAR+"= ? AND "+SESSION_USER+"= ?";
        String[] whereArgs = {String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),String.valueOf(calendar.get(Calendar.MONTH)),
                              String.valueOf(calendar.get(Calendar.YEAR)),user};
        Cursor cursor = db.query(SESSION_TABLE,args,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    //trofeo 15: usare l'app per un mese
    //ritorna true se c'è almeno una sessione al giorno per 30 giorni, false altrimenti
    public boolean oneMonth() {
        this.openReadableDB();
        String[] args = {"DISTINCT "+SESSION_DAY,SESSION_MONTH,SESSION_YEAR};
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,null,null,null);
        int result = cursor.getCount();
        if(cursor != null)
            cursor.close();
        this.closeDB();

        if(result < 30)
            return false;
        else
            return true;
    }

    //trofeo 16: 42 sessioni di qualunque tipo
    //ritorna il numero di sessioni totali
    public int numberOfSessions() {
        this.openReadableDB();
        String[] args = {"COUNT(*)"};
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,null,null,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    //trofeo 17: numero di posti diversi in cui è stata svolta almeno una sessione
    //ritorna il numero di posti diversi dalla tabella delle sessioni
    public int getLocationsCount() {
        this.openReadableDB();
        String[] args = {SESSION_LOCATION_NAME};
        String where = SESSION_LOCATION_NAME + "<> ?";
        String[] whereArgs = {""};
        String group = SESSION_LOCATION_NAME;
        Cursor cursor = db.query(SESSION_TABLE,args,where,whereArgs,group,null,null);
        int result = cursor.getCount();
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    //trofeo 20: PLATINO!!!
    //ritorna true se il numero di trofei sbloccati è 19, false altrimenti
    //questo andrebbe chiamato ogni volta si sblocca un trofeo ma può risultare pesante
    public boolean platinum() {
        this.openReadableDB();
        String[] args = {"COUNT(*)"};
        String where = TROPHY_UNLOCKED+"= 1";
        Cursor cursor = db.query(TROPHY_TABLE,args,where,null,null,null,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();

        if(result == 19)
            return true;
        else
            return false;
    }

    //query per gestire l'ultimo indice memorizzato sul server

    public void insertUser(String user) {
        this.openWriteableDB();
        ContentValues cv = new ContentValues();
        cv.put(USER_ID,user);
        cv.put(USER_LASTID,0);
        db.insert(USER_TABLE,null,cv);
        this.closeDB();
    }

    public int getLastIndex(String user) {
        this.openReadableDB();
        String[] args = {USER_LASTID};
        String where = USER_ID + "= ?";
        String[] whereArgs = {user};
        Cursor cursor = db.query(USER_TABLE,args,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }

    public int setLastIndex(String user,int newIndex) {
        this.openWriteableDB();
        String where = USER_ID + "= ?";
        String[] whereArgs = {user};
        ContentValues cv = new ContentValues();
        cv.put(USER_LASTID,newIndex);
        int rowCount = db.update(USER_TABLE,cv,where,whereArgs);
        this.closeDB();
        return rowCount;
    }

    //query per ottenere l'indice più alto nella tabella delle sessioni
    public int getMaxLocalIndex() {
        this.openReadableDB();
        String[] args = {"MAX("+SESSION_ID+")"};
        Cursor cursor = db.query(SESSION_TABLE,args,null,null,null,null,null);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        if(cursor != null)
            cursor.close();
        this.closeDB();
        return result;
    }
}