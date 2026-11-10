
package com.example.week10guidb;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class HelloController {

    @FXML private Label welcomeText;

    // STUDENTS
    @FXML private ListView<Student> studentListView;
    @FXML private TextField studentFilterTextField;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField majorTextField;
    @FXML private TextField gpaTextField;
    @FXML private Button selectStudentButton;
    @FXML private Button deleteStudentButton;
    @FXML private Button addStudentButton;

    private ArrayList<Student> students = new ArrayList<>();

    // COURSES
    @FXML private ListView<Course> courseListView;
    @FXML private TextField courseDepartmentField;
    @FXML private TextField courseNumberField;
    @FXML private TextField courseTitleField;
    @FXML private TextField courseCreditsField;

    // FACULTY
    @FXML private ListView<Faculty> facultyListView;
    @FXML private TextField facultyFirstNameField;
    @FXML private TextField facultyLastNameField;
    @FXML private TextField facultyDepartmentField;

    // SECTIONS
    @FXML private ListView<Section> sectionListView;
    @FXML private TextField sectionCourseIDField;
    @FXML private TextField sectionFacultyIDField;
    @FXML private TextField sectionSemesterField;
    @FXML private TextField sectionDayField;
    @FXML private TextField sectionStartTimeField;
    @FXML private TextField sectionEndTimeField;
    @FXML private TextField sectionModalityField;

    // ENROLLMENTS
    @FXML private ListView<Enrollment> enrollmentListView;
    @FXML private TextField enrollmentSectionIDField;
    @FXML private TextField enrollmentStudentIDField;
    @FXML private TextField enrollmentGradeField;

    public void initialize() {
        deleteStudentButton.setDisable(true);

        studentListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) ->
                        deleteStudentButton.setDisable(selected == null));

        courseListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) -> {
                    if (selected != null) {
                        courseDepartmentField.setText(selected.getDepartment());
                        courseNumberField.setText(
                                String.valueOf(selected.getNumber()));
                        courseTitleField.setText(selected.getTitle());
                        courseCreditsField.setText(
                                String.valueOf(selected.getCredits()));
                    }
                });

        facultyListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) -> {
                    if (selected != null) {
                        facultyFirstNameField.setText(selected.getFirstName());
                        facultyLastNameField.setText(selected.getLastName());
                        facultyDepartmentField.setText(selected.getDepartment());
                    }
                });

        sectionListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) -> {
                    if (selected != null) {
                        sectionCourseIDField.setText(
                                String.valueOf(selected.getCourseID()));
                        sectionFacultyIDField.setText(
                                String.valueOf(selected.getFacultyID()));
                        sectionSemesterField.setText(selected.getSemester());
                        sectionDayField.setText(selected.getDay());
                        sectionStartTimeField.setText(selected.getStartTime());
                        sectionEndTimeField.setText(selected.getEndTime());
                        sectionModalityField.setText(selected.getModality());
                    }
                });

        enrollmentListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, selected) -> {
                    if (selected != null) {
                        enrollmentSectionIDField.setText(
                                String.valueOf(selected.getSectionID()));
                        enrollmentStudentIDField.setText(
                                String.valueOf(selected.getStudentID()));
                        enrollmentGradeField.setText(selected.getGrade());
                    }
                });

        onHelloButtonClick();
        loadCourses();
        loadFaculty();
        loadSections();
        loadEnrollments();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Please check your information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String required(TextField field, String name) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty.");
        }
        return value;
    }

    private int positiveInt(TextField field, String name) {
        try {
            int value = Integer.parseInt(required(field, name));
            if (value <= 0) {
                throw new IllegalArgumentException(
                        name + " must be greater than zero.");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    name + " must be a whole number.");
        }
    }

    // STUDENT METHODS

    @FXML
    protected void onHelloButtonClick() {
        students = CollegeDB.getStudents();
        studentListView.getItems().setAll(students);
    }

    @FXML
    protected void selectedStudentButtonClicked() {
        Student selected =
                studentListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a student first.");
            return;
        }

        welcomeText.setText(selected.toString());
        deleteStudentButton.setDisable(false);
    }

    @FXML
    protected void addStudentButtonClicked() {
        try {
            String first = required(firstNameTextField, "First name");
            String last = required(lastNameTextField, "Last name");
            String major = majorTextField.getText().trim();
            double gpa = Double.parseDouble(
                    required(gpaTextField, "GPA"));

            if (!Double.isFinite(gpa) || gpa < 0 || gpa > 4) {
                throw new IllegalArgumentException(
                        "GPA must be between 0 and 4.");
            }

            CollegeDB.addStudent(
                    new Student(0, first, last, major, gpa));
            onHelloButtonClick();

        } catch (NumberFormatException e) {
            showError("GPA must be a valid number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void deleteStudentButtonClicked() {
        Student selected =
                studentListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a student first.");
            return;
        }

        CollegeDB.deleteStudent(selected);
        onHelloButtonClick();
        deleteStudentButton.setDisable(true);
    }

    @FXML
    protected void studentFilterTyped() {
        String filter = studentFilterTextField.getText().toLowerCase();
        ArrayList<Student> filtered = new ArrayList<>();

        for (Student student : students) {
            if (student.getFirstName().toLowerCase().contains(filter)
                    || student.getLastName().toLowerCase().contains(filter)) {
                filtered.add(student);
            }
        }

        studentListView.getItems().setAll(filtered);
    }

    // COURSE METHODS

    @FXML
    protected void loadCourses() {
        courseListView.getItems().setAll(CollegeDB.getCourses());
    }

    @FXML
    protected void addCourseClicked() {
        try {
            Course course = new Course(
                    0,
                    required(courseDepartmentField, "Department"),
                    positiveInt(courseNumberField, "Course number"),
                    required(courseTitleField, "Course title"),
                    positiveInt(courseCreditsField, "Credits")
            );

            CollegeDB.addCourse(course);
            loadCourses();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void updateCourseClicked() {
        Course selected =
                courseListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a course first.");
            return;
        }

        try {
            Course updated = new Course(
                    selected.getID(),
                    required(courseDepartmentField, "Department"),
                    positiveInt(courseNumberField, "Course number"),
                    required(courseTitleField, "Course title"),
                    positiveInt(courseCreditsField, "Credits")
            );

            CollegeDB.updateCourse(updated);
            loadCourses();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // FACULTY METHODS

    @FXML
    protected void loadFaculty() {
        facultyListView.getItems().setAll(CollegeDB.getFaculty());
    }

    @FXML
    protected void addFacultyClicked() {
        try {
            Faculty faculty = new Faculty(
                    0,
                    required(facultyFirstNameField, "First name"),
                    required(facultyLastNameField, "Last name"),
                    required(facultyDepartmentField, "Department")
            );

            CollegeDB.addFaculty(faculty);
            loadFaculty();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void updateFacultyClicked() {
        Faculty selected =
                facultyListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a faculty member first.");
            return;
        }

        try {
            Faculty updated = new Faculty(
                    selected.getID(),
                    required(facultyFirstNameField, "First name"),
                    required(facultyLastNameField, "Last name"),
                    required(facultyDepartmentField, "Department")
            );

            CollegeDB.updateFaculty(updated);
            loadFaculty();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // SECTION METHODS

    @FXML
    protected void loadSections() {
        sectionListView.getItems().setAll(CollegeDB.getSections());
    }

    private Section readSection(int id) {
        return new Section(
                id,
                positiveInt(sectionCourseIDField, "Course ID"),
                positiveInt(sectionFacultyIDField, "Faculty ID"),
                required(sectionSemesterField, "Semester"),
                sectionDayField.getText().trim(),
                sectionStartTimeField.getText().trim(),
                sectionEndTimeField.getText().trim(),
                sectionModalityField.getText().trim()
        );
    }

    @FXML
    protected void addSectionClicked() {
        try {
            CollegeDB.addSection(readSection(0));
            loadSections();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void updateSectionClicked() {
        Section selected =
                sectionListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select a section first.");
            return;
        }

        try {
            CollegeDB.updateSection(readSection(selected.getID()));
            loadSections();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // ENROLLMENT METHODS

    @FXML
    protected void loadEnrollments() {
        enrollmentListView.getItems().setAll(
                CollegeDB.getEnrollments());
    }

    private Enrollment readEnrollment(int id) {
        return new Enrollment(
                id,
                positiveInt(enrollmentSectionIDField, "Section ID"),
                positiveInt(enrollmentStudentIDField, "Student ID"),
                enrollmentGradeField.getText().trim()
        );
    }

    @FXML
    protected void addEnrollmentClicked() {
        try {
            CollegeDB.addEnrollment(readEnrollment(0));
            loadEnrollments();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void updateEnrollmentClicked() {
        Enrollment selected =
                enrollmentListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select an enrollment first.");
            return;
        }

        try {
            CollegeDB.updateEnrollment(
                    readEnrollment(selected.getID()));
            loadEnrollments();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    protected void deleteEnrollmentClicked() {
        Enrollment selected =
                enrollmentListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Select an enrollment first.");
            return;
        }

        CollegeDB.deleteEnrollment(selected.getID());
        loadEnrollments();
    }
}
