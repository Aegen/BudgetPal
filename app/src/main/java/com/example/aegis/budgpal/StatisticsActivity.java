package com.example.aegis.budgpal;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //get the current user's userID from the in-app preferences database
        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();
        long myUserID = Preferences.getLong("UserID",-1);

        //get budgets for the current user
        ArrayList<Budget> myBudgets = new ArrayList<Budget>();
        myBudgets = Budget.getBudgetsByUser(getApplicationContext(),myUserID);

        double thisBudgetAmount = 0;
        double thisBudgetExpenditures = 0;
        double thisBudgetExpByCategory = 0;
        long thisBudgetID;

        //loop through a users budget
        for(Budget aBudget: myBudgets){

        }

    }
}
