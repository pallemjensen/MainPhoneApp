package com.example.mainphoneapp.DB;

import android.content.Context;

public class DataAccessFactoryFirestore
{
    static IDataAccess mInstance;

    public static void init(Context context)
    {
        mInstance = new FirestoreImpl (context);
    }
    public static IDataAccess getInstance()
    {
        return mInstance;
    }
}
