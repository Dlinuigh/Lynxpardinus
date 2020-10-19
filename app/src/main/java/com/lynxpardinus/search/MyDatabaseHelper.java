package com.lynxpardinus.search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private final String command;
    public MyDatabaseHelper(@Nullable Context mcontext, String com, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(mcontext, name, factory, version);
        context =mcontext;
        command = com;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(command);
        db.setLocale(Locale.CHINA);
        Toast.makeText(context, "成功创建了数据库",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
