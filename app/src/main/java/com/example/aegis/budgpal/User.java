package com.example.aegis.budgpal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

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

    public User(Context context){
        this.aDBHandler = new DatabaseHandler(context, "database", null, 1);
    }

    public User(String uName, String pw, boolean dl, Context context){
        this.username = uName;
        this.password = StatUtils.GetHashedString(pw);
        this.lastModified = StatUtils.GetCurrentDate();
        this.deleted = dl;
        this.aDBHandler = new DatabaseHandler(context, "database", null, 1);
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
        this.password = StatUtils.GetHashedString(password);
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
