package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "ViewEvents";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        PopulateList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        Log.d(TAG, "Entered");

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        StatUtils.InitializeNavigationDrawer(this);

        SetupEventList();

    }

    /**
     * Add all items to the listview.
     */
    private void PopulateList(){
        Long UserID = Preferences.getLong("UserID", -1);

        ListView EventsList = (ListView)findViewById(R.id.viewEventsListView);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        EventsList.setAdapter(listAdapter);

        listAdapter.clear();

        ArrayList<Event> eventsList = Event.getEventsByUserID(getApplicationContext(), UserID);
        for(int i = 0; i < eventsList.size(); i++){
            listAdapter.add(Long.toString(eventsList.get(i).getEventID()) + "- " + eventsList.get(i).getDescription() + "- Starts: " + eventsList.get(i).getStartDate());
        }
    }

    /**
     * Adds events to the list and adds a listener.
     */
    private void SetupEventList(){
        ListView EventsList = (ListView)findViewById(R.id.viewEventsListView);

        PopulateList();

        EventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] items = parent.getItemAtPosition(position).toString().split("-");

                long EventID = Long.parseLong(items[0]);

                Event ev = Event.getEventByEventID(getApplicationContext(), EventID);

                Intent goToEventDetails = new Intent(ViewEvents.this, EventDetailsActivity.class);
                goToEventDetails.putExtra("EventID", ev.getEventID());

                startActivityForResult(goToEventDetails, 0);
            }
        });
    }

}
