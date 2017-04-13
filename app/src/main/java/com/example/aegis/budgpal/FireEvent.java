package com.example.aegis.budgpal;

import android.content.Context;

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
public class FireEvent {
    @Exclude
    public String eventKey = "";
    public String userKey;
    public String lastModified;
    public String startDate;
    public String endDate;
    public String description;

    public FireEvent(String userKey, String startDate, String endDate, String description, String lastModified){
        this.startDate = startDate;
        this.endDate = endDate;
        this.userKey = userKey;
        this.description = description;
        this.lastModified = lastModified;
    }

    public FireEvent(){}

    public void pushToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("db");

        final FireEvent tempEvent = this;

        myRef.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean wasFound = false;

                if(!tempEvent.eventKey.equals("")){
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        if(item.getKey().equals(tempEvent.eventKey)){
                            myRef.child("Events").child(item.getKey()).setValue(tempEvent);
                            wasFound = true;
                        }
                    }

                    if(!wasFound){
                        myRef.child("Events").push().setValue(tempEvent);
                    }
                }else{
                    myRef.child("Events").push().setValue(tempEvent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

