package com.example.aegis.budgpal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private EditText AmountField;
    private EditText DescriptionField;
    private EditText CreatedByField;
    private EditText CreatedOnField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        AmountField = (EditText)findViewById(R.id.amountField);
        DescriptionField = (EditText)findViewById(R.id.descriptionField);
        CreatedByField = (EditText)findViewById(R.id.CreatedByField);
        CreatedOnField = (EditText)findViewById(R.id.CreatedOnField);

        AmountField.setText(getIntent().getStringExtra("Amount"));
        DescriptionField.setText(getIntent().getStringExtra("Description"));
        CreatedByField.setText(Long.toString(getIntent().getLongExtra("User", -1)));
        CreatedOnField.setText(getIntent().getStringExtra("LastModified"));
    }
}
