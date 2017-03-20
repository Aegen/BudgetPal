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
    private SQLiteDatabase db;

    public Expense(Context context){
        this.handler = new DatabaseHandler(context, "database", null, 1);
    }

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
        this.db = StatUtils.GetDatabase(context);
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
            this.handler.updateExpense(this);//Not implemented yet
        }
    }

    public static Expense getExpenseByExpenseID(Context context, long ExpenseID){
        Expense tempExpense= new Expense(context);
        SQLiteDatabase db = StatUtils.GetDatabase(context);
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

    public static ArrayList<Expense> getExpensesByUser(Context context, long UserID){
        SQLiteDatabase db = StatUtils.GetDatabase(context);
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
}
