package com.example.project;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageStudents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageStudents extends Fragment {
    DataBaseHelper db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageStudents() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageStudents.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageStudents newInstance(String param1, String param2) {
        ManageStudents fragment = new ManageStudents();
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
        View root = inflater.inflate(R.layout.fragment_manage_students, container, false);

        db = new DataBaseHelper(requireContext(), "alaaDB", null, 4);

        LinearLayout studentContainer = root.findViewById(R.id.studentContainer);

        Cursor cursor = db.getAllStudents();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String firstName  = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName   = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String department = cursor.getString(cursor.getColumnIndexOrThrow("department"));
                String level      = cursor.getString(cursor.getColumnIndexOrThrow("level"));
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                View card = inflater.inflate(R.layout.student_card, studentContainer, false);

                TextView StudentName   = card.findViewById(R.id.StudentName);
                TextView tvDepartment  = card.findViewById(R.id.Department);
                TextView tvLevel       = card.findViewById(R.id.Level);

                StudentName.setText(firstName + " " + lastName);
                tvDepartment.setText(department);
                tvLevel.setText(level);



                MaterialButton btnInfoStudent   = card.findViewById(R.id.btnInfoStudent);
                MaterialButton btnRemoveStudent = card.findViewById(R.id.btnRemoveStudent);

                btnInfoStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor1 = db.StudentDataID(String.valueOf(studentId));
                        if (cursor1 != null) {
                            if (cursor1.moveToFirst()) {
                                String StudentUniversityId = cursor1.getString(cursor1.getColumnIndexOrThrow("university_id"));
                                String StudentFirstName = cursor1.getString(cursor1.getColumnIndexOrThrow("first_name"));
                                String StudentLastName = cursor1.getString(cursor1.getColumnIndexOrThrow("last_name"));
                                String StudentEmail = cursor1.getString(cursor1.getColumnIndexOrThrow("email"));
                                String StudentDepartment = cursor1.getString(cursor1.getColumnIndexOrThrow("department"));
                                String StudentLevel = cursor1.getString(cursor1.getColumnIndexOrThrow("level"));

                                LayoutInflater inflater2 = LayoutInflater.from(requireContext());
                                View dialogView = inflater2.inflate(R.layout.student_info, null);

                                TextView StudentFullName = dialogView.findViewById(R.id.student_name);
                                TextView StudentUniversityID = dialogView.findViewById(R.id.university_id);
                                TextView StudentEmail2 = dialogView.findViewById(R.id.email);
                                TextView StudentDepartment2 = dialogView.findViewById(R.id.department);
                                TextView StudentLevel2 = dialogView.findViewById(R.id.level);

                                StudentFullName.setText(StudentFirstName + " " + StudentLastName);
                                StudentUniversityID.setText(StudentUniversityId);
                                StudentEmail2.setText(StudentEmail);
                                StudentDepartment2.setText(StudentDepartment);
                                StudentLevel2.setText(StudentLevel);

                                new AlertDialog.Builder(requireContext())
                                        .setView(dialogView)
                                        .setPositiveButton("Close", null)
                                        .show();
                            }
                        }
                    }
                });

                btnRemoveStudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            db.removeStudent(String.valueOf(studentId));
                            studentContainer.removeView(card);
                            Toast.makeText(requireContext(), "Student removed successfully", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(requireContext(), "Error removing student", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                studentContainer.addView(card);
            }
            cursor.close();
        }

        return root;
    }
}