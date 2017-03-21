package com.example.aegis.budgpal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Aegis on 3/3/17.
 */

public class SwitchManager {

    private final static String TAG = "SwitchManager";
    /**
     *
     * @param context Should be given getApplicationContext()
     * @param activity Should be given the name that will match with the intended activity
     * @return An intent used to change activities
     */
    public static Intent SwitchActivity(Context context, String activity){
        Intent tempIntent;
        switch (activity){
            case "Homepage":
                tempIntent = new Intent(context, LandingPage.class);
                break;
            case "Change Budget":
                tempIntent = new Intent(context, SetBudget.class);
                break;
            case "Add Expense":
                tempIntent = new Intent(context, AddExpenses.class);
                break;
            case "Add Event":
                tempIntent = new Intent(context, AddEvent.class);
                break;
            case "View Expenses":
                tempIntent = new Intent(context, ViewHistory.class);
                break;
            case "View Events":
                tempIntent = new Intent(context, ViewEvents.class);
                break;
            case "Logout":
                tempIntent = new Intent(context, MainActivity.class);
                tempIntent.putExtra("CameFromLogout", true);
                break;
            case "Settings":
                tempIntent = new Intent(context, LandingPage.class);
                break;
            default:
                Log.e(TAG, activity + " failed to match with an activity.");
                return null;
        }

        return tempIntent;
    }
}
