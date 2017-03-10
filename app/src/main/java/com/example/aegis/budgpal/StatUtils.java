package com.example.aegis.budgpal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

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
}
