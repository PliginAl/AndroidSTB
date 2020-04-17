package com.example.myapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageActivity  extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    ImageView im_photo;
    TextView tv_photo;

    String sql;
    byte[] photo;
    Long id_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        im_photo = (ImageView) findViewById(R.id.im_photo_status);
        tv_photo = (TextView) findViewById(R.id.tv_photo);

        databaseHelper = new DatabaseHelper(getApplicationContext());



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_status = extras.getLong("id_status");
        }



        // открываем подключение
        db = databaseHelper.getReadableDatabase();
    }


    @Override
    public void onResume() {
        super.onResume();

        sql="SELECT foto\n" +
                "FROM STATUS\n" +
                "WHERE _id = " + id_status;

        userCursor =  db.rawQuery(sql, null);
        userCursor.moveToFirst();
       /* Toast toast = Toast.makeText(getApplicationContext(),  "Ваш выбор: " + userCursor.getBlob(0).toString(), Toast.LENGTH_SHORT);
        toast.show();*/


        if (userCursor.isNull(0)){
            tv_photo.setVisibility(View.VISIBLE);
            im_photo.setVisibility(View.GONE);
        }
        else{
            tv_photo.setVisibility(View.GONE);
            im_photo.setVisibility(View.VISIBLE);
            photo = userCursor.getBlob(0);
            this.im_photo.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
            userCursor.close();
            db.close();
        }

    }
}
