package com.example.andro.tododo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by andro on 29-06-2017.
 */

public class ToDoOpenHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME="todo";
    public final static String ID="_id";
    public final static String TITLE="title";
    public final static String TIME="time";
    public final static String DATE="date";
    public final static String DESCRIPTION="description";
    public final static String PRIORITY="priority";
    public static ToDoOpenHelper toDoOpenHelper;

    public static ToDoOpenHelper toDoOpenHelperInstance(Context context){
        if(toDoOpenHelper==null){
            toDoOpenHelper=new ToDoOpenHelper(context);
        }
        return toDoOpenHelper;
    }

    private ToDoOpenHelper(Context context ) {
        super(context, "ToDo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String query="create table "+TABLE_NAME+" ( "+ID+" integer primary key autoincrement"+" , "+TITLE+" text "+" ," +
               " "+TIME+" text "+" , "+ DATE+" BIGINT "+" , "+DESCRIPTION+" text"+" , "+PRIORITY+" integer);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
