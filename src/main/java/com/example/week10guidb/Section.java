
package com.example.week10guidb;

public class Section {
    private int ID;
    private int courseID;
    private int facultyID;
    private String semester;
    private String day;
    private String startTime;
    private String endTime;
    private String modality;

    public Section(int ID, int courseID, int facultyID,
                   String semester, String day,
                   String startTime, String endTime,
                   String modality) {
        this.ID = ID;
        this.courseID = courseID;
        this.facultyID = facultyID;
        this.semester = semester;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modality = modality;
    }

    public int getID() {
        return ID;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getFacultyID() {
        return facultyID;
    }

    public String getSemester() {
        return semester;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getModality() {
        return modality;
    }

    @Override
    public String toString() {
        return "Section{" +
                "ID=" + ID +
                ", courseID=" + courseID +
                ", facultyID=" + facultyID +
                ", semester='" + semester + '\'' +
                ", day='" + day + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", modality='" + modality + '\'' +
                '}';
    }
}
