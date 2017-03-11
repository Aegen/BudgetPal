package com.example.aegis.budgpal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;

/**
 * Created by Aegis on 3/10/17.
 *
 * A library of static methods for use elsewhere in the app
 */

public class StatUtils {

    /**
     * Takes the input of an unhashed password(string)
     * and returns it after hashing it with SHA-256
     */
    public static String GetHashedString(String rawSTR){

        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(rawSTR.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            hash = null;
        }

        return new String(hash);
    }

    /**
     * Pass getApplicationContext() to be given an instance of the database
     * @param cont
     * @return Instance of the database
     */
    public static SQLiteDatabase GetDatabase(Context cont){
        DatabaseHandler a = new DatabaseHandler(cont, "database", null, 1); //Create database accessor
        return a.getWritableDatabase();
    }

    public static String GetCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        return  tempDate;
    }

}
