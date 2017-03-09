package com.example.aegis.budgpal;

/**
 * Created by Harrison on 2/27/2017.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";

    private static final String TABLE_USERS = "User";
    private static final String TABLE_BUDGETS = "Budget";
    private static final String TABLE_EXPENSES = "Expense";
    private static final String TABLE_EVENTS = "Event";

    public static final String U_USER_ID = "UserID";
    public static final String U_USERNAME = "Username";
    public static final String U_PASSWORD = "HashedPassword";
    public static final String U_LAST_MODIFIED = "LastModified";
    public static final String U_DELETED = "Deleted";

    public static final String B_BUDGET_ID = "BudgetID";
    public static final String B_USER_ID = "UserID";
    public static final String B_TIME_PERIOD = "TimePeriod";
    public static final String B_RESET_CODE = "ResetCode";
    public static final String B_ANCHOR_DATE = "AnchorDate";
    public static final String B_START_DATE = "StartDate";
    public static final String B_LAST_MODIFIED = "LastModified";
    public static final String B_AMOUNT = "Amount";
    public static final String B_ACTIVE = "Active";
    public static final String B_DELETED = "Deleted";

    public static final String EX_EXPENSE_ID = "ExpenseID";
    public static final String EX_USER_ID = "UserID";
    public static final String EX_BUDGET_ID = "BudgetID";
    public static final String EX_AMOUNT = "Amount";
    public static final String EX_LAST_MODIFIED = "LastModified";
    public static final String EX_DATE_CREATED = "DateCreated";
    public static final String EX_CATEGORY = "Category";
    public static final String EX_DESCRIPTION = "Description";
    public static final String EX_EXEMPT = "Exempt";
    public static final String EX_DELETED = "Deleted";

    public static final String EV_EVENT_ID = "EventID";
    public static final String EV_USER_ID = "UserID";
    public static final String EV_LAST_MODIFIED = "LastModified";
    public static final String EV_START_DATE = "StartDate";
    public static final String EV_END_DATE = "EndDate";
    public static final String EV_DESCRIPTION = "Description";
    public static final String EV_DELETED = "Deleted";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("woot", "db");

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                U_USER_ID + " INTEGER PRIMARY KEY," +
                U_USERNAME + " STRING," +
                U_PASSWORD + " STRING," +
                U_LAST_MODIFIED + " DATETIME," +
                U_DELETED + " BIT" + ")";

        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGETS + "(" +
                B_BUDGET_ID + " INTEGER PRIMARY KEY," +
                B_USER_ID + " INTEGER," +
                B_TIME_PERIOD + " INTEGER," +
                B_RESET_CODE + " INTEGER," +
                B_ANCHOR_DATE + " DATETIME," +
                B_START_DATE + " DATETIME," +
                B_LAST_MODIFIED + " DATETIME," +
                B_AMOUNT + " FLOAT," +
                B_ACTIVE + " BIT," +
                B_DELETED + " BIT" + ")";

        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "(" +
                EX_EXPENSE_ID + " INTEGER PRIMARY KEY," +
                EX_USER_ID + " INTEGER," +
                EX_BUDGET_ID + " INTEGER," +
                EX_AMOUNT + " FLOAT," +
                EX_LAST_MODIFIED + " DATETIME," +
                EX_DATE_CREATED + " DATETIME," +
                EX_CATEGORY + " INTEGER," +
                EX_DESCRIPTION + " STRING," +
                EX_EXEMPT + " BIT," +
                EX_DELETED + " BIT" + ")";

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" +
                EV_EVENT_ID + " INTEGER PRIMARY KEY," +
                EV_USER_ID + " INTEGER," +
                EV_LAST_MODIFIED + " DATETIME," +
                EV_START_DATE + " DATETIME," +
                EV_END_DATE + " DATETIME," +
                EV_DESCRIPTION + " STRING," +
                EV_DELETED + " BIT" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BUDGET_TABLE);
        db.execSQL(CREATE_EXPENSE_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);

        db.execSQL("INSERT INTO User (Username, HashedPassword, LastModified, Deleted) VALUES ('harrison', 'password', '1996-01-01 12:00:00', 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        onCreate(db);
    }

    public void addUser(User user) {

        ContentValues values = new ContentValues();
        values.put(U_USER_ID, user.getUserID());
        values.put(U_USERNAME, user.getUsername());
        values.put(U_PASSWORD, user.getPassword());
        values.put(U_LAST_MODIFIED, user.getLastModified().toString());
        values.put(U_DELETED, user.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        Cursor idCursor = db.rawQuery("SELECT * FROM User WHERE " + U_USERNAME + " = " + user.getUsername() +" ORDER BY " + U_LAST_MODIFIED + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        user.setUserID(id);
        db.close();

    }

    public void addExpense(Expense expense) {

        ContentValues values = new ContentValues();
        values.put(EX_USER_ID, expense.getUserID());
        values.put(EX_BUDGET_ID, expense.getBudgetID());
        values.put(EX_AMOUNT, expense.getAmount());
        values.put(EX_LAST_MODIFIED, expense.getLastModified().toString());
        values.put(EX_DATE_CREATED, expense.getDateCreated().toString());
        values.put(EX_CATEGORY, expense.getCategory());
        values.put(EX_DESCRIPTION, expense.getDescription());
        values.put(EX_EXEMPT, expense.getExpenseID());
        values.put(EX_DELETED, expense.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EXPENSES, null, values);
        Cursor idCursor = db.rawQuery("SELECT * FROM Expense WHERE " + EX_USER_ID + " = " + expense.getUserID() +" ORDER BY " + EX_LAST_MODIFIED + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        expense.setExpenseID(id);
        db.close();

    }

    public void addEvent(Event event) {

        ContentValues values = new ContentValues();
        values.put(EV_USER_ID, event.getEventID());
        values.put(EV_LAST_MODIFIED, event.getLastModified().toString());
        values.put(EV_START_DATE, event.getStartDate().toString());
        values.put(EV_END_DATE, event.getEndDate().toString());
        values.put(EV_DESCRIPTION, event.getDescription());
        values.put(EV_DELETED, event.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EVENTS, null, values);

        Cursor idCursor = db.rawQuery("SELECT * FROM Event WHERE " + EV_USER_ID + " = " + event.getUserID() +" ORDER BY " + EV_LAST_MODIFIED + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        event.setEventID(id);
        
        db.close();

    }

    public void addBudget(Budget budget) {



        ContentValues values = new ContentValues();
        values.put(B_USER_ID, budget.getUserID());
        values.put(B_TIME_PERIOD, budget.getTimePeriod());
        values.put(B_RESET_CODE, budget.getResetCode());
        values.put(B_ANCHOR_DATE, budget.getAnchorDate().toString());
        values.put(B_START_DATE, budget.getStartDate().toString());
        values.put(B_LAST_MODIFIED, budget.getLastModified().toString());
        values.put(B_AMOUNT, budget.getAmount());
        values.put(B_ACTIVE, 1);
        values.put(B_DELETED, budget.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_BUDGETS, null, values);
        Cursor idCursor = db.rawQuery("SELECT * FROM Budget WHERE " + B_USER_ID + " = " + budget.getUserID() +" ORDER BY " + B_LAST_MODIFIED + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        budget.setBudgetID(id);

        db.close();
    }
}
