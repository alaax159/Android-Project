package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hbb20.CountryCodePicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterLibrarrian#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterLibrarrian extends Fragment {
    DataBaseHelper db;
    String firstNameValidation = "^[A-Za-z]{3,}$";
    String lastNameValidation = "^[A-Za-z]{3,}$";
    String emailValidationStudent = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@university.edu$";
    String emailValidationLibrarian = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@library.edu$";
    String passwordValidation = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$";
    String phoneNumberValidation = "^[0-9]{10}$";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterLibrarrian() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterLibrarrian.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterLibrarrian newInstance(String param1, String param2) {
        RegisterLibrarrian fragment = new RegisterLibrarrian();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_librarrian, container, false);
        LinearLayout main = root.findViewById(R.id.main);
        EditText firstName = root.findViewById(R.id.firstName);
        EditText lastName = root.findViewById(R.id.lastName);
        EditText email = root.findViewById(R.id.email);
        EditText confirmedEmail = root.findViewById(R.id.confirmedEmail);
        EditText password = root.findViewById(R.id.password);
        EditText confirmPassword = root.findViewById(R.id.confirmPassword);
        CountryCodePicker countryCodePicker = root.findViewById(R.id.countryCodePicker);
        EditText phoneNumber = root.findViewById(R.id.phoneNumber);
        Button registerButton = root.findViewById(R.id.registerButton);

       int pass = 0;

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

        if(pass == 7) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    registerButton.setText("Registering...");
                    String countryCode = countryCodePicker.getSelectedCountryCode();
                    String first_name = firstName.getText().toString().trim();
                    String last_name = lastName.getText().toString().trim();
                    String emailAddress = email.getText().toString().trim();
                    String password_hashed = password.getText().toString().trim();
                    String phone_number = phoneNumber.getText().toString().trim();

                    db.registerLibrarian( first_name, last_name, emailAddress, password_hashed, countryCode + phone_number);
                    registerButton.setText("Registered");
                }
            }, 2000);
        }
        return root;
    }
}