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
    public static Intent SwitchActiviy(Context context, String activity){
        Intent tempIntent;
        switch (activity){
            case "Homepage":
                tempIntent = new Intent(context, MainActivity.class);
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
            case "Settings":
                tempIntent = new Intent(context, LandingPage.class);
                break;
            default:
                Log.e("Invalid Selection", activity + " failed to match with an activity.");
                return null;
        }

        return tempIntent;
    }
}
