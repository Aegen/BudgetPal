package com.example.aegis.budgpal;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private EditText AmountField;
    private EditText DescriptionField;
    private EditText CreatedByField;
    private EditText CreatedOnField;

    private Button UpdateButton;
    private Button DeleteButton;

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "ExpenseDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        AmountField = (EditText)findViewById(R.id.amountField);
        DescriptionField = (EditText)findViewById(R.id.descriptionField);
        CreatedByField = (EditText)findViewById(R.id.CreatedByField);
        CreatedOnField = (EditText)findViewById(R.id.CreatedOnField);
        UpdateButton = (Button)findViewById(R.id.expenseDetailsUpdateButton);
        DeleteButton = (Button)findViewById(R.id.expenseDetailsDeleteButton);
        UpdateButton.setEnabled(false);

        /*AmountField.setText(getIntent().getStringExtra("Amount"));
        DescriptionField.setText(getIntent().getStringExtra("Description"));
        CreatedByField.setText(Long.toString(getIntent().getLongExtra("User", -1)));
        CreatedOnField.setText(getIntent().getStringExtra("LastModified"));*/

        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //Code goes here
                            FireExpense ex = Tasks.await(FireExpense.getExpenseByExpenseKey(getIntent().getStringExtra("ExpenseKey")));
                            ex.setDeleted(true);

                            setResult(RESULT_OK);
//                EventDetailsActivity.super.onBackPressed();
                            finish();

                        }catch (Exception e){
                            Log.d(TAG, "Failed");
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });

                t.start();

                /*Expense ex = Expense.getExpenseByExpenseID(getApplicationContext(), getIntent().getLongExtra("ExpenseID", new Long(-1)));
                ex.setDeleted(true);

                setResult(RESULT_OK);
//                EventDetailsActivity.super.onBackPressed();
                finish();*/
            }
        });

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //Code goes here
                            FireExpense ex = Tasks.await(FireExpense.getExpenseByExpenseKey(getIntent().getStringExtra("ExpenseKey")));

                            if(ex.lastModified.equals("1990-01-01")){
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        Toast.makeText(ExpenseDetailsActivity.this, "No expense found", Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }
                            ex.setAmount(Float.parseFloat(AmountField.getText().toString()));
                            ex.setDescription(DescriptionField.getText().toString());
                            ex.setLastModified(StatUtils.GetCurrentDate());
                            Tasks.await(ex.pushToDatabase());

                            setResult(RESULT_OK);
                            ExpenseDetailsActivity.super.onBackPressed();
                            finish();

                        }catch (Exception e){
                            Log.d(TAG, "Failed");
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });

                t.start();
                /*Expense ex = Expense.getExpenseByExpenseID(getApplicationContext(), getIntent().getLongExtra("ExpenseID", new Long(-1)));

                if(ex.getUserID() == -1){
                    Toast.makeText(getApplicationContext(), "No expense found", Toast.LENGTH_SHORT).show();
                    return;
                }
                ex.setAmount(Float.parseFloat(AmountField.getText().toString()));
                ex.setDescription(DescriptionField.getText().toString());
                ex.setLastModified(StatUtils.GetCurrentDate());
                ex.pushToDatabase();

                setResult(RESULT_OK);
                ExpenseDetailsActivity.super.onBackPressed();
                finish();*/

            }
        });

        if(!getIntent().getStringExtra("ExpenseKey").equals(null)){

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        //Code goes here
                        final FireExpense house = Tasks.await(FireExpense.getExpenseByExpenseKey(getIntent().getStringExtra("ExpenseKey")));
                        final FireUser tempo = Tasks.await(FireUser.getUserByUserKey(house.getUserKey()));

                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                AmountField.setText(house.getAmount() + "");
                                DescriptionField.setText(house.getDescription());
                                CreatedByField.setText(tempo.getUsername());
                                CreatedOnField.setText(house.getLastModified());

                                UpdateButton.setEnabled(true);
                            }
                        });


                    }catch (Exception e){
                        Log.d(TAG, "Failed");
                        Log.d(TAG, e.getMessage());
                    }
                }
            });

            t.start();

            /*Expense house = Expense.getExpenseByExpenseID(getApplicationContext(), getIntent().getLongExtra("ExpenseID", new Long(-1)));
            AmountField.setText(house.getAmount() + "");
            DescriptionField.setText(house.getDescription());
            CreatedByField.setText(User.getUserByUserID(getApplicationContext(), house.getUserID()).getUsername());
            CreatedOnField.setText(house.getLastModified());

            UpdateButton.setEnabled(true);*/

        }
    }
}
