package com.example.aegis.budgpal;

import android.content.Context;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * Created by Harrison on 2/25/2017.
 */

public class Budget {


    private long budgetID;
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
        this.handler.addBudget(this);
    }

}
