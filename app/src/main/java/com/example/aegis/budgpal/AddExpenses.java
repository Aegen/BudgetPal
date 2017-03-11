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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExpenses extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;
    private String[] Categories;

    private SQLiteDatabase db;

    private Long UserID;
    private Long BudgetID;

    private Button AddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        db = StatUtils.GetDatabase(getApplicationContext());

        AddButton = (Button)findViewById(R.id.expenseAddButton);

        UserID = getIntent().getLongExtra("UserID", -1);
        BudgetID = StatUtils.GetBudgetID(getApplicationContext(), UserID);

        if(BudgetID == -1){
            AddButton.setEnabled(false);
        }

        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

        Categories = getResources().getStringArray(R.array.expenseCategories);
        Spinner categorySelector = (Spinner)findViewById(R.id.expenseCategorySpinner);
        categorySelector.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories));

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActivity(AddExpenses.this, parent.getItemAtPosition(position).toString(), UserID);

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Expense tempExp = new Expense();
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
