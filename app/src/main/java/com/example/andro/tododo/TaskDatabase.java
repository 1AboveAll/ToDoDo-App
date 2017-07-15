package com.example.andro.tododo;

import android.animation.ObjectAnimator;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by andro on 14-07-2017.
 */
@Database(entities = {TaskRoom.class},version = 1)
public abstract class TaskDatabase extends RoomDatabase{


    private static TaskDatabase INSTANCE;
    private static Object LOCK=new Object();
    public static final String DB_NAME="tasks_db";

    public static TaskDatabase getInstance(Context context){

        if(INSTANCE==null){
            synchronized (LOCK){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,TaskDatabase.DB_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract TaskDao taskDao();

}
