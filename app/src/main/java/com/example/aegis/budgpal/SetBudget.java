package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class SetBudget extends AppCompatActivity {

    private final static String TAG = "SetBudgetActivity";

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        StatUtils.InitializeNavigationDrawer(this);

        SetupSaveButton();

        SetupPreviousBudgetText();

        SetupCheckBoxListeners();
    }

    private void SetupCheckBoxListeners(){
        final CheckBox DailyBox = (CheckBox)findViewById(R.id.budgetDailyCheckBox);
        final CheckBox WeeklyBox = (CheckBox)findViewById(R.id.budgetWeeklyCheckBox);
        final CheckBox BiweeklyBox = (CheckBox)findViewById(R.id.budgetBiweeklyCheckBox);
        final CheckBox MonthlyBox = (CheckBox)findViewById(R.id.budgetMonthlyCheckBox);

        final Button SaveButton  = (Button)findViewById(R.id.budgetSaveButton);

        DailyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveButton.setEnabled(true);

                if (DailyBox.isChecked()){
                    WeeklyBox.setChecked(false);
                    MonthlyBox.setChecked(false);
                    BiweeklyBox.setChecked(false);
                }
                else{
                    SaveButton.setEnabled(false);

                }
            }
        });
        WeeklyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveButton.setEnabled(true);

                if (WeeklyBox.isChecked()){
                    DailyBox.setChecked(false);
                    MonthlyBox.setChecked(false);
                    BiweeklyBox.setChecked(false);
                }
                else{
                    SaveButton.setEnabled(false);

                }
            }
        });
        MonthlyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveButton.setEnabled(true);

                if (MonthlyBox.isChecked()){
                    WeeklyBox.setChecked(false);
                    DailyBox.setChecked(false);
                    BiweeklyBox.setChecked(false);

                }else {
                    SaveButton.setEnabled(false);
                }
            }
        });


        BiweeklyBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SaveButton.setEnabled(true);

                if (BiweeklyBox.isChecked()){
                    WeeklyBox.setChecked(false);
                    DailyBox.setChecked(false);
                    MonthlyBox.setChecked(false);
                }
                else{
                    SaveButton.setEnabled(false);
                }
            }
        });
    }

    private void SetupSaveButton(){
        final CheckBox DailyBox = (CheckBox)findViewById(R.id.budgetDailyCheckBox);
        final CheckBox WeeklyBox = (CheckBox)findViewById(R.id.budgetWeeklyCheckBox);
        final CheckBox BiweeklyBox = (CheckBox)findViewById(R.id.budgetBiweeklyCheckBox);
        final CheckBox MonthlyBox = (CheckBox)findViewById(R.id.budgetMonthlyCheckBox);

        final Button SaveButton  = (Button)findViewById(R.id.budgetSaveButton);

        final String UserKey = Preferences.getString("UserKey", "");

        SaveButton.setEnabled(false);


                    //Code goes here

                    final EditText AmountField = (EditText)findViewById(R.id.newBudgetText);





                    SaveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        //Code goes here

                                        final FireBudget tempB = Tasks.await(FireBudget.getCurrentBudgetForUser(UserKey));

                                        if (!AmountField.getText().toString().equals("")) {

                                            if (!tempB.getLastModified().equals("1990-01-01")) {
                                                tempB.active = false;
                                                Tasks.await(tempB.pushToDatabase());
                                            }

                                            int TPC = getResources().getInteger(R.integer.DAY_CODE);
                                            int RSC = 1;
                                            if (DailyBox.isChecked()) {
                                                TPC = getResources().getInteger(R.integer.DAY_CODE);
                                                RSC = 1;
                                            } else if (WeeklyBox.isChecked()) {
                                                TPC = getResources().getInteger(R.integer.WEEK_CODE);
                                                RSC = StatUtils.GetWeeklyResetCode();
                                            } else if (BiweeklyBox.isChecked()) {
                                                TPC = getResources().getInteger(R.integer.BIWEEK_CODE);
                                                RSC = StatUtils.GetWeeklyResetCode();
                                            } else if (MonthlyBox.isChecked()) {
                                                TPC = getResources().getInteger(R.integer.MONTH_CODE);
                                                RSC = StatUtils.GetMonthResetCode();
                                            }
                                            String am = AmountField.getText().toString();
                                            float amo = Float.valueOf(am);
                                            FireBudget newB = new FireBudget(UserKey, TPC, RSC, StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), amo, true);
                                            try {
                                                Tasks.await(newB.pushToDatabase());
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                                            finish();
                                        }
                                    }catch (Exception e){
                                        Log.d(TAG, "Failed");
                                        Log.d(TAG, e.getMessage());
                                    }
                                }
                            });

                            t.start();

                        }
                    });




        /*final EditText AmountField = (EditText)findViewById(R.id.newBudgetText);
        final Budget tempB = Budget.getCurrentBudgetForUser(getApplicationContext(), UserID);


        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AmountField.getText().toString().equals("")) {

                    if (tempB.getBudgetID() != -1) {
                        tempB.setDeleted(true);
                        tempB.pushToDatabase();
                    }

                    int TPC = getResources().getInteger(R.integer.DAY_CODE);
                    int RSC = 1;
                    if (DailyBox.isChecked()) {
                        TPC = getResources().getInteger(R.integer.DAY_CODE);
                        RSC = 1;
                    } else if (WeeklyBox.isChecked()) {
                        TPC = getResources().getInteger(R.integer.WEEK_CODE);
                        RSC = StatUtils.GetWeeklyResetCode();
                    } else if (BiweeklyBox.isChecked()) {
                        TPC = getResources().getInteger(R.integer.BIWEEK_CODE);
                        RSC = StatUtils.GetWeeklyResetCode();
                    } else if (MonthlyBox.isChecked()) {
                        TPC = getResources().getInteger(R.integer.MONTH_CODE);
                        RSC = StatUtils.GetMonthResetCode();
                    }
                    String am = AmountField.getText().toString();
                    float amo = Float.valueOf(am);
                    Budget newB = new Budget(UserID, TPC, RSC, StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), amo, getApplicationContext());
                    newB.pushToDatabase();

                    startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                    finish();
                }
            }
        });
        */
    }

    private void SetupPreviousBudgetText(){
        final TextView oldBudget = (TextView) findViewById(R.id.currentBudgetText);

        final Long UserID = Preferences.getLong("UserID", -1);

        final Budget tempB = Budget.getCurrentBudgetForUser(getApplicationContext(), UserID);

        if(tempB.getBudgetID() != -1){
            String period;

            switch (tempB.getTimePeriod()){
                case 1:
                    period = "day";
                    break;
                case 2:
                    period = "week";
                    break;
                case 3:
                    period = "2 weeks";
                    break;
                case 4:
                    period = "month";
                    break;
                default:
                    period = "cycle";
                    break;
            }

            oldBudget.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                    .format(tempB.getAmount()) + " per " + period);
        }
    }
}