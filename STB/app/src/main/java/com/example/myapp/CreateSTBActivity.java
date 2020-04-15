package com.example.myapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CreateSTBActivity extends AppCompatActivity {

    String sql;
    long id_org = 0;
    long id_loc = 0;
    long id_desc = 0;

    Button saveButton;
    Spinner spinOrg, spinLoc, spinDesc;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    String[] from = new String[] {"name"};
    int[] to  = new int[]{android.R.id.text1};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newstb_activity);

        saveButton = (Button) findViewById(R.id.saveSTB);
        spinOrg = (Spinner) findViewById(R.id.spinOrg);
        spinLoc = (Spinner) findViewById(R.id.spinLoc);
        spinDesc = (Spinner) findViewById(R.id.spinDesc);

        databaseHelper = new DatabaseHelper(getApplicationContext());


         /*       spinOrg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                //intent.putExtra("id", id);
                //startActivity(intent);
                id_org = id;

            }
        });*/

        spinOrg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                id_org = selectedId;
                fill_spinLoc();
                //Toast toast = Toast.makeText(getApplicationContext(),  "Ваш выбор: " + selectedId, Toast.LENGTH_SHORT);
                //toast.show();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                id_loc = selectedId;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinDesc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                id_desc = selectedId;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // открываем подключение
        db = databaseHelper.getWritableDatabase();
    }




    public void fill_spinLoc() {

        //получаем данные из бд в виде курсора

        sql="SELECT locats._id, nameloc as name\n" +
                "FROM ORG INNER JOIN locats ON ORG._id=locats.idorg\n" +
                "WHERE ORG._id = " + id_org;

        userCursor =  db.rawQuery(sql, null);

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, userCursor, from, to,0);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinLoc.setAdapter(userAdapter);

    }



    @Override
    public void onResume() {
        super.onResume();

        //получаем данные из бд в виде курсора
        sql="SELECT _id, nameORG as name\n" +
                "FROM ORG";

        userCursor =  db.rawQuery(sql, null);

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, userCursor, from, to,0);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinOrg.setAdapter(userAdapter);

        sql = "SELECT _id, name1 as name\n" +
                "FROM texts";
        userCursor =  db.rawQuery(sql, null);

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, userCursor, from, to,0);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinDesc.setAdapter(userAdapter);
    }

    public void saveSTB(View view){

        Toast toast = Toast.makeText(getApplicationContext(),  "Ваш выбор: " + id_desc, Toast.LENGTH_SHORT);
        toast.show();

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        //db.close();
        //userCursor.close();
    }
}