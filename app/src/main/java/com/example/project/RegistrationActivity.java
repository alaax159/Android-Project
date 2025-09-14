package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistrationActivity extends AppCompatActivity {


    String universityIdValidation = "[0-2][0-9]{3}\\d{4}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText universityID = findViewById(R.id.universityID);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.email);
        EditText confirmedEmail = findViewById(R.id.confirmedEmail);
        EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        Spinner academicLevel = findViewById(R.id.academicLevel);
        Spinner department = findViewById(R.id.department);
        Button register = findViewById(R.id.registerButton);
        String[] academicLevelOptions = { "Select Academic Level", "Freshman", "Sophomore", "Junior", "Senior","Graduate" };
        final Spinner academicSpinner =(Spinner)
                findViewById(R.id.academicLevel);
        ArrayAdapter<String> objAcademicArr = new
                ArrayAdapter<>(this,android.R.layout.simple_spinner_item, academicLevelOptions);
        academicSpinner.setAdapter(objAcademicArr);
        String[] departmentOptions = {  "Select Department", "Computer Science", "Engineering", "Business", "Literature", "Medicine" };
        final Spinner departmentSpinner =(Spinner)
                findViewById(R.id.department);
        ArrayAdapter<String> objdepartmentArr = new
                ArrayAdapter<>(this,android.R.layout.simple_spinner_item, departmentOptions);
        departmentSpinner.setAdapter(objdepartmentArr);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!universityID.getText().toString().trim().matches(universityIdValidation)){
                    universityID.setError("University ID must be Unique and in form YYYY####");
                }
            }
        });



    }
}