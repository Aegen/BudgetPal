package com.example.aegis.budgpal;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class ViewHistory extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "ViewExpenses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        Log.d(TAG, "Entered");

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        StatUtils.InitializeNavigationDrawer(this);

        SetupBudgetSpinner();

        SetupListView();

    }

    @Override
    protected void onActivityResult(int code, int res, Intent intent){
        super.onActivityResult(code, res, intent);

        PopulateList(GetActiveBudgetID());
    }

    /**
     * Adds items to the listview.
     * @param myBudgetID ID of the budget being used
     */
    private void PopulateList(Long myBudgetID){

        Long UserID = Preferences.getLong("UserID", -1);

        ListView expenseListView = (ListView) findViewById(R.id.viewHistoryExpensesListView);
        ArrayAdapter<String> adapter = (ArrayAdapter)expenseListView.getAdapter();
        adapter.clear();

        ArrayList<Expense> expenses = Expense.getExpensesByUser(getApplicationContext(), UserID);
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getBudgetID() == myBudgetID) {
                String temp = expenses.get(i).getExpenseID() + ": " + expenses.get(i).getDescription() + " = ";
                String temp2 = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(expenses.get(i).getAmount());

                adapter.add(temp + temp2);
            }
        }

    }

    /**
     * Adds items to the listview and adds a listener.
     */
    private void SetupListView() {
        Long UserID = Preferences.getLong("UserID", -1);

        Long BudgetID = Budget.getCurrentBudgetForUser(getApplicationContext(), UserID).getBudgetID();
        //myBudgetID = Budget.getCurrentBudgetForUser(getApplicationContext(), UserID).getBudgetID();

        final ListView expensesListView = (ListView) findViewById(R.id.viewHistoryExpensesListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        expensesListView.setAdapter(adapter);

        PopulateList(BudgetID);

        SetupListViewListener();
    }

    /**
     * Adds the on click listener to the listview
     */
    private void SetupListViewListener(){
        Long UserID = Preferences.getLong("UserID", -1);

        final ListView expensesListView = (ListView) findViewById(R.id.viewHistoryExpensesListView);

        expensesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] items = parent.getItemAtPosition(position).toString().split(":");
                Log.d("id", items[0]);
                long ExpenseID = Long.parseLong(items[0]);
                Log.d("post", Long.toString(ExpenseID));

                //Expense ex = Expense.getExpenseByExpenseID(getApplicationContext(), ExpenseID);

                Intent goToExpenseDetails = new Intent(ViewHistory.this, ExpenseDetailsActivity.class);

                goToExpenseDetails.putExtra("ExpenseID", ExpenseID);

                startActivityForResult(goToExpenseDetails, 0);
            }
        });
    }

    /**
     * Adds the budget items to the spinner
     */
    private void SetupBudgetSpinner(){
        Long UserID = Preferences.getLong("UserID", -1);

        final Spinner expenseSpinner = (Spinner) findViewById(R.id.viewExpensesPeriodSpinner);

        ArrayList<Budget> budgets = Budget.getBudgetsByUser(getApplicationContext(),UserID);
        ArrayList<String> budgetInfo = new ArrayList<String>();
        String someBudgetInfo;
        Budget someBudget;

        for(int i = 0; i < budgets.size(); i ++){
            someBudget = budgets.get(i);
            someBudgetInfo = someBudget.getBudgetID() + ": " + NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(someBudget.getAmount())+ " Start Date: " + someBudget.getStartDate();
            budgetInfo.add(someBudgetInfo);
        }

        expenseSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,budgetInfo));

        expenseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Long myBudgetID = Long.parseLong(parent.getItemAtPosition(position).toString().split(":")[0]);

                PopulateList(myBudgetID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Gets the current budgetID.
     * @return The budgetID of the currently selected budget.
     */
    private Long GetActiveBudgetID(){
        Long output = 0L;

        final Spinner expenseSpinner = (Spinner) findViewById(R.id.viewExpensesPeriodSpinner);

        output = Long.valueOf(expenseSpinner.getSelectedItem().toString().split(":")[0]);

        return output;
    }

}
