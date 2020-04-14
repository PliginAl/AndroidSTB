package com.example.myapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends SimpleCursorAdapter {

    private int l;

    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        l=layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(l, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //String title = cursor.getString(cursor.getColumnIndex(AppData.COLUMN_TITLE));
        //Double price = Double.parseDouble(_cursor.getString(_cursor.getColumnIndex(AppData.COLUMN_PRICE)));
        TextView a = (TextView) view.findViewById(R.id.a);
        TextView b = (TextView) view.findViewById(R.id.b);
        TextView c = (TextView) view.findViewById(R.id.c);
        a.setText(cursor.getString(cursor.getColumnIndex("_id")));
        b.setText(cursor.getString(cursor.getColumnIndex("_id")));
        c.setText(cursor.getString(cursor.getColumnIndex("_id")));
    }

}
