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
import android.widget.CheckBox;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import static com.example.andro.tododo.R.id.editButton;

/**
 * Created by andro on 28-06-2017.
 */

public class TaskListAdapter extends ArrayAdapter<TaskRoom>{
    List<TaskRoom> toDoArrayList;
    Context context;
    OnListButtonClickedListener listButtonClickListener;
    public TaskListAdapter(@NonNull Context context, @LayoutRes int resource , List<TaskRoom> toDoArrayList) {
        super(context,0,toDoArrayList);
        this.context=context;
        this.toDoArrayList=toDoArrayList;
        try {
            this.listButtonClickListener = ((OnListButtonClickedListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnListButtonClickListner");
        }
    }
    static class ToDoDoViewHolder{
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        TextView priority;
        Button editButton;
        CheckBox itemCheckBox;
        public ToDoDoViewHolder(TextView titleTextView, TextView dateTextView, TextView timeTextView,TextView priority,Button editButton,CheckBox itemCheckBox) {
            this.titleTextView = titleTextView;
            this.dateTextView = dateTextView;
            this.timeTextView = timeTextView;
            this.editButton=editButton;
            this.priority=priority;
            this.itemCheckBox=itemCheckBox;
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
            CheckBox itemCheckbox=(CheckBox)convertView.findViewById(R.id.itemCheckBox);
            ToDoDoViewHolder toDoDoViewHolder=new ToDoDoViewHolder(titleTextView,timeTextView,dateTextView,priority,editButton,itemCheckbox);
            convertView.setTag(toDoDoViewHolder);
        }
        TaskRoom task=toDoArrayList.get(position);
        ToDoDoViewHolder toDoDoViewHolder =(ToDoDoViewHolder)convertView.getTag();
        toDoDoViewHolder.titleTextView.setText(task.getTitle());


        toDoDoViewHolder.dateTextView.setText(task.getDate()+"");
        toDoDoViewHolder.timeTextView.setText(task.getTime());
        toDoDoViewHolder.priority.setText("Priority is "+task.getPriority());
        Boolean checked=toDoDoViewHolder.itemCheckBox.isChecked();
        if(checked==true){
            toDoDoViewHolder.itemCheckBox.setChecked(!checked);
            //To Disable Checkbox Animation
            toDoDoViewHolder.itemCheckBox.jumpDrawablesToCurrentState();
        }
        toDoDoViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listButtonClickListener != null)
                    listButtonClickListener.listButtonClicked(view, position);
            }
        });
        toDoDoViewHolder.itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listButtonClickListener != null)
                    listButtonClickListener.listButtonClicked(view, position);
            }
        });
        return convertView;
    }
    interface OnListButtonClickedListener{
        void listButtonClicked(View v, int pos);
    }
}
