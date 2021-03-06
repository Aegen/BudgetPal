package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.Tasks;

import static android.app.Activity.RESULT_OK;

public class EventDetailsActivity extends AppCompatActivity {

    private EditText DescriptionField;
    private EditText DateField;
    private EditText CreatedByField;
    private EditText CreatedOnField;
    private Button UpdateButton;
    private Button DeleteButton;

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "EventDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

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
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //Code goes here

                            FireEvent ev = Tasks.await(FireEvent.getEventByEventKey( getIntent().getStringExtra("EventKey")));
                            ev.setDeleted(true);

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

                /*Event ev = Event.getEventByEventID(getApplicationContext(), getIntent().getLongExtra("EventID", new Long(-1)));
                ev.setDeleted(true);
                ev.pushToDatabase();

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

                            FireEvent ev = Tasks.await(FireEvent.getEventByEventKey(getIntent().getStringExtra("EventKey")));

                            if(ev.getLastModified().equals("1990-01-01")){
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        Toast.makeText(getApplicationContext(), "No event found", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return;
                            }
                            ev.setDescription(DescriptionField.getText().toString());
                            if(StatUtils.IsValidDate(DateField.getText().toString())){
                                ev.setStartDate(DateField.getText().toString());
                                ev.setEndDate(DateField.getText().toString());
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {

                                        Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            ev.setLastModified(StatUtils.GetCurrentDate());

                            Tasks.await(ev.pushToDatabase());

                            setResult(RESULT_OK);
                            //EventDetailsActivity.super.onBackPressed();
                            finish();

                        }catch (Exception e){
                            Log.d(TAG, "Failed");
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });

                t.start();

                /* Event ev = Event.getEventByEventID(getApplicationContext(), getIntent().getLongExtra("EventID", new Long(-1)));

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
                */
            }
        });


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //Code goes here

                    if(!getIntent().getStringExtra("EventKey").equals(null)){
                        final FireEvent house = Tasks.await(FireEvent.getEventByEventKey(getIntent().getStringExtra("EventKey")));
                        final FireUser home = Tasks.await(FireUser.getUserByUserKey( house.getUserKey()));
                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                DescriptionField.setText(house.getDescription());
                                DateField.setText(house.getStartDate());
                                CreatedByField.setText(home.getUsername());
                                CreatedOnField.setText(house.getLastModified());
                                UpdateButton.setEnabled(true);
                            }
                        });


                    }
                }catch (Exception e){
                    Log.d(TAG, "Failed");
                    Log.d(TAG, e.getMessage());
                }
            }
        });

        t.start();


        /*if(getIntent().getLongExtra("EventID", new Long(-1)) != -1){
            Event house = Event.getEventByEventID(getApplicationContext(), getIntent().getLongExtra("EventID", new Long(-1)));
            DescriptionField.setText(house.getDescription());
            DateField.setText(house.getStartDate());
            CreatedByField.setText(User.getUserByUserID(getApplicationContext(), house.getUserID()).getUsername());
            CreatedOnField.setText(house.getLastModified());

            UpdateButton.setEnabled(true);

        }*/
    }
}
