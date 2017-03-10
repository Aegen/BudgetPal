package com.example.aegis.budgpal;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.content.Intent;
import android.icu.util.Calendar;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


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
        //Log.d("Date",new Date(1,1,1,).toString());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        DatabaseHandler a = new DatabaseHandler(getApplicationContext(), "database", null, 1); //Create database accessor
        db = a.getWritableDatabase(); //Create Database object, declared globally above
        //User horse = new User("dave", "asdlhfasd;l", tempDate, false, a);
        //a.addUser(horse);
        //horse.pushToDatabase();

        db = a.getWritableDatabase(); //Create Database object, declared globally above
        //db.execSQL("INSERT INTO User (Username, HashedPassword, LastModified, Deleted) VALUES ('harrison', 'password', '1996-01-01 12:00:00', 0);"); //Load item into db
        Cursor curse = db.rawQuery("SELECT * FROM User WHERE Username = 'dave'", null); //Self explanatory
        curse.moveToFirst(); //Important, sets the cursor to the first result, exception gets thrown if you try to get the contents without running this first
        String hash = curse.getString(curse.getColumnIndex("HashedPassword"));
        Log.d("hash", hash);
        String pw = "asdlhfasd;l";
        String temp = StatUtils.GetHashedString(pw);
        Boolean c = temp.equals(hash);
        Toast.makeText(getApplicationContext(), c.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), curse.getString(1), Toast.LENGTH_LONG).show(); //Make username appear on screen

        /***********************************************/


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
