package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.common.sdkinternal.SharedPrefManager;

public class LoginActivity extends AppCompatActivity  {
    DataBaseHelper db;
    SharedPreManager sharedpref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView register = findViewById(R.id.registeration);
        register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        });
        sharedpref = SharedPreManager.getInstance(LoginActivity.this);
        String emailShared = sharedpref.readString("email", "");
        String passwordShared = sharedpref.readString("password", "");
        EditText emailOrUniversityID = findViewById(R.id.email_text);
        EditText password = findViewById(R.id.Password);
        if(!emailShared.equals("") && !passwordShared.equals("")){
            emailOrUniversityID.setText(emailShared);
            password.setText(passwordShared);
        }
        Button login = findViewById(R.id.login);
        CheckBox remember = findViewById(R.id.rememberMe);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpref = SharedPreManager.getInstance(LoginActivity.this);
                db = new DataBaseHelper(LoginActivity.this, "test11", null, 4);
                Cursor account = db.checkInformations(emailOrUniversityID.getText().toString().trim(), password.getText().toString().trim());
                int pass = 0;
                if(emailOrUniversityID.getText().toString().length() == 0){
                    emailOrUniversityID.setError("Enter a Valid Email or University ID");
                }else{
                    pass++;
                }
                if(password.getText().toString().length() == 0){
                    password.setError("Enter a Valid Password");
                }else{
                    pass++;
                }
                if(pass == 2 && account.getCount() > 0){
                    account.moveToFirst();
                    int id = account.getInt(account.getColumnIndexOrThrow("id"));
                    sharedpref.deleteFromSharedPref("id");
                    if(remember.isChecked()){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                login.setText("Logging In...");
                                sharedpref.deleteFromSharedPref("email");
                                sharedpref.deleteFromSharedPref("password");
                                sharedpref.writeString("email", emailOrUniversityID.getText().toString().trim());
                                sharedpref.writeString("password", password.getText().toString().trim());
                            }
                        }, 3000);
                    }
                    else{
                        sharedpref.deleteFromSharedPref("email");
                        sharedpref.deleteFromSharedPref("password");
                    }
                    sharedpref.writeString("id", String.valueOf(id));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong in Password or Email", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}