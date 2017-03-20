package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity {

    private SQLiteDatabase db;
    private Button CreateButton;
    private EditText UsernameField;
    private EditText PasswordField;
    private EditText ConfirmPasswordField;

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Preferences = getSharedPreferences(getString(R.string.preferences_name), MODE_PRIVATE);
        PreferencesEditor = getSharedPreferences(getString(R.string.preferences_name),MODE_PRIVATE).edit();

        db = StatUtils.GetDatabase(getApplicationContext());

        CreateButton = (Button)findViewById(R.id.newUserNextButton);

        UsernameField = (EditText)findViewById(R.id.newUserUsernameField);
        PasswordField = (EditText)findViewById(R.id.newUserPasswordField);
        ConfirmPasswordField = (EditText)findViewById(R.id.newUserConfirmPasswordField);

        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = UsernameField.getText().toString();
                String password = PasswordField.getText().toString();
                String confirmpw = ConfirmPasswordField.getText().toString();
                if(username.isEmpty() || password.isEmpty() || confirmpw.isEmpty() || !password.equals(confirmpw)){
                    startActivity(getIntent());
                    finish();
                    return;
                }
                Cursor curs = db.rawQuery("SELECT * FROM User WHERE Username = '" + username + "'", null);
                if(curs.getCount() != 0){
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                    finish();
                    return;
                }
                curs.close();



                User newU = new User(username, password, false, getApplicationContext() );
                newU.pushToDatabase();

                Intent goToLanding = new Intent(CreateUser.this, LandingPage.class).putExtra("UserID", newU.getUserID());

                PreferencesEditor.putLong("UserID", newU.getUserID());
                PreferencesEditor.commit();

                goToLanding.putExtra("CameFromEntry", true);
                startActivity(goToLanding);
                finish();
            }
        });
    }
}
