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

    /**
     *
     * @param context Should be given getApplicationContext()
     * @param activity Should be given the name that will match with the intended activity
     * @param UserID Should be provided a userID that will be maintained throughout the session
     * @return An intent used to change activities
     */
    public static Intent SwitchActivity(Context context, String activity, Long UserID){
        Intent tempIntent;
        switch (activity){
            case "Homepage":
                tempIntent = new Intent(context, LandingPage.class).putExtra("UserID", UserID);
                break;
            case "Change Budget":
                tempIntent = new Intent(context, SetBudget.class).putExtra("UserID", UserID);
                break;
            case "Add Expense":
                tempIntent = new Intent(context, AddExpenses.class).putExtra("UserID", UserID);
                break;
            case "Add Event":
                tempIntent = new Intent(context, AddEvent.class).putExtra("UserID", UserID);
                break;
            case "View Expenses":
                tempIntent = new Intent(context, ViewHistory.class).putExtra("UserID", UserID);
                break;
            case "View Events":
                tempIntent = new Intent(context, ViewEvents.class).putExtra("UserID", UserID);
                break;
            case "Logout":
                tempIntent = new Intent(context, MainActivity.class).putExtra("UserID", UserID);
                break;
            case "Settings":
                tempIntent = new Intent(context, LandingPage.class).putExtra("UserID", UserID);
                break;
            default:
                Log.e("Invalid Selection", activity + " failed to match with an activity.");
                return null;
        }

        return tempIntent;
    }
}
