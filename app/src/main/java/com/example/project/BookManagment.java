package com.example.project;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookManagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookManagment extends Fragment {
    DataBaseHelper db;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookManagment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookManagment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookManagment newInstance(String param1, String param2) {
        BookManagment fragment = new BookManagment();
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
        View root = inflater.inflate(R.layout.fragment_book_managment, container, false);
        db = new DataBaseHelper(requireContext(), "test11", null, 4);
        LinearLayout BookContainer = root.findViewById(R.id.BooksManagementV);

        Cursor cursor = db.getAllBooks();
        if (cursor != null){
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                String isbn = cursor.getString(3);
                String category = cursor.getString(4);
                String availability = cursor.getString(5);
                //String cover_url = cursor.getString(6);
                String publication_year = cursor.getString(7);

                View card = inflater.inflate(R.layout.book_card_management, BookContainer, false);

                TextView bookID = card.findViewById(R.id.book_id);
                TextView bookTitle = card.findViewById(R.id.book_title);
                TextView bookAuthor = card.findViewById(R.id.book_author);
                TextView bookCategory = card.findViewById(R.id.category);
                TextView bookAvailability = card.findViewById(R.id.availability_badge);
                TextView bookPublicationYear = card.findViewById(R.id.publication_year);
                TextView bookISBN = card.findViewById(R.id.isbn);
               // TextView bookCoverURL = card.findViewById(R.id.bookCoverURL);

                bookID.setText("ID: " + id);
                bookTitle.setText(title);
                bookAuthor.setText("by " + author);
                bookCategory.setText(category);
                bookAvailability.setText(availability);
                bookPublicationYear.setText(publication_year);
                bookISBN.setText(isbn);
                //bookCoverURL.setText(cover_url);
                BookContainer.addView(card);

                Button EditBtn = card.findViewById(R.id.btn_edit);
                Button deleteBtn = card.findViewById(R.id.btn_delete);

                EditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor1 = db.BookInfo(id);
                        if (cursor1 != null && cursor1.moveToFirst()) {
                            String id1 = cursor1.getString(0);
                            String title1 = cursor1.getString(1);
                            String author1 = cursor1.getString(2);
                            String isbn1 = cursor1.getString(3);
                            String category1 = cursor1.getString(4);
                            int availability1 = cursor1.getInt(5);
                            int publicationYear1 = cursor1.getInt(7);

                            LayoutInflater inflater = LayoutInflater.from(requireContext());
                            View dialogView = inflater.inflate(R.layout.edit_book, null);

                            TextInputEditText titleET = dialogView.findViewById(R.id.book_title);
                            TextInputEditText authorET = dialogView.findViewById(R.id.book_author);
                            TextInputEditText isbnET = dialogView.findViewById(R.id.isbn);
                            TextInputEditText categoryET = dialogView.findViewById(R.id.category);
                            TextInputEditText availabilityET = dialogView.findViewById(R.id.status);
                            TextInputEditText publicationYearET = dialogView.findViewById(R.id.publication_year);

                            // Pre-fill fields in dialog
                            titleET.setText(title1);
                            authorET.setText(author1);
                            isbnET.setText(isbn1);
                            categoryET.setText(category1);
                            availabilityET.setText(availability1 == 1 ? "Available" : "Unavailable");
                            publicationYearET.setText(String.valueOf(publicationYear1));

                            Button saveBtn = dialogView.findViewById(R.id.btn_edit2);

                            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                    .setView(dialogView)
                                    .setPositiveButton("Close", null)
                                    .create();

                            saveBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String newTitle = titleET.getText().toString().trim();
                                    String newAuthor = authorET.getText().toString().trim();
                                    String newIsbn = isbnET.getText().toString().trim();
                                    String newCategory = categoryET.getText().toString().trim();
                                    String availabilityText = availabilityET.getText().toString().trim();
                                    String newYear = publicationYearET.getText().toString().trim();

                                    int newAvailability = availabilityText.equalsIgnoreCase("Available") ? 1 : 0;

                                    db.updateBook(id1, newTitle, newAuthor, newIsbn, newCategory, newAvailability, newYear);

                                    TextView bookTitleCard = requireView().findViewById(R.id.book_title);
                                    TextView bookAuthorCard = requireView().findViewById(R.id.book_author);
                                    TextView isbnCard = requireView().findViewById(R.id.isbn);
                                    TextView categoryCard = requireView().findViewById(R.id.category);
                                    TextView statusCard = requireView().findViewById(R.id.status);
                                    TextView yearCard = requireView().findViewById(R.id.publication_year);

                                    bookTitleCard.setText(newTitle);
                                    bookAuthorCard.setText(newAuthor);
                                    isbnCard.setText(newIsbn);
                                    categoryCard.setText(newCategory);
                                    statusCard.setText(newAvailability == 1 ? "Available" : "Unavailable");
                                    yearCard.setText(newYear);

                                    Toast.makeText(requireContext(), "Book updated successfully", Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.removeBook(id);
                        BookContainer.removeView(card);
                    }
                });
            }
        }
        cursor.close();

        Button addBookBtn = root.findViewById(R.id.AddBtn);

        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                View dialogView = inflater.inflate(R.layout.add_book_form, null);

                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setView(dialogView)
                        .setPositiveButton("Close", null) // just closes the dialog
                        .create();

                TextInputEditText title = dialogView.findViewById(R.id.Title);
                TextInputEditText author = dialogView.findViewById(R.id.Author);
                TextInputEditText isbn = dialogView.findViewById(R.id.Isbn);
                AutoCompleteTextView category = dialogView.findViewById(R.id.Category);
                SwitchMaterial availabilitySwitch = dialogView.findViewById(R.id.switchAvailability);
                TextInputEditText coverURL = dialogView.findViewById(R.id.CoverUrl);
                TextInputEditText publicationYear = dialogView.findViewById(R.id.PublicationYear);
                Button addBtn = dialogView.findViewById(R.id.btnAddBook);
                String[] categories = new String[] {
                        "Science", "Technology", "Engineering", "Mathematics", "Fiction", "History"
                };
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        categories
                );
                category.setAdapter(adapter);
                category.setText("Engineering", false);


                addBtn.setOnClickListener(view -> {
                    int availabilityInt = availabilitySwitch.isChecked() ? 1 : 0;

                    String pubYearText = publicationYear.getText().toString().trim();
                    int publicationYearInt = 0;

                    if (!pubYearText.isEmpty()) {
                        try {
                            publicationYearInt = Integer.parseInt(pubYearText);
                        } catch (NumberFormatException e) {
                            publicationYear.setError("Invalid number");
                            return;
                        }
                    } else {
                        publicationYear.setError("Publication year is required");
                        return;
                    }

                    Books book = new Books();
                    book.setTitle(title.getText().toString());
                    book.setAuthor(author.getText().toString());
                    book.setIsbn(isbn.getText().toString());
                    book.setCategory(category.getText().toString());
                    book.setAvailability(availabilityInt);
                    book.setPublication_year(publicationYearInt);
                    book.setCover_url(coverURL.getText().toString());

                    db.addBook(book);

                    Toast.makeText(requireContext(), "Book added successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
                dialog.show();
            }
        });



        return root;
    }
}