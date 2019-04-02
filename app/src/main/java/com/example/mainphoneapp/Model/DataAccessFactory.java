package com.example.mainphoneapp.Model;

import android.content.Context;

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
