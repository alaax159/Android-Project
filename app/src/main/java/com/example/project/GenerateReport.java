package com.example.project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateReport extends Fragment {
    DataBaseHelper db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenerateReport() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerateReport.
     */
    // TODO: Rename and change types and number of parameters
    public static GenerateReport newInstance(String param1, String param2) {
        GenerateReport fragment = new GenerateReport();
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
        View root = inflater.inflate(R.layout.fragment_generate_report, container, false);
        LinearLayout ReportContainer = root.findViewById(R.id.ReportContainer);
        LinearLayout overdueItemsContainer = root.findViewById(R.id.overdue_items_container);

        db = new DataBaseHelper(requireContext(), "alaaDB", null, 4);

        int studentActivity = db.getActiveStudentsCount();
        TextView numberOfActiveStudents = root.findViewById(R.id.student_activity_report);
        numberOfActiveStudents.setText("number of active students: "+ String.valueOf(studentActivity));

        String borrowedBooks = db.getMostBorrowedBook();

        TextView mostBorrowedBook = root.findViewById(R.id.popular_books_report);

        mostBorrowedBook.setText(borrowedBooks);

        Cursor overdueItemsCursor = db.getOverdueItems();
        if (overdueItemsCursor != null) {
            while (overdueItemsCursor.moveToNext()) {
                String title = overdueItemsCursor.getString(0);
                String reservation_date = overdueItemsCursor.getString(1);
                String due_date = overdueItemsCursor.getString(2);
                String firstName = overdueItemsCursor.getString(3);
                String lastName = overdueItemsCursor.getString(4);

                View card = inflater.inflate(R.layout.overdue_card, ReportContainer, false);
                TextView bookTitle = card.findViewById(R.id.BookTitle);
                TextView reservationDate = card.findViewById(R.id.BorrowedDate);
                TextView studentName = card.findViewById(R.id.studentName);
                TextView fine = card.findViewById(R.id.Fine);

                bookTitle.setText(title);
                reservationDate.setText(reservation_date);
                studentName.setText(firstName + " " + lastName);
                fine.setText("Fine: 50$");

                overdueItemsContainer.addView(card);
            }
        }
        return root;
    }
}