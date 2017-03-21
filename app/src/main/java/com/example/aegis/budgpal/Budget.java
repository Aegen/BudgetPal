package com.example.aegis.budgpal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Harrison on 2/25/2017.
 */

public class Budget {


    private long budgetID = -1;
    private long userID;
    private int timePeriod;
    private int resetCode;
    private String anchorDate;
    private String startDate;
    private String lastModified;
    private float amount;
    private boolean active;
    private boolean deleted;
    private DatabaseHandler handler;

    private final static String TAG = "Budget";

    public Budget(Context context){
        this.handler = new DatabaseHandler(context, "database", null, 1);
    }

    public Budget(Long UID, int TP, int RC, String AD, String SD, float AM, Context context){
        this.userID = UID;
        this.timePeriod = TP;
        this.resetCode = RC;
        this.anchorDate = AD;
        this.startDate = SD;
        this.lastModified = StatUtils.GetCurrentDate();
        this.amount = AM;
        handler = new DatabaseHandler(context, "database", null, 1);
    }


    public long getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(long budgetID) {
        this.budgetID = budgetID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public int getResetCode() {
        return resetCode;
    }

    public void setResetCode(int resetCode) {
        this.resetCode = resetCode;
    }

    public String getAnchorDate() {
        return anchorDate;
    }

    public void setAnchorDate(String anchorDate) {
        this.anchorDate = anchorDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void pushToDatabase(){
        if(budgetID == -1) {
            this.handler.addBudget(this);
        }else{
            this.handler.updateBudget(this);
        }
    }

    public static Budget getCurrentBudgetForUser(Context context, long UserID){
        SQLiteDatabase db = StatUtils.GetDatabase(context);

        long BID;
        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE UserID = " + UserID + " AND Deleted = 0", null);
        curs.moveToFirst();
        if(curs.getCount() > 0){
            BID =  curs.getLong(curs.getColumnIndex("BudgetID"));
        }else{
            return new Budget(context);
        }

        return getBudgetByBudgetID(context, BID);
    }

    public static ArrayList<Budget> getBudgetsByUser(Context context, long UserID){
        SQLiteDatabase db = StatUtils.GetDatabase(context);
        ArrayList<Budget> output = new ArrayList<>();

        if(UserID != -1) {
            Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE UserID = " + UserID + " ORDER BY BudgetID DESC", null);

            if (curs.getCount() > 0) {
                curs.moveToFirst();
                while (!curs.isAfterLast()) {
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
                    if (del == 0) {
                        tempBool = false;
                    } else {
                        tempBool = true;
                    }
                    tempBudg.setDeleted(tempBool);
                    curs.moveToNext();

                    output.add(tempBudg);
                }
            }

            curs.close();
        }

        return output;
    }

    public static Budget getBudgetByBudgetID(Context context, long BudgetID){
        Budget tempBudg = new Budget(context);

        SQLiteDatabase db = StatUtils.GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM Budget WHERE BudgetID = " + BudgetID + " AND Deleted = 0", null);

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

}
