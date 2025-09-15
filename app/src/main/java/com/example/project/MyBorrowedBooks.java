package com.example.project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBorrowedBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBorrowedBooks extends Fragment {
    DataBaseHelper db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyBorrowedBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBorrowedBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBorrowedBooks newInstance(String param1, String param2) {
        MyBorrowedBooks fragment = new MyBorrowedBooks();
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
        View root = inflater.inflate(R.layout.fragment_my_borrowed_books, container, false);

        RecyclerView rv = root.findViewById(R.id.recyclerBorrowedBooks);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        BorrowedBooksAdapter adapter = new BorrowedBooksAdapter();
        rv.setAdapter(adapter);

        db = new DataBaseHelper(requireContext(), "LibraryDB", null, 4);

        List<BorrowedBook> data = new ArrayList<>();
        try (Cursor cursor = db.getBooks()) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title      = cursor.isNull(0) ? null : cursor.getString(0);
                    String author     = cursor.isNull(1) ? null : cursor.getString(1);
                    String resDate    = cursor.isNull(2) ? null : cursor.getString(2);
                    String dueDate    = cursor.isNull(3) ? null : cursor.getString(3);
                    String status     = cursor.isNull(4) ? null : cursor.getString(4);
                    String returnDate = cursor.isNull(5) ? null : cursor.getString(5);
                    String fine       = "0.00";
                    LocalDate today = LocalDate.now();
                    if (dueDate != null) {
                        LocalDate dueDate2 = LocalDate.parse(dueDate);

                        if (today.isAfter(dueDate2)) {
                            fine = "50";   // overdue → apply fine
                        } else if (today.isBefore(dueDate2)) {
                            fine = "0.00"; // still on time
                        } else {
                            fine = "0.00"; // due today, no fine
                        }
                    }
                    data.add(new BorrowedBook(title, author, resDate, dueDate, returnDate, status, fine));
                }
                adapter.setItems(data);
            }else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }
}