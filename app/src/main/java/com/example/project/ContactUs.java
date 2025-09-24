package com.example.project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUs extends Fragment {
    SharedPreManager sharedPref;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactUs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUs.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUs newInstance(String param1, String param2) {
        ContactUs fragment = new ContactUs();
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
        View root = inflater.inflate(R.layout.fragment_contact_us, container, false);
        sharedPref = new SharedPreManager(requireContext());

        Button btnCall = root.findViewById(R.id.CallLy);
        Button btnFindUs = root.findViewById(R.id.FindUs);
        Button btnEmail = root.findViewById(R.id.Email);

        TextView phoneNumberTextView = root.findViewById(R.id.phone_number);
        TextView emailAddressTextView = root.findViewById(R.id.email_address);
        phoneNumberTextView.setText(sharedPref.readString("phone_number2", ""));
        emailAddressTextView.setText(sharedPref.readString("email_address2", ""));

        TextView workingHoursTextView = root.findViewById(R.id.working_hours);
        workingHoursTextView.setText(sharedPref.readString("working_hours2", ""));
        TextView servicesTextView = root.findViewById(R.id.TheServices);
        servicesTextView.setText(sharedPref.readString("TheServices2", ""));

        TextView policiesTextView = root.findViewById(R.id.Policies);
        policiesTextView.setText(sharedPref.readString("Policies2", ""));

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent =new Intent();
                dialIntent.setAction(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phoneNumberTextView.getText().toString()));
                startActivity(dialIntent);
            }
        });

        btnFindUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapsIntent =new Intent();
                mapsIntent.setAction(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse("geo:31.9723,35.1906?q=Birzeit+University+Library"));
                startActivity(mapsIntent);
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gmailIntent =new Intent();
                gmailIntent.setAction(Intent.ACTION_SENDTO);
                gmailIntent.setType("message/rfc822");
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL,emailAddressTextView.getText().toString());
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT,"My Subject");
                gmailIntent.putExtra(Intent.EXTRA_TEXT,"Content of the message");
                startActivity(gmailIntent);
            }
        });


        return root;
    }
}