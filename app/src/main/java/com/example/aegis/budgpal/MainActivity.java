package com.example.aegis.budgpal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

//harrison
    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("first", "first");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean dbExists = checkForDatabase();



        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);

        /***********************************************/
        //Database access zone

        DatabaseHandler a = new DatabaseHandler(getApplicationContext(), "database", null, 1); //Create database accessor
        db = a.getWritableDatabase(); //Create Database object, declared globally above

        //db.execSQL("INSERT INTO User (Username, HashedPassword, LastModified, Deleted) VALUES ('harrison', 'password', '1996-01-01 12:00:00', 0);"); //Load item into db
        Cursor curse = db.rawQuery("SELECT * FROM User WHERE Username = 'harrison'", null); //Self explanatory
        curse.moveToFirst(); //Important, sets the cursor to the first result, exception gets thrown if you try to get the contents without running this first

        //Toast.makeText(getApplicationContext(), c, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), curse.getString(1), Toast.LENGTH_LONG).show(); //Make username appear on screen

        /***********************************************/


        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
                NavDrawer.closeDrawer(Gravity.LEFT);
            }
        });
    }

    public boolean checkForDatabase(){
        return true;
    }
}
