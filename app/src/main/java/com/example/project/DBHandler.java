package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // Db name
    private static final String DB_NAME = "FinanceDB";

    // Db version
    private static final int DB_VERSION = 1;

    // table name
    private static final String TABLE_NAME = "myFinances";

    // columns
    private static final String ID_COL = "id"; // useless primary key in case we need it
    private static final String SPENT_COL = "spent";
    private static final String DESCRIPTION_COL = "description";
    private static final String CATEGORY_COL = "category";
    private static final String DATE_COL = "date"; // YYYY-MM-DD <- this should be the sql format

    // creating a constructor for our database handler.
    public DBHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Creates DB by running a SQLite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SPENT_COL + " DECIMAL(10,2),"
                + DESCRIPTION_COL + " TEXT,"
                + CATEGORY_COL + " TEXT,"
                + DATE_COL + " TEXT)";
        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // method adds new finance records to the db
    public void addNewFinanceRecord(double spent, String description, String category, String date)
    {
        // sqlite variable which calls writable method to write data to the db
        SQLiteDatabase db = this.getWritableDatabase();

        // Variable holds content values
        ContentValues values = new ContentValues();

        // passing values along with its key and value pair
        values.put(SPENT_COL, spent);
        values.put(DESCRIPTION_COL, description);
        values.put(CATEGORY_COL, category);
        values.put(DATE_COL, date);

        // after adding all values we are passing content values to our table.
        db.insert(TABLE_NAME, null, values);

        // Close db after adding db
        db.close();
    }

    // method reads financial records entered
    public ArrayList<FinanceModel> readFinances() {
        // variable to read database
        SQLiteDatabase db = this.getReadableDatabase();

        // search query: all data
        Cursor cursorEverything = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // new array list holds values searched
        ArrayList<FinanceModel> addedFinanceModelArrayList = new ArrayList<>();

        // moving cursor to first position
        if (cursorEverything.moveToFirst()) {
            do {
                // add data from cursor to array
                addedFinanceModelArrayList.add(new FinanceModel(
                     cursorEverything.getInt(1),
                     cursorEverything.getString(4),
                     cursorEverything.getString(2),
                     cursorEverything.getString(3)));
            } while (cursorEverything.moveToNext());
            // moves cursor to next row
        }

        // close cursor
        cursorEverything.close();
        // return our array list
        return addedFinanceModelArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // does not create the table if it exists
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_NAME));
        onCreate(db);
    }
}
