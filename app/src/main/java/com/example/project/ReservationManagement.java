package com.example.project;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationManagement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationManagement extends Fragment {
    DataBaseHelper db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReservationManagement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationManagement.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationManagement newInstance(String param1, String param2) {
        ReservationManagement fragment = new ReservationManagement();
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
        View root = inflater.inflate(R.layout.fragment_reservation_management, container, false);

        db = new DataBaseHelper(getActivity(), "alaaDB", null, 4);
        Cursor cursor = db.getAllReservationsData();

        LinearLayout reservationContainer = root.findViewById(R.id.reservationContainer);

        if (cursor.moveToFirst()) {
            do {
                View cardView = inflater.inflate(R.layout.reservation_card, reservationContainer, false);

                String reservationId = cursor.getString(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String bookId = cursor.getString(3);
                String studentId = cursor.getString(4);
                String student_first_name = cursor.getString(5);
                String student_last_name = cursor.getString(6);
                String reservationDate = cursor.getString(7);
                String dueDate = cursor.getString(8);
                String method = cursor.getString(9);
                String notes = cursor.getString(10);
                String returnDate = cursor.getString(11);
                String status = cursor.getString(12);

                TextView reservationIdTextView = cardView.findViewById(R.id.reservation_id);
                TextView bookIdTextView = cardView.findViewById(R.id.book_id);
                TextView studentIdTextView = cardView.findViewById(R.id.student_id);
                TextView reservationDateTextView = cardView.findViewById(R.id.reservation_date);
                TextView returnDateTextView = cardView.findViewById(R.id.return_date);
                TextView methodTextView = cardView.findViewById(R.id.collection_method);
                TextView notesTextView = cardView.findViewById(R.id.special_notes);
                TextView titleTextView = cardView.findViewById(R.id.book_title);
                TextView authorTextView = cardView.findViewById(R.id.book_author);
                TextView studentNameTextView = cardView.findViewById(R.id.student_name);
                TextView dueDateTextView = cardView.findViewById(R.id.due_date);

                reservationIdTextView.setText(reservationId);
                bookIdTextView.setText(bookId);
                studentIdTextView.setText(studentId);
                reservationDateTextView.setText(reservationDate);
                returnDateTextView.setText(returnDate);
                methodTextView.setText(method);
                notesTextView.setText(notes);
                titleTextView.setText(title);
                authorTextView.setText(author);
                studentNameTextView.setText(student_first_name + " " + student_last_name);
                dueDateTextView.setText(dueDate);

                Button ac1 = cardView.findViewById(R.id.ac1);
                Button ac2 = cardView.findViewById(R.id.ac2);

                if (status.equalsIgnoreCase("pending")) {
                    ac1.setText("approve");
                    ac2.setText("reject");

                    ac1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.changeStatus(reservationId,"Borrowed");
                            ac1.setText("return");
                            ac2.setText("extend");
                            Toast.makeText(getActivity(), "Reservation Approved", Toast.LENGTH_SHORT).show();
                        }
                    });

                    ac2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.removeReservation(reservationId);
                            Toast.makeText(getActivity(), "Reservation Rejected", Toast.LENGTH_SHORT).show();
                            reservationContainer.removeView(cardView);
                        }
                    });

                }else{
                    ac1.setText("return");
                    ac2.setText("extend");

                    ac1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.changeStatus(reservationId,"Returned");
                            Toast.makeText(getActivity(), "Reservation Returned", Toast.LENGTH_SHORT).show();
                        }
                    });
                    ac2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] options = {"1 week", "2 weeks", "3 weeks", "4 weeks"};
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Choose Borrowing Duration")
                                    .setItems(options, (dialog, which) -> {
                                        int weeks = which + 1;
                                        TextView returnDateTextView = requireView().findViewById(R.id.return_date);
                                        String returnDateStr = returnDateTextView.getText().toString();

                                        LocalDate baseDate;
                                        try {
                                            baseDate = LocalDate.parse(returnDateStr);
                                        } catch (Exception e) {
                                            baseDate = LocalDate.now();
                                        }
                                        String newDueDate = baseDate.plusWeeks(weeks).toString();
                                        returnDateTextView.setText(newDueDate);
                                        Toast.makeText(requireContext(),
                                                "Due date extended to " + newDueDate,
                                                Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    });
                }

                reservationContainer.addView(cardView);

            } while (cursor.moveToNext());
        }

        return root;
    }

}