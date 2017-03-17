package com.example.aegis.budgpal;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.aegis.budgpal.StatUtils.GetBudgetID;
import static com.example.aegis.budgpal.StatUtils.getExpenses;

public class ViewHistory extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    private SQLiteDatabase db;
//harrison was here
    private Long UserID;
    private Long myBudgetID;

    private ListView expensesListView;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        db = StatUtils.GetDatabase(getApplicationContext());

        UserID = getIntent().getLongExtra("UserID", -1);
        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

        NavDrawer = (DrawerLayout) findViewById(R.id.navDrawer);
        NavDrawerList = (ListView) findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActivity(ViewHistory.this, parent.getItemAtPosition(position).toString(), UserID);

                if (tempIntent != null) {
                    startActivity(tempIntent);
                }
            }
        });

        expensesListView = (ListView) findViewById(R.id.viewHistoryExpensesListView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        expensesListView.setAdapter(adapter);

        myBudgetID = StatUtils.GetBudgetID(getApplicationContext(), UserID);

        ArrayList<Expense> expenses = getExpenses(getApplicationContext(), UserID);
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getBudgetID() == myBudgetID) {
                String temp = expenses.get(i).getExpenseID() + ": " + expenses.get(i).getDescription() + " = ";
                String temp2 = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(expenses.get(i).getAmount());

                adapter.add(temp + temp2);
            }
        }

            expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] items = parent.getItemAtPosition(position).toString().split(":");
                    Log.d("id", items[0]);
                    long ExpenseID = Long.parseLong(items[0]);
                    Log.d("post", Long.toString(ExpenseID));

                    Expense ex = StatUtils.GetExpense(getApplicationContext(), ExpenseID);

                    Intent goToExpenseDetails = new Intent(ViewHistory.this, ExpenseDetailsActivity.class);
//                    goToExpenseDetails.putExtra("Amount", ex.getAmount() + "");
//                    goToExpenseDetails.putExtra("Description", ex.getDescription());
//                    goToExpenseDetails.putExtra("User", ex.getUserID());
//                    goToExpenseDetails.putExtra("LastModified", ex.getLastModified());
                    goToExpenseDetails.putExtra("ExpenseID", ExpenseID);

                    startActivityForResult(goToExpenseDetails, 0);
                }
            });


    }

    @Override
    protected void onActivityResult(int code, int res, Intent intent){
        super.onActivityResult(code, res, intent);
        adapter.clear();



        ArrayList<Expense> expenses = getExpenses(getApplicationContext(), UserID);
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getBudgetID() == myBudgetID) {
                String temp = expenses.get(i).getExpenseID() + ": " + expenses.get(i).getDescription() + " = ";
                String temp2 = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(expenses.get(i).getAmount());

                adapter.add(temp + temp2);
            }
        }

    }
}
