package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileManagement extends Fragment {
    SharedPreManager sharedPref;
    DataBaseHelper db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileManagement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileManagement.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileManagement newInstance(String param1, String param2) {
        ProfileManagement fragment = new ProfileManagement();
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
        View root = inflater.inflate(R.layout.fragment_profile_management, container, false);
        db = new DataBaseHelper(requireContext(), "alaaDB", null, 4);
        sharedPref = new SharedPreManager(requireContext());
        String sid = sharedPref.readString("student_id", "");

        Cursor cursor = db.StudentDataID(sid);
        String passwordValidation = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$";


        if (cursor.moveToFirst()) {
           String universityId = cursor.getString(1);
           String firstName    = cursor.getString(2);
           String lastName     = cursor.getString(3);
           String email        = cursor.getString(4);
           String department   = cursor.getString(7);
           String level        = cursor.getString(8);
           String phone        = cursor.getString(6);

            TextView universityIDTV = root.findViewById(R.id.universityID);
            TextView departmentTV   = root.findViewById(R.id.department);
            TextView levelTV        = root.findViewById(R.id.StudentLevel);
            TextView studentName    = root.findViewById(R.id.user_name_display);
            TextView EmailTV        = root.findViewById(R.id.user_email);

            TextInputEditText firstNameET = root.findViewById(R.id.EditFirst_name);
            TextInputEditText lastNameET  = root.findViewById(R.id.EditLast_name);
            TextInputLayout phoneLayout   = root.findViewById(R.id.StartCPhone_number);
            TextInputEditText phoneET     = root.findViewById(R.id.phone_number);

            universityIDTV.setText(universityId);
            departmentTV.setText(department);
            levelTV.setText(level);
            studentName.setText(firstName + " " + lastName);
            EmailTV.setText(email);
            firstNameET.setText(firstName);
            lastNameET.setText(lastName);
            phoneET.setText(phone);
        }

        Button updatePersonalInfo = root.findViewById(R.id.btn_update_personal_info);
        Button updatePassword = root.findViewById(R.id.btn_change_password);
        Button viewHistory = root.findViewById(R.id.btn_view_history);
        Button logout = root.findViewById(R.id.btn_logout);


        updatePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText firstNameET = root.findViewById(R.id.EditFirst_name);
                TextInputEditText lastNameET = root.findViewById(R.id.EditLast_name);
                TextInputEditText phoneET = root.findViewById(R.id.phone_number);
                db.updateStudentInfo(sid, firstNameET.getText().toString().trim(), lastNameET.getText().toString().trim(), phoneET.getText().toString().trim());
                Toast.makeText(requireContext(), "Personal Information Updated", Toast.LENGTH_SHORT).show();
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText password = root.findViewById(R.id.EditCurrent_password);
                TextInputEditText NewPassword = root.findViewById(R.id.EditNew_password);
                TextInputEditText confirmPassword = root.findViewById(R.id.EditConfirm_password);

                int pass = 0;
                if(!NewPassword.getText().toString().trim().matches(passwordValidation)){
                    NewPassword.setError("Password must be at least 6 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character!");
                }
                else{
                    pass++;
                }
                if(!confirmPassword.getText().toString().trim().equals(NewPassword.getText().toString().trim())){
                    confirmPassword.setError("Passwords must match!");
                }
                else{
                    pass++;
                }
                if(!db.checkPassword(sid, password.getText().toString().trim())){
                    password.setError("password is not correct!");
                }else {
                    pass++;
                }
                if (pass == 3){
                    db.updatePassword(sid, NewPassword.getText().toString().trim());
                    password.setText("");
                    NewPassword.setText("");
                    confirmPassword.setText("");
                }
            }
        });

        viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                View dialogView = inflater.inflate(R.layout.borrowed_history, null);
                LinearLayout historyContainer = dialogView.findViewById(R.id.BorrowedHistory);

                Cursor cursor1 = db.getBorrowedHistory(sid);
                if (cursor1 != null && cursor1.moveToFirst()) {
                    do {
                        String title = cursor1.getString(0);
                        String reservationDate = cursor1.getString(1);
                        String status = cursor1.getString(2);

                        View card = inflater.inflate(R.layout.borrowed_card, historyContainer, false);

                        TextView titleTV = card.findViewById(R.id.BookTitle);
                        TextView reservationDateTV = card.findViewById(R.id.BorrowedDate);
                        TextView statusTV = card.findViewById(R.id.Status);

                        titleTV.setText(title);
                        reservationDateTV.setText("Borrowed: " + reservationDate);

                        if ("Overdue".equalsIgnoreCase(status)) {
                            statusTV.setText("Overdue");
                            statusTV.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
                        } else if ("Borrowed".equalsIgnoreCase(status)) {
                            statusTV.setText("Borrowed");
                            statusTV.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark));
                        } else {
                            statusTV.setText("Returned");
                            statusTV.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
                        }

                        historyContainer.addView(card);

                    } while (cursor1.moveToNext());
                    cursor1.close();
                }

                new AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setPositiveButton("Close", null)
                        .show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        TextView total_borrowed = root.findViewById(R.id.tv_total_borrowed);
        TextView currently_borrowed = root.findViewById(R.id.tv_currently_borrowed);
        TextView overdue_books = root.findViewById(R.id.tv_overdue_books);

        int[] counts = db.getBorrowedCounts(sid);
        total_borrowed.setText(String.valueOf(counts[0]));
        currently_borrowed.setText(String.valueOf(counts[1]));
        overdue_books.setText(String.valueOf(counts[2]));

        cursor.close();
        return root;
    }
}