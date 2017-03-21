package com.example.aegis.budgpal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.icu.util.TimeUnit;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * Created by Aegis on 3/10/17.
 *
 * A library of static methods for use elsewhere in the app
 */

public class StatUtils {


    private static final String TAG = "StatUtils";
    /**
     * Returns the output of hashing the string with SHA-256.
     * @param rawSTR Passworod to be hashed.
     * @return The resulting hashed string.
     */
    public static String GetHashedString(String rawSTR){

        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(rawSTR.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            Log.e(TAG, "GetHashedString - Hashing failed");
            return null;
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

    /**
     * Gets the date
     * @return
     */
    public static String GetCurrentDate(){
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        return  tempDate;
    }



    /**
     * Returns the code for the input string
     * @param name
     * @return
     */
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


    /**
     * Gets the code for the day of the month
     * @return
     */
    @TargetApi(24)
    public static int GetWeeklyResetCode(){

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

    @TargetApi(24)
    public static int GetMonthResetCode(){
        Calendar cal = Calendar.getInstance();

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Determines whether the input string is a valid date.
     * @param date String in the format yyyy-MM-dd.
     * @return Boolean indicating if the date is valid.
     */
    public static boolean IsValidDate(String date){
        String[] blocks = date.split("-");

        if(blocks.length != 3){
            return false;
        }

        int year = 0;
        int month = 0;
        int day = 0;
        try {
            year = Integer.parseInt(blocks[0]);
        }catch (NumberFormatException e){
            Log.e(TAG, "IsValidDate - Year parse failed");
            return false;
        }

        try{
            month = Integer.parseInt(blocks[1]);
        }catch (NumberFormatException e){
            Log.e(TAG, "IsValidDate - Month parse failed");
            return false;
        }

        if(month < 1 || month > 12){
            return false;
        }

        try{
            day = Integer.parseInt(blocks[2]);
        }catch (NumberFormatException e){
            Log.e(TAG, "IsValidDate - Day parse failed");
            return false;
        }

        int maxDay = 0;

        switch (month){
            case 1:
                maxDay = 31;
                break;
            case 2:
                maxDay = 28;
                break;
            case 3:
                maxDay = 31;
                break;
            case 4:
                maxDay = 30;
                break;
            case 5:
                maxDay = 31;
                break;
            case 6:
                maxDay = 30;
                break;
            case 7:
                maxDay = 31;
                break;
            case 8:
                maxDay = 30;
                break;
            case 9:
                maxDay = 31;
                break;
            case 10:
                maxDay = 31;
                break;
            case 11:
                maxDay = 30;
                break;
            case 12:
                maxDay = 31;
                break;
        }

        return !(day < 1 || day > maxDay);

    }


//    @TargetApi(9)
    public static Long DaysSince(String DateToCompare){

        if(IsValidDate(DateToCompare)) {
            SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
            Date curr;
            Date comp;
            try {
                curr = form.parse(GetCurrentDate());
                comp = form.parse(DateToCompare);
            } catch (ParseException e) {
                Log.wtf(TAG, "DaysSince - Date Parsing Failed");
                return 0L;
            }

            Long diff = curr.getTime() - comp.getTime();
//        Log.d("Diff", diff.toString());
//        Log.d("Ceil", Double.toString(Math.ceil((double)diff/((1000 * 60 * 60 * 24)))));
            return (long) Math.ceil((double) diff / ((1000 * 60 * 60 * 24)));
        }else {
            return 0L;
        }
    }

    public static ArrayList<Budget> GetBudgets(Context context, Long UserID){
        SQLiteDatabase db = GetDatabase(context);
        ArrayList<Budget> output = new ArrayList<>();

        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE UserID = " + UserID + " ORDER BY BudgetID DESC", null);

        if(curs.getCount() > 0) {
            curs.moveToFirst();
            while(!curs.isAfterLast()){
                Budget tempBudg = new Budget(context);
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
                curs.moveToNext();

                output.add(tempBudg);
            }
        }

        curs.close();

        return output;
    }

    public static void ChangeBudget(Context context, Long UserID){

        Budget original = Budget.getCurrentBudgetForUser(context, UserID);
//      Long UID, int TP, int RC, String AD, String SD, float AM, Context context

        if(original.getBudgetID() != -1) {
            Budget replacement = new Budget(UserID,
                    original.getTimePeriod(),
                    original.getResetCode(),
                    GetCurrentDate(),
                    GetCurrentDate(),
                    original.getAmount(),
                    context);

            original.setDeleted(true);

            original.pushToDatabase();
            replacement.pushToDatabase();
        }

    }

    public static String AddDaysToDate(String date, int days){
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");

        long diff = days * (1000 * 60 * 60 * 24);

        Log.d("hoper", Long.toString(diff));

        Date time = new Date();
        if(StatUtils.IsValidDate(date)) {
            try {
                time = form.parse(date);
            } catch (ParseException e) {
                Log.wtf(TAG, "AddDaysToDate - Date Parse Failed");
                return date;
            }
        }else{
            return date;
        }

        time.setTime(time.getTime() + diff);


        return form.format(time);
    }

    /**
     * Adds the items to the list view and sets the onclick listener for each item.
     * @param context The application context.
     */
    public static void InitializeNavigationDrawer(final Context context){

        String[] navDrawerItems = context.getResources().getStringArray(R.array.navListItems);
        final DrawerLayout NavDrawer = (DrawerLayout) ((Activity)context).findViewById(R.id.navDrawer);
        ListView NavList = (ListView) ((Activity)context).findViewById(R.id.navDrawerList);

        NavList.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, navDrawerItems));


        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.START);
                Intent tempIntent = SwitchManager.SwitchActivity(context, parent.getItemAtPosition(position).toString());

                if(tempIntent != null){
                    context.startActivity(tempIntent);
                }
            }
        });
    }

}


