package com.example.mainphoneapp.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mainphoneapp.DB.IDataAccess;

import java.util.ArrayList;
import java.util.List;

public class SQLiteImpl implements IDataAccess {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "friend_table";


    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;


    public SQLiteImpl(Context context) {

        OpenHelper openHelper = new OpenHelper(context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }


    private static final String INSERT = "insert into " + TABLE_NAME
            + "(name, phone, lat, lon, mail, website, picture, birthday, address) values (?,?,?,?,?,?,?,?,?)";


    public long insert(BEFriend f) {
        this.insertStmt.bindString(1,f.getName());
        this.insertStmt.bindString(2,f.getPhone());
        this.insertStmt.bindDouble(3,f.getLat());
        this.insertStmt.bindDouble(4,f.getLon());
        this.insertStmt.bindString(5,f.getMail());
        this.insertStmt.bindString(6,f.getWebsite());
        this.insertStmt.bindString(7,f.getPicture());
        this.insertStmt.bindString(8,f.getBirthday());
        this.insertStmt.bindString(9,f.getAddress());

        long id = this.insertStmt.executeInsert();

        f.setId(id);

        return id;
    }


    public void deleteAll() {
        this.db.delete(TABLE_NAME, null, null);
    }


    public void deleteById(long id) {
        this.db.delete(TABLE_NAME, "id = ?", new String[]{""+id});
    }


    public void update(BEFriend p) {

    }


    public List<BEFriend> getAll() {
        List<BEFriend> list = new ArrayList<BEFriend>();
        Cursor cursor = this.db.query(TABLE_NAME,
                new String[]{"id", "name", "phone", "lat", "lon", "mail", "website", "picture", "birthday", "address"},
                null, null,
                null, null, "name");
        if (cursor.moveToFirst()) {
            do {
                list.add(new BEFriend(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)
                        ));
            }
            while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();

        }
        return list;
    }

    public BEFriend getById(long id) {

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[] {""+id});

        if (cursor.moveToFirst()) {
            return new BEFriend(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getDouble(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9)
            );
        }
        throw new IllegalArgumentException("Could not get Friend by id " + id);
    }


    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, name TEXT, phone TEXT, lat DOUBLE, lon DOUBLE, mail TEXT, website TEXT, picture TEXT, birthday TEXT, address TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
