package com.cfish.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by GKX100217 on 2016/5/19.
 */
public class DBHelper extends SQLiteOpenHelper {
    //创建表
    private static final String CREATE_NOTE = "create table Note(" +
            "id integer primary key autoincrement,"+ "content text," + "time text";

    private Context mContext;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name,factory, version);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE);
        Toast.makeText(mContext, "db Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
