package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class LandingPage extends AppCompatActivity {

    private DrawerLayout NavDrawer;

    private Long UserID;
    private Boolean CameFromEntry;

    private int DayCode;
    private int WeekCode;
    private int BiweekCode;
    private int MonthCode;

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        /*************************************** Initialization Area ***************************************************/

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        //Get Resources
        UserID = Preferences.getLong("UserID", -1);
        CameFromEntry = getIntent().getBooleanExtra("CameFromEntry", false);
        DayCode = getResources().getInteger(R.integer.DAY_CODE);
        WeekCode = getResources().getInteger(R.integer.WEEK_CODE);
        BiweekCode = getResources().getInteger(R.integer.BIWEEK_CODE);
        MonthCode = getResources().getInteger(R.integer.MONTH_CODE);
        Budget budget = Budget.getCurrentBudgetForUser(getApplicationContext(),  UserID);

        //End Get Resources



        //Get Views
        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        ListView navDrawerList = (ListView) findViewById(R.id.navDrawerList);
        TextView currentBudgetText = (TextView) findViewById(R.id.landingPageCurrentBudgetText);
        TextView remainingBudgetText = (TextView) findViewById(R.id.landingPageRemainingBudgetText);
        ListView upcomingEvents = (ListView) findViewById(R.id.upcomingEventsListView);
        //End Get Views

        String[] navDrawerItems = getResources().getStringArray(R.array.navListItems);





        /**************************************** End Initialization Area **********************************************/

        navDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navDrawerItems));


        navDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.START);
                Intent tempIntent = SwitchManager.SwitchActivity(LandingPage.this, parent.getItemAtPosition(position).toString());

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });


        if(budget != null) {
            float amount = budget.getAmount();

            ArrayList<Expense> expenses = Expense.getExpensesByUser(getApplicationContext(), UserID);
            for (int i = 0; i < expenses.size(); i++) {
                if (budget.getBudgetID() == expenses.get(i).getBudgetID())
                    amount -= expenses.get(i).getAmount();
            }

            ArrayList<Event> houser = Event.getEventsByUserID(getApplicationContext(), UserID);

            ArrayList<String> budgetListItems = new ArrayList<>();

            for (int i = houser.size() - 1; i >= 0; i--) {
                if (StatUtils.DaysSince(houser.get(i).getStartDate()) <= 0) {
                    budgetListItems.add(houser.get(i).getDescription() + " Starts: " + houser.get(i).getStartDate());
                }
            }

            upcomingEvents.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, budgetListItems));

            String period = GetPeriodText(budget.getTimePeriod());

            int daysToAdd = GetPeriodDays(budget.getTimePeriod(), budget.getStartDate());


            if (Budget.getCurrentBudgetForUser(getApplicationContext(), UserID).getBudgetID() != -1) {
                currentBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(budget.getAmount()) + " per " + period);
                remainingBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                        .format(amount) + " until " + StatUtils.AddDaysToDate(budget.getStartDate(), daysToAdd));
            }
        }
    }


    @Override
    public void onBackPressed(){
        if(CameFromEntry){
            return;
        }else{
            super.onBackPressed();
        }
    }

    public String GetPeriodText(int code){
        String period;

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

    public int GetPeriodDays(int code, String date){
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

    public int GetMonthCode(String date){
        String[] dates = date.split("-");

        int code = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[0]);

        int ret = 0;

        switch(code){
            case 1:
                ret = 31;
                break;
            case 2:
                ret = 28;
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
}
