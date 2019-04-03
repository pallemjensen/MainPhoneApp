package com.example.mainphoneapp.DB;

import android.content.Context;

import com.example.mainphoneapp.Model.SQLiteImpl;

public class DataAccessFactory {

    static IDataAccess mInstance;

    public static void init(Context context)
    {
        mInstance = new SQLiteImpl(context);

    }
    public static IDataAccess getInstance()
    {
        return mInstance;
    }
}
