package com.example.andro.tododo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTask extends AppCompatActivity {
    TextView addTask;
    EditText titleEditText;
    EditText timeEditText;
    EditText dateEditText;
    EditText descriptionEditText;
    TextView priorityTextView;
    SeekBar prioritySeekBar;
    Button finishButton;
    int priority=0;
    long epochTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        addTask=(TextView)findViewById(R.id.addTask);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        priorityTextView=(TextView)findViewById(R.id.priorityTextView);
        prioritySeekBar=(SeekBar)findViewById(R.id.prioritySeekBar);
        finishButton=(Button)findViewById(R.id.finishButton);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                int month = newCalendar.get(Calendar.MONTH);
                int year = newCalendar.get(Calendar.YEAR);
                int currentDay=newCalendar.get(Calendar.DAY_OF_MONTH);
                showDatePicker(AddTask.this, year, month, currentDay);
            }
        });
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hour= calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog= new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        int mHour=i;
                        int mMinute=i1;
                        if(mHour<10&&mMinute<10){
                            timeEditText.setText("0"+mHour+":"+"0"+mMinute);
                        }
                        else
                            if(mHour<10&&mMinute>10){
                                timeEditText.setText("0"+mHour+":"+mMinute);
                        }
                        else
                            if(mHour>10&&mMinute<10){
                                timeEditText.setText(mHour+":"+"0"+mMinute);
                            }
                            else
                                timeEditText.setText(mHour+":"+mMinute);
//                        String AM_PM;
//                        if(mHour<12){
//                            AM_PM="AM";
//                        }else {
//                            AM_PM = "PM";
//                            mHour=mHour-12;
//                        }
                    }
                },hour,minute,true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        prioritySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                priority=i;
                priorityTextView.setText("Priority is "+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Intent i=getIntent();

        if(i.getExtras()!=null){
            finishButton.setText("Finish Editing");
            addTask.setText("Edit Task");
            final Task e=(Task)i.getSerializableExtra(ToDoOpenHelper.ID);
            int id=e.id;
            ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.toDoOpenHelperInstance(AddTask.this);
            SQLiteDatabase database=toDoOpenHelper.getWritableDatabase();
            String addQuery="select * from "+ToDoOpenHelper.TABLE_NAME+" where "+ToDoOpenHelper.ID+" = "+id+" ;";
            Cursor cursor=database.rawQuery(addQuery,null);
            if(cursor.moveToNext()) {
                titleEditText.setText(cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TITLE)));
                descriptionEditText.setText(cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.DESCRIPTION)));
                timeEditText.setText(cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.TIME)));
                epochTime=cursor.getLong(cursor.getColumnIndex(ToDoOpenHelper.DATE));
                if(epochTime==0){
                    dateEditText.setText("");
                }else {
                    Date d = new Date(epochTime);
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String formatted = format.format(d);
                    dateEditText.setText(formatted);
                }
                priorityTextView.setText("Priority is " + cursor.getString(cursor.getColumnIndex(ToDoOpenHelper.PRIORITY)));
                prioritySeekBar.setProgress(cursor.getInt(cursor.getColumnIndex(ToDoOpenHelper.PRIORITY)));
            }

        }

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int latestID=0;
                ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.toDoOpenHelperInstance(AddTask.this);
                //ToDoOpenHelper toDoOpenHelper=new ToDoOpenHelper(AddTask.this);
                SQLiteDatabase database=toDoOpenHelper.getWritableDatabase();
                String title=titleEditText.getText().toString();
                String description=descriptionEditText.getText().toString();
                String time=timeEditText.getText().toString();
                if(getIntent().getExtras()!=null) {
                    Task e=(Task)getIntent().getSerializableExtra(ToDoOpenHelper.ID);
                    latestID=e.id;

                }
                if(latestID==0){
                    ContentValues cv = new ContentValues();
                    cv.put(ToDoOpenHelper.TITLE, title);
                    cv.put(ToDoOpenHelper.DESCRIPTION, description);
                    cv.put(ToDoOpenHelper.TIME, time);
                    cv.put(ToDoOpenHelper.PRIORITY, priority);
                    cv.put(ToDoOpenHelper.DATE, epochTime);
                    database.insert(ToDoOpenHelper.TABLE_NAME, null, cv);
                    Intent it = new Intent();
                    setResult(RESULT_OK, it);
                    finish();
                }
                else{
                    ContentValues cv = new ContentValues();
                    cv.put(ToDoOpenHelper.TITLE, title);
                    cv.put(ToDoOpenHelper.DESCRIPTION, description);
                    cv.put(ToDoOpenHelper.TIME, time);
                    cv.put(ToDoOpenHelper.PRIORITY, priority);
                    cv.put(ToDoOpenHelper.DATE, epochTime);
                    database.update(ToDoOpenHelper.TABLE_NAME,cv,ToDoOpenHelper.ID+" = "+latestID,null);
                    Intent it = new Intent();
                    setResult(RESULT_OK, it);
                    finish();
                }
            }
        });
    }


    public void showDatePicker(Context context, int initialYear, int initialMonth, int initialDay) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        epochTime = calendar.getTime().getTime();
                        dateEditText.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, initialYear, initialMonth, initialDay);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();

    }







}
