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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

//harrison

    private SQLiteDatabase db;

    private EditText UsernameField;
    private EditText PasswordField;
    private Button LoginButton;
    private Button NewUserButton;

    private Boolean CameFromLogout;

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        db = StatUtils.GetDatabase(getApplicationContext());

        UsernameField = (EditText)findViewById(R.id.loginUsernameField);
        PasswordField = (EditText)findViewById(R.id.loginPasswordField);

        LoginButton = (Button)findViewById(R.id.loginButton);
        NewUserButton = (Button)findViewById(R.id.newUserButton);

        CameFromLogout = getIntent().getBooleanExtra("CameFromLogout", false);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = UsernameField.getText().toString();
                String password = PasswordField.getText().toString();

                String hashedPassword = StatUtils.GetHashedString(password);

                Cursor cursee = db.rawQuery("SELECT * FROM User WHERE Username = '" + username + "'", null);

                if(cursee.getCount() != 0){

                    cursee.moveToFirst();

                    String comp = cursee.getString(cursee.getColumnIndex("HashedPassword"));

                    if(comp.equals(hashedPassword)){

                        Long UserID = cursee.getLong(cursee.getColumnIndex("UserID"));
                        Budget tempB = StatUtils.GetBudget(getApplicationContext(), StatUtils.GetBudgetID(getApplicationContext(), UserID));

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

                        Intent goToLanding = SwitchManager.SwitchActivity(getApplicationContext(), "Homepage");//new Intent(MainActivity.this, LandingPage.class).putExtra("UserID", cursee.getLong(cursee.getColumnIndex("UserID")));

                        PreferencesEditor.putLong("UserID", UserID);
                        PreferencesEditor.commit();

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

        NewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent(MainActivity.this, CreateUser.class);
                startActivity(temp);
            }
        });

    }

    @Override
    public void onBackPressed(){
        if(CameFromLogout){
            return;
        }else{
            super.onBackPressed();
        }
    }

    private Long GetMonthLength() {
        String date = StatUtils.GetCurrentDate();
        String[] dates = date.split("-");

        int code = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[0]);

        Long ret = new Long(0);

        switch (code){
            case 1:
                ret = new Long(30);
                break;
            case 2:
                ret = new Long(30);
                break;
            case 3:
                if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
                    ret = new Long(28);
                }else{
                    ret = new Long(27);
                }
                break;
            case 4:
                ret = new Long(30);
                break;
            case 5:
                ret = new Long(29);
                break;
            case 6:
                ret = new Long(30);
                break;
            case 7:
                ret = new Long(29);
                break;
            case 8:
                ret = new Long(30);
                break;
            case 9:
                ret = new Long(29);
                break;
            case 10:
                ret = new Long(30);
                break;
            case 11:
                ret = new Long(30);
                break;
            case 12:
                ret = new Long(29);
                break;

        }

        return ret;
    }
}
