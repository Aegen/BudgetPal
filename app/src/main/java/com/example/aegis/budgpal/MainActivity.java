package com.example.aegis.budgpal;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;





public class MainActivity extends AppCompatActivity {

//harrison
    private DrawerLayout NavDrawer;
    private ListView NavDrawerList;
    private String[] NavDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("first", "first");
        UserDatabaseHandler a = new UserDatabaseHandler(getApplicationContext(), "database", null, 1);
        SQLiteDatabase db = a.getWritableDatabase(); //Create Database object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean dbExists = checkForDatabase();

        db.execSQL("INSERT INTO User (Username, HashedPassword, LastModified, Deleted) VALUES ('harrison', 'password', '1996-01-01 12:00:00', 0);"); //Load item into db
        Cursor curse = db.rawQuery("SELECT * FROM User WHERE Username = 'harrison'", null);
        curse.moveToFirst();

        NavDrawer      = (DrawerLayout)findViewById(R.id.navDrawer);
        NavDrawerList  = (ListView)findViewById(R.id.navDrawerList);
        NavDrawerItems = getResources().getStringArray(R.array.navListItems);
        //Toast.makeText(getApplicationContext(), Integer.toString(curse.getColumnCount()), Toast.LENGTH_LONG).show(); //print number of columns
        //runToast.makeText(getApplicationContext(), curse.getString(1), Toast.LENGTH_LONG).show();
        NavDrawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NavDrawerItems));

        NavDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();*/
                NavDrawer.closeDrawer(Gravity.LEFT);
                Intent tempIntent = SwitchManager.SwitchActiviy(MainActivity.this, parent.getItemAtPosition(position).toString());

                if(tempIntent != null){
                    startActivity(tempIntent);
                }
            }
        });
    }

    public boolean checkForDatabase(){
        return true;
    }
}
