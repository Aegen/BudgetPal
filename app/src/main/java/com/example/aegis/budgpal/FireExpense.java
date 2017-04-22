package com.example.aegis.budgpal;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Aegis on 4/12/17.
 */

@IgnoreExtraProperties
public class FireExpense {

    @Exclude
    public String expenseKey = "";
    public String userKey;
    public String budgetKey;
    public float amount;
    public String lastModified;
    public String dateCreated;
    public int category;
    public String description;
    public boolean exempt;

    private final static String TAG = "Expense";

    public FireExpense(){}

    public FireExpense(String userKey, String budgetKey, float amount, String lastModified, String dateCreated, int category, String description, boolean exempt){
        this.userKey = userKey;
        this.budgetKey = budgetKey;
        this.amount = amount;
        this.lastModified = lastModified;
        this.dateCreated = dateCreated;
        this.category = category;
        this.description = description;
        this.exempt = exempt;
    }

    @Exclude
    public Task<Boolean> pushToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("db");

        final TaskCompletionSource<Boolean> output = new TaskCompletionSource<>();

        final FireExpense tempExpense = this;

        myRef.child("Expenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean wasFound = false;

                if(!tempExpense.expenseKey.equals("")){
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        if(item.getKey().equals(tempExpense.expenseKey)){
                            myRef.child("Expenses").child(item.getKey()).setValue(tempExpense);
                            wasFound = true;
                            output.setResult(true);
                        }
                    }

                    if(!wasFound){
                        myRef.child("Expenses").push().setValue(tempExpense);
                        output.setResult(true);
                    }
                }else{
                    myRef.child("Expenses").push().setValue(tempExpense);
                    output.setResult(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }

    @Exclude
    public static Task<FireExpense> getExpenseByExpenseKey(final String expenseKey){
        final TaskCompletionSource<FireExpense> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Expenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(expenseKey)){
                    FireExpense temp = dataSnapshot.child(expenseKey).getValue(FireExpense.class);
                    temp.expenseKey = expenseKey;
                    output.setResult(temp);
                }else{
                    output.setResult(new FireExpense("blank", "blank", (float)-1, "blank", "blank", -1, "1990-01-01", false));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }

    @Exclude
    public static Task<ArrayList<FireExpense>> getExpensesByUser(final String userKey){
        final ArrayList<FireExpense> holder = new ArrayList<>();

        final TaskCompletionSource<ArrayList<FireExpense>> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Expenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("userKey").getValue(String.class).equals(userKey)){
                        FireExpense tempExpense = item.getValue(FireExpense.class);
                        tempExpense.expenseKey = item.getKey();

                        holder.add(tempExpense);
                    }
                }

                output.setResult(holder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }

    @Exclude
    public static Task<ArrayList<FireExpense>> getExpensesByBudget(final String budgetKey){
        final ArrayList<FireExpense> holder = new ArrayList<>();

        final TaskCompletionSource<ArrayList<FireExpense>> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Expenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("budgetKey").getValue(String.class).equals(budgetKey)){
                        FireExpense tempExpense = item.getValue(FireExpense.class);
                        tempExpense.expenseKey = item.getKey();

                        holder.add(tempExpense);
                    }
                }

                output.setResult(holder);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }


    @Exclude
    public String getExpenseKey() {
        return expenseKey;
    }

    @Exclude
    public void setExpenseKey(String expenseID) {
        this.expenseKey = expenseKey;
    }

    @Exclude
    public String getUserKey() {
        return userKey;
    }

    @Exclude
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Exclude
    public String getBudgetKey() {
        return budgetKey;
    }

    @Exclude
    public void setBudgetKey(String budgetKey) {
        this.budgetKey = budgetKey;
    }

    @Exclude
    public float getAmount() {
        return amount;
    }

    @Exclude
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Exclude
    public String getLastModified() {
        return lastModified;
    }

    @Exclude
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Exclude
    public String getDateCreated() {
        return dateCreated;
    }

    @Exclude
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Exclude
    public int getCategory() {
        return category;
    }

    @Exclude
    public void setCategory(int category) {
        this.category = category;
    }

    @Exclude
    public String getDescription() {
        return description;
    }

    @Exclude
    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude
    public boolean isExempt() {
        return exempt;
    }

    @Exclude
    public void setExempt(boolean exempt) {
        this.exempt = exempt;
    }

    @Exclude
    public boolean isDeleted() {
        return false;
    }

    @Exclude
    public void setDeleted(boolean deleted) {
        FireExpense holder = this;

        if(deleted == true){


            if(!holder.expenseKey.equals("")){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("db");

                myRef.child("Expenses").child(holder.expenseKey).removeValue();
            }
        }

    }

}
