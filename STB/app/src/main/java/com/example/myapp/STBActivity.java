package com.example.myapp;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;


public class STBActivity extends AppCompatActivity {

    String sql;
    long id_stb = 0;
    Boolean stbCode;

    ListView stbList;
    Button addSTBButton;


    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    String[] from = new String[] {"date","org","loc","status","desc"};
    int[] to = new  int[] {R.id.date, R.id.org, R.id.loc, R.id.status, R.id.desc};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stb);

        initInstances();

        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stbCode = extras.getBoolean("code");
        }

        stbList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_stb) {
                Intent intent = new Intent(getApplicationContext(), StatusActivity.class);
                intent.putExtra("id_stb", id_stb);
                startActivity(intent);
            }
        });


    }

    private void initInstances() {
        stbList = (ListView)findViewById(R.id.list_stb);
        addSTBButton = (Button) findViewById(R.id.addSTB);
    }

    public void createSimCurAd() {
        userAdapter = new SimpleCursorAdapter(this, R.layout.item,userCursor,from,to,0);
    }



    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        //получаем данные из бд в виде курсора
        if (stbCode == true)
        {
            sql="SELECT STB._id, tab1.date as date, ORG.nameOrg as org, locats.nameloc as loc, tab1.namest as status, texts.name1 as desc\n" +
                    "FROM\n" +
                    "(((((SELECT idstb, MAX(begins) as date, idstatusname, statusN.namest \n" +
                    "FROM STATUS INNER JOIN statusN ON STATUS.idstatusname = statusN._id\n" +
                    "GROUP BY (idstb)) AS tab1) \n" +
                    "INNER JOIN STB ON STB._id=tab1.idstb) \n" +
                    "INNER JOIN ORG ON STB.idorg = ORG._id) \n" +
                    "INNER JOIN locats ON STB.idl = locats._id) \n" +
                    "INNER JOIN texts ON STB.idt = texts._id\n" +
                    "WHERE tab1.idstatusname < 90";
        }
        else {
            addSTBButton.setVisibility(View.GONE);
            sql="SELECT STB._id, tab1.date as date, ORG.nameOrg as org, locats.nameloc as loc, tab1.namest as status, texts.name1 as desc\n" +
                    "FROM\n" +
                    "(((((SELECT idstb, MAX(begins) as date, idstatusname, statusN.namest \n" +
                    "FROM STATUS INNER JOIN statusN ON STATUS.idstatusname = statusN._id\n" +
                    "GROUP BY (idstb)) AS tab1) \n" +
                    "INNER JOIN STB ON STB._id=tab1.idstb) \n" +
                    "INNER JOIN ORG ON STB.idorg = ORG._id) \n" +
                    "INNER JOIN locats ON STB.idl = locats._id) \n" +
                    "INNER JOIN texts ON STB.idt = texts._id\n" +
                    "WHERE tab1.idstatusname > 90";
        }

        userCursor =  db.rawQuery(sql, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        // создаем адаптер, передаем в него курсор
        createSimCurAd();
      /*  userAdapter = new MyCursorAdapter(this, android.R.layout.list_item,
                userCursor, headers, new int[]{android.R.id.accessibilityActionContextClick}, 0);*/
        stbList.setAdapter(userAdapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }


    public void addSTB(View view){
        // userCursor =  db.rawQuery("select passsen from USERS where _id =" + id_pass, null);
        // userCursor.moveToFirst();
        Intent intent = new Intent(getApplicationContext(), CreateSTBActivity.class);
        startActivity(intent);
    }


}


