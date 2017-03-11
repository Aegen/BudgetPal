package com.example.aegis.budgpal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

/**
 * Created by Aegis on 3/10/17.
 *
 * A library of static methods for use elsewhere in the app
 */

public class StatUtils {

    /**
     * Takes the input of an unhashed password(string)
     * and returns it after hashing it with SHA-256
     */
    public static String GetHashedString(String rawSTR){

        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(rawSTR.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            hash = null;
        }

        return new String(hash);
    }

    /**
     * Pass getApplicationContext() to be given an instance of the database
     * @param cont Should be given getApplicationContext()
     * @return Instance of the database
     */
    public static SQLiteDatabase GetDatabase(Context cont){
        DatabaseHandler a = new DatabaseHandler(cont, "database", null, 1); //Create database accessor
        return a.getWritableDatabase();
    }

    public static String GetCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        return  tempDate;
    }

    public static Long GetBudgetID(Context context, Long UserID){
        SQLiteDatabase db = GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE UserID = " + UserID + " AND Deleted = 0", null);
        curs.moveToFirst();
        if(curs.getCount() > 0){
            return curs.getLong(curs.getColumnIndex("BudgetID"));
        }else{
            return new Long(-1);
        }
    }

    public static Budget GetBudget(Context context, Long budgetID){
        Budget tempBudg = new Budget(context);

        SQLiteDatabase db = GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE BudgetID = " + budgetID + " AND Deleted = 0 AND ", null);

        if(curs.getCount() > 0){
            //tempBudg.setActive();
        }

        return tempBudg;
    }

    public static int GetCategoryCode(String name){
        int code;
        switch (name){
            case "Grocery":
                code = 1;
                break;
            case "Payment":
                code = 2;
                break;
            case "Personal Expense":
                code = 3;
                break;
            case "Bills":
                code = 4;
                break;
            case "Miscellaneous":
                code = 5;
                break;
            default:
                code = 6;
                break;
        }

        return code;
    }

}
