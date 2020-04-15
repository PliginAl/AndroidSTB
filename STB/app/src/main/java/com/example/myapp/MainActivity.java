package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Boolean stbCode;
    Button controlButton;
    Button archiveButton;
    DatabaseHelper databaseHelper;
    ForCreateDB createDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlButton = (Button) findViewById(R.id.control);
        archiveButton = (Button) findViewById(R.id.archive);

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



    public void control(View view){
            Intent intent = new Intent(getApplicationContext(), STBActivity.class);
            stbCode = true;
            intent.putExtra("code", stbCode);
            startActivity(intent);
    }

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
