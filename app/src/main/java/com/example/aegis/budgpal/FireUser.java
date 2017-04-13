package com.example.aegis.budgpal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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

import java.io.IOException;

/**
 * Created by Aegis on 4/11/17.
 */

@IgnoreExtraProperties
public class FireUser {

    public String name;
    public String hashedPassword;
    public String date;

    @Exclude
    public String userKey;

    @Exclude
    private final static String TAG = "User";

    public void pushToDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("db");

        final FireUser tempUser = this;

        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean wasFound = false;
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("name").getValue(String.class).equals(tempUser.name)){
                        myRef.child("Users").child(item.getKey()).setValue(tempUser);
                        wasFound = true;
                    }
                }

                if(!wasFound){
                    myRef.child("Users").push().setValue(tempUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public FireUser(){}

    public FireUser(String name, String hashedPassword, String date){
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.date = date;
    }


    @Exclude
    public String getUsername() {
        return this.name;
    }

    @Exclude
    public void setUsername(String name) {
        this.name = name;
    }

    @Exclude
    public String getPassword() {
        return this.hashedPassword;
    }

    @Exclude
    public void setPassword(String password) {
        this.hashedPassword = StatUtils.GetHashedString(password);
    }

    @Exclude
    public Task<Boolean> isDeleted() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        final TaskCompletionSource<Boolean> tcs = new TaskCompletionSource<>();

        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tcs.setResult(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        tcs.setException(new IOException(TAG, firebaseError.toException()));
                    }
                });

        return tcs.getTask();
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        final BoolWrap del = new BoolWrap(true);

        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("name").getValue(String.class).equals(name)){
                        del.deleted = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return del.deleted;*/
    }


    @Exclude
    public void setDeleted(boolean deleted) {
        FireUser holder = this;

        if(deleted == true){
            getUserKey().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    String hold = task.getResult();

                    if(!hold.equals("")){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("db");

                        myRef.child("Users").child(hold).removeValue();
                    }
                }
            });
        }
    }

    @Exclude
    public static Task<FireUser> getUserByUserName(final String username){
        final TaskCompletionSource<FireUser> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        ValueEventListener horse = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Boolean wasFound = false;
                Log.d(TAG, "Fireuser");
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("name").getValue(String.class).equals(username)){
                        FireUser temp = item.getValue(FireUser.class);
                        temp.userKey = item.getKey();
                        output.setResult(temp);
                        wasFound = true;
                    }
                }
                if(!wasFound){
                    output.setResult(new FireUser("blank", "blank", "1990-01-01"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.child("Users").addListenerForSingleValueEvent(horse);

        return output.getTask();
    }



    @Exclude
    public static Task<FireUser> getUserByUserKey(final String UserKey){
        final TaskCompletionSource<FireUser> output = new TaskCompletionSource<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(UserKey)){
                    output.setResult(dataSnapshot.child(UserKey).getValue(FireUser.class));
                }else{
                    output.setResult(new FireUser("blank", "blank", "1990-01-01"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }


    @Exclude
    public Task<String> getUserKey(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("db");

        //final BoolWrap temp = new BoolWrap();

        final TaskCompletionSource<String> output = new TaskCompletionSource<>();

        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean wasFound = false;
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    if(item.child("name").getValue(String.class).equals(name)){
                        output.setResult(item.getKey());
                        wasFound = true;
                    }
                }

                if(!wasFound){
                    output.setResult("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return output.getTask();
    }

    @IgnoreExtraProperties
    public class BoolWrap{
        @Exclude
        Boolean deleted = false;

        @Exclude
        String val;

        public BoolWrap(){}

        public BoolWrap(Boolean wrap){
            this.deleted = wrap;
        }
    }
}