package com.example.myapp;


import android.app.AppComponentFactory;
import android.app.ListActivity;
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
    String[] headers;

    ListView stbList;
    Button addButton;


    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    int[] to = new  int[] {R.id.a};


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
        addButton = (Button) findViewById(R.id.add);
    }



    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        //получаем данные из бд в виде курсора
        if (stbCode == true)
        {
            sql="SELECT idstb as _id\n" +
                    "FROM(\n" +
                    "SELECT idstb, MAX(begins), idstatusname\n" +
                    "FROM STATUS\n" +
                    "GROUP BY (idstb)) status1\n" +
                    "WHERE status1.idstatusname < 90";
        }
        else {
            addButton.setVisibility(View.GONE);
            sql="SELECT idstb as _id\n" +
                    "FROM(\n" +
                    "SELECT idstb, MAX(begins), idstatusname\n" +
                    "FROM STATUS\n" +
                    "GROUP BY (idstb)) status1\n" +
                    "WHERE status1.idstatusname > 90";
        }

        userCursor =  db.rawQuery(sql, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        headers = new String[] {"_id"};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.item,
                userCursor, headers, to, 0);
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

}


