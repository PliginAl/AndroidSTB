package com.example.myapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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


public class SelectActivity extends AppCompatActivity {

    Button buttonImage;
    ImageView imageView;
    Intent takePictureIntent;
    String mCurrentPhotoPath;
    InputStream ims;
    byte[] image;
    private static final int REQUEST_TAKE_PHOTO = 1;

    EditText nameBox;
    EditText yearBox;
    Button delButton;
    Button saveButton;

    Bitmap bp;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //Для обхода
        // StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        initInstances();


        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            nameBox.setText(userCursor.getString(1));
            yearBox.setText(String.valueOf(userCursor.getInt(2)));
            image = userCursor.getBlob(3);
            this.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }

    }




    private void initInstances() {

        nameBox = (EditText) findViewById(R.id.name);
        yearBox = (EditText) findViewById(R.id.year);
        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        this.imageView = (ImageView) this.findViewById(R.id.IV);
        this.buttonImage = (Button) this.findViewById(R.id.photo);

        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
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


    //Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();



    void captureImage() throws IOException{
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_TAKE_PHOTO);

            }
        } else {

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }

        }


    }



   /* void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("Access to External Storage is required")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .show();
    }*/



    // Here, thisActivity is the current activity




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
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {






                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
                imageView.setImageBitmap(BitmapFactory.decodeStream(ims));
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
        }
    }



    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }*/




    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(yearBox.getText().toString()));


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageView.buildDrawingCache();
        bp = imageView.getDrawingCache();
        bp.compress(Bitmap.CompressFormat.PNG, 0, stream);
        cv.put(DatabaseHelper.COLUMN_PHOTO, stream.toByteArray());
        // convert from bitmap to byte array
        //public static byte[] getBytes(Bitmap bitmap) {


        //bp.compress(Bitmap.CompressFormat.PNG, 0, stream);
        //return stream.toByteArray();
        //}

        //cv.put(DatabaseHelper.COLUMN_PHOTO, stream.toByteArray());


        if (userId > 0) {
            // this.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            //cv.put(DatabaseHelper.COLUMN_PHOTO, image);
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        } else {

            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //this.imageView.setImageBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.toByteArray().length));
            //imageView.buildDrawingCache();
            //bp = imageView.getDrawingCache();
            //bp = BitmapFactory.decodeStream(ims);
            //bp.compress(Bitmap.CompressFormat.PNG, 0, stream);
            //return stream.toByteArray();
            //}
            cv.put(DatabaseHelper.COLUMN_PHOTO, stream.toByteArray());
            db.insert(DatabaseHelper.TABLE, null, cv);


        }
        goHome();
    }


    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}