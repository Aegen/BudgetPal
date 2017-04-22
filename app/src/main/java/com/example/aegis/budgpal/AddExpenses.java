package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;

public class AddExpenses extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "AddExpense";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        StatUtils.InitializeNavigationDrawer(this);

        SetupCategorySpinner();

        SetupAddButton();
    }

    /**
     * Adds the predefined items in Strings.xml to the catergory spinner.
     */
    private void SetupCategorySpinner(){
        final Spinner CategorySpinner = (Spinner)findViewById(R.id.expenseCategorySpinner);

        String[] categories = getResources().getStringArray(R.array.expenseCategories);

        CategorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories));
    }

    /**
     * Adds the onclick listener to the Add button.
     */
    private void SetupAddButton(){
        final Spinner CategorySpinner = (Spinner)findViewById(R.id.expenseCategorySpinner);
        final EditText AmountField = (EditText)findViewById(R.id.expenseAmountText);
        final EditText DescriptionField = (EditText)findViewById(R.id.expenseDescriptionText);

        final Button addButton = (Button) findViewById(R.id.expenseAddButton);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Code goes here

                    final String UserKey = Preferences.getString("UserKey", "");
                    final String BudgetLastModified = Tasks.await(FireBudget.getCurrentBudgetForUser(UserKey)).getLastModified();
                    final FireBudget myBudget = Tasks.await(FireBudget.getCurrentBudgetForUser(UserKey));

                    if(BudgetLastModified.equals("1990-01-01")){
                        Log.e(TAG, "SetupAddButton - BudgetID is -1");
                        addButton.setEnabled(false);
                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "No budget set for this user", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Thread u = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                //Code goes here

                                        if(!AmountField.getText().toString().isEmpty()) {
                                            Float am = Float.valueOf(AmountField.getText().toString());
                                            FireExpense tempExp = new FireExpense(UserKey, myBudget.getBudgetKey(), am, StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), StatUtils.GetCategoryCode(CategorySpinner.getSelectedItem().toString()), DescriptionField.getText().toString(), false);
                                            tempExp.pushToDatabase();


                                            startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                                            finish();
                                        }else{
                                            runOnUiThread(new Runnable() {
                                                public void run()
                                                {
                                                    Toast.makeText(getApplicationContext(), "You must enter an amount", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                    }catch (Exception e){
                                        Log.d(TAG, "Failed");
                                        Log.d(TAG, e.getMessage());
                                    }


                                }
                            });
                            u.start();
                        }
                    });


                    /*addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!AmountField.getText().toString().isEmpty()) {
                                Float am = Float.valueOf(AmountField.getText().toString());
                                Expense tempExp = new Expense(UserID, BudgetID, am, StatUtils.GetCurrentDate(), StatUtils.GetCategoryCode(CategorySpinner.getSelectedItem().toString()), DescriptionField.getText().toString(), false, getApplicationContext());
                                tempExp.pushToDatabase();


                                startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "You must enter an amount", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); */

                }catch (Exception e){
                    Log.d(TAG, "Failed");
                    Log.d(TAG, e.getMessage());
                }
            }
        });

        t.start();

        /*final Long UserID = Preferences.getLong("UserID", -1);
        final Long BudgetID = Budget.getCurrentBudgetForUser(getApplicationContext(), UserID).getBudgetID();

        if(BudgetID == -1){
            Log.e(TAG, "SetupAddButton - BudgetID is -1");
            addButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "No budget set for this user", Toast.LENGTH_LONG).show();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!AmountField.getText().toString().isEmpty()) {
                    Float am = Float.valueOf(AmountField.getText().toString());
                    Expense tempExp = new Expense(UserID, BudgetID, am, StatUtils.GetCurrentDate(), StatUtils.GetCategoryCode(CategorySpinner.getSelectedItem().toString()), DescriptionField.getText().toString(), false, getApplicationContext());
                    tempExp.pushToDatabase();


                    startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "You must enter an amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }
}
