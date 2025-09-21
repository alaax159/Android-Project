package com.example.project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileManagement extends Fragment {
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
        db = new DataBaseHelper(requireContext(), "AdnanDB", null, 4);
        Cursor cursor = db.StudentDataID("1");
        String universityId = "";
        String firstName = "";
        String lastName = "";
        String email = "";
        String department = "";
        String level = "";
        String phone = "";
        if (cursor != null && cursor.moveToFirst()) {
            universityId = cursor.getString(cursor.getColumnIndexOrThrow("university_id"));
            firstName    = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
            lastName     = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            email        = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            department   = cursor.getString(cursor.getColumnIndexOrThrow("department"));
            level        = cursor.getString(cursor.getColumnIndexOrThrow("level"));
            phone        = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
        }
        if (cursor != null) cursor.close();


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
        return root;
    }
}