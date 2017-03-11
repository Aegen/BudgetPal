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


public class MainActivity extends AppCompatActivity {

//harrison

    private SQLiteDatabase db;

    private EditText UsernameField;
    private EditText PasswordField;
    private Button LoginButton;
    private Button NewUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("first", "first");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean dbExists = checkForDatabase();

        UsernameField = (EditText)findViewById(R.id.loginUsernameField);
        PasswordField = (EditText)findViewById(R.id.loginPasswordField);

        LoginButton = (Button)findViewById(R.id.loginButton);
        NewUserButton = (Button)findViewById(R.id.newUserButton);


        /***********************************************/
        //Database access zone
        //Log.d("Date",new Date(1,1,1,).toString());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
         //Create Database object, declared globally above
        //User horse = new User("dave", "asdlhfasd;l", tempDate, false, a);
        //a.addUser(horse);
        //horse.pushToDatabase();

        db = StatUtils.GetDatabase(getApplicationContext()); //Create Database object, declared globally above
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
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();


                        Intent goToLanding = SwitchManager.SwitchActivity(getApplicationContext(), "Homepage", cursee.getLong(cursee.getColumnIndex("UserID")));//new Intent(MainActivity.this, LandingPage.class).putExtra("UserID", cursee.getLong(cursee.getColumnIndex("UserID")));
                        startActivity(goToLanding);
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

    public boolean checkForDatabase(){
        return true;
    }
}
