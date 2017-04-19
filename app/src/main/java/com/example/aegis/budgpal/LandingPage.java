package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class LandingPage extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "LandingPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        Toast.makeText(getApplicationContext(), Preferences.getString("UserKey", "Fail"), Toast.LENGTH_LONG).show();

        StatUtils.InitializeNavigationDrawer(this);

        SetBudgetDetails();

        SetUpcomingEventsList();
    }


    /**
     * Stops the back button from  trying to return to the previous screen if this activity was
     * created by the create user activity.
     */
    @Override
    public void onBackPressed(){
        if(getIntent().getBooleanExtra("CameFromEntry", false)){
            return;
        }else{
            super.onBackPressed();
        }
    }

    /**
     * Returns the correponding text for the code input.
     * If the code is invalid, returns "cycle".
     * @param code The time period code
     * @return String representation of the code
     */
    public String GetPeriodText(int code){
        String period;

        int DayCode = getResources().getInteger(R.integer.DAY_CODE);
        int WeekCode = getResources().getInteger(R.integer.WEEK_CODE);
        int BiweekCode = getResources().getInteger(R.integer.BIWEEK_CODE);
        int MonthCode = getResources().getInteger(R.integer.MONTH_CODE);

        if(code == DayCode){
            period = "day";
        }else if(code == WeekCode){
            period = "week";
        }else if(code == BiweekCode){
            period = "2 weeks";
        }else if(code == MonthCode){
            period = "month";
        }else{
            Toast.makeText(getApplicationContext(), "Error: Invalid Time Period", Toast.LENGTH_SHORT).show();
            period = "cycle";
        }
        return  period;
    }

    /**
     * Returns the number of days that should be added to the date
     * in order to obtain the start date of the next cycle.
     * @param code The time period code for the budget.
     * @param date The start date of the budget.
     * @return The number of days to be added.
     */
    private int GetPeriodDays(int code, String date){

        int DayCode = getResources().getInteger(R.integer.DAY_CODE);
        int WeekCode = getResources().getInteger(R.integer.WEEK_CODE);
        int BiweekCode = getResources().getInteger(R.integer.BIWEEK_CODE);
        int MonthCode = getResources().getInteger(R.integer.MONTH_CODE);

        int daysToAdd;
        if(code == DayCode){
            daysToAdd = 1;
        }else if(code == WeekCode){
            daysToAdd = 7;
        }else if(code == BiweekCode){
            daysToAdd = 14;
        }else if(code == MonthCode && StatUtils.IsValidDate(date)){
            daysToAdd = GetMonthCode(date);
        }else{
            Toast.makeText(getApplicationContext(), "Error: Invalid Time Period", Toast.LENGTH_SHORT).show();
            daysToAdd = 1;
        }
        return daysToAdd;
    }

    /**
     * Returns the length of the month in the date given.
     * @param date The date string in the format yyyy-MM-dd.
     * @return An integer containing the number of days in the month.
     */
    private static int GetMonthCode(String date){
        String[] dates = date.split("-");

        int code = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[0]);

        int ret = 0;

        switch(code){
            case 1:
                ret = 31;
                break;
            case 2:
                if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
                    ret = 29;
                }else {
                    ret = 28;
                }
                break;
            case 3:
                ret = 31;
                break;
            case 4:
                ret = 30;
                break;
            case 5:
                ret = 31;
                break;
            case 6:
                ret = 30;
                break;
            case 7:
                ret = 31;
                break;
            case 8:
                ret = 30;
                break;
            case 9:
                ret = 31;
                break;
            case 10:
                ret = 31;
                break;
            case 11:
                ret = 30;
                break;
            case 12:
                ret = 31;
                break;

        }

        return ret;
    }

    /**
     * Retrieves the list of events for the user and adds all future events to the listview.
     */
    private void SetUpcomingEventsList(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Code goes here

                    String UserKey = Preferences.getString("UserKey", "");

                    final ListView upcomingEvents = (ListView) findViewById(R.id.upcomingEventsListView);

                    ArrayList<FireEvent> allEvents = Tasks.await(FireEvent.getEventsByUserkey(UserKey));

                    ArrayList<String> eventListItems = new ArrayList<>();

                    final ArrayList<FireEvent> upcomingList = new ArrayList<>();

                    for (int i = allEvents.size() - 1; i >= 0; i--) {
                        if (StatUtils.DaysSince(allEvents.get(i).getStartDate()) <= 0) {
                            eventListItems.add(allEvents.get(i).getDescription() + " Starts: " + allEvents.get(i).getStartDate());
                            upcomingList.add(allEvents.get(i));
                        }
                    }

//        upcomingEvents.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventListItems));
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            upcomingEvents.setAdapter(new EventListAdapter(LandingPage.this, R.layout.event_list_item, upcomingList));
                        }
                    });

                    //upcomingEvents.setAdapter(new EventListAdapter(LandingPage.this, R.layout.event_list_item, upcomingList));

                }catch (Exception e){
                    Log.d(TAG, "Failed");
                    Log.d(TAG, e.getMessage());
                }
            }
        });

        t.start();

        /*Long UserID = Preferences.getLong("UserID", -1);

        ListView upcomingEvents = (ListView) findViewById(R.id.upcomingEventsListView);

        ArrayList<Event> allEvents = Event.getEventsByUserID(getApplicationContext(), UserID);

        ArrayList<String> eventListItems = new ArrayList<>();

        ArrayList<Event> upcomingList = new ArrayList<>();

        for (int i = allEvents.size() - 1; i >= 0; i--) {
            if (StatUtils.DaysSince(allEvents.get(i).getStartDate()) <= 0) {
                eventListItems.add(allEvents.get(i).getDescription() + " Starts: " + allEvents.get(i).getStartDate());
                upcomingList.add(allEvents.get(i));
            }
        }

//        upcomingEvents.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventListItems));

        upcomingEvents.setAdapter(new EventListAdapter(this, R.layout.event_list_item, upcomingList));*/
    }

    /**
     * Adds the text to the appropriate textviews.
     */
    private void SetBudgetDetails(){

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Code goes here

                    String UserKey = Preferences.getString("UserKey", "");

                    final FireBudget budget = Tasks.await(FireBudget.getCurrentBudgetForUser(UserKey));

                    final TextView currentBudgetText = (TextView) findViewById(R.id.landingPageCurrentBudgetText);
                    final TextView remainingBudgetText = (TextView) findViewById(R.id.landingPageRemainingBudgetText);

                    if(budget != null) {
                        float amount = budget.getAmount();

                        ArrayList<FireExpense> expenses = Tasks.await(FireExpense.getExpensesByUser(UserKey));
                        for (int i = 0; i < expenses.size(); i++) {
                            if (budget.getBudgetKey().equals(expenses.get(i).getBudgetKey()))
                                amount -= expenses.get(i).getAmount();
                        }

                        final String period = GetPeriodText(budget.getTimePeriod());

                        final int daysToAdd = GetPeriodDays(budget.getTimePeriod(), budget.getStartDate());

                        final float famount = amount;
                        if (!Tasks.await(FireBudget.getCurrentBudgetForUser(UserKey)).getLastModified().equals("1990-01-01")) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    currentBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                            .format(budget.getAmount()) + " per " + period);
                                    remainingBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                                            .format(famount) + " until " + StatUtils.AddDaysToDate(budget.getStartDate(), daysToAdd));
                                }
                            });

                        }
                    }

                }catch (Exception e){
                    Log.d(TAG, "Failed");
                    Log.d(TAG, e.getMessage());
                }
            }
        });

        t.start();

        /*Long UserID = Preferences.getLong("UserID", -1);

        Budget budget = Budget.getCurrentBudgetForUser(getApplicationContext(),  UserID);

        TextView currentBudgetText = (TextView) findViewById(R.id.landingPageCurrentBudgetText);
        TextView remainingBudgetText = (TextView) findViewById(R.id.landingPageRemainingBudgetText);

        if(budget != null) {
            float amount = budget.getAmount();

            ArrayList<Expense> expenses = Expense.getExpensesByUser(getApplicationContext(), UserID);
            for (int i = 0; i < expenses.size(); i++) {
                if (budget.getBudgetID() == expenses.get(i).getBudgetID())
                    amount -= expenses.get(i).getAmount();
            }

            String period = GetPeriodText(budget.getTimePeriod());

            int daysToAdd = GetPeriodDays(budget.getTimePeriod(), budget.getStartDate());


            if (Budget.getCurrentBudgetForUser(getApplicationContext(), UserID).getBudgetID() != -1) {
                currentBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(budget.getAmount()) + " per " + period);
                remainingBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(amount) + " until " + StatUtils.AddDaysToDate(budget.getStartDate(), daysToAdd));
            }
        }*/
    }
}
