package com.example.project;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewArrivalsBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewArrivalsBooks extends Fragment {
    DataBaseHelper db;
    SharedPreManager sharedPref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewArrivalsBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewArrivalsBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static NewArrivalsBooks newInstance(String param1, String param2) {
        NewArrivalsBooks fragment = new NewArrivalsBooks();
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
        View root = inflater.inflate(R.layout.fragment_new_arrivals_books, container, false);
        LinearLayout newArrivalsContainer = root.findViewById(R.id.newArrivalsContainer);
        sharedPref = new SharedPreManager(requireContext());
        String sid = sharedPref.readString("student_id", "");
        db = new DataBaseHelper(requireContext(), "alaaDB", null, 4);

        Cursor cursor = db.getNewArrivals();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String isbn = cursor.getString(3);
                String category = cursor.getString(4);
                String availability = cursor.getString(5);
                //String cover_url = cursor.getString(6);
                String publication_year = cursor.getString(7);

                View card = inflater.inflate(R.layout.new_arrival_card, newArrivalsContainer, false);

                TextView bookID = card.findViewById(R.id.book_id);
                TextView bookTitle = card.findViewById(R.id.book_title);
                TextView bookAuthor = card.findViewById(R.id.book_author);
                TextView bookCategory = card.findViewById(R.id.category);
                TextView bookAvailability = card.findViewById(R.id.availability_badge);
                TextView bookPublicationYear = card.findViewById(R.id.publication_year);
                TextView bookISBN = card.findViewById(R.id.isbn);
                //TextView bookCoverURL = card.findViewById(R.id.book_cover);

                bookID.setText("ID: " + id);
                bookTitle.setText(title);
                bookAuthor.setText("by " + author);
                bookCategory.setText(category);
                bookAvailability.setText(availability);
                bookPublicationYear.setText(publication_year);
                bookISBN.setText(isbn);
                //bookCoverURL.setText(cover_url);
                newArrivalsContainer.addView(card);


                int bookId = Integer.parseInt(id);
                int acc_id = Integer.parseInt(sid);

                CheckBox addFavorite = card.findViewById(R.id.starToggle);
                Cursor c2 = db.getFavorites(bookId, acc_id);
                if(c2.getCount() > 0){
                    addFavorite.setChecked(true);
                }else{
                    addFavorite.setChecked(false);
                }

                addFavorite.setOnCheckedChangeListener((btn, nowChecked) -> {
                    if (nowChecked) {
                        db.addFavorite(bookId, acc_id);

                    } else {
                        db.removeFavorite(bookId, acc_id);

                    }
                });

                TextView reserveButton = card.findViewById(R.id.reserveButton);

                reserveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(db.isReserved(acc_id, bookId)){
                            Toast.makeText(requireContext(), "You have already reserved this book", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View dialogView = inflater.inflate(R.layout.reservationsfromdashboard, null);

                            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                    .setView(dialogView)
                                    .create();

                            dialog.show();
                            Button cancel = dialogView.findViewById(R.id.btnCancel);
                            Button confirm = dialogView.findViewById(R.id.btnConfirm);
                            cancel.setOnClickListener(btn -> {
                                dialog.dismiss();
                            });
                            confirm.setOnClickListener(btn -> {
                                Slider slider = dialogView.findViewById(R.id.sliderWeeks);
                                RadioGroup rg = dialogView.findViewById(R.id.rgMethod);
                                TextInputEditText etNotes = dialogView.findViewById(R.id.etNotes);
                                String notes = etNotes.getText().toString();
                                int weeks = (int) slider.getValue();
                                String method = (rg.getCheckedRadioButtonId() == R.id.rbDigital) ? "digital" : "pickup";
                                db.reserveBook(bookId, acc_id, weeks, method, notes);
                                Toast.makeText(requireContext(), "Book Reserved", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });
                        }

                    }
                });

                ImageView shareButton = card.findViewById(R.id.shareButton);
                shareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String shareText = "Check out this book:\n"
                                + title + " by " + author
                                + "\nISBN: " + isbn
                                + "\nCategory: " + category
                                + "\nPublished: " + publication_year;

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Library Book Recommendation");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

                        startActivity(Intent.createChooser(shareIntent, "Share book via"));
                    }
                });
            }
        }
        return root;
    }
}