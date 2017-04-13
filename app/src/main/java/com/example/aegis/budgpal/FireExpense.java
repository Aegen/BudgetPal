package com.example.aegis.budgpal;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

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
    public void pushToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("db");

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
                        }
                    }

                    if(!wasFound){
                        myRef.child("Expenses").push().setValue(tempExpense);
                    }
                }else{
                    myRef.child("Expenses").push().setValue(tempExpense);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
