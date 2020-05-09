package com.example.myapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class StatusActivity extends AppCompatActivity {

    String sql, comment = "";
    long id_org = 0;
    long id_loc = 0;
    long id_desc = 0;
    long id_stb = 0;

    Button addButton;
    TextView tv_id, tv_org, tv_loc, tv_desc;
    ListView statusList;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    String[] from = new String[] {"stop","status","date","comment"};
    int[] to = new  int[] {R.id.stop, R.id.status, R.id.date, R.id.comment};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        addButton = (Button) findViewById(R.id.createStatus);
        statusList = (ListView) findViewById(R.id.list_status);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_org = (TextView) findViewById(R.id.tv_org);
        tv_loc = (TextView) findViewById(R.id.tv_loc);
        tv_desc = (TextView) findViewById(R.id.tv_desc);



        databaseHelper = new DatabaseHelper(getApplicationContext());



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_stb = extras.getLong("id_stb");
        }


        statusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id_status) {

                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                intent.putExtra("id_status", id_status);
                startActivity(intent);
            }
        });









        // открываем подключение
        db = databaseHelper.getWritableDatabase();
    }



    @Override
    public void onResume() {
        super.onResume();

        sql="SELECT STB._id, ORG.nameOrg as org, locats.nameloc as loc, texts.name1 as desc\n" +
                "FROM ((STB INNER JOIN ORG ON STB.idorg = ORG._id) \n" +
                "INNER  JOIN locats ON STB.idl = locats._id) \n" +
                "INNER JOIN texts ON STB.idt = texts._id\n" +
                "WHERE STB._id = "+id_stb;

        userCursor =  db.rawQuery(sql, null);
        userCursor.moveToFirst();


        tv_id.setText("№ ");
        tv_org.setText("Участок: ");
        tv_loc.setText("Выработка: ");
        tv_desc.setText("Описание: ");

        tv_id.append(userCursor.getString(0));
        tv_org.append(userCursor.getString(1));
        tv_loc.append(userCursor.getString(2));
        tv_desc.append(userCursor.getString(3));


        //получаем данные из бд в виде курсора
        sql="SELECT STATUS._id, zapret as stop, statusN.namest as status , begins as date, commentstatus as comment\n" +
                "FROM STATUS INNER JOIN statusN ON STATUS.idstatusname = statusN._id\n" +
                "WHERE STATUS.idstb = "+id_stb+" and statusN._id > 0\n" +
                "ORDER BY date DESC";

        userCursor =  db.rawQuery(sql, null);

        userCursor.moveToFirst();
        if (userCursor.getCount()>0){
            comment = userCursor.getString(4);
        }

        userAdapter = new SimpleCursorAdapter(this, R.layout.item_status,userCursor,from,to,0);
        statusList.setAdapter(userAdapter);

    }

    public void createStatus(View view){
        Intent intent = new Intent(getApplicationContext(), CreateStatusActivity.class);
        intent.putExtra("id_stb", id_stb);
        intent.putExtra("desc",tv_desc.getText().toString());
        intent.putExtra("comment", comment);
        startActivity(intent);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

    private void goHome(){
        // закрываем подключение
        db.close();
        userCursor.close();

        Intent intent = new Intent(this, STBActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}


