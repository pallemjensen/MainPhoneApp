package com.example.mainphoneapp.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteOpenHelper;

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

    @Override
    public long insert(BEFriend p) {
        return 0;
    }

    @Override
    public void deleteAll() {
        this.db.delete(TABLE_NAME, null, null);
    }

    @Override
    public void deleteById(long id) {
        this.db.delete(TABLE_NAME, "id = ?", new String[]{""+id});
    }

    @Override
    public void update(BEFriend p) {

    }

    @Override
    public List<BEFriend> getAll() {
        return null;
    }

    @Override
    public BEFriend getById(long id) {
        return null;
    }


    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + " (id INTEGER PRIMARY KEY, name TEXT, phone TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
