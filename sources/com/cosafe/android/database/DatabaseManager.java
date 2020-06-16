package com.cosafe.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.cosafe.android.models.BreathingBean;
import com.cosafe.android.models.LocationBean;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static String ACCURACY_LOCATION = "acclog";
    private static final String DB_NAME = "SAFE_TOGETHER_DB";
    private static int DB_VERSION = 1;
    private static String EXHALE = "Exhale";
    private static String EXHALE_HOLD = "ExhaleHold";
    private static String HOME_LOCATION = "homeloc";
    private static String ID = "Id";
    private static String ID_LOCATION = "Id";
    private static String INHALE = "Inhale";
    private static String INHALE_HOLD = "InhaleHold";
    private static String LAT_LOCATION = "latloc";
    private static String LOG_LOCATION = "logloc";
    private static String LOG_TIME = "LogTime";
    private static String QUENTINE = "quentine";
    private static String TABLE_BREATHING_DATA = "TableBreathingData";
    private static String TABLE_LOCATION_DATA = "TableLocation";
    private static DatabaseManager mDBConnection;
    private final String TAG = "DatabaseManager";
    private int mOpenCounter;
    private final Context myContext;
    private SQLiteDatabase myDB;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
    }

    public static synchronized DatabaseManager getDBAdapterInstance(Context context) {
        DatabaseManager databaseManager;
        synchronized (DatabaseManager.class) {
            if (mDBConnection == null) {
                mDBConnection = new DatabaseManager(context);
            }
            databaseManager = mDBConnection;
        }
        return databaseManager;
    }

    public synchronized SQLiteDatabase openDatabase() {
        int i = this.mOpenCounter + 1;
        this.mOpenCounter = i;
        if (i == 1) {
            try {
                this.myDB = mDBConnection.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.myDB;
    }

    public synchronized void closeDatabase() {
        int i = this.mOpenCounter - 1;
        this.mOpenCounter = i;
        if (i == 0) {
            try {
                this.myDB.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public synchronized void insertBreathingData(BreathingBean breathingBean) {
        openDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(LOG_TIME, breathingBean.getLogTime());
            contentValues.put(INHALE, breathingBean.getInhale());
            contentValues.put(INHALE_HOLD, breathingBean.getInhaleHold());
            contentValues.put(EXHALE, breathingBean.getExhale());
            contentValues.put(EXHALE_HOLD, breathingBean.getExhaleHold());
            this.myDB.insert(TABLE_BREATHING_DATA, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDatabase();
    }

    public synchronized void insertLocationData(LocationBean locationBean) {
        openDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(LAT_LOCATION, locationBean.getLat());
            contentValues.put(LOG_LOCATION, locationBean.getLog());
            contentValues.put(ACCURACY_LOCATION, locationBean.getAccuracy());
            contentValues.put(HOME_LOCATION, locationBean.getHome_location());
            contentValues.put(QUENTINE, locationBean.getQurentine());
            this.myDB.insert(TABLE_LOCATION_DATA, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDatabase();
    }

    public synchronized ArrayList<BreathingBean> getBreathingData() throws SQLException {
        ArrayList<BreathingBean> arrayList;
        arrayList = new ArrayList<>();
        openDatabase();
        try {
            Cursor query = this.myDB.query(TABLE_BREATHING_DATA, new String[]{LOG_TIME, INHALE, INHALE_HOLD, EXHALE, EXHALE_HOLD}, null, null, null, null, null);
            if (query != null) {
                if (query.moveToFirst()) {
                    do {
                        BreathingBean breathingBean = new BreathingBean();
                        breathingBean.setLogTime(query.getString(0) == null ? "" : query.getString(0));
                        breathingBean.setInhale(query.getString(1) == null ? "" : query.getString(1));
                        breathingBean.setInhaleHold(query.getString(2) == null ? "" : query.getString(2));
                        breathingBean.setExhale(query.getString(3) == null ? "" : query.getString(3));
                        breathingBean.setExhaleHold(query.getString(4) == null ? "" : query.getString(4));
                        arrayList.add(breathingBean);
                    } while (query.moveToNext());
                }
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDatabase();
        return arrayList;
    }

    public synchronized ArrayList<LocationBean> getLocationData() throws SQLException {
        ArrayList<LocationBean> arrayList;
        arrayList = new ArrayList<>();
        openDatabase();
        try {
            Cursor query = this.myDB.query(TABLE_LOCATION_DATA, new String[]{LAT_LOCATION, LOG_LOCATION, ACCURACY_LOCATION}, null, null, null, null, null);
            if (query != null) {
                if (query.moveToFirst()) {
                    do {
                        LocationBean locationBean = new LocationBean();
                        locationBean.setLat(query.getString(0) == null ? "" : query.getString(0));
                        locationBean.setLog(query.getString(1) == null ? "" : query.getString(1));
                        locationBean.setAccuracy(query.getString(2) == null ? "" : query.getString(2));
                        locationBean.setHome_location(query.getString(2) == null ? "" : query.getString(3));
                        locationBean.setQurentine(query.getString(2) == null ? "" : query.getString(4));
                        arrayList.add(locationBean);
                    } while (query.moveToNext());
                }
                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDatabase();
        return arrayList;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table ");
        sb.append(TABLE_LOCATION_DATA);
        sb.append(" ( ");
        sb.append(ID_LOCATION);
        sb.append(" integer primary key autoincrement not null,");
        sb.append(LOG_LOCATION);
        String str = " text,";
        sb.append(str);
        sb.append(LOG_LOCATION);
        sb.append(str);
        sb.append(ACCURACY_LOCATION);
        sb.append(str);
        sb.append(HOME_LOCATION);
        sb.append(str);
        sb.append(QUENTINE);
        sb.append(" text );");
        sQLiteDatabase.execSQL(sb.toString());
    }
}
