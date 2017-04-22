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
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private static final String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        StatUtils.InitializeNavigationDrawer(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Code goes here
                    //get list of current categories for expenses
                    String[] expenseCategories = getResources().getStringArray(R.array.expenseCategories);

                    //get the current user's userID from the in-app preferences database
                    Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
                    PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();
                    String myUserKey = Preferences.getString("UserKey","");

                    //get budgets for the current user
                    ArrayList<FireBudget> myBudgets = new ArrayList<>();
                    myBudgets = Tasks.await(FireBudget.getBudgetsByUser(myUserKey));

                    float thisBudgetAmount = 0;
                    String thisBudgetID;

                    ArrayList<Float> categoryPercentOfExpendituresList = new ArrayList<>();
                    ArrayList<Float> budgetAmountList = new ArrayList<>();
                    ArrayList<Float> expendituresList = new ArrayList<>();
                    ArrayList<Float> sumOfCategoryPercentsList = new ArrayList<>();


                    ArrayList<FireExpense> thisBudgetExpenses = new ArrayList<>();

                    ArrayList<Float> rollingCategoryPercent = new ArrayList<>();
                    float numberOfBudgets = 0;
                    for (int h = 0; h < expenseCategories.length; h++){
                        rollingCategoryPercent.add((float) 0);
                    }

                    //loop through a users budget
                    for(FireBudget aBudget: myBudgets){
                        numberOfBudgets++;
                        //stores the sum of expenses under each Category divided by the total expenditures (for one budget)
                        ArrayList<Float> thisBudgetCategoryPercent = new ArrayList<>();
                        for (int h = 0; h < expenseCategories.length; h++){
                            thisBudgetCategoryPercent.add((float) 0);
                        }


                        float thisBudgetExpenditures = 0;
                        thisBudgetID = aBudget.getBudgetKey();
                        thisBudgetAmount = aBudget.getAmount();
                        budgetAmountList.add(thisBudgetAmount);

                        float[] categoryExpenseTotals = new float[expenseCategories.length];

                        //get expenses for this budget
                        thisBudgetExpenses = Tasks.await(FireExpense.getExpensesByBudget(thisBudgetID));

                        //loop through expenses
                        for (FireExpense anExpense: thisBudgetExpenses){
                            thisBudgetExpenditures += anExpense.getAmount();

                            //add expense to total for appropriate category
                            categoryExpenseTotals[anExpense.getCategory() - 1] += anExpense.getAmount();
                        }

                        //s
                        for(int f = 0; f < expenseCategories.length; f++){
                            if (thisBudgetExpenditures == 0){
                                thisBudgetCategoryPercent.set(f,(float) 0);
                            }
                            else {
                                thisBudgetCategoryPercent.set(f, (categoryExpenseTotals[f] / thisBudgetExpenditures));
                            }
                        }

                        for(int g = 0; g < expenseCategories.length; g++){
                            rollingCategoryPercent.set(g, ( (((numberOfBudgets-1)/numberOfBudgets)*rollingCategoryPercent.get(g)) + (1/numberOfBudgets)*thisBudgetCategoryPercent.get(g) ) );
                            Log.d("Number of Budgets", numberOfBudgets + "");
                            Log.d("this budget percent", thisBudgetCategoryPercent.get(g) + "");
                            Log.d("Rolling percent", rollingCategoryPercent.get(g) + "");
                        }

                        expendituresList.add(thisBudgetExpenditures);



                    }

                    //calculate averages
                    //calculate average percent used

                    float totalExpenditures = 0;
                    float totalBudgetAmounts = 0;

                    for (int k = 0; k < expendituresList.size(); k++){
                        totalExpenditures += expendituresList.get(k);
                        totalBudgetAmounts += budgetAmountList.get(k);
                    }
                    final float average = totalExpenditures/totalBudgetAmounts * 100;

                    //display
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView txtAverage = (TextView) findViewById(R.id.statsTextAvgBudget);
                            txtAverage.setText("Average Percent of Budget Used: " + average + "%");
                        }
                    });

                    PopulateList(rollingCategoryPercent, expenseCategories);

                }catch (Exception e){
                    Log.d(TAG, "Failed");
                    Log.d(TAG, e.getMessage());
                }
            }
        });

        t.start();


        //get list of current categories for expenses
        /*String[] expenseCategories = getResources().getStringArray(R.array.expenseCategories);

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

        ArrayList<Float> rollingCategoryPercent = new ArrayList<Float>();
        float numberOfBudgets = 0;
        for (int h = 0; h < expenseCategories.length; h++){
            rollingCategoryPercent.add((float) 0);
        }

        //loop through a users budget
        for(Budget aBudget: myBudgets){
            numberOfBudgets++;
            //stores the sum of expenses under each Category divided by the total expenditures (for one budget)
            ArrayList<Float> thisBudgetCategoryPercent = new ArrayList<Float>();
            for (int h = 0; h < expenseCategories.length; h++){
                thisBudgetCategoryPercent.add((float) 0);
            }


            float thisBudgetExpenditures = 0;
            thisBudgetID = aBudget.getBudgetID();
            thisBudgetAmount = aBudget.getAmount();
            budgetAmountList.add(thisBudgetAmount);

            float[] categoryExpenseTotals = new float[expenseCategories.length];

            //get expenses for this budget
            thisBudgetExpenses = Expense.getExpensesByBudget(getApplicationContext(),thisBudgetID);

            //loop through expenses
            for (Expense anExpense: thisBudgetExpenses){
                thisBudgetExpenditures += anExpense.getAmount();

                //add expense to total for appropriate category
                categoryExpenseTotals[anExpense.getCategory() - 1] += anExpense.getAmount();
            }

            //s
            for(int f = 0; f < expenseCategories.length; f++){
                if (thisBudgetExpenditures == 0){
                    thisBudgetCategoryPercent.set(f,(float) 0);
                }
                else {
                    thisBudgetCategoryPercent.set(f, (categoryExpenseTotals[f] / thisBudgetExpenditures));
                }
                }

            for(int g = 0; g < expenseCategories.length; g++){
                rollingCategoryPercent.set(g, ( (((numberOfBudgets-1)/numberOfBudgets)*rollingCategoryPercent.get(g)) + (1/numberOfBudgets)*thisBudgetCategoryPercent.get(g) ) );
                Log.d("Number of Budgets", numberOfBudgets + "");
                Log.d("this budget percent", thisBudgetCategoryPercent.get(g) + "");
                Log.d("Rolling percent", rollingCategoryPercent.get(g) + "");
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
        average = totalExpenditures/totalBudgetAmounts * 100;

        //display
        TextView txtAverage = (TextView) findViewById(R.id.statsTextAvgBudget);
        txtAverage.setText("Average Percent of Budget Used: " + average + "%");

        PopulateList(rollingCategoryPercent, expenseCategories);*/

    }

    /**
     * Add all items to the listview.
     */
    private void PopulateList(final ArrayList<Float> percentsList, final String[] expCategories){

        runOnUiThread(new Runnable() {
            public void run()
            {
                ListView expenseTotalsList = (ListView)findViewById(R.id.statsExpenseTotalsListView);

                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(StatisticsActivity.this, android.R.layout.simple_list_item_1);

                expenseTotalsList.setAdapter(listAdapter);

                listAdapter.clear();

                for(int i = 0; i < percentsList.size(); i++){
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(4);
                    listAdapter.add(expCategories[i] + ": " + df.format(percentsList.get(i)*100) + "%");
                    float temp = percentsList.get(i);
                    Log.d("TEMP", temp + "");
                }
            }
        });

        /*ListView expenseTotalsList = (ListView)findViewById(R.id.statsExpenseTotalsListView);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        expenseTotalsList.setAdapter(listAdapter);

        listAdapter.clear();

        for(int i = 0; i < percentsList.size(); i++){
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(4);
            listAdapter.add(expCategories[i] + ": " + df.format(percentsList.get(i)*100) + "%");
            float temp = percentsList.get(i);
            Log.d("TEMP", temp + "");
        }*/
    }

}
