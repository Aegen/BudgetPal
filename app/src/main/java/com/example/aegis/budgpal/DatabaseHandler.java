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

    private static final String U_USER_ID = "UserID";
    private static final String U_USERNAME = "Username";
    private static final String U_PASSWORD = "HashedPassword";
    private static final String U_LAST_MODIFIED = "LastModified";
    private static final String U_DELETED = "Deleted";

    private static final String B_BUDGET_ID = "BudgetID";
    private static final String B_USER_ID = "UserID";
    private static final String B_TIME_PERIOD = "TimePeriod";
    private static final String B_RESET_CODE = "ResetCode";
    private static final String B_ANCHOR_DATE = "AnchorDate";
    private static final String B_START_DATE = "StartDate";
    private static final String B_LAST_MODIFIED = "LastModified";
    private static final String B_AMOUNT = "Amount";
    private static final String B_ACTIVE = "Active";
    private static final String B_DELETED = "Deleted";

    private static final String EX_EXPENSE_ID = "ExpenseID";
    private static final String EX_USER_ID = "UserID";
    private static final String EX_BUDGET_ID = "BudgetID";
    private static final String EX_AMOUNT = "Amount";
    private static final String EX_LAST_MODIFIED = "LastModified";
    private static final String EX_DATE_CREATED = "DateCreated";
    private static final String EX_CATEGORY = "Category";
    private static final String EX_DESCRIPTION = "Description";
    private static final String EX_EXEMPT = "Exempt";
    private static final String EX_DELETED = "Deleted";

    private static final String EV_EVENT_ID = "EventID";
    private static final String EV_USER_ID = "UserID";
    private static final String EV_LAST_MODIFIED = "LastModified";
    private static final String EV_START_DATE = "StartDate";
    private static final String EV_END_DATE = "EndDate";
    private static final String EV_DESCRIPTION = "Description";
    private static final String EV_DELETED = "Deleted";

    private final static String TAG = "DatabaseHandler";

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
        //values.put(U_USER_ID, user.getUserID());
        values.put(U_USERNAME, user.getUsername());
        values.put(U_PASSWORD, user.getPassword());
        values.put(U_LAST_MODIFIED, user.getLastModified());
        Log.d("Dateer", user.getLastModified().toString());
        values.put(U_DELETED, user.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        Cursor idCursor = db.rawQuery("SELECT * FROM User WHERE " + U_USERNAME + " = '" + user.getUsername() +"' ORDER BY " + U_USER_ID + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        user.setUserID(id);
        idCursor.close();
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(U_PASSWORD, user.getPassword());
        values.put(U_LAST_MODIFIED, StatUtils.GetCurrentDate());
        values.put(U_DELETED, user.isDeleted());

        if(user.getUserID() != -1) {
            db.update(TABLE_USERS, values, "UserID = " + user.getUserID(), null);
        }
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
        values.put(EX_EXEMPT, expense.isExempt());
        values.put(EX_DELETED, expense.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EXPENSES, null, values);
        Cursor idCursor = db.rawQuery("SELECT * FROM Expense WHERE " + EX_USER_ID + " = " + expense.getUserID() +" ORDER BY " + EX_EXPENSE_ID + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        expense.setExpenseID(id);
        idCursor.close();

    }

    public void updateExpense(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EX_AMOUNT, expense.getAmount());
        values.put(EX_DESCRIPTION, expense.getDescription());
        values.put(EX_CATEGORY, expense.getCategory());
        values.put(EX_DELETED, expense.isDeleted());
        values.put(EX_EXEMPT, expense.isExempt());
        values.put(EX_LAST_MODIFIED, StatUtils.GetCurrentDate());

        if(expense.getExpenseID() != -1) {
            db.update(TABLE_EXPENSES, values, "ExpenseID = " + expense.getExpenseID(), null);
        }

    }

    public void addEvent(Event event) {

        ContentValues values = new ContentValues();
        values.put(EV_USER_ID, event.getUserID());
        values.put(EV_LAST_MODIFIED, event.getLastModified().toString());
        values.put(EV_START_DATE, event.getStartDate().toString());
        values.put(EV_END_DATE, event.getEndDate().toString());
        values.put(EV_DESCRIPTION, event.getDescription());
        values.put(EV_DELETED, event.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EVENTS, null, values);

        Cursor idCursor = db.rawQuery("SELECT * FROM Event WHERE " + EV_USER_ID + " = " + event.getUserID() +" ORDER BY " + EV_EVENT_ID + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        event.setEventID(id);
        idCursor.close();
        


    }

    public void updateEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EV_DESCRIPTION, event.getDescription());
        values.put(EV_DELETED, event.isDeleted());
        values.put(EV_LAST_MODIFIED, StatUtils.GetCurrentDate());
        values.put(EV_START_DATE, event.getStartDate());
        values.put(EV_END_DATE, event.getStartDate());

        if(event.getEventID() != -1) {
            db.update(TABLE_EVENTS, values, "EventID = " + event.getEventID(), null);
        }
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
        Cursor idCursor = db.rawQuery("SELECT * FROM Budget WHERE " + B_USER_ID + " = " + budget.getUserID() +" ORDER BY " + B_BUDGET_ID + " DESC;",null);
        idCursor.moveToFirst();

        long id = idCursor.getLong(0);
        budget.setBudgetID(id);
        idCursor.close();
    }

    public void updateBudget(Budget budget){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(B_DELETED, budget.isDeleted());
        values.put(B_ACTIVE, budget.isActive());

        if(budget.getBudgetID() != -1) {
            db.update(TABLE_BUDGETS, values, "BudgetID = " + budget.getBudgetID(), null);
        }


    }
}
