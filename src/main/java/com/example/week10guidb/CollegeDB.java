package com.example.week10guidb;

import java.sql.*;
import java.util.ArrayList;

public class CollegeDB {
    private static Connection getConnection() throws SQLException {
        String dbUrl = "jdbc:sqlite:college.db";
        Connection connection = DriverManager.getConnection(dbUrl);
        return connection;
    }

    public static void updateStudent(Student student){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update student set FirstName = ?, LastName = ?, Major = ?, GPA = ? where ID = ?");

            // first value is index 1, it's ok to cry
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getMajor());
            preparedStatement.setDouble(4, student.getGpa());
            preparedStatement.setInt(5, student.getID());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static ArrayList<Student> getStudent(int id){
        ArrayList<Student> students = new ArrayList<Student>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM Student where id = ?");

            preparedStatement.setInt(1, id);

            ResultSet studentsQuery = preparedStatement.executeQuery();

            while (studentsQuery.next()) {
                String firstName = studentsQuery.getString("FirstName");
                String lastName = studentsQuery.getString("LastName");
                String major = studentsQuery.getString("Major");
                double gpa = studentsQuery.getDouble("GPA");

                students.add(new Student(id, firstName, lastName, major, gpa));
            }
            studentsQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return students;
    }

    public static boolean deleteStudent(Student student){
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Student where id = ?");

            preparedStatement.setInt(1, student.getID());

            preparedStatement.execute();


            preparedStatement.close();

            connection.close();

            return true;
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<Student> getStudents(){
        ArrayList<Student> students = new ArrayList<Student>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet studentsQuery = statement.executeQuery(
                    "SELECT * FROM Student");

            while (studentsQuery.next()) {
                int id = studentsQuery.getInt("ID");
                String firstName = studentsQuery.getString("FirstName");
                String lastName = studentsQuery.getString("LastName");
                String major = studentsQuery.getString("Major");
                double gpa = studentsQuery.getDouble("GPA");

                students.add(new Student(id, firstName, lastName, major, gpa));
            }
            studentsQuery.close();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }

        return students;
    }

    public static void addStudent(Student student){
        try {
            Connection connection = getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into Student ( FirstName, LastName, Major, GPA ) values ( ?, ?, ?, ? )");

            // first value is index 1, it's ok to cry
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getMajor());
            preparedStatement.setDouble(4, student.getGpa());

            preparedStatement.execute();

            connection.close();
        } catch ( SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
    }


