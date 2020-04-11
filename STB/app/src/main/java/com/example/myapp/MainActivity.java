package com.example.myapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper databaseHelper;
    ForCreateDB createDB;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userList = (ListView)findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        createDB = new ForCreateDB(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора

        userCursor =  db.rawQuery("select _id, fio, posts from USERS;", null);
        //userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {"fio","posts"};
        //String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_YEAR};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        //userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
        //userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }
    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void add(View view){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}
