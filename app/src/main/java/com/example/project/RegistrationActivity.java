package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class RegistrationActivity extends AppCompatActivity {

    DataBaseHelper db;
    String universityIdValidation = "^[0-2][0-9]{3}\\d{4}$";
    String firstNameValidation = "^[A-Za-z]{3,}$";
    String lastNameValidation = "^[A-Za-z]{3,}$";
    String emailValidationStudent = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@university.edu$";
    String emailValidationLibrarian = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@library.edu$";
    String passwordValidation = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$";
    String phoneNumberValidation = "^[0-9]{10}$";

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
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                db = new DataBaseHelper(RegistrationActivity.this, "alaaDB", null, 4);
                int pass = 0;
                if(!universityID.getText().toString().trim().matches(universityIdValidation) || !db.checkUniversityId(universityID.getText().toString().trim())){
                    universityID.setError("University ID must be Unique and in form YYYY####");

                }
                else{
                    pass++;
                }
                if(!firstName.getText().toString().trim().matches(firstNameValidation)){
                    firstName.setError("Name must be at least 3 characters!");
                }else{
                    pass++;
                }
                if(!lastName.getText().toString().trim().matches(lastNameValidation)){
                    lastName.setError("Name must be at least 3 characters!");
                }else{
                    pass++;
                }
                if(email.getText().toString().trim().matches(emailValidationStudent) || email.getText().toString().trim().matches(emailValidationLibrarian)){
                    email.setError(null);
                    pass++;
                }
                else{
                    email.setError("Email must be in the form @university.edu or @library.edu");
                }
                if(!confirmedEmail.getText().toString().trim().equals(email.getText().toString().trim())){
                    confirmedEmail.setError("Emails must match!");
                }
                else{
                    pass++;
                }
                if(!password.getText().toString().trim().matches(passwordValidation)){
                    password.setError("Password must be at least 6 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character!");
                }
                else{
                    pass++;
                }
                if(!confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())){
                    confirmPassword.setError("Passwords must match!");
                }
                else{
                    pass++;
                }
                if(!phoneNumber.getText().toString().trim().matches(phoneNumberValidation)){
                    phoneNumber.setError("Phone number must be 10 digits!");
                }
                else{
                    pass++;
                }
                if(academicLevel.getSelectedItem().toString().equals("Select Academic Level")){

                    Toast.makeText(RegistrationActivity.this, "Please select an academic level", Toast.LENGTH_SHORT).show();
                }else{
                    pass++;
                }
                if(department.getSelectedItem().toString().equals("Select Department")){
                    Toast.makeText(RegistrationActivity.this, "Please select a department", Toast.LENGTH_SHORT).show();
                }else{
                    pass++;
                }
                if(pass == 10) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            register.setText("Registering...");
                            CountryCodePicker countryCodePicker = findViewById(R.id.countryCodePicker);
                            String countryCode = countryCodePicker.getSelectedCountryCode();
                            String universityId = universityID.getText().toString().trim();
                            String first_name = firstName.getText().toString().trim();
                            String last_name = lastName.getText().toString().trim();
                            String emailAddress = email.getText().toString().trim();
                            String password_hashed = password.getText().toString().trim();
                            String department = departmentSpinner.getSelectedItem().toString();
                            String level = academicSpinner.getSelectedItem().toString();
                            String phone_number = phoneNumber.getText().toString().trim();

                            db.registerStudent(universityId, first_name, last_name, emailAddress, password_hashed, department, level, countryCode + phone_number);
                            register.setText("Registered");
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }, 2000);
                }

            }
        });



    }
}