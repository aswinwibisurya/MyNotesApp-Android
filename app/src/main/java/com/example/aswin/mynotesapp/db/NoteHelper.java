package com.example.aswin.mynotesapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.aswin.mynotesapp.entity.Note;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.aswin.mynotesapp.db.DatabaseContract.NoteColumns.DATE;
import static com.example.aswin.mynotesapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.example.aswin.mynotesapp.db.DatabaseContract.NoteColumns.TITLE;
import static com.example.aswin.mynotesapp.db.DatabaseContract.TABLE_NOTE;

public class NoteHelper {
    private static String DATABASE_TABLE = TABLE_NOTE;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase db;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLiteException {
        dataBaseHelper = new DatabaseHelper(context);
        db = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public ArrayList<Note> query() {
        ArrayList<Note> arrayList = new ArrayList<>();
        Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null, _ID + " DESC");
        cursor.moveToFirst();
        Note note;
        if(cursor.getCount() > 0) {
            do {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                note.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));

                arrayList.add(note);
                cursor.moveToNext();
            } while(!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Note note) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, note.getTitle());
        initialValues.put(DESCRIPTION, note.getDescription());
        initialValues.put(DATE, note.getDate());

        return db.insert(TABLE_NOTE, null, initialValues);
    }

    public int update(Note note) {
        ContentValues args = new ContentValues();
        args.put(TITLE, note.getTitle());
        args.put(DESCRIPTION, note.getDescription());
        args.put(DATE, note.getDate());

        return db.update(TABLE_NOTE, args, _ID + "=?", new String[]{""+note.getId()});
    }

    public int delete(int id){
        return db.delete(TABLE_NOTE, _ID + " = '"+id+"'", null);
    }
}
