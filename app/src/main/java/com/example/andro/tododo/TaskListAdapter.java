package com.example.andro.tododo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;

import static com.example.andro.tododo.R.id.editButton;

/**
 * Created by andro on 28-06-2017.
 */

public class TaskListAdapter extends ArrayAdapter<Task>{

    ArrayList<Task> toDoArrayList;
    Context context;
//    OnListButtonClickedListener listButtonClickListener;
//
//    void setOnListButtonClickListener(OnListButtonClickedListener listButtonClickListener){
//        this.listButtonClickListener=listButtonClickListener;
//    }

    public TaskListAdapter(@NonNull Context context, @LayoutRes int resource , ArrayList<Task> toDoArrayList) {
        super(context,0,toDoArrayList);
        this.context=context;
        this.toDoArrayList=toDoArrayList;
    }

    static class ToDoDoViewHolder{
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView priority;
        Button editButton;


        public ToDoDoViewHolder(TextView titleTextView, TextView dateTextView, TextView timeTextView,TextView priority,Button editButton) {
            this.titleTextView = titleTextView;
            this.dateTextView = dateTextView;
            this.timeTextView = timeTextView;
            this.editButton=editButton;
            this.priority=priority;


        }
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null) {
            convertView= LayoutInflater.from(context).inflate(R.layout.item_view,null);
            TextView titleTextView =(TextView)convertView.findViewById(R.id.titleTextView);
            TextView dateTextView =(TextView)convertView.findViewById(R.id.dateTextView);
            TextView timeTextView =(TextView)convertView.findViewById(R.id.timeTextView);
            TextView priority=(TextView)convertView.findViewById(R.id.priority);

            Button editButton=(Button)convertView.findViewById(R.id.editButton);
            ToDoDoViewHolder toDoDoViewHolder=new ToDoDoViewHolder(titleTextView,timeTextView,dateTextView,priority,editButton);
            convertView.setTag(toDoDoViewHolder);
        }
        Task task=toDoArrayList.get(position);
        ToDoDoViewHolder toDoDoViewHolder =(ToDoDoViewHolder)convertView.getTag();

        toDoDoViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, AddTask.class);
                Task e=toDoArrayList.get(position);
                i.putExtra(ToDoOpenHelper.ID,e);
                ((Activity)context).startActivityForResult(i,2);
            }
        });

        //ToDoDoViewHolder toDoDoViewHolder=(ToDoDoViewHolder)convertView.getTag();
        toDoDoViewHolder.titleTextView.setText(task.title);
        toDoDoViewHolder.dateTextView.setText(task.date);
        toDoDoViewHolder.timeTextView.setText(task.time);
        toDoDoViewHolder.priority.setText("Priority is "+task.priority);



        return convertView;
    }
//    interface OnListButtonClickedListener{
//        void listButtonClicked(View v, int pos);
//    }

}
