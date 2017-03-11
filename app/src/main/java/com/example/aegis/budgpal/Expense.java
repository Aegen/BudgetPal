package com.example.aegis.budgpal;

import android.content.Context;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * Created by Harrison on 2/25/2017.
 */

public class Expense {


    private long expenseID = -1;
    private long userID;
    private long budgetID;
    private float amount;
    private String lastModified;
    private String dateCreated;
    private int category;
    private String description;
    private boolean exempt;
    private boolean deleted;
    private DatabaseHandler handler;

    public Expense(){}

    public Expense(Long UID, Long BID, Float AM, String DC, int CAT, String DESC, Boolean EX, Context context){
        if(DC.equals(null)){
            DC = StatUtils.GetCurrentDate();
        }
        this.userID = UID;
        this.budgetID = BID;
        this.amount = AM;
        this.lastModified = StatUtils.GetCurrentDate();
        this.dateCreated = DC;
        this.category = CAT;
        this.description = DESC;
        this.exempt = EX;
        this.deleted = false;
        this.handler = new DatabaseHandler(context, "database", null, 1);
    }

    public long getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(long expenseID) {
        this.expenseID = expenseID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(long budgetID) {
        this.budgetID = budgetID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExempt() {
        return exempt;
    }

    public void setExempt(boolean exempt) {
        this.exempt = exempt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void pushToDatabase(){
        if(this.expenseID == -1){
            this.handler.addExpense(this);
        }else {

        }
    }
}
