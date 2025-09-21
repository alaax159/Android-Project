package com.example.project;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboardFragment extends Fragment {
    DataBaseHelper db;
    SharedPreManager sharedpref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public dashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboardFragment newInstance(String param1, String param2) {
        dashboardFragment fragment = new dashboardFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        LinearLayout containerBooks = root.findViewById(R.id.scrollViewLayout);

        db = new DataBaseHelper(requireContext(), "AdnanDB", null, 4);
        sharedpref = SharedPreManager.getInstance(requireContext());
        int acc_id = Integer.parseInt(sharedpref.readString("id", ""));
        try (Cursor c = db.getAllBooks()) {
            if (c != null && c.moveToFirst()) {
                int tvId     = c.getColumnIndexOrThrow("id");
                int dbTitle  = c.getColumnIndexOrThrow("title");
                int dbAuthor = c.getColumnIndexOrThrow("author");
                int dbIsbn   = c.getColumnIndexOrThrow("isbn");
                int dbCat    = c.getColumnIndexOrThrow("category");
                int dbAvail  = c.getColumnIndexOrThrow("availability");
                int dbCover  = c.getColumnIndexOrThrow("cover_url");
                int dbYear   = c.getColumnIndexOrThrow("publication_year");

                do {
                    String title = c.getString(dbTitle);
                    String author = c.getString(dbAuthor);
                    String isbn = c.getString(dbIsbn);
                    String category = c.getString(dbCat);
                    int availability = c.getInt(dbAvail);
                    String availabilityStatus = (availability == 1) ? "Available" : "Not Available";
                    String coverUrl = c.getString(dbCover);
                    int publicationYear = c.getInt(dbYear);
                    int bookId = c.getInt(tvId);

                    View card = inflater.inflate(R.layout.fragment_book_catalog, containerBooks, false);

                    TextView tvBookId   = card.findViewById(R.id.tv_book_id);
                    TextView tvTitle    = card.findViewById(R.id.tv_book_title);
                    TextView tvAuthor   = card.findViewById(R.id.tv_book_author);
                    TextView tvStatus   = card.findViewById(R.id.tv_availability_status);
                    TextView tvIsbn     = card.findViewById(R.id.tv_isbn);
                    TextView tvYear     = card.findViewById(R.id.tv_publication_year);
                    TextView tvCategory = card.findViewById(R.id.tv_category);
                    TextView tvBadge    = card.findViewById(R.id.tv_availability_badge);
                    ImageView ivCover   = card.findViewById(R.id.iv_book_cover);

                    tvBookId.setText("ID: " + bookId + "");
                    tvTitle.setText(title);
                    tvAuthor.setText(author);
                    tvStatus.setText(availabilityStatus);
                    tvIsbn.setText(isbn);
                    tvYear.setText(String.valueOf(publicationYear));
                    tvCategory.setText(category);
                    tvBadge.setText(availabilityStatus);


                    containerBooks.addView(card);
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
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

}