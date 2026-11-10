package com.example.week10guidb;

public class Course {
    private int ID;
    private String department;
    private int number;
    private String title;
    private int credits;

    public Course(int ID, String department, int number,
                  String title, int credits) {
        this.ID = ID;
        this.department = department;
        this.number = number;
        this.title = title;
        this.credits = credits;
    }

    public int getID() {
        return ID;
    }

    public String getDepartment() {
        return department;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return "Course{" +
                "ID=" + ID +
                ", department='" + department + '\'' +
                ", number=" + number +
                ", title='" + title + '\'' +
                ", credits=" + credits +
                '}';
    }
}
