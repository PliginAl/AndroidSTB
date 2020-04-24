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
    }
}
