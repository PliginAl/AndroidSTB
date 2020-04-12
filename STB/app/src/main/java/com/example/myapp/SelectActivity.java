package com.example.myapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;



public class SelectActivity extends AppCompatActivity {

    long id_org = 0;
    long id_locats = 0;
    long userId = 0;
    String[] headers;

    ListView orgList;
    ListView locatsList;


    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        initInstances();

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        orgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_org = id;
                createLocatsList();
            }
        });


        locatsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_locats = id;
                long idUser_idLocats[] = {userId,id_locats};
                Intent intent = new Intent(getApplicationContext(), textSActivity.class);
                intent.putExtra("idUser_idLocats", idUser_idLocats);
                startActivity(intent);
            }
        });
    }


    private void initInstances() {

        orgList = (ListView)findViewById(R.id.org);
        locatsList = (ListView)findViewById(R.id.locats);
    }


    public void createLocatsList() {

        userCursor =  db.rawQuery("SELECT _id, nameloc FROM locats Where idorg = "+id_org, null);
        headers = new String[] {"nameloc"};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        locatsList.setAdapter(userAdapter);

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
        orgList.setAdapter(userAdapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

}


