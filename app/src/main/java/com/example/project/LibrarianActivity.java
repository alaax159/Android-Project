package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class LibrarianActivity extends AppCompatActivity {
    SharedPreManager sharedPref;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    DataBaseHelper db;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);
        sharedPref = SharedPreManager.getInstance(this);
        drawerLayout = findViewById(R.id.drawer_layout_lib);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Toast.makeText(LibrarianActivity.this, "nav_Books", Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new BookManagment())
                .commit();
        toolbar.setTitle("Books Management");


        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_dashboard);
        db = new DataBaseHelper(LibrarianActivity.this, "alaaDB", null, 4);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_Manage_Student) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ManageStudents())
                            .commit();
                    toolbar.setTitle("Student Management");

                    Toast.makeText(LibrarianActivity.this, "nav_Manage_Student", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_Books) {
                    Toast.makeText(LibrarianActivity.this, "nav_Books", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new BookManagment())
                            .commit();
                    toolbar.setTitle("Books Management");
                } else if (id == R.id.nav_Reservation) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ReservationManagement())
                            .commit();
                    Toast.makeText(LibrarianActivity.this, "Reservation", Toast.LENGTH_SHORT).show();
                    toolbar.setTitle("Reservation Management");
                } else if (id == R.id.nav_Reports) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new GenerateReport())
                            .commit();
                    toolbar.setTitle("Library Report");
                    Toast.makeText(LibrarianActivity.this, "nav_Reports", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_Library_Settings) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new EditLibrarySettings())
                            .commit();
                    Toast.makeText(LibrarianActivity.this, "Library_Settings", Toast.LENGTH_SHORT).show();
                    toolbar.setTitle("Library Settings");
                } else if (id == R.id.nav_logout) {

                    Intent intent = new Intent(LibrarianActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // allow default behavior (exit app or go back)
                    setEnabled(false);
                    LibrarianActivity.super.onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (toggle != null) {
            toggle.syncState();
        }
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

}