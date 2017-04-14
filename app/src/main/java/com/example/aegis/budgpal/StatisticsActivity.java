package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //get list of current categories for expenses
        String[] expenseCategories = getResources().getStringArray(R.array.expenseCategories);

        //get the current user's userID from the in-app preferences database
        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();
        long myUserID = Preferences.getLong("UserID",-1);

        //get budgets for the current user
        ArrayList<Budget> myBudgets = new ArrayList<Budget>();
        myBudgets = Budget.getBudgetsByUser(getApplicationContext(),myUserID);

        float thisBudgetAmount = 0;
        long thisBudgetID;

        ArrayList<Float> categoryPercentOfExpendituresList = new ArrayList<Float>();
        ArrayList<Float> budgetAmountList = new ArrayList<Float>();
        ArrayList<Float> expendituresList = new ArrayList<Float>();
        ArrayList<Float> sumOfCategoryPercentsList = new ArrayList<Float>();


        ArrayList<Expense> thisBudgetExpenses = new ArrayList<Expense>();


        //loop through a users budget
        for(Budget aBudget: myBudgets){

            float thisBudgetExpenditures = 0;
            thisBudgetID = aBudget.getBudgetID();
            thisBudgetAmount = aBudget.getAmount();
            budgetAmountList.add(thisBudgetAmount);

            float[] categoryExpenseTotals = new float[expenseCategories.length];

            //get expenses for this budget
            thisBudgetExpenses = Expense.getExpensesByBudget(getApplicationContext(),thisBudgetID);

            for (Expense anExpense: thisBudgetExpenses){
                thisBudgetExpenditures += anExpense.getAmount();

                //add expense to total for appropriate category
                categoryExpenseTotals[anExpense.getCategory() - 1] += anExpense.getAmount();
            }
            Log.d("STATS catExpTot = ",categoryExpenseTotals.length + "");

            //sizes the arrayList to the correct length
            for (int fillList = 0; fillList < categoryExpenseTotals.length; fillList++){
                sumOfCategoryPercentsList.add((float) 0);
            }

            Log.d("STATS sumCatPerList = ",sumOfCategoryPercentsList.size() + "");
            for(int f = 0; f < categoryExpenseTotals.length; f++){
                sumOfCategoryPercentsList.set(f,(sumOfCategoryPercentsList.get(f) + categoryExpenseTotals[f]));
            }

            expendituresList.add(thisBudgetExpenditures);



        }

        //calculate averages
        //calculate average percent used
        float average;
        float totalExpenditures = 0;
        float totalBudgetAmounts = 0;

        for (int k = 0; k < expendituresList.size(); k++){
            totalExpenditures += expendituresList.get(k);
            totalBudgetAmounts += budgetAmountList.get(k);
        }
        Log.d("TOTAL EXPENDITURES ",totalExpenditures + "");
        Log.d("TOTAL BUDGET AMOUNTS", totalBudgetAmounts + "");
        average = totalExpenditures/totalBudgetAmounts * 100;

        //calculate percent of spending by category
        for(int j = 0; j < expenseCategories.length; j++){
            categoryPercentOfExpendituresList.add((sumOfCategoryPercentsList.get(j)/expenseCategories.length));
        }

        //display
        TextView txtAverage = (TextView) findViewById(R.id.statsTextAvgBudget);
        txtAverage.setText("Average Percent of Budget Used: " + average + "%");

        PopulateList(categoryPercentOfExpendituresList, expenseCategories);

    }

    /**
     * Add all items to the listview.
     */
    private void PopulateList(ArrayList<Float> percentsList, String[] expCategories){

        ListView expenseTotalsList = (ListView)findViewById(R.id.statsExpenseTotalsListView);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        expenseTotalsList.setAdapter(listAdapter);

        listAdapter.clear();

        for(int i = 0; i < percentsList.size(); i++){
            listAdapter.add(expCategories[i] + ": " + percentsList.get(i) + "%");
        }
    }

}
