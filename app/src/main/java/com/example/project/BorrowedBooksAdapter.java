package com.example.project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.VH> {
    private final List<BorrowedBook> items = new ArrayList<>();

    static class VH extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView tvBookTitle, tvAuthor, tvBorrowDate, tvDueDate, tvReturnDate, tvStatus, tvFine;

        VH(@NonNull View v) {
            super(v);
            card         = (MaterialCardView) v;
            tvBookTitle  = v.findViewById(R.id.tvBookTitle);
            tvAuthor     = v.findViewById(R.id.tvAuthor);
            tvBorrowDate = v.findViewById(R.id.tvBorrowDate);
            tvDueDate    = v.findViewById(R.id.tvDueDate);
            tvReturnDate = v.findViewById(R.id.tvReturnDate);
            tvStatus     = v.findViewById(R.id.tvStatus);
            tvFine       = v.findViewById(R.id.tvFine);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_borrowed_book, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        BorrowedBook m = items.get(position);

        String emoji;
        if ("Returned".equalsIgnoreCase(m.status)) {
            emoji = "📗 ";
        } else if ("Overdue".equalsIgnoreCase(m.status)) {
            emoji = "📕 ";
        } else if ("Extended".equalsIgnoreCase(m.status)) {
            emoji = "📙 ";
        } else if ("Pending".equalsIgnoreCase(m.status)){
            emoji = "⏳ ";
        } else {
            emoji = "📘 ";
        }


        h.tvBookTitle.setText(emoji + (m.title == null ? "Untitled" : m.title));
        h.tvAuthor.setText(m.author == null ? "Unknown Author" : m.author);
        h.tvBorrowDate.setText("Borrow Date: " + (m.reservationDate == null ? "—" : m.reservationDate));
        h.tvDueDate.setText("Due Date: " + (m.dueDate == null ? "—" : m.dueDate));
        h.tvReturnDate.setText("Return Date: " + (m.returnDate == null ? "—" : m.returnDate));
        h.tvStatus.setText("Status: " + (m.status == null ? "—" : m.status));
        h.tvFine.setText("Fine: " + (m.fine == null ? "0.00" : m.fine));

        if ("Returned".equalsIgnoreCase(m.status)) {
            h.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // green
        } else if ("Overdue".equalsIgnoreCase(m.status)) {
            h.tvStatus.setTextColor(Color.parseColor("#C62828")); // red
        } else if ("Extended".equalsIgnoreCase(m.status)) {
            h.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // orange
        } else {
            h.tvStatus.setTextColor(Color.parseColor("#1565C0")); // blue
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<BorrowedBook> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
}
