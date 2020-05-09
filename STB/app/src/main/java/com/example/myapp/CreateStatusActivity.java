package com.example.myapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateStatusActivity extends AppCompatActivity {



    Intent takePhotoIntent;
    String mCurrentPhotoPath;
    String sql, desc = " ", comment="";
    Boolean photo = false;
    InputStream ims;
    byte[] image;
    private static final int REQUEST_TAKE_PHOTO = 1;

    EditText et_comment;
    Button saveStatus;
    Button addPhoto;
    Spinner spinStatus;
    ImageView im_photo;
    CheckBox cb_zapret;
    TextView tv_desc;

    Bitmap bp;

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    long id_stb=0;
    long id_statusN=0;
    int zapret = 0;

    String[] from = new String[] {"status"};
    int[] to = new  int[] {R.id.t1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newstatus_activity);

        //Объявление элементов и обр. событий
        initInstances();


        databaseHelper = new DatabaseHelper(getApplicationContext());

        //Получение данных, переданных порождающим окном
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_stb = extras.getLong("id_stb");
            desc  = extras.getString("desc");
            comment = extras.getString("comment");
        }
        //

        //Обработчик события выбора элемента из списка
        spinStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                id_statusN = selectedId;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //
        // открываем подключение
        db = databaseHelper.getWritableDatabase();
        im_photo.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        tv_desc.setText(desc);

        if (comment.length()>0) {
            et_comment.setText(comment);
        }

        sql = "SELECT _id, namest as status\n" +
                "FROM statusN\n" +
                "WHERE _id > 0";
        userCursor =  db.rawQuery(sql, null);

        userAdapter = new SimpleCursorAdapter(this, R.layout.spin, userCursor, from, to,0);
        userAdapter.setDropDownViewResource(R.layout.spin);

        spinStatus.setAdapter(userAdapter);

    }



    private void initInstances() {

        et_comment = (EditText) findViewById(R.id.et_comment);
        saveStatus = (Button) findViewById(R.id.saveStatus);
        addPhoto = (Button) findViewById(R.id.addPhoto);
        spinStatus = (Spinner) findViewById(R.id.spinStatus);
        cb_zapret= (CheckBox) findViewById(R.id.cb_zapret);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

        this.im_photo = (ImageView) this.findViewById(R.id.im_photo);
        this.addPhoto = (Button) this.findViewById(R.id.addPhoto);

        //Обработчик события нажатия на кнопку "добавить изображение"
       this.addPhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    captureImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Вызываем камеру
    void captureImage() throws IOException{
        takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {



            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_TAKE_PHOTO);

            }
        } else {

            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                if (photoFile != null) {
                    Uri photoURI = null;
                    try {
                        photoURI = FileProvider.getUriForFile(this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                createImageFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }
    //


    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            ex.printStackTrace();
                            return;
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = null;
                            try {
                                photoURI = FileProvider.getUriForFile(this,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        createImageFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                        }
                    }




                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            try {
                ims = new FileInputStream(file);
                im_photo.setVisibility(View.VISIBLE);
                im_photo.setImageBitmap(BitmapFactory.decodeStream(ims));
                photo = true;
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });

            try {
                file.delete();
            }
            catch (Exception e){

            }
        }
    }


    public void saveStatus(View view){
        ContentValues cv = new ContentValues();
        cv.put("idstb", id_stb);
        cv.put("commentstatus", et_comment.getText().toString());
        cv.put("idstatusname", id_statusN);


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (photo.equals(true)){

            im_photo.buildDrawingCache();
            bp = im_photo.getDrawingCache();
            bp.compress(Bitmap.CompressFormat.PNG, 0, stream);
            cv.put("foto", stream.toByteArray());
        }

        if(cb_zapret.isChecked())
        {
            zapret = 1;
        }
        else
        {
            zapret = 0;
        }

        cv.put("zapret", zapret);
        db.insert("STATUS", null, cv);

        goHome();
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