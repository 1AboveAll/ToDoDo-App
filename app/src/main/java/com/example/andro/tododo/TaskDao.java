package com.example.andro.tododo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by andro on 14-07-2017.
 */
@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY priority DESC")
    List<TaskRoom> getTasks();
    @Insert
    void insertTask(TaskRoom taskRoom);
    @Delete
    void deleteTask(TaskRoom taskRoom);
    @Query("Update tasks set title = :Title, time = :Time, date = :Date, description = :Description, priority = :Priority WHERE id = :Id" )
    void updateTask(String Title, String Time, long Date,String Description, int Priority, int Id);
    @Query("SELECT * FROM tasks WHERE id = (SELECT MAX(id) FROM tasks)")
    List<TaskRoom>addNewTask();
    @Query("DELETE FROM tasks WHERE id=:ID")
    void deleteUsingID(String ID);
    @Query("SELECT * FROM tasks WHERE id=:ID")
    List<TaskRoom>selectWhereID(String ID);

}
