package com.example.aegis.budgpal;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Harrison on 2/25/2017.
 */

public class User {


    private int userID;
    private String username;
    private int password;
    private Date lastModified;
    private boolean deleted;

    public User(){

    }

    public User(int uID, String uName, int pw, Date lastMod, boolean dl){
        this.userID = uID;
        this.username = uName;
        this.password = pw;
        this.lastModified = lastMod;
        this.deleted = dl;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
