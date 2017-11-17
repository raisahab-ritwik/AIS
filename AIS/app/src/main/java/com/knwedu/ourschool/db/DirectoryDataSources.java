package com.knwedu.ourschool.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ddasgupta on 2/27/2016.
 */
public class DirectoryDataSources {

    //private SQLiteDatabase database;
    private DatabaseAdapter dbAdapter;
    Context context;

    public DirectoryDataSources(Context contx)
    {
        dbAdapter = new DatabaseAdapter(context);
    }


}
