package com.example.aegis.budgpal;

/**
 * Created by Harrison on 2/27/2017.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class UserDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";

    private static final String TABLE_USERS = "User";
    private static final String TABLE_BUDGETS = "Budget";
    private static final String TABLE_EXPENSES = "Expense";
    private static final String TABLE_EVENTS = "Event";

    public static final String U_USER_ID = "UserID";
    public static final String U_USERNAME = "Username";
    public static final String U_PASSWORD = "HashedPassword";
    public static final String U_LASTMODIFIED = "LastModified";
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

    public UserDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME,factory,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("woot", "db");

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                U_USER_ID + " INTEGER PRIMARY KEY," +
                U_USERNAME + " STRING," +
                U_PASSWORD + " STRING," +
                U_LASTMODIFIED + " DATETIME," +
                U_DELETED + " BOOLEAN" + ")";

        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGETS + "(" +
                B_BUDGET_ID + " INTEGER PRIMARY KEY," +
                B_USER_ID + " INTEGER," +
                B_TIME_PERIOD + " INTEGER," +
                B_RESET_CODE + " INTEGER," +
                B_ANCHOR_DATE + " DATETIME," +
                B_START_DATE + " DATETIME," +
                B_LAST_MODIFIED + " DATETIME," +
                B_AMOUNT + " FLOAT," +
                B_ACTIVE + " BOOLEAN," +
                B_DELETED + " BOOLEAN" + ")";

        String CREATE_EXPENSE_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "(" +
                EX_EXPENSE_ID + " INTEGER PRIMARY KEY," +
                EX_USER_ID + " INTEGER," +
                EX_BUDGET_ID + " INTEGER," +
                EX_AMOUNT + " FLOAT," +
                EX_LAST_MODIFIED + " DATETIME," +
                EX_DATE_CREATED + " DATETIME," +
                EX_CATEGORY + " INTEGER," +
                EX_DESCRIPTION + " STRING," +
                EX_EXEMPT + " BOOLEAN," +
                EX_DELETED + " BOOLEAN" + ")";

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" +
                EV_EVENT_ID + " INTEGER PRIMARY KEY," +
                EV_USER_ID + " INTEGER," +
                EV_LAST_MODIFIED + " DATETIME," +
                EV_START_DATE + " DATETIME," +
                EV_END_DATE + " DATETIME," +
                EV_DESCRIPTION + " STRING," +
                EV_DELETED + " BOOLEAN" + ")";

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
        values.put(U_LASTMODIFIED, user.getLastModified().toString());
        values.put(U_DELETED, user.isDeleted());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();

    }
}
