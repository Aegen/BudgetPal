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
    private CheckBox MonthlyBox;

    private Long UserID;
    private Long BudgetID;

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
        MonthlyBox = (CheckBox)findViewById(R.id.budgetMonthlyCheckBox);


        UserID = getIntent().getLongExtra("UserID", -1);
        BudgetID = StatUtils.GetBudgetID(getApplicationContext(), UserID);

        if(BudgetID != -1){
            Budget tempB = StatUtils.GetBudget(getApplicationContext(), BudgetID);
            OldBudget.setText(new Float(tempB.getAmount()).toString());
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

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BudgetID != -1) {
                    Budget tempB = StatUtils.GetBudget(getApplicationContext(), BudgetID);
                    tempB.setDeleted(true);
                    tempB.pushToDatabase();
                }
                int TPC = 1;
                int RSC = 1;
                if(DailyBox.isChecked()){
                    TPC = 1;
                    RSC = 1;
                }else if(WeeklyBox.isChecked()){
                    TPC = 2;
                    RSC = StatUtils.GetWeeklyResetCode();
                }else if(MonthlyBox.isChecked()){
                    TPC = 3;
                    RSC = 1;
                }
                String am = AmountField.getText().toString();
                float amo = new Float(am);
                Budget newB = new Budget(UserID, TPC, RSC, StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), amo, getApplicationContext());
                newB.pushToDatabase();
            }
        });
    }
}
