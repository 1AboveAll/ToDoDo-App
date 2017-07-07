package com.example.andro.tododo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TaskListAdapter.OnListButtonClickedListener {
    ListView mainListView;
    ArrayList<Task> toDoArrayList;
    TaskListAdapter taskListAdapter;
    ImageButton AddButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainListView=(ListView)findViewById(R.id.mainListView);
        AddButton=(ImageButton)findViewById(R.id.AddButton);
        AddButton.setOnClickListener(this);
        toDoArrayList=new ArrayList<>();
        taskListAdapter=new TaskListAdapter(this,0,toDoArrayList);
        mainListView.setAdapter(taskListAdapter);
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it=new Intent(MainActivity.this,TaskView.class);
                Task e=toDoArrayList.get(i);
                it.putExtra(ToDoOpenHelper.ID,e);
                startActivityForResult(it,3);
            }
        });
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos=i;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Task");
                builder.setCancelable(false);
                builder.setMessage("Are your Sure you want to Remove it ?");
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int it) {
                        Task e=toDoArrayList.get(pos);
                        int id=e.id;
                        ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.toDoOpenHelperInstance(MainActivity.this);
                        SQLiteDatabase database=toDoOpenHelper.getWritableDatabase();
                        database.delete(ToDoOpenHelper.TABLE_NAME,ToDoOpenHelper.ID+" = "+id,null);
                        populateToDo();
                        Toast.makeText(MainActivity.this,"Task "+e.title+" Removed Successfully",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Not Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        populateToDo();
    }
    @Override
    public void onClick(View view) {
        ImageButton imageButton=(ImageButton)view;
        if(imageButton.getId()==R.id.AddButton) {
            Intent i=new Intent(this,AddTask.class);
            startActivityForResult(i,1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                addToToDo();
                populateToDo();
            }
        }
        else if(requestCode==2){
            if(resultCode==RESULT_OK){
                populateToDo();
            }
        }
        else if(requestCode==3){
            if(resultCode==RESULT_OK){
                populateToDo();
                Toast.makeText(this,"Task Finished and Removed from the List",Toast.LENGTH_SHORT);
            }
        }
    }
    public void addToToDo(){
        ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.toDoOpenHelperInstance(this);
        SQLiteDatabase database=toDoOpenHelper.getWritableDatabase();
        String addQuery="select * from "+ToDoOpenHelper.TABLE_NAME+" where "+ToDoOpenHelper.ID+" = (select MAX("+ToDoOpenHelper.ID+") from "+ToDoOpenHelper.TABLE_NAME+" );";
        Cursor cursor=database.rawQuery(addQuery,null);
        if(cursor.moveToNext()) {
            String formatted="";
            String title = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.DESCRIPTION));
            String time = cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TIME));
            int id = cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.ID));
            long date = cursor.getLong(cursor.getColumnIndex(ToDoOpenHelper.DATE));
            int priority = cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.PRIORITY));
            if (date != 0) {
                Date d = new Date(date);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                formatted = format.format(d);
            }
                Task t = new Task(id, title, time, formatted, description, priority);
                toDoArrayList.add(t);
                taskListAdapter.notifyDataSetChanged();
            }
        }
    public void populateToDo(){
        String formatted="";
        ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.toDoOpenHelperInstance(this);
        SQLiteDatabase database=toDoOpenHelper.getReadableDatabase();
        toDoArrayList.clear();
        String addQuery="select * from "+ToDoOpenHelper.TABLE_NAME+" order by "+ToDoOpenHelper.PRIORITY+" desc;";
        Cursor cursor=database.rawQuery(addQuery,null);
        while(cursor.moveToNext()){
            String title=cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TITLE));
            String description=cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.DESCRIPTION));
            String time=cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TIME));
            int id=cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.ID));
            long date=cursor.getLong(cursor.getColumnIndex(ToDoOpenHelper.DATE));
            if(date!=0){
                Date d=new Date(date);
                DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                formatted=format.format(d);
            }
            int priority=cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.PRIORITY));
            Task t= new Task(id,title,time,formatted,description,priority);
            toDoArrayList.add(t);
        }
        taskListAdapter.notifyDataSetChanged();
    }
    @Override
    public void listButtonClicked(View v, int pos) {
        Intent i=new Intent(this, AddTask.class);
        Task e=toDoArrayList.get(pos);
        i.putExtra(ToDoOpenHelper.ID,e);
        startActivityForResult(i,2);
    }
}
