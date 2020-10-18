package com.lynxpardinus.search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Locale;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private final String CREATE_ENTRY ="create table Entry("
            +"id integer primary key autoincrement,"
            +"kinds text,"
            +"name text,"+
            "usage text,"
            +"describe text,"
            +"example text," +
            "[CreatedTime] TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))," +
            "author text)";
    private final Context context;

    public MyDatabaseHelper(@Nullable Context mcontext, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(mcontext, name, factory, version);
        context =mcontext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ENTRY);
        db.setLocale(Locale.CHINA);
        Toast.makeText(context, "成功创建了数据库",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
