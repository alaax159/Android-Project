package com.example.project;

public class Student {
    private int id;
    private String universityId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String department;
    private String level;

    public Student() {
    }

    public Student(int id, String level, String department, String passwordHash, String email, String lastName, String firstName, String universityId) {
        this.id = id;
        this.level = level;
        this.department = department;
        this.passwordHash = passwordHash;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.universityId = universityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", universityId='" + universityId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", department='" + department + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
