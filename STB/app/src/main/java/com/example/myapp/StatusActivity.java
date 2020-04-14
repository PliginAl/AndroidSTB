package com.example.myapp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;



public class StatusActivity extends AppCompatActivity {

    long id_locats = 0;
    long id_texts = 0;
    long idUser_idLocats_[] = new long[3];

    long userId = 0;
    String[] headers;

    ListView textSList;
    Button addButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texts);

        initInstances();

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //idUser_idLocats = extras.getLongArray("idUser_idLocats");
        }

        textSList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_texts = id;
            }
        });

    }


    private void initInstances() {

        textSList = (ListView)findViewById(R.id.textS);
        addButton = (Button) findViewById(R.id.add);
    }




    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select _id, nameOrg from ORG;", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        headers = new String[] {"nameOrg"};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        textSList.setAdapter(userAdapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

}


