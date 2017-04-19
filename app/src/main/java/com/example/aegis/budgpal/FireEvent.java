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

    @Exclude
    public Task<Boolean> pushToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("db");

        final TaskCompletionSource<Boolean> output = new TaskCompletionSource<>();

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
                            output.setResult(true);
                        }
                    }

                    if(!wasFound){
                        myRef.child("Events").push().setValue(tempEvent);
                        output.setResult(true);
                    }
                }else{
                    myRef.child("Events").push().setValue(tempEvent);
                    output.setResult(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  output.getTask();
    }

    @Exclude
    public static Task<FireEvent> getEventByEventKey(final String eventKey){
        final TaskCompletionSource<FireEvent> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(eventKey)){
                    FireEvent temp = dataSnapshot.child(eventKey).getValue(FireEvent.class);
                    temp.eventKey = eventKey;
                    output.setResult(temp);
                }else{
                    output.setResult(new FireEvent("blank", "1990-01-01", "1990-01-01", "blank", "1990-01-01"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }

    @Exclude
    public static Task<ArrayList<FireEvent>> getEventsByUserkey(final String userKey){
        final ArrayList<FireEvent> holder = new ArrayList<>();

        final TaskCompletionSource<ArrayList<FireEvent>> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("userKey").getValue(String.class).equals(userKey)){
                        FireEvent tempEvent = item.getValue(FireEvent.class);
                        tempEvent.eventKey = item.getKey();

                        holder.add(tempEvent);
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
    public String getEventKey() {
        return eventKey;
    }

    @Exclude
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Exclude
    public String getUserKey() {
        return userKey;
    }

    @Exclude
    public void setUserID(String userKey) {
        this.userKey = userKey;
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
    public String getStartDate() {
        return startDate;
    }

    @Exclude
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Exclude
    public String getEndDate() {
        return endDate;
    }

    @Exclude
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
    public boolean isDeleted() {
        return false;
    }

    @Exclude
    public void setDeleted(boolean deleted) {

    }
}

