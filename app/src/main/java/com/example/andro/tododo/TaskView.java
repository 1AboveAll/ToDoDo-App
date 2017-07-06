package com.example.andro.tododo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskView extends AppCompatActivity {
    TextView title;
    TextView time;
    TextView date;
    TextView description;
    Button taskfinishedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        title=(TextView)findViewById(R.id.title);
        time=(TextView)findViewById(R.id.time);
        date=(TextView)findViewById(R.id.date);
        description=(TextView)findViewById(R.id.description);
        taskfinishedButton=(Button)findViewById(R.id.taskCompletedButton);
        Intent i=getIntent();
        Task e=(Task)i.getSerializableExtra(ToDoOpenHelper.ID);
        title.setText(e.title);
        time.setText(e.time);
        date.setText(e.date);
        description.setText(e.description);
        final int id=e.id;
        taskfinishedButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskView.this);
                builder.setTitle("Task Finished");
                builder.setCancelable(false);
                builder.setMessage("Have you finished the task "+title.getText().toString()+" ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.toDoOpenHelperInstance(TaskView.this);
                        SQLiteDatabase database=toDoOpenHelper.getWritableDatabase();
                        database.delete(ToDoOpenHelper.TABLE_NAME,ToDoOpenHelper.ID+" = "+id,null);
                        Intent it = new Intent();
                        setResult(RESULT_OK, it);
                        Toast.makeText(TaskView.this,"Task "+title.getText().toString()+" Finished",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }
}
