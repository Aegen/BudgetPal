package com.example.aegis.budgpal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateUser extends AppCompatActivity {

    private SQLiteDatabase db;
    private Button CreateButton;
    private EditText UsernameField;
    private EditText PasswordField;
    private EditText ConfirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

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
                    finish();
                    startActivity(getIntent());
                    return;
                }
                Cursor curs = db.rawQuery("SELECT * FROM User WHERE Username = '" + username + "'", null);
                if(curs.getCount() != 0){
                    finish();
                    startActivity(getIntent());
                    return;
                }



                User newU = new User(username, password, StatUtils.GetCurrentDate(), false, new DatabaseHandler(getApplicationContext(), "database", null, 1) );
                newU.pushToDatabase();

                Intent goToLanding = new Intent(CreateUser.this, ViewHistory.class).putExtra("UserID", newU.getUserID());
                startActivity(goToLanding);
            }
        });
    }
}
