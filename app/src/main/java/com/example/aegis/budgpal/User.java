package com.example.aegis.budgpal;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.app.Activity;
/**
 * Created by Harrison on 2/25/2017.
 */

public class User {


    private long userID;
    private String username;
    private String password;
    private String lastModified;
    private boolean deleted;
    private DatabaseHandler aDBHandler;

    public User(){

    }

    public User(String uName, String pw, String lastMod, boolean dl, DatabaseHandler adbH){
        this.username = uName;
        this.password = pw;
        this.lastModified = lastMod;
        this.deleted = dl;
        this.aDBHandler = adbH;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void pushToDatabase(){
        //DatabaseHandler a = new DatabaseHandler();
        aDBHandler.addUser(this);
    }
}
