package edu.uoc.eduocation.view;

import com.google.gson.Gson;
import edu.uoc.eduocation.EdUOCation;
import edu.uoc.eduocation.controller.EdUOCationController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for the play view.
 */
public class PlayViewController {

    private EdUOCationController controller;

    @FXML
    private TableView<List<String>> mainTableView;

    @FXML
    private Button backButton;

    private String currentLevel = "schools"; // Tracks the current table level

    private String selectedSchool;
    private String selectedGroup;
    private String selectedStudent;

    @FXML
    public void initialize() {
        controller = new EdUOCationController("schools.txt", "teachers.txt", "courses.txt", "students.txt", "enrollments.txt");
        loadSchools();
        setupBackButton();
    }

    private void loadSchools() {
        currentLevel = "schools";
        List<String> schools = controller.getSchools();

        setupTable(
                List.of("School Name", "Number of Locations", "Number of Groups"),
                List.of("name", "locationsCount", "groupsCount"),
                schools,
                selection -> {
                    selectedSchool = selection.getFirst();
                    loadGroups(selectedSchool);
                }
        );

    }

    private void loadGroups(String schoolName) {
        currentLevel = "groups";
        List<String> groups = controller.getGroups(schoolName);

        setupTable(
                List.of("Group Name", "Tutor Name", "Number of Students"), // Column headers
                List.of("name", "tutor", "studentsCount"), // JSON keys
                groups,
                selection -> {
                    selectedGroup = selection.getFirst();
                    loadStudents(selectedSchool, selectedGroup);
                }
        );
    }

    private void loadStudents(String schoolName, String groupName) {
        currentLevel = "students";
        List<String> students = controller.getStudents(schoolName, groupName);

        setupTable(
                List.of("NIF", "Name", "Surname", "Birthdate"), // Column headers
                List.of("nif", "name", "surname", "birthdate"), // JSON keys
                students,
                selection -> {
                    selectedStudent = selection.getFirst();
                    loadEnrollments(selectedStudent);
                }
        );
    }

    private void loadEnrollments(String studentNif) {
        currentLevel = "enrollments";
        List<String> enrollments = controller.getEnrollments(studentNif);

        setupTable(
                List.of("Course", "Semester", "Status", "Mark"), // Column headers
                List.of("course", "semester", "status", "mark"), // JSON keys
                enrollments,
                null
        );

        TableColumn<List<String>, String> column = createTableColumn();

        mainTableView.setEditable(true);
        mainTableView.getSelectionModel().setCellSelectionEnabled(false);
        mainTableView.getSelectionModel().clearSelection();
        mainTableView.setOnMouseClicked(null);
    }

    private TableColumn<List<String>, String> createTableColumn() {
        TableColumn<List<String>, String> column = new TableColumn<>("Column Name");
        column.setCellValueFactory(cellData -> {
            List<String> row = cellData.getValue();
            return new SimpleStringProperty(row.getFirst());
        });
        return column;
    }

    private void updateGradeInController(List<String> row, String newGrade) {
        String course = row.get(0); // Extract course name from the row
        String semester = row.get(1); // Extract semester from the row
        String status = row.get(2); // Extract status from the row
        double mark;

        try {
            mark = Double.parseDouble(newGrade); // Parse the grade to a double
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid input", "The mark must be a valid number.");
            row.set(3, ""); // Clear the grade if it's invalid
            mainTableView.refresh(); // Refresh the table to show the cleared value
            return;
        }

        try {
            // Call the controller to update the grade
            boolean success = controller.updateEnrollmentMark(course, semester, selectedStudent, mark);

            if (success) {
                System.out.println("Grade updated successfully for course: " + course + " and student: " + selectedStudent);
                row.set(3, String.format("%.1f", mark)); // Update the grade in the table
            } else {
                System.err.println("Failed to update grade for course: " + course + " and student: " + selectedStudent);
                row.set(3, ""); // Clear the grade if the update fails
                showErrorDialog("Update failed", "Could not update the mark for the selected enrollment.");
            }
        } catch (IllegalArgumentException e) {
            // Show error dialog with the exception message
            showErrorDialog("Invalid mark", e.getMessage());
            row.set(3, ""); // Clear the grade if it's invalid
        }

        mainTableView.refresh(); // Refresh the table to reflect changes
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void setupTable(List<String> headers, List<String> keys, List<String> data, TableRowClickListener listener) {
        mainTableView.getColumns().clear();

        // Crear columnas dinámicamente
        for (int i = 0; i < headers.size(); i++) {
            int columnIndex = i;
            TableColumn<List<String>, String> column = new TableColumn<>(headers.get(i));
            column.setCellValueFactory(param -> {
                List<String> row = param.getValue();
                if (row != null && row.size() > columnIndex) {
                    String value = row.get(columnIndex);
                    return new SimpleStringProperty(value != null ? value : ""); // Convertir null a ""
                }
                return new SimpleStringProperty("");
            });
            mainTableView.getColumns().add(column);
        }

        List<List<String>> rows = new ArrayList<>();
        for (String rowJson : data) {
            rows.add(parseJsonToList(rowJson, keys)); // Convertir JSON a lista de valores
        }

        mainTableView.setItems(FXCollections.observableArrayList(rows));
        mainTableView.setVisible(true);

        if (listener != null) mainTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && mainTableView.getSelectionModel().getSelectedItem() != null) {
                listener.onRowClick(mainTableView.getSelectionModel().getSelectedItem());
            }
        });
    }

    private List<String> parseJsonToList(String json, List<String> keys) {
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(json, Map.class);
        List<String> values = new ArrayList<>();

        for (String key : keys) {
            Object value = map.get(key);
            if (value == null && map.containsKey("details")) {
                Object detailsObject = map.get("details");
                if (detailsObject instanceof Map) {
                    Map<String, Object> detailsMap = (Map<String, Object>) detailsObject;
                    value = detailsMap.get(key);
                }
            }

            if ("mark".equals(key)) {
                if (value instanceof Double) {
                    values.add(String.format("%.1f", (Double) value));
                } else if (value == null || "null".equals(value.toString())) {
                    values.add("");
                } else {
                    values.add(value.toString());
                }
            } else {
                if (value instanceof Double) {
                    values.add(String.valueOf(((Double) value).intValue()));
                } else if (value == null || "null".equals(value.toString())) {
                    values.add("");
                } else {
                    values.add(value.toString());
                }
            }
        }
        return values;
    }



    @FXML
    private void setupBackButton() {
        backButton.setOnAction(e -> {
            try {
                switch (currentLevel) {
                    case "groups" -> loadSchools();
                    case "students" -> loadGroups(selectedSchool);
                    case "enrollments" -> loadStudents(selectedSchool, selectedGroup);
                    case null, default -> EdUOCation.main.goScene("main");
                }
            } catch (IOException ex) {
                System.exit(1);
            }
        });
    }

    @FunctionalInterface
    private interface TableRowClickListener {
        void onRowClick(List<String> row);
    }
}
