package com.example.project;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadingListSection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingListSection extends Fragment {
    DataBaseHelper db;
    SharedPreManager sharedPref;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReadingListSection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadingListSection.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadingListSection newInstance(String param1, String param2) {
        ReadingListSection fragment = new ReadingListSection();
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
        View root = inflater.inflate(R.layout.fragment_reading_list_section, container, false);
        LinearLayout containerBooks = root.findViewById(R.id.containerBooks);
        sharedPref = new SharedPreManager(requireContext());
        String sid = sharedPref.readString("student_id", "");
        db = new DataBaseHelper(requireContext(), "test11", null, 4);

        try (Cursor cursor = db.getAllBooksReading(sid)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title       = cursor.getString(0);
                    String author      = cursor.getString(1);
                    int availability   = cursor.getInt(2);
                    int bookId         = cursor.getInt(3);

                    View card = inflater.inflate(R.layout.books_card, containerBooks, false);

                    TextView Title = card.findViewById(R.id.bookTitle);
                    TextView Author = card.findViewById(R.id.bookAuthor);
                    TextView Availability = card.findViewById(R.id.bookAvailability);

                    MaterialButton btnBorrow = card.findViewById(R.id.btnBorrow);
                    MaterialButton btnInfo = card.findViewById(R.id.btnInfo);
                    MaterialButton btnRemove = card.findViewById(R.id.btnRemoveFav);

                    Title.setText(title);
                    Author.setText(author);
                    Availability.setText(availability == 1 ? "Available" : "Not Available");

                    btnBorrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] options = {"1 week", "2 weeks", "3 weeks", "4 weeks"};

                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Choose Borrowing Duration")
                                    .setItems(options, (dialog, which) -> {
                                        int weeks = which + 1; // index 0 = 1 week, 1 = 2 weeks...
                                        String reservationDate = LocalDate.now().toString();
                                        String dueDate = LocalDate.now().plusWeeks(weeks).toString();
                                        boolean success = db.addToBorrowedBooks(
                                                1,
                                                bookId,
                                                reservationDate,
                                                dueDate,
                                                "Pending"
                                        );
                                        if (success) {
                                            Toast.makeText(requireContext(),
                                                    "Waiting librarian to approve your request " + title + " for " + weeks + " week(s)",
                                                    Toast.LENGTH_SHORT).show();

                                            btnBorrow.setEnabled(false);
                                            btnBorrow.setAlpha(0.5f);
                                        } else {
                                            Toast.makeText(requireContext(),
                                                    "Failed to borrow " + title,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        }
                    });

                    btnInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Cursor cursor1 = db.BookInfo(String.valueOf(bookId));
                            if (cursor1 != null && cursor1.moveToFirst()) {
                                int id = cursor1.getInt(0);
                                String title = cursor1.getString(1);
                                String author = cursor1.getString(2);
                                String isbn = cursor1.getString(3);
                                String category = cursor1.getString(4);
                                int availability = cursor1.getInt(5);
                                String coverUrl = cursor1.getString(6);
                                int publicationYear = cursor1.getInt(7);

                                Books book = new Books(publicationYear, coverUrl, availability,
                                        category, isbn, author, title, id);

                                LayoutInflater inflater = LayoutInflater.from(requireContext());
                                View dialogView = inflater.inflate(R.layout.book_info_window, null);

                                TextView t1 = dialogView.findViewById(R.id.tv_book_id);
                                TextView t2 = dialogView.findViewById(R.id.tv_book_title);
                                TextView t3 = dialogView.findViewById(R.id.tv_book_author);
                                TextView t4 = dialogView.findViewById(R.id.tv_isbn);
                                TextView t5 = dialogView.findViewById(R.id.tv_category);
                                TextView t6 = dialogView.findViewById(R.id.tv_publication_year);
                                TextView t7 = dialogView.findViewById(R.id.tv_availability_status);

                                t1.setText("ID: " + book.getId());
                                t2.setText(book.getTitle());
                                t3.setText("by " + book.getAuthor());
                                t4.setText(book.getIsbn());
                                t5.setText(book.getCategory());
                                t6.setText(String.valueOf(book.getPublication_year()));
                                t7.setText(book.getAvailability() == 1 ? "Available" : "Unavailable");

                                ImageView Cover = dialogView.findViewById(R.id.iv_book_cover);
                                if (book.getCover_url() != null && !book.getCover_url().isEmpty()) {
                                } else {
                                    Cover.setImageResource(R.drawable.ic_book_placeholder);
                                }

                                new AlertDialog.Builder(requireContext())
                                        .setView(dialogView)
                                        .setPositiveButton("Close", null)
                                        .show();
                            }
                        }
                    });
                    btnRemove.setOnClickListener(v -> {
                        int deleted = db.removeBookFromReadingList("1", bookId);
                        if (deleted > 0) {
                            containerBooks.removeView(card);
                            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Not found in favorites", Toast.LENGTH_SHORT).show();
                        }
                    });
                    containerBooks.addView(card);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return root;
    }
}