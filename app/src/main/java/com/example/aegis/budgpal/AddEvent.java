package com.example.aegis.budgpal;

import android.content.Intent;
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

import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;
    private Long UserID;

    private SQLiteDatabase db;

    private EditText DescriptionField;
    private EditText DateField;
    private Button AddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        DescriptionField = (EditText)findViewById(R.id.addEventNameTextEdit);
        DateField = (EditText)findViewById(R.id.addEventDateTextEdit);
        AddButton = (Button)findViewById(R.id.addEventButton);

        db = StatUtils.GetDatabase(getApplicationContext());

        UserID = getIntent().getLongExtra("UserID", -1);
        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActivity(AddEvent.this, parent.getItemAtPosition(position).toString(), UserID);

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = DescriptionField.getText().toString();
                //Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_LONG).show();
                Log.d("Description", desc);
                String date = DateField.getText().toString();
                Toast.makeText(getApplicationContext(), StatUtils.GetCurrentDate(), Toast.LENGTH_LONG).show();
                Log.d("Date", date);

                if(StatUtils.IsValidDate(date)){
                    Event tempEv = new Event(UserID, date, date, desc, getApplicationContext());
                    tempEv.pushToDatabase();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Date must be of the form yyyy-mm-dd", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
