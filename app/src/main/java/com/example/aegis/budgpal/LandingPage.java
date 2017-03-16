package com.example.aegis.budgpal;

import android.content.Intent;
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

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.aegis.budgpal.StatUtils.GetBudget;
import static com.example.aegis.budgpal.StatUtils.GetBudgetID;
import static com.example.aegis.budgpal.StatUtils.getExpenses;

public class LandingPage extends AppCompatActivity {

    private SQLiteDatabase db;

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    private Long UserID;
    private Boolean CameFromEntry;

    private TextView currentBudgetText;
    private TextView remainingBudgetText;

    private int DayCode;
    private int WeekCode;
    private int BiweekCode;
    private int MonthCode;

    private ArrayList<Expense> expenses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        db = StatUtils.GetDatabase(getApplicationContext());

        DayCode = getResources().getInteger(R.integer.DAY_CODE);
        WeekCode = getResources().getInteger(R.integer.WEEK_CODE);
        BiweekCode = getResources().getInteger(R.integer.BIWEEK_CODE);
        MonthCode = getResources().getInteger(R.integer.MONTH_CODE);

        UserID = getIntent().getLongExtra("UserID", -1);
        CameFromEntry = getIntent().getBooleanExtra("CameFromEntry", false);
        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(getApplicationContext(), StatUtils.DaysSince("2017-03-11").toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), StatUtils.GetCurrentDate(), Toast.LENGTH_SHORT).show();

//        Log.d("2017-01-31", StatUtils.DaysSince("2016-03-15").toString());
//        Log.d("2017-02-01", StatUtils.DaysSince("2017-02-01").toString());
//        Log.d("2017-02-02", StatUtils.DaysSince("2017-02-02").toString());
//        Log.d("2017-02-03", StatUtils.DaysSince("2017-02-03").toString());
//        Log.d("2017-02-04", StatUtils.DaysSince("2017-02-04").toString());
//        Log.d("2017-02-05", StatUtils.DaysSince("2017-02-05").toString());
//        Log.d("2017-02-06", StatUtils.DaysSince("2017-02-06").toString());
//        Log.d("2017-02-07", StatUtils.DaysSince("2017-02-07").toString());
//        Log.d("2017-02-08", StatUtils.DaysSince("2017-02-08").toString());
//        Log.d("2017-02-09", StatUtils.DaysSince("2017-02-09").toString());
//        Log.d("2017-02-10", StatUtils.DaysSince("2017-02-10").toString());
//        Log.d("2017-02-11", StatUtils.DaysSince("2017-02-11").toString());
//        Log.d("2017-02-12", StatUtils.DaysSince("2017-02-12").toString());
//        Log.d("2017-02-13", StatUtils.DaysSince("2017-02-13").toString());
//        Log.d("2017-02-14", StatUtils.DaysSince("2017-02-14").toString());
//        Log.d("2017-02-15", StatUtils.DaysSince("2017-02-15").toString());
//        Log.d("2017-02-16", StatUtils.DaysSince("2017-02-16").toString());
//        Log.d("2017-02-17", StatUtils.DaysSince("2017-02-17").toString());
//        Log.d("2017-02-18", StatUtils.DaysSince("2017-02-18").toString());
//        Log.d("2017-02-19", StatUtils.DaysSince("2017-02-19").toString());
//        Log.d("2017-02-20", StatUtils.DaysSince("2017-02-20").toString());
//        Log.d("2017-02-21", StatUtils.DaysSince("2017-02-21").toString());
//        Log.d("2017-02-22", StatUtils.DaysSince("2017-02-22").toString());
//        Log.d("2017-02-23", StatUtils.DaysSince("2017-02-23").toString());
//        Log.d("2017-02-24", StatUtils.DaysSince("2017-02-24").toString());
//        Log.d("2017-02-25", StatUtils.DaysSince("2017-02-25").toString());
//        Log.d("2017-02-26", StatUtils.DaysSince("2017-02-26").toString());
//        Log.d("2017-02-27", StatUtils.DaysSince("2017-02-27").toString());
//        Log.d("2017-02-28", StatUtils.DaysSince("2017-02-28").toString());
//        Log.d("2017-03-01", StatUtils.DaysSince("2017-03-01").toString());
//        Log.d("2017-03-02", StatUtils.DaysSince("2017-03-02").toString());
//        Log.d("2017-03-03", StatUtils.DaysSince("2017-03-03").toString());
//        Log.d("2017-03-04", StatUtils.DaysSince("2017-03-04").toString());
//        Log.d("2017-03-05", StatUtils.DaysSince("2017-03-05").toString());
//        Log.d("2017-03-06", StatUtils.DaysSince("2017-03-06").toString());
//        Log.d("2017-03-07", StatUtils.DaysSince("2017-03-07").toString());
//        Log.d("2017-03-08", StatUtils.DaysSince("2017-03-08").toString());
//        Log.d("2017-03-09", StatUtils.DaysSince("2017-03-09").toString());
//        Log.d("2017-03-10", StatUtils.DaysSince("2017-03-10").toString());
//        Log.d("2017-03-11", StatUtils.DaysSince("2017-03-11").toString());
//        Log.d("2017-03-12", StatUtils.DaysSince("2017-03-12").toString());
//        Log.d("2017-03-13", StatUtils.DaysSince("2017-03-13").toString());
//        Log.d("2017-03-14", StatUtils.DaysSince("2017-03-14").toString());
//        Log.d("2017-03-15", StatUtils.DaysSince("2017-03-15").toString());

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActivity(LandingPage.this, parent.getItemAtPosition(position).toString(), UserID);

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });

        currentBudgetText = (TextView)findViewById(R.id.landingPageCurrentBudgetText);
        remainingBudgetText = (TextView)findViewById(R.id.landingPageRemainingBudgetText);

        Budget budget = GetBudget(getApplicationContext(), GetBudgetID(getApplicationContext(), UserID));
        float amount = budget.getAmount();

        expenses = getExpenses(getApplicationContext(), UserID);
        for(int i=0; i<expenses.size(); i++) {
            if(budget.getBudgetID() == expenses.get(i).getBudgetID())
                amount -= expenses.get(i).getAmount();
        }

        String period = new String();

/*        switch (budget.getTimePeriod()){
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
                Toast.makeText(getApplicationContext(), "Error: Invalid Time Period", Toast.LENGTH_SHORT).show();
                period = "cycle";
                break;
        }
*/
        if(budget.getTimePeriod() == DayCode){
            period = "day";
        }else if(budget.getTimePeriod() == WeekCode){
            period = "week";
        }else if(budget.getTimePeriod() == BiweekCode){
            period = "2 weeks";
        }else if(budget.getTimePeriod() == MonthCode){
            period = "month";
        }else{
            Toast.makeText(getApplicationContext(), "Error: Invalid Time Period", Toast.LENGTH_SHORT).show();
            period = "cycle";
        }

        int daysToAdd = 0;
        if(budget.getTimePeriod() == DayCode){
            period = "day";
            daysToAdd = 1;
        }else if(budget.getTimePeriod() == WeekCode){
            period = "week";
            daysToAdd = 7;
        }else if(budget.getTimePeriod() == BiweekCode){
            period = "2 weeks";
            daysToAdd = 14;
        }else if(budget.getTimePeriod() == MonthCode){
            period = "month";
            daysToAdd = GetMonthCode(budget.getStartDate());
        }else{
            Toast.makeText(getApplicationContext(), "Error: Invalid Time Period", Toast.LENGTH_SHORT).show();
            period = "cycle";
        }


        currentBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(budget.getAmount()).toString() + " per " + period);
        remainingBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(amount) + " until " + StatUtils.AddDaysToDate(budget.getStartDate(), daysToAdd));
    }


    @Override
    public void onBackPressed(){
        if(CameFromEntry){
            return;
        }else{
            super.onBackPressed();
        }
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
                ret = 30;
                break;

        }

        return ret;
    }
}
