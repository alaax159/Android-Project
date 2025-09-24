package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.activity.OnBackPressedCallback;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements MyBorrowedBooks.CommunicatorBorrowedBooks {
    SharedPreManager sharedPref;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    DataBaseHelper db;

    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = SharedPreManager.getInstance(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new dashboardFragment())
                .commit();
        toolbar.setTitle("Library Dashboard");
        Toast.makeText(MainActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
        db = new DataBaseHelper(MainActivity.this, "alaaDB", null, 4);

        student = new Student();
        String id = sharedPref.readString("id", "");
        Cursor cursor = db.StudentDataID(id);
        if (cursor.moveToFirst()) {
            student.setId(cursor.getInt(0));
            student.setUniversityId(cursor.getString(1));
            student.setFirstName(cursor.getString(2));
            student.setLastName(cursor.getString(3));
            student.setEmail(cursor.getString(4));
            student.setPasswordHash(cursor.getString(5));
            student.setDepartment(cursor.getString(7));
            student.setLevel(cursor.getString(8));
        }
        cursor.close();

        if (student != null) {

            View headerView = navigationView.getHeaderView(0);

            TextView tvStudentName = headerView.findViewById(R.id.tvStudentName);
            TextView tvDepartment = headerView.findViewById(R.id.tvDepartment);
            TextView tvLevel = headerView.findViewById(R.id.tvLevel);
            TextView tvUniversityId = headerView.findViewById(R.id.tvUniversityId);

            tvStudentName.setText(student.getFirstName() + " " + student.getLastName());
            tvDepartment.setText("Department: " + student.getDepartment());
            tvLevel.setText("Level: " + student.getLevel());
            tvUniversityId.setText("ID: " + student.getUniversityId());
        }

        sharedPref.writeString("student_id", String.valueOf(student.getId()));


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_dashboard) {
                    dashboardFragment fragment = new dashboardFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                    toolbar.setTitle("Library Dashboard");
                    Toast.makeText(MainActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_borrowed_books) {
                    Toast.makeText(MainActivity.this, "Borrowed Books", Toast.LENGTH_SHORT).show();
                    MyBorrowedBooks fragment = new MyBorrowedBooks();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                    toolbar.setTitle("Library borrowed books");
                } else if (id == R.id.nav_reading_list) {
                    Toast.makeText(MainActivity.this, "Reading List", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ReadingListSection())
                            .commit();
                    toolbar.setTitle("Library reading list");
                } else if (id == R.id.nav_new_arrivals) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new NewArrivalsBooks())
                            .commit();
                    Toast.makeText(MainActivity.this, "New Arrivals", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ProfileManagement())
                            .commit();
                    toolbar.setTitle("profile management");
                    Toast.makeText(MainActivity.this, "Profile Management", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_info) {
                    Toast.makeText(MainActivity.this, "Library Info", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ContactUs())
                            .commit();
                    toolbar.setTitle("Library info");
                } else if (id == R.id.nav_logout) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
                    MainActivity.super.onBackPressed();
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

    @Override
    public void setDataBorrowedBooks(int data) {

    }
//    @Override
//    public String setDataBorrowedBooks(String data) {
//       return data;
//    }
}
