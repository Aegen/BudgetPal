package com.example.aegis.budgpal;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewEvents extends AppCompatActivity {

    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    private ListView EventsList;

    private Long UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        UserID = getIntent().getLongExtra("UserID", -1);

        Toast.makeText(getApplicationContext(), UserID.toString(), Toast.LENGTH_SHORT).show();

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));
        EventsList = (ListView)findViewById(R.id.viewEventsListView);

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActivity(ViewEvents.this, parent.getItemAtPosition(position).toString(), UserID);

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        EventsList.setAdapter(listAdapter);

        ArrayList<Event> eventsList = StatUtils.GetAllEvents(getApplicationContext(), UserID);
        for(int i = 0; i < eventsList.size(); i++){
            listAdapter.add(Long.toString(eventsList.get(i).getEventID()) + "- " + eventsList.get(i).getDescription() + "- Starts: " + eventsList.get(i).getStartDate());
        }

        EventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] items = parent.getItemAtPosition(position).toString().split("-");
                Log.d("id", items[0]);
                long EventID = Long.parseLong(items[0]);
                Log.d("post", Long.toString(EventID));

                Event ev = StatUtils.GetEvent(getApplicationContext(), EventID);

                Intent goToEventDetails = new Intent(ViewEvents.this, EventDetailsActivity.class);
                goToEventDetails.putExtra("Description", ev.getDescription());
                goToEventDetails.putExtra("Date", ev.getStartDate());
                goToEventDetails.putExtra("User", ev.getUserID());
                goToEventDetails.putExtra("LastModified", ev.getLastModified());

                startActivity(goToEventDetails);
            }
        });
    }
}
