package com.example.aegis.budgpal;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.icu.util.TimeUnit;
import android.util.Log;

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

    /**
     * Returns the output of hashing the string with SHA-256
     * @param rawSTR
     * @return
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
     * Gets the budget ID for the specified user
     * @param context
     * @param UserID
     * @return
     */
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

    /**
     * Gets the specified budget
     * @param context
     * @param budgetID
     * @return
     */
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

    /**
     * This method returns an Expense object associated with the given ExpenseID
     * @param context
     * @param ExpenseID
     * @return
     */
    public static Expense GetExpense(Context context, Long ExpenseID) {

        Expense tempExpense= new Expense(context);
        SQLiteDatabase db = GetDatabase(context);
        Cursor curs = db.rawQuery("SELECT * FROM Expense WHERE ExpenseID = " + ExpenseID + " AND Deleted = 0", null);

        if(curs.getCount() > 0) {
            curs.moveToFirst();

            //Obtain the index of each individual column within the Expense Table
            int expenseIDColumn = curs.getColumnIndex("ExpenseID");
            int userIDColumn = curs.getColumnIndex("UserID");
            int budgetIDColumn = curs.getColumnIndex("BudgetID");
            int amountColumn = curs.getColumnIndex("Amount");
            int lastModifiedColumn = curs.getColumnIndex("LastModified");
            int dateCreatedColumn = curs.getColumnIndex("DateCreated");
            int categoryColumn = curs.getColumnIndex("Category");
            int descriptionColumn = curs.getColumnIndex("Description");
            int exemptColumn = curs.getColumnIndex("Exempt");
            int deletedColumn = curs.getColumnIndex("Deleted");

            //Obtain the actual values at each of the previous indexes
            long expenseID =  curs.getLong(expenseIDColumn);
            long userID = curs.getLong(userIDColumn);
            long budgetID = curs.getLong(budgetIDColumn);
            float amount = curs.getFloat(amountColumn);
            String lastModified = curs.getString(lastModifiedColumn);
            String dateCreated = curs.getString(dateCreatedColumn);
            int category = curs.getInt(categoryColumn);
            String description = curs.getString(descriptionColumn);
            int exempt = curs.getInt(exemptColumn);
            int deleted = curs.getInt(deletedColumn);
            boolean deletedBool = false;
            boolean exemptBool = false;

            if(exempt != 0) {
                exemptBool = true;
            } else {
                exemptBool = false;
            }

            if(deleted != 0) {
                deletedBool = true;
            } else {
                deletedBool = false;
            }

            //Set each field of the new Expense
            tempExpense.setExpenseID(expenseID);
            tempExpense.setUserID(userID);
            tempExpense.setBudgetID(budgetID);
            tempExpense.setAmount(amount);
            tempExpense.setLastModified(lastModified);
            tempExpense.setDateCreated(dateCreated);
            tempExpense.setCategory(category);
            tempExpense.setDescription(description);
            tempExpense.setExempt(exemptBool);
            tempExpense.setDeleted(deletedBool);
        }
        return tempExpense;
    }

    /**
     * This method queries the database and then returns all expenses associated with the given UserID
     * @param context
     * @param UserID
     * @return
     */
    public static ArrayList<Expense> getExpenses(Context context, Long UserID) {

        SQLiteDatabase db = GetDatabase(context);
        Cursor curs = db.rawQuery("SELECT * FROM Expense WHERE UserID = '" + UserID + "'", null);
        ArrayList<Expense> result = new ArrayList<Expense>();

        if(curs.getCount() > 0){
            curs.moveToFirst();

            while(!curs.isAfterLast()) {
                Expense tempExpense = new Expense(context);

                //Obtain the index of each individual column within the Expense Table
                int expenseIDColumn = curs.getColumnIndex("ExpenseID");
                int userIDColumn = curs.getColumnIndex("UserID");
                int budgetIDColumn = curs.getColumnIndex("BudgetID");
                int amountColumn = curs.getColumnIndex("Amount");
                int lastModifiedColumn = curs.getColumnIndex("LastModified");
                int dateCreatedColumn = curs.getColumnIndex("DateCreated");
                int categoryColumn = curs.getColumnIndex("Category");
                int descriptionColumn = curs.getColumnIndex("Description");
                int exemptColumn = curs.getColumnIndex("Exempt");
                int deletedColumn = curs.getColumnIndex("Deleted");

                //Obtain the actual values at each of the previous indexes
                long expenseID =  curs.getLong(expenseIDColumn);
                long userID = curs.getLong(userIDColumn);
                long budgetID = curs.getLong(budgetIDColumn);
                float amount = curs.getFloat(amountColumn);
                String lastModified = curs.getString(lastModifiedColumn);
                String dateCreated = curs.getString(dateCreatedColumn);
                int category = curs.getInt(categoryColumn);
                String description = curs.getString(descriptionColumn);
                int exempt = curs.getInt(exemptColumn);
                int deleted = curs.getInt(deletedColumn);
                boolean deletedBool = false;
                boolean exemptBool = false;

                if(exempt != 0) {
                    exemptBool = true;
                } else {
                    exemptBool = false;
                }

                if(deleted != 0) {
                    deletedBool = true;
                } else {
                    deletedBool = false;
                }
                //
                //Set each field of the new Expense
                tempExpense.setExpenseID(expenseID);
                tempExpense.setUserID(userID);
                tempExpense.setBudgetID(budgetID);
                tempExpense.setAmount(amount);
                tempExpense.setLastModified(lastModified);
                tempExpense.setDateCreated(dateCreated);
                tempExpense.setCategory(category);
                tempExpense.setDescription(description);
                tempExpense.setExempt(exemptBool);
                tempExpense.setDeleted(deletedBool);

                //Expense temp = new Expense(userID, budgetID, amount, dateCreated, category, description, exemptBool, context);
                result.add(tempExpense);
                curs.moveToNext();
            }
        }
        return result;
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
     * Determines whether the input string is a valid date
     * @param date
     * @return
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
            return false;
        }

        try{
            month = Integer.parseInt(blocks[1]);
        }catch (NumberFormatException e){
            return false;
        }

        if(month < 1 || month > 12){
            return false;
        }

        try{
            day = Integer.parseInt(blocks[2]);
        }catch (NumberFormatException e){
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

        if(day < 1 || day > maxDay){
            return false;
        }

        return true;
    }

    public static ArrayList<Event> GetAllEvents(Context context, Long UserID){
        SQLiteDatabase db = GetDatabase(context);
        ArrayList<Event> output = new ArrayList<Event>();

        Cursor curs = db.rawQuery("SELECT * FROM Event WHERE UserID = " + UserID + " AND Deleted = 0 ORDER BY StartDate DESC", null);

        if(curs.getCount() > 0) {
            curs.moveToFirst();
            while(!curs.isAfterLast()){
                Event tempEv = new Event(context);
                tempEv.setEventID(curs.getLong(curs.getColumnIndex("EventID")));
                tempEv.setDeleted(false);
                tempEv.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
                tempEv.setDescription(curs.getString(curs.getColumnIndex("Description")));
                tempEv.setUserID(UserID);
                tempEv.setStartDate(curs.getString(curs.getColumnIndex("StartDate")));
                tempEv.setEndDate(tempEv.getStartDate());
                curs.moveToNext();

                output.add(tempEv);
            }
        }

        curs.close();

        return output;
    }

    public static Event GetEvent(Context context, long EventID){
        SQLiteDatabase db = GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM Event WHERE EventID = " + EventID, null);
        curs.moveToFirst();
        Log.d("other id:", Long.toString(EventID));
        Event tempEv = new Event(context);
        tempEv.setEventID(EventID);
        tempEv.setDeleted(false);
        tempEv.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
        tempEv.setDescription(curs.getString(curs.getColumnIndex("Description")));
        tempEv.setUserID(curs.getLong(curs.getColumnIndex("UserID")));
        tempEv.setStartDate(curs.getString(curs.getColumnIndex("StartDate")));
        tempEv.setEndDate(tempEv.getStartDate());
        Log.d("Description", tempEv.getDescription());
        curs.close();
        return tempEv;
    }

    public static User GetUser(Context context, long UserID){
        SQLiteDatabase db = GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM User WHERE UserID = " + UserID + " AND Deleted = 0", null);
        if(curs.getCount() > 0){
            curs.moveToFirst();
        }else {
            return null;
        }

        User tempU = new User(context);
        tempU.setUserID(UserID);
        tempU.setDeleted(false);
        tempU.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
        tempU.setPassword(curs.getString(curs.getColumnIndex("HashedPassword")));
        tempU.setUsername(curs.getString(curs.getColumnIndex("Username")));

        return tempU;
    }

//    @TargetApi(9)
    public static Long DaysSince(String DateToCompare){

        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date curr;
        Date comp;
        try {
            curr = form.parse(GetCurrentDate());
            comp = form.parse(DateToCompare);
        }catch (ParseException e){
            return new Long(0);
        }

        Long diff = curr.getTime() - comp.getTime();
//        Log.d("Diff", diff.toString());
//        Log.d("Ceil", Double.toString(Math.ceil((double)diff/((1000 * 60 * 60 * 24)))));
        return (long)Math.ceil((double)diff/((1000 * 60 * 60 * 24)));
    }

    public static ArrayList<Budget> GetBudgets(Context context, Long UserID){
        SQLiteDatabase db = GetDatabase(context);
        ArrayList<Budget> output = new ArrayList<>();

        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE UserID = " + UserID + " ORDER BY StartDate DESC", null);

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
        Long BudgetID = GetBudgetID(context, UserID);

//      Long UID, int TP, int RC, String AD, String SD, float AM, Context context

        if(BudgetID != -1) {
            Budget original = StatUtils.GetBudget(context, StatUtils.GetBudgetID(context, UserID));
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

            }
        }else{
            return date;
        }

        time.setTime(time.getTime() + diff);


        return form.format(time);
    }

}
