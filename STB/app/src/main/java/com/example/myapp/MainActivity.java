package com.example.myapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    long id_pass;


    Boolean stbCode;
    Button controlButton;
    Button archiveButton;
    DatabaseHelper databaseHelper;
    ForCreateDB createDB;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlButton = (Button) findViewById(R.id.control);
        archiveButton = (Button) findViewById(R.id.archive);

        /*userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });*/

       /* userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                //intent.putExtra("id", id);
                //startActivity(intent);
                id_pass = id;
                loginButton.setVisibility(View.VISIBLE);
                passBox.setVisibility(View.VISIBLE);
            }
        });*/

        createDB = new ForCreateDB(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        //db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора

        //userCursor =  db.rawQuery("select _id, fio, posts from USERS;", null);
        //userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        //String[] headers = new String[] {"fio","posts"};
        //String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_YEAR};
        // создаем адаптер, передаем в него курсор
        //userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                //userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        //userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
        //userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        //userList.setAdapter(userAdapter);

    }





    public void control(View view){

       // userCursor =  db.rawQuery("select passsen from USERS where _id =" + id_pass, null);
       // userCursor.moveToFirst();
            Intent intent = new Intent(getApplicationContext(), STBActivity.class);
            stbCode = true;
            intent.putExtra("code", stbCode);
            startActivity(intent);
    }


    // по нажатию на кнопку запускаем UserActivity для добавления данных
    public void archive(View view){
        Intent intent = new Intent(this, STBActivity.class);
        stbCode = false;
        intent.putExtra("code", stbCode);
        startActivity(intent);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        //db.close();
        //userCursor.close();
    }
}
