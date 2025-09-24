package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditLibrarySettings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditLibrarySettings extends Fragment {
    SharedPreManager sharedPref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditLibrarySettings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditLibrarySettings.
     */
    // TODO: Rename and change types and number of parameters
    public static EditLibrarySettings newInstance(String param1, String param2) {
        EditLibrarySettings fragment = new EditLibrarySettings();
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
        View root = inflater.inflate(R.layout.fragment_edit_library_settings, container, false);
        sharedPref = new SharedPreManager(requireContext());


        LinearLayout linearLayout = root.findViewById(R.id.editLibrarySettings);

        TextInputEditText phoneNumberEditText = root.findViewById(R.id.phone_number);
        TextInputEditText emailEditText = root.findViewById(R.id.email_address);
        TextInputEditText workingHoursEditText = root.findViewById(R.id.working_hours);
        TextInputEditText servicesEditText = root.findViewById(R.id.TheServices);
        TextInputEditText policiesEditText = root.findViewById(R.id.Policies);

        phoneNumberEditText.setText(sharedPref.readString("phone_number2", ""));
        emailEditText.setText(sharedPref.readString("email_address2", ""));
        workingHoursEditText.setText(sharedPref.readString("working_hours2", ""));
        servicesEditText.setText(sharedPref.readString("TheServices2", ""));
        policiesEditText.setText(sharedPref.readString("Policies2", ""));


        Button saveButton = root.findViewById(R.id.btnSubmitInfo);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPref.writeString("phone_number2", phoneNumberEditText.getText().toString());
                sharedPref.writeString("email_address2", emailEditText.getText().toString());
                sharedPref.writeString("working_hours2", workingHoursEditText.getText().toString());
                sharedPref.writeString("TheServices2", servicesEditText.getText().toString());
                sharedPref.writeString("Policies2", policiesEditText.getText().toString());

            }
        });


        return root;
    }
}