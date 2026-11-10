
package com.example.week10guidb;

public class Enrollment {
    private int ID;
    private int sectionID;
    private int studentID;
    private String grade;

    public Enrollment(int ID, int sectionID,
                      int studentID, String grade) {
        this.ID = ID;
        this.sectionID = sectionID;
        this.studentID = studentID;
        this.grade = grade;
    }

    public int getID() {
        return ID;
    }

    public int getSectionID() {
        return sectionID;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "ID=" + ID +
                ", sectionID=" + sectionID +
                ", studentID=" + studentID +
                ", grade='" + grade + '\'' +
                '}';
    }
}
