package com.example.aegis.budgpal;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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

    private void FirebasePlayground() {


        //FireEvent temp = new FireEvent("-KhOxJw3Rd9WzezsI4s7", StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), "horse", StatUtils.GetCurrentDate());


        /*Task<FireEvent> hold = FireEvent.getEventByEventKey("-KhZvt5ySuocI5NEhivL");

        hold.addOnCompleteListener(new OnCompleteListener<FireEvent>() {
            @Override
            public void onComplete(@NonNull Task<FireEvent> task) {
                FireEvent temp = task.getResult();
                Toast.makeText(getApplicationContext(), temp.eventKey, Toast.LENGTH_LONG).show();
            }
        });*/

        FireBudget temp = new FireBudget("-KhOxJw3Rd9WzezsI4s7", 1, 1, StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), StatUtils.GetCurrentDate(), (float)12, false);

        temp.pushToDatabase();
        //Toast.makeText(getApplicationContext(),temp.isDeleted().getResult().toString(), Toast.LENGTH_LONG).show();
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

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("db");



                ValueEventListener horse = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        EditText UsernameField = (EditText)findViewById(R.id.loginUsernameField);
                        EditText PasswordField = (EditText)findViewById(R.id.loginPasswordField);

                        Log.d(TAG, "Fireuser");
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            if(item.child("name").getValue(String.class).equals(UsernameField.getText().toString())){
                                FireUser temp = item.getValue(FireUser.class);

                                if(temp.hashedPassword.equals(StatUtils.GetHashedString(PasswordField.getText().toString()))){
                                    Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_LONG).show();

                                    PreferencesEditor.putString("UserKey", item.getKey());
                                    PreferencesEditor.commit();

                                    Intent goToLanding = SwitchManager.SwitchActivity(getApplicationContext(), "Homepage");
                                    startActivity(goToLanding);
                                }else{
                                    Toast.makeText(getApplicationContext(), "False", Toast.LENGTH_LONG).show();
                                }

                                //Toast.makeText(getApplicationContext(), temp.name, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                myRef.child("Users").addListenerForSingleValueEvent(horse);

                /*String username = UsernameField.getText().toString();
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
                }*/
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
    public static class EventListing{

        public String description;
        public String date;

        public EventListing(){}

        public EventListing(String description, String date){
            this.description = description;
            this.date = date;
        }
    }


}
