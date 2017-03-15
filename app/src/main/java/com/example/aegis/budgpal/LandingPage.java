package com.example.aegis.budgpal;

import android.content.Intent;
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

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    private ArrayList<Expense> expenses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        db = StatUtils.GetDatabase(getApplicationContext());

        UserID = getIntent().getLongExtra("UserID", -1);
        CameFromEntry = getIntent().getBooleanExtra("CameFromEntry", false);
        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

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
            amount -= expenses.get(i).getAmount();
        }

        currentBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(budget.getAmount()).toString());
        remainingBudgetText.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(amount));
    }

    @Override
    public void onBackPressed(){
        if(CameFromEntry){
            return;
        }else{
            super.onBackPressed();
        }
    }
}