    public static void showTables() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(
                     "SELECT name, sql FROM sqlite_master WHERE type = 'table'")) {

            while (results.next()) {
                System.out.println(results.getString("name"));
                System.out.println(results.getString("sql"));
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // CREATE
        Enrollment enrollment = new Enrollment(0, 1, 1, "B");
        addEnrollment(enrollment);

        // READ
        System.out.println("All Enrollments:");
        for (Enrollment e : getEnrollments()) {
            System.out.println(e);
        }

        // Find the newly added enrollment
        ArrayList<Enrollment> enrollments = getEnrollments();

        if (!enrollments.isEmpty()) {
            Enrollment last = enrollments.get(enrollments.size() - 1);

            // UPDATE
            Enrollment updated = new Enrollment(
                    last.getID(),
                    last.getSectionID(),
                    last.getStudentID(),
                    "A"
            );

            updateEnrollment(updated);

            System.out.println("Updated Enrollments:");
            for (Enrollment e : getEnrollments()) {
                System.out.println(e);
            }

            // DELETE
            deleteEnrollment(last.getID());
        }

        // READ AFTER DELETE
        System.out.println("Remaining Enrollments:");
        for (Enrollment e : getEnrollments()) {
            System.out.println(e);
        }
    }

    public static ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<Course>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            ResultSet courseQuery = statement.executeQuery(
                    "SELECT * FROM Course");

            while (courseQuery.next()) {
                int id = courseQuery.getInt("ID");
                String department = courseQuery.getString("Department");
                int number = courseQuery.getInt("Number");
                String title = courseQuery.getString("Title");
                int credits = courseQuery.getInt("Credits");

                courses.add(new Course(id, department, number,
                        title, credits));
            }

            courseQuery.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        return courses;
    }


    public static void addCourse(Course course) {
        String sql = "INSERT INTO Course " +
                "(Department, Number, Title, Credits) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, course.getDepartment());
            statement.setInt(2, course.getNumber());
            statement.setString(3, course.getTitle());
            statement.setInt(4, course.getCredits());

            statement.executeUpdate();

            System.out.println("Course added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    public static void updateCourse(Course course) {
        String sql = "UPDATE Course SET Department = ?, " +
                "Number = ?, Title = ?, Credits = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, course.getDepartment());
            statement.setInt(2, course.getNumber());
            statement.setString(3, course.getTitle());
            statement.setInt(4, course.getCredits());
            statement.setInt(5, course.getID());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Course updated successfully!");
            } else {
                System.out.println("Course not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating course: " + e.getMessage());
        }
    }

    public static ArrayList<Faculty> getFaculty() {
        ArrayList<Faculty> facultyList = new ArrayList<>();

        String sql = "SELECT * FROM Faculty";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                int id = results.getInt("ID");
                String firstName = results.getString("FirstName");
                String lastName = results.getString("LastName");
                String department = results.getString("Department");

                facultyList.add(new Faculty(
                        id, firstName, lastName, department));
            }

        } catch (SQLException e) {
            System.out.println("Error reading faculty: " + e.getMessage());
        }

        return facultyList;
    }

    public static void addFaculty(Faculty faculty) {
        String sql = "INSERT INTO Faculty " +
                "(FirstName, LastName, Department) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, faculty.getFirstName());
            statement.setString(2, faculty.getLastName());
            statement.setString(3, faculty.getDepartment());

            statement.executeUpdate();
            System.out.println("Faculty added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding faculty: " + e.getMessage());
        }
    }

    public static void updateFaculty(Faculty faculty) {
        String sql = "UPDATE Faculty SET FirstName = ?, " +
                "LastName = ?, Department = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, faculty.getFirstName());
            statement.setString(2, faculty.getLastName());
            statement.setString(3, faculty.getDepartment());
            statement.setInt(4, faculty.getID());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Faculty updated successfully!");
            } else {
                System.out.println("Faculty not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating faculty: " + e.getMessage());
        }
    }

    public static ArrayList<Section> getSections() {
        ArrayList<Section> sections = new ArrayList<>();

        String sql = "SELECT * FROM Section";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                sections.add(new Section(
                        results.getInt("ID"),
                        results.getInt("CourseID"),
                        results.getInt("FacultyID"),
                        results.getString("Semester"),
                        results.getString("Day"),
                        results.getString("StartTime"),
                        results.getString("EndTime"),
                        results.getString("Modality")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error reading sections: " + e.getMessage());
        }

        return sections;
    }

    public static void addSection(Section section) {
        String sql = "INSERT INTO Section " +
                "(CourseID, FacultyID, Semester, Day, " +
                "StartTime, EndTime, Modality) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, section.getCourseID());
            statement.setInt(2, section.getFacultyID());
            statement.setString(3, section.getSemester());
            statement.setString(4, section.getDay());
            statement.setString(5, section.getStartTime());
            statement.setString(6, section.getEndTime());
            statement.setString(7, section.getModality());

            statement.executeUpdate();
            System.out.println("Section added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding section: " + e.getMessage());
        }
    }

    public static void updateSection(Section section) {
        String sql = "UPDATE Section SET CourseID = ?, " +
                "FacultyID = ?, Semester = ?, Day = ?, " +
                "StartTime = ?, EndTime = ?, Modality = ? " +
                "WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, section.getCourseID());
            statement.setInt(2, section.getFacultyID());
            statement.setString(3, section.getSemester());
            statement.setString(4, section.getDay());
            statement.setString(5, section.getStartTime());
            statement.setString(6, section.getEndTime());
            statement.setString(7, section.getModality());
            statement.setInt(8, section.getID());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Section updated successfully!");
            } else {
                System.out.println("Section not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating section: " + e.getMessage());
        }
    }

    public static ArrayList<Enrollment> getEnrollments() {
        ArrayList<Enrollment> enrollments = new ArrayList<>();

        String sql = "SELECT * FROM Enrollment";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                enrollments.add(new Enrollment(
                        results.getInt("ID"),
                        results.getInt("SectionID"),
                        results.getInt("StudentID"),
                        results.getString("Grade")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error reading enrollments: " + e.getMessage());
        }

        return enrollments;
    }

    public static void addEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO Enrollment " +
                "(SectionID, StudentID, Grade) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, enrollment.getSectionID());
            statement.setInt(2, enrollment.getStudentID());
            statement.setString(3, enrollment.getGrade());

            statement.executeUpdate();
            System.out.println("Enrollment added successfully!");

        } catch (SQLException e) {
            System.out.println("Error adding enrollment: " + e.getMessage());
        }
    }

    public static void updateEnrollment(Enrollment enrollment) {
        String sql = "UPDATE Enrollment SET SectionID = ?, " +
                "StudentID = ?, Grade = ? WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, enrollment.getSectionID());
            statement.setInt(2, enrollment.getStudentID());
            statement.setString(3, enrollment.getGrade());
            statement.setInt(4, enrollment.getID());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Enrollment updated successfully!");
            } else {
                System.out.println("Enrollment not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating enrollment: " + e.getMessage());
        }
    }

    public static void deleteEnrollment(int id) {
        String sql = "DELETE FROM Enrollment WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Enrollment deleted successfully!");
            } else {
                System.out.println("Enrollment not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting enrollment: " + e.getMessage());
        }
    }






}
