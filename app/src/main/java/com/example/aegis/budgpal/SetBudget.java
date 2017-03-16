package com.example.aegis.budgpal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class SetBudget extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    private SQLiteDatabase db;

    private TextView OldBudget;
    private EditText AmountField;
    private Button SaveButton;
    private CheckBox DailyBox;
    private CheckBox WeeklyBox;
    private CheckBox BiweeklyBox;
    private CheckBox MonthlyBox;

    private Long UserID;
    private Long BudgetID;

    private boolean timePeriodSet = false;
    private boolean amountSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        db = StatUtils.GetDatabase(getApplicationContext());

        OldBudget = (TextView)findViewById(R.id.currentBudgetText);

        AmountField = (EditText)findViewById(R.id.newBudgetText);

        SaveButton  = (Button)findViewById(R.id.budgetSaveButton);

        DailyBox = (CheckBox)findViewById(R.id.budgetDailyCheckBox);
        WeeklyBox = (CheckBox)findViewById(R.id.budgetWeeklyCheckBox);
        BiweeklyBox = (CheckBox)findViewById(R.id.budgetBiweeklyCheckBox);
        MonthlyBox = (CheckBox)findViewById(R.id.budgetMonthlyCheckBox);

        UserID = getIntent().getLongExtra("UserID", -1);
        BudgetID = StatUtils.GetBudgetID(getApplicationContext(), UserID);

        if(BudgetID != -1){
            String period;
            Budget tempB = StatUtils.GetBudget(getApplicationContext(), BudgetID);

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

            OldBudget.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                    .format(tempB.getAmount()).toString() + " per " + period);
        }

        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActivity(SetBudget.this, parent.getItemAtPosition(position).toString(), UserID);

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });

        DailyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePeriodSet = true;
                boolean dIsChecked = DailyBox.isChecked();
                if (dIsChecked == true){
                    WeeklyBox.setChecked(false);
                    MonthlyBox.setChecked(false);
                    BiweeklyBox.setChecked(false);
                }
                else{
                    timePeriodSet = false;
                }
            }
        });
        WeeklyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePeriodSet = true;
                boolean dIsChecked = WeeklyBox.isChecked();
                if (dIsChecked == true){
                    DailyBox.setChecked(false);
                    MonthlyBox.setChecked(false);
                    BiweeklyBox.setChecked(false);
                }
                else{
                    timePeriodSet = false;
                }
            }
        });
        MonthlyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePeriodSet = true;
                boolean dIsChecked = MonthlyBox.isChecked();
                if (dIsChecked == true){
                    WeeklyBox.setChecked(false);
                    DailyBox.setChecked(false);
                    BiweeklyBox.setChecked(false);
                }
                else{
                    timePeriodSet = false;
                }
            }
        });
        BiweeklyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePeriodSet = true;
                boolean dIsChecked = BiweeklyBox.isChecked();
                if (dIsChecked == true){
                    WeeklyBox.setChecked(false);
                    DailyBox.setChecked(false);
                    MonthlyBox.setChecked(false);
                }
                else{
                    timePeriodSet = false;
                }
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePeriodSet == true && AmountField.getText().toString().equals("") == false) {

                    if (BudgetID != -1) {
                        Budget tempB = StatUtils.GetBudget(getApplicationContext(), BudgetID);
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
                    float amo = new Float(am);
                    Budget newB = new Budget(UserID, TPC, RSC, StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), amo, getApplicationContext());
                    newB.pushToDatabase();

                    startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage", UserID));
                    finish();
                }
            }
        });
    }
}
