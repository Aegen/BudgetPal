package com.example.aegis.budgpal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EventDetailsActivity extends AppCompatActivity {

    private EditText DescriptionField;
    private EditText DateField;
    private EditText CreatedByField;
    private EditText CreatedOnField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        DateField = (EditText)findViewById(R.id.DateField);
        DescriptionField = (EditText)findViewById(R.id.DescriptionField);
        CreatedByField = (EditText)findViewById(R.id.CreatedByField);
        CreatedOnField = (EditText)findViewById(R.id.CreatedOnField);

        DescriptionField.setText(getIntent().getStringExtra("Description"));
        DateField.setText(getIntent().getStringExtra("Date"));
        CreatedByField.setText(StatUtils.GetUser(getApplicationContext(), getIntent().getLongExtra("User", -1)).getUsername());
        CreatedOnField.setText(getIntent().getStringExtra("LastModified"));
    }
}
