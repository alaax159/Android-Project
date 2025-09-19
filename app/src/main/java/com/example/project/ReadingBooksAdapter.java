package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ReadingBooksAdapter extends RecyclerView.Adapter<ReadingBooksAdapter.VH> {

    private final List<Books> items = new ArrayList<>();

    // ViewHolder for book item
    static class VH extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView bookTitle, bookAuthor, bookAvailability;
        Button btnBorrow, btnInfo, btnRemoveFav;
        ImageView bookCoverImage;

        public VH(@NonNull View itemView) {
            super(itemView);
            card = (MaterialCardView) itemView;

            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookAvailability = itemView.findViewById(R.id.bookAvailability);
            btnBorrow = itemView.findViewById(R.id.btnBorrow);
            btnInfo = itemView.findViewById(R.id.btnInfo);
            btnRemoveFav = itemView.findViewById(R.id.btnRemoveFav);
            //bookCoverImage = itemView.findViewById(R.id.bookCoverImage);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_card, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Books book = items.get(position);
        DataBaseHelper db = new DataBaseHelper(h.itemView.getContext(), "test11", null, 4);

        h.bookTitle.setText(book.getTitle());
        h.bookAuthor.setText(book.getAuthor());
        h.bookAvailability.setText(book.getCategory());

//        if (book.getCoverResId() != 0) {
//            h.bookCoverImage.setImageResource(book.getCoverResId());
//        } else {
//            h.bookCoverImage.setImageResource(R.drawable.ic_book_placeholder); // fallback image
//        }

        h.btnRemoveFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeBookFromReadingList("2",2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Books> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
}
