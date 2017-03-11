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

public class SetBudget extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    private Long UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

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
                Intent tempIntent = SwitchManager.SwitchActivity(SetBudget.this, parent.getItemAtPosition(position).toString(), UserID);

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });
    }
}
