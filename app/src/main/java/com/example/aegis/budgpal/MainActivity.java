package com.example.aegis.budgpal;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.content.Intent;
import android.icu.util.Calendar;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Entered");

        FirebasePlayground();

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        AddLoginButtonListener();

        AddNewUserButtonListener();
    }

    @Override
    public void onBackPressed(){
        if(getIntent().getBooleanExtra("CameFromLogout", false)){
            return;
        }else{
            super.onBackPressed();
        }
    }

    private void FirebasePlayground(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //myRef.child("bootest").setValue(true);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                StringBuilder finString = new StringBuilder();
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Integer val = item.getValue(Integer.class);
                    finString.append(val.toString());
                }
                Toast.makeText(getApplicationContext(), finString.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        //myRef.addValueEventListener(postListener);

        Tester temp = new Tester("24", "Jon");
        //myRef.child("Test").setValue(temp);
        HashMap<String, Tester> power = new HashMap<>();

        power.put("first", new Tester("34", "Arnold"));
        power.put("second", new Tester("19", "Wyatt"));
        power.put("third", temp);

        myRef.setValue(power);

        //DatabaseReference theRef = database.getReference("house");

        //theRef.removeValue();
    }

    /**
     * Adds the on click event listener to the New User button
     */
    private void AddNewUserButtonListener(){

        Button newUserButton = (Button) findViewById(R.id.newUserButton);

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent(MainActivity.this, CreateUser.class);
                startActivity(temp);
            }
        });
    }

    /**
     * Add the on click event listener to the Login button
     */
    private void AddLoginButtonListener(){
        final SQLiteDatabase db = StatUtils.GetDatabase(getApplicationContext());

        final EditText UsernameField = (EditText)findViewById(R.id.loginUsernameField);
        final EditText PasswordField = (EditText)findViewById(R.id.loginPasswordField);

        Button loginButton = (Button) findViewById(R.id.loginButton);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = UsernameField.getText().toString();
                String password = PasswordField.getText().toString();

                String hashedPassword = StatUtils.GetHashedString(password);

                Cursor cursee = db.rawQuery("SELECT * FROM User WHERE Username = '" + username + "'", null);

                if(cursee.getCount() != 0){

                    cursee.moveToFirst();

                    PreferencesEditor.putLong("UserID", cursee.getLong(cursee.getColumnIndex("UserID")));
                    PreferencesEditor.commit();

                    String comp = cursee.getString(cursee.getColumnIndex("HashedPassword"));

                    if(comp.equals(hashedPassword)){

                        CycleBudgetIfNecessary();

                        Intent goToLanding = SwitchManager.SwitchActivity(getApplicationContext(), "Homepage");//new Intent(MainActivity.this, LandingPage.class).putExtra("UserID", cursee.getLong(cursee.getColumnIndex("UserID")));

                        goToLanding.putExtra("CameFromEntry", true);
                        startActivity(goToLanding);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No user found by that name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checks if the budget needs to be cycled, then creates a new one if it does.
     */
    public void CycleBudgetIfNecessary(){
        Long UserID = Preferences.getLong("UserID", -1);
        Budget tempB = Budget.getCurrentBudgetForUser(getApplicationContext(), UserID);

        if(tempB.getTimePeriod() != -1){
            switch (tempB.getTimePeriod()){
                case 1:
                    if(StatUtils.DaysSince(tempB.getStartDate()) > 0){
                        StatUtils.ChangeBudget(getApplicationContext(), UserID);
                    }
                    break;
                case 2:
                    if(StatUtils.DaysSince(tempB.getStartDate()) > 6){
                        StatUtils.ChangeBudget(getApplicationContext(), UserID);
                    }
                    break;
                case 3:
                    if(StatUtils.DaysSince(tempB.getStartDate()) > 13){
                        StatUtils.ChangeBudget(getApplicationContext(), UserID);
                    }
                case 4:

                    if(StatUtils.DaysSince(tempB.getStartDate()) > GetMonthLength()){
                        StatUtils.ChangeBudget(getApplicationContext(), UserID);
                    }
                    break;

            }
        }
    }

    /**
     * Gets how many days are in the current month.
     * @return The number of days in this month.
     */
    private Long GetMonthLength() {
        String date = StatUtils.GetCurrentDate();
        String[] dates = date.split("-");

        int code = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[0]);

        Long ret = 0L;

        switch (code){
            case 1:
                ret = 30L;
                break;
            case 2:
                ret = 30L;
                break;
            case 3:
                if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
                    ret = 28L;
                }else{
                    ret = 27L;
                }
                break;
            case 4:
                ret = 30L;
                break;
            case 5:
                ret = 29L;
                break;
            case 6:
                ret = 30L;
                break;
            case 7:
                ret = 29L;
                break;
            case 8:
                ret = 30L;
                break;
            case 9:
                ret = 29L;
                break;
            case 10:
                ret = 30L;
                break;
            case 11:
                ret = 30L;
                break;
            case 12:
                ret = 29L;
                break;

        }

        return ret;
    }

    @IgnoreExtraProperties
    public static class Tester{

        public String age;
        public String name;

        public Tester(){}

        public Tester(String age, String name){
            this.age = age;
            this.name = name;
        }
    }
}
