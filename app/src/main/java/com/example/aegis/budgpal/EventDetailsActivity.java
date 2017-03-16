package com.example.aegis.budgpal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EventDetailsActivity extends AppCompatActivity {

    private EditText DescriptionField;
    private EditText DateField;
    private EditText CreatedByField;
    private EditText CreatedOnField;
    private Button UpdateButton;
    private Button DeleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        DateField = (EditText)findViewById(R.id.DateField);
        DescriptionField = (EditText)findViewById(R.id.DescriptionField);
        CreatedByField = (EditText)findViewById(R.id.CreatedByField);
        CreatedOnField = (EditText)findViewById(R.id.CreatedOnField);
        UpdateButton = (Button)findViewById(R.id.eventDetailsUpdateButton);
        DeleteButton = (Button)findViewById(R.id.eventDetailsDeleteButton);
        UpdateButton.setEnabled(false);

        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event ev = StatUtils.GetEvent(getApplicationContext(), getIntent().getLongExtra("EventID", new Long(-1)));
                ev.setDeleted(true);
                ev.pushToDatabase();

                setResult(RESULT_OK);
//                EventDetailsActivity.super.onBackPressed();
                finish();
            }
        });

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event ev = StatUtils.GetEvent(getApplicationContext(), getIntent().getLongExtra("EventID", new Long(-1)));

                if(ev.getUserID() == -1){
                    Toast.makeText(getApplicationContext(), "No event found", Toast.LENGTH_SHORT).show();
                    return;
                }
                ev.setDescription(DescriptionField.getText().toString());
                if(StatUtils.IsValidDate(DateField.getText().toString())){
                    ev.setStartDate(DateField.getText().toString());
                    ev.setEndDate(DateField.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT).show();
                    return;
                }
                ev.setLastModified(StatUtils.GetCurrentDate());

                ev.pushToDatabase();

                setResult(RESULT_OK);
                EventDetailsActivity.super.onBackPressed();
                finish();

            }
        });

        if(getIntent().getLongExtra("EventID", new Long(-1)) != -1){
            Event house = StatUtils.GetEvent(getApplicationContext(), getIntent().getLongExtra("EventID", new Long(-1)));
            DescriptionField.setText(house.getDescription());
            DateField.setText(house.getStartDate());
            CreatedByField.setText(StatUtils.GetUser(getApplicationContext(), house.getUserID()).getUsername());
            CreatedOnField.setText(house.getLastModified());

            UpdateButton.setEnabled(true);

        }
    }
}
