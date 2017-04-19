package com.example.aegis.budgpal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.android.gms.tasks.Tasks;

public class CreateUser extends AppCompatActivity {

    private SQLiteDatabase db;
    private Button CreateButton;
    private EditText UsernameField;
    private EditText PasswordField;
    private EditText ConfirmPasswordField;

    private SharedPreferences Preferences;
    private SharedPreferences.Editor PreferencesEditor;

    private final static String TAG = "CreateUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Log.d(TAG, "Entered");

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


                final String username = UsernameField.getText().toString();
                final String password = PasswordField.getText().toString();
                String confirmpw = ConfirmPasswordField.getText().toString();
                if(username.isEmpty() || password.isEmpty() || confirmpw.isEmpty() || !password.equals(confirmpw)){
                    startActivity(getIntent());
                    finish();
                    return;
                }

                /*FireUser.getUserByUserName(username).addOnCompleteListener(new OnCompleteListener<FireUser>() {
                    @Override
                    public void onComplete(@NonNull Task<FireUser> task) {
                        FireUser user = task.getResult();

                        if(user.date.equals("1990-01-01")){
                            final FireUser newy = new FireUser(username, StatUtils.GetHashedString(password), StatUtils.GetCurrentDate());
                            newy.pushToDatabase().addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                @Override
                                public void onComplete(@NonNull Task<Boolean> task) {
                                    Intent goToLanding = new Intent(CreateUser.this, LandingPage.class);

                                    PreferencesEditor.putString("UserKey", newy.getUserKey().getResult());
                                    PreferencesEditor.commit();

                                    goToLanding.putExtra("CameFromEntry", true);
                                    startActivity(goToLanding);
                                    finish();
                                }
                            });
                        }
                    }
                });*/


                /*Cursor curs = db.rawQuery("SELECT * FROM User WHERE Username = '" + username + "'", null);
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
                finish();*/

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FireUser tempU = Tasks.await(FireUser.getUserByUserName(username));
                            if (!tempU.date.equals("1990-01-01")) {
                                //Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                                startActivity(getIntent());
                                finish();
                                return;
                            }


                            FireUser newU = new FireUser(username, StatUtils.GetHashedString(password), StatUtils.GetCurrentDate());
                            newU.pushToDatabase();

                            Intent goToLanding = new Intent(CreateUser.this, LandingPage.class);

                            PreferencesEditor.putString("UserKey", Tasks.await(newU.getUserKey()));
                            PreferencesEditor.commit();

                            goToLanding.putExtra("CameFromEntry", true);
                            startActivity(goToLanding);
                            finish();
                        }catch (Exception e){
                            Log.d(TAG, "Failed");
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });

                t.start();
            }
        });
    }
}
