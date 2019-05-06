package com.example.mainphoneapp.DB;

import android.content.Context;

public class DataAccessFactorySql {

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
