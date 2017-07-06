package com.example.andro.tododo;

import java.io.Serializable;

/**
 * Created by andro on 28-06-2017.
 */

public class Task implements Serializable {

    int id;
    String title;
    String time;
    String date;
    String description;
    int priority;



    public Task(int id,String title, String time, String date, String description, int priority) {
        this.id=id;
        this.title = title;
        this.time = time;
        this.date = date;
        this.description = description;
        this.priority=priority;
    }
}
