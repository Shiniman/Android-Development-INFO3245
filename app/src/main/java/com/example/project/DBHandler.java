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

    // myFinance table columns - column variables are strings cause they are names
    private static final String ID_COL = "id"; // useless primary key in case we need it
    private static final String SPENT_COL = "spent";
    private static final String DESCRIPTION_COL = "description";
    private static final String CATEGORY_COL = "category";
    private static final String DATE_COL = "date"; // YYYY-MM-DD <- this should be the sql format


    // table #2 name
    private static final String TABLE_NAME_2 = "myBudget";

    // myBudget table columns
    private static final String INCOME_COL = "Income";
    private static final String ENTERTAINMENT_COL = "Entertainment";
    private static final String UTILITIES_COL = "Utilities";
    private static final String FOOD_COL = "Food";
    private static final String OTHER_EXPENSES_COL = "Other";
    private static final String SAVINGS_GOAL_COL = "SavingsGoal";

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
        String myFinanceQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SPENT_COL + " DECIMAL(10,2),"
                + DESCRIPTION_COL + " TEXT,"
                + CATEGORY_COL + " TEXT,"
                + DATE_COL + " TEXT)";
        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(myFinanceQuery);

        String myBudgetQuery = "CREATE TABLE " + TABLE_NAME_2 + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT," // used for both to reduce redundancy
                + INCOME_COL + " DECIMAL(10,2),"
                + ENTERTAINMENT_COL + " DECIMAL(10,2),"
                + UTILITIES_COL + " DECIMAL(10,2),"
                + FOOD_COL + " DECIMAL(10,2),"
                + OTHER_EXPENSES_COL + " DECIMAL(10,2),"
                + SAVINGS_GOAL_COL + " DECIMAL(10,2))";
        db.execSQL(myBudgetQuery);
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

    public void addBudget(double netIncome, double entertainmentBudget, double utilitiesBudget, double foodBudget, double otherExpensesBudget, double savingsGoal)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Check if the table already has a row
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2 + " WHERE " + ID_COL + " = 1", null);
        boolean rowExists = cursor.moveToFirst();
        cursor.close();

        if (rowExists) { // if row with id 1 exists update values related to it
            values.put(INCOME_COL, netIncome);
            values.put(ENTERTAINMENT_COL, entertainmentBudget);
            values.put(UTILITIES_COL, utilitiesBudget);
            values.put(FOOD_COL, foodBudget);
            values.put(OTHER_EXPENSES_COL, otherExpensesBudget);
            values.put(SAVINGS_GOAL_COL, savingsGoal);

            db.update(TABLE_NAME_2, values, ID_COL + " = ?", new String[] {"1"});
        } else {
            // if no row with ID = 1 exists, insert a new row with default values
            values.put(INCOME_COL, netIncome);
            values.put(ENTERTAINMENT_COL, entertainmentBudget);
            values.put(UTILITIES_COL, utilitiesBudget);
            values.put(FOOD_COL, foodBudget);
            values.put(OTHER_EXPENSES_COL, otherExpensesBudget);
            values.put(SAVINGS_GOAL_COL, savingsGoal);

            db.insert(TABLE_NAME_2, null, values);
        }

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
                     cursorEverything.getDouble(1), // Index of spent column
                     cursorEverything.getString(2), // Index of description column
                     cursorEverything.getString(3), // Index of category column
                     cursorEverything.getString(4))); // Index of date column
            } while (cursorEverything.moveToNext());
            // moves cursor to next row
        }

        // close cursor
        cursorEverything.close();
        // return our array list
        return addedFinanceModelArrayList;
    }

    // method reads budget records entered
    public ArrayList<BudgetModel> readBudget() {
        // variable to read database
        SQLiteDatabase db = this.getReadableDatabase();

        // search query: all data
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);

        // new array list holds values searched
        ArrayList<BudgetModel> addedBudgetModelArrayList = new ArrayList<>();

        // moving cursor to first position
        if (cursor.moveToFirst()) {
            do {
                // add data from cursor to array
                addedBudgetModelArrayList.add(new BudgetModel(
                        cursor.getDouble(1), // Index of income column
                        cursor.getDouble(2), // Index of entertainment column
                        cursor.getDouble(3), // Index of utilities column
                        cursor.getDouble(4), // Index of food column
                        cursor.getDouble(5), // Index of other column
                        cursor.getDouble(6))); // Index for savings %
            } while (cursor.moveToNext());
            // moves cursor to next row
        }

        // close cursor
        cursor.close();
        // return our array list
        return addedBudgetModelArrayList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // does not create the table if it exists
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_NAME));
        db.execSQL(("DROP TABLE IF EXISTS " + TABLE_NAME_2));
        onCreate(db);
    }
}
