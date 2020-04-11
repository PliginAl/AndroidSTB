package com.example.myapp;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ForCreateDB extends SQLiteOpenHelper {

    public static final String TABLE = "sen.db";
    public static final String COLUMN_ID = "sen.db";
    public static final String COLUMN_YEAR = "sen.db";
    public static final String COLUMN_NAME = "sen.db";
    public static final String COLUMN_PHOTO = "sen.db";


    private static String DB_NAME = "sen.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public ForCreateDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }
}














/*import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {



    private static final String DATABASE_NAME = "sen.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PHOTO = "photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }







    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE ORG (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tparent int4 NULL,\n" +
                "\tnameOrg text NULL\n" +
                ");");
        db.execSQL("CREATE TABLE statusN (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tavtor INTEGER NULL,\n" +
                "\tnamest text NULL\n" +
                ");");
        db.execSQL("CREATE TABLE userS (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tidsen text NOT NULL,\n" +
                "\tavtor INTEGER NULL,\n" +
                "\tfio text NOT NULL,\n" +
                "\tphone text NULL,\n" +
                "\tpasssen text NULL,\n" +
                "\tposts text NOT NULL,\n" +
                "\tidORG INTEGER NOT NULL REFERENCES ORG(id),\n" +
                "\tseverenessOrg INTEGER NOT NULL DEFAULT 0\n" +
                ");");
        db.execSQL("CREATE TABLE machine (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tnamem text NULL\n" +
                ");");
        db.execSQL("CREATE TABLE orgMachine (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tidorg INTEGER REFERENCES ORG(id),\n" +
                "\tidm INTEGER REFERENCES machine(id),\n" +
                "\tnameMachine text NULL,\n" +
                "\tсost numeric NULL\n" +
                ");");
        db.execSQL("CREATE TABLE locats (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tidorg INTEGER REFERENCES ORG(id),\n" +
                "\tnameloc text NULL,\n" +
                "\tmarsrut text NULL\n" +
                ");");
        db.execSQL("CREATE TABLE texts (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tname1 TEXT NULL,\n" +
                "\tname2 text NULL,\n" +
                "\tname3 text NULL,\n" +
                "\tball INTEGER NULL,\n" +
                "\tstop INTEGER NULL\n" +
                ");");
        db.execSQL("CREATE TABLE textScan (\n" +
                "\tid INTEGER PRIMARY KEY,\n" +
                "\tparent INTEGER NULL,\n" +
                "\tidtext INTEGER REFERENCES texts(id)\n" +
                ");");
        db.execSQL("CREATE TABLE STB (\n" +
                "idmob INTEGER PRIMARY KEY,\n" +
                "\tidorg INTEGER REFERENCES ORG(id),\n" +
                "\tidm INTEGER REFERENCES orgMachine(id),\n" +
                "\ttm text NULL,\n" +
                "\tidl INTEGER REFERENCES locats(id),\n" +
                "\ttl text NULL,\n" +
                "\tidt INTEGER REFERENCES texts(id),\n" +
                "\tts text NULL,\n" +
                "\tmobiles NOT NULL DEFAULT 1\n" +
                ");");
        db.execSQL("CREATE TABLE STATUS (\n" +
                "    idmobs INTEGER PRIMARY KEY,\n" +
                "    idstb INTEGER REFERENCES STB(idmob),\n" +
                "    zapret INTEGER NOT NULL DEFAULT 0,\n" +
                "    xi INTEGER REFERENCES userS(id),\n" +
                "    begins REAL DEFAULT (datetime('now', 'localtime')),\n" +
                "    ends REAL DEFAULT (datetime('now', 'localtime')),\n" +
                "    idstatusname INTEGER REFERENCES statusN(id),\n" +
                "    commentstatus text NULL,\n" +
                "    us text NOT NULL DEFAULT 'sen3',\n" +
                "    foto BLOB NULL,\n" +
                "    mobiles NOT NULL DEFAULT 1\n" +
                ");");
        db.execSQL("CREATE TRIGGER STB_STATUS AFTER INSERT\n" +
                "ON STB\n" +
                "BEGIN\n" +
                "INSERT INTO STATUS(idstb, begins, ends, idstatusname, us) VALUES (NEW.idmob, DATETIME('now','localtime'), DATETIME('now','localtime'), 0, 'sen3');\n" +
                "END;");


        //db.execSQL("CREATE TABLE users (" + COLUMN_ID
            //    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
             //   + " TEXT, " + COLUMN_YEAR + " INTEGER, " + COLUMN_PHOTO + " BLOB);");
        // добавление начальных данных
      //  db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME
          //      + ", " + COLUMN_YEAR  + ") VALUES ('Том Смит', 1981);");
    }







    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}*/