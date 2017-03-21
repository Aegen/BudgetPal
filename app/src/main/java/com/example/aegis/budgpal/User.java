package com.example.aegis.budgpal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by Harrison on 2/25/2017.
 */

public class User {


    private long userID = -1;
    private String username;
    private String password;
    private String lastModified;
    private boolean deleted;
    private DatabaseHandler aDBHandler;

    private final static String TAG = "User";

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
        if(userID == -1) {
            this.aDBHandler.addUser(this);
        }else{
            this.aDBHandler.updateUser(this);
        }
    }

    public static User getUserByUsername(Context context, String username){
        SQLiteDatabase db = StatUtils.GetDatabase(context);
        Cursor cursee = db.rawQuery("SELECT * FROM User WHERE Username = '" + username + "'", null);
        User output = new User(context);
        if(cursee.getCount() > 0) {
            output.setUsername(cursee.getString(cursee.getColumnIndex(context.getString(R.string.User_Username))));
            output.setPassword(cursee.getString(cursee.getColumnIndex(context.getString(R.string.User_Password))));
            output.setDeleted(false);
            output.setLastModified(cursee.getString(cursee.getColumnIndex(context.getString(R.string.User_LastModified))));
            output.setUserID(cursee.getLong(cursee.getColumnIndex(context.getString(R.string.User_UserID))));
        }
        return output;
    }

    public static User getUserByUserID(Context context, long UserID){
        SQLiteDatabase db = StatUtils.GetDatabase(context);

        Cursor curs = db.rawQuery("SELECT * FROM User WHERE UserID = " + UserID + " AND Deleted = 0", null);
        if(curs.getCount() > 0){
            curs.moveToFirst();
        }else {
            return null;
        }

        User tempU = new User(context);
        tempU.setUserID(UserID);
        tempU.setDeleted(false);
        tempU.setLastModified(curs.getString(curs.getColumnIndex("LastModified")));
        tempU.setPassword(curs.getString(curs.getColumnIndex("HashedPassword")));
        tempU.setUsername(curs.getString(curs.getColumnIndex("Username")));

        return tempU;
    }
}
