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
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;

import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "AddEvent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();


        StatUtils.InitializeNavigationDrawer(this);

        SetAddButtonListener();

    }

    private void SetAddButtonListener(){
        final EditText DescriptionField = (EditText)findViewById(R.id.addEventNameTextEdit);
        final EditText DateField = (EditText)findViewById(R.id.addEventDateTextEdit);
        final Button AddButton = (Button)findViewById(R.id.addEventButton);

        final String UserKey = Preferences.getString("UserKey", "");

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            //Code goes here

                            String desc = DescriptionField.getText().toString();

                            String date = DateField.getText().toString();


                            if(StatUtils.IsValidDate(date)){
                                FireEvent tempEv = new FireEvent(UserKey, date, date, desc, StatUtils.GetCurrentDate());
                                Tasks.await(tempEv.pushToDatabase());

                                startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                                finish();
                            }else{
                                runOnUiThread(new Runnable() {
                                    public void run()
                                    {
                                        Toast.makeText(AddEvent.this, "Invalid Date must be of the form yyyy-mm-dd", Toast.LENGTH_LONG).show();
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
                /*String desc = DescriptionField.getText().toString();

                String date = DateField.getText().toString();


                if(StatUtils.IsValidDate(date)){
                    Event tempEv = new Event(UserID, date, date, desc, getApplicationContext());
                    tempEv.pushToDatabase();

                    startActivity(SwitchManager.SwitchActivity(getApplicationContext(), "Homepage"));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Date must be of the form yyyy-mm-dd", Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }
}
