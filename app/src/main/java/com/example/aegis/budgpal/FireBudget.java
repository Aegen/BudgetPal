package com.example.aegis.budgpal;

import android.content.Context;

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
 * Created by Aegis on 4/13/17.
 */

@IgnoreExtraProperties
public class FireBudget {

    @Exclude
    public String budgetKey = "";
    public String userKey;
    public int timePeriod;
    public int resetCode;
    public String anchorDate;
    public String startDate;
    public String lastModified;
    public float amount;
    public boolean active;

    private final static String TAG = "Budget";

    public FireBudget(){}

    public FireBudget(String userKey, int timePeriod, int resetCode, String anchorDate, String startDate, String lastModified, float amount, boolean active){
        this.userKey = userKey;
        this.timePeriod = timePeriod;
        this.resetCode = resetCode;
        this.anchorDate = anchorDate;
        this.startDate = startDate;
        this.lastModified = lastModified;
        this.amount = amount;
        this.active = active;
    }

    @Exclude
    public void pushToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("db");

        final FireBudget tempBudget = this;

        myRef.child("Budgets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean wasFound = false;

                if(!tempBudget.budgetKey.equals("")){
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        if(item.getKey().equals(tempBudget.budgetKey)){
                            myRef.child("Budgets").child(item.getKey()).setValue(tempBudget);
                            wasFound = true;
                        }
                    }

                    if(!wasFound){
                        tempBudget.active = true;
                        myRef.child("Budgets").push().setValue(tempBudget);
                    }
                }else{
                    tempBudget.active = true;
                    myRef.child("Budgets").push().setValue(tempBudget);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Exclude
    public static Task<FireBudget> getCurrentBudgetForUser(final String userKey){
        //final FireBudget holder = new FireBudget();

        final TaskCompletionSource<FireBudget> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Budgets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean wasFound = false;

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("userKey").getValue(String.class).equals(userKey) && item.child("active").getValue(Boolean.class) == true){
                        FireBudget tempBudget = item.getValue(FireBudget.class);
                        tempBudget.budgetKey = item.getKey();

                        output.setResult(tempBudget);

                        wasFound = true;
                    }
                }

                if(!wasFound) {
                    output.setResult(new FireBudget("blank", -1, -1, "blank", "blank", "1990-01-01", (float) -1, false));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }

    @Exclude
    public static Task<ArrayList<FireBudget>> getBudgetsByUser(final String userKey){
        final ArrayList<FireBudget> holder = new ArrayList<>();

        final TaskCompletionSource<ArrayList<FireBudget>> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Budgets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("userKey").getValue(String.class).equals(userKey)){
                        FireBudget tempBudget = item.getValue(FireBudget.class);
                        tempBudget.budgetKey = item.getKey();

                        holder.add(tempBudget);
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
    public static Task<FireBudget> getBudgetByBudgetKey(final String budgetKey){
        final TaskCompletionSource<FireBudget> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Budgets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(budgetKey)){
                    FireBudget temp = dataSnapshot.child(budgetKey).getValue(FireBudget.class);
                    temp.budgetKey = budgetKey;
                    output.setResult(temp);
                }else{
                    output.setResult(new FireBudget("blank", -1, -1, "blank", "blank", "1990-01-01", (float)-1, false));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
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
    public String getUserKey() {return userKey;}

    @Exclude
    public void setUserID(String userKey) {
        this.userKey = userKey;
    }

    @Exclude
    public int getTimePeriod() {
        return timePeriod;
    }

    @Exclude
    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Exclude
    public int getResetCode() {
        return resetCode;
    }

    @Exclude
    public void setResetCode(int resetCode) {
        this.resetCode = resetCode;
    }

    @Exclude
    public String getAnchorDate() {
        return anchorDate;
    }

    @Exclude
    public void setAnchorDate(String anchorDate) {
        this.anchorDate = anchorDate;
    }

    @Exclude
    public String getStartDate() {
        return startDate;
    }

    @Exclude
    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
    public float getAmount() {
        return amount;
    }

    @Exclude
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Exclude
    public boolean isActive() {
        return active;
    }

    @Exclude
    public void setActive(boolean active) {
        this.active = active;
    }

    @Exclude
    public boolean isDeleted() {
        return false;
    }

    @Exclude
    public void setDeleted(boolean deleted) {

    }
}
