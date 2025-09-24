package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IntroductionActivity extends AppCompatActivity {

    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_introduction);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new DataBaseHelper(IntroductionActivity.this, "alaaDB", null, 4);
        Button btnGetStarted = findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(v -> {
            String url = "https://68d11b08e6c0cbeb39a38ce2.mockapi.io/Books/categories/Books";
            new FetchAndInsertBooksTask(IntroductionActivity.this, db, btnGetStarted).execute(url);
        });

//        db.insertDummyBooks();
//        db.insertDummyStudents();
//        db.insertDummyReservations();
//        db.insertDummyReadingList();
//        db.insertNewBook();


    }
}