package com.example.aegis.budgpal;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE BudgetID = " + budgetID + " AND Deleted = 0", null);

        if(curs.getCount() > 0){
            curs.moveToFirst();

            tempBudg.setBudgetID(curs.getLong(curs.getColumnIndex("BudgetID")));
            tempBudg.setUserID(curs.getLong(curs.getColumnIndex("UserID")));
            tempBudg.setTimePeriod(curs.getInt(curs.getColumnIndex("TimePeriod")));
            tempBudg.setResetCode(curs.getInt(curs.getColumnIndex("ResetCode")));
            tempBudg.setAnchorDate(curs.getString(curs.getColumnIndex("AnchorDate")));
            tempBudg.setStartDate(curs.getString(curs.getColumnIndex("StartDate")));
            tempBudg.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
            tempBudg.setAmount(curs.getFloat(curs.getColumnIndex("Amount")));
            tempBudg.setActive(false);
            int del = curs.getInt(curs.getColumnIndex("Deleted"));
            boolean tempBool;
            if(del == 0){
                tempBool = false;
            }else{
                tempBool = true;
            }
            tempBudg.setDeleted(tempBool);
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

    @TargetApi(24)
    public static int GetWeeklyResetCode(){

        Date a = new Date();

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.SUNDAY:
                return 1;

            case Calendar.MONDAY:
                return 2;

            case Calendar.TUESDAY:
                return 3;

            case Calendar.WEDNESDAY:
                return 4;

            case Calendar.THURSDAY:
                return 5;

            case Calendar.FRIDAY:
                return 6;

            case Calendar.SATURDAY:
                return 7;

            default:
                return 0;

        }

    }

}
