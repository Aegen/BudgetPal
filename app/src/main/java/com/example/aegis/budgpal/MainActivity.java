package com.example.aegis.budgpal;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();*/
                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent;
                switch (parent.getItemAtPosition(position).toString()){
                    case "Change Budget":
                        tempIntent = new Intent(MainActivity.this, SetBudget.class);
                        break;
                    case "Add Expense":
                        tempIntent = new Intent(MainActivity.this, AddExpenses.class);
                        break;
                    case "Add Event":
                        tempIntent = new Intent(MainActivity.this, AddEvent.class);
                        break;
                    case "View Expenses":
                        tempIntent = new Intent(MainActivity.this, ViewHistory.class);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Not Implemented", Toast.LENGTH_SHORT).show();
                        Log.e("Invalid Selection", parent.getItemAtPosition(position).toString() + " failed to match with an activity.");
                        return;
                }

                startActivity(tempIntent);
            }
        });
    }
}
