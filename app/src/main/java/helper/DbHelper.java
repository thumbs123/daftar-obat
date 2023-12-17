package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "daftar_oubat.db";
    public static final String TABLE_DEMAM = "demam";
    public static final String TABLE_GIGI = "gigi";
    public static final String TABLE_PERUT = "perut";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_IMAGE_PATH = "image_path";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db, TABLE_DEMAM);
        createTable(db, TABLE_GIGI);
        createTable(db, TABLE_PERUT);
    }

    private void createTable(SQLiteDatabase db, String tableName) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + tableName + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA + " TEXT NOT NULL, " +
                COLUMN_DESKRIPSI + " TEXT NOT NULL, " +
                COLUMN_IMAGE_PATH + " TEXT)";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEMAM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIGI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERUT);
        onCreate(db);
    }

    public void insertWithImage(String tableName, String nama, String deskripsi, String imagePath) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_IMAGE_PATH, imagePath);

        try {
            database.insert(tableName, null, values);
            Log.e("insert sqlite", "Data added to " + tableName);
        } catch (Exception e) {
            Log.e("insert sqlite", "Error: " + e.getMessage());
        }
    }

    public void updateWithImage(String tableName, int id, String nama, String deskripsi, String imagePath) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_IMAGE_PATH, imagePath);

        database.update(tableName, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        Log.e("update sqlite", "Data updated in " + tableName);
    }

    public void delete(String tableName, int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(tableName, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        Log.e("delete sqlite", "Data deleted from " + tableName);
    }

    public ArrayList<HashMap<String, String>> getAllData(String tableName) {
        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + tableName;
        SQLiteDatabase database = this.getWritableDatabase();

        try (Cursor cursor = database.rawQuery(selectQuery, null)) {
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_NAMA, cursor.getString(1));
                map.put(COLUMN_DESKRIPSI, cursor.getString(2));
                map.put(COLUMN_IMAGE_PATH, cursor.getString(3));
                wordList.add(map);
            }
        } catch (Exception e) {
            Log.e("select sqlite", "Error: " + e.getMessage());
        }
        return wordList;
    }
}