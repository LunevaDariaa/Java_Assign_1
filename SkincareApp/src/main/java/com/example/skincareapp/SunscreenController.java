package com.example.skincareapp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class SunscreenController {
    @FXML
    private ComboBox<String> skinTypeFilter;

    @FXML
    private ComboBox<Integer> spfFilter;

    @FXML
    private BarChart<String, Number> barChart;

    // ObservableList for storing sunscreen data
    private ObservableList<Sunscreen> sunscreenData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize filters
        skinTypeFilter.getItems().addAll("All","Combination", "Dry", "Oily", "Sensitive");
        spfFilter.getItems().addAll( 50,30,15);
        skinTypeFilter.getSelectionModel().selectFirst();
        spfFilter.getSelectionModel().selectFirst();

        // Load initial data
        applyFilters();
    }

    @FXML
    private void applyFilters() {
        String skinType = skinTypeFilter.getValue();
        int spf = spfFilter.getValue();

        // Fetch sunscreen data from DB based on filters
        fetchSunscreenDataFromDB(skinType, spf);
        // Update bar chart
        updateBarChart(sunscreenData);
    }

    private void fetchSunscreenDataFromDB(String skinType, int spf) {
        sunscreenData.clear(); // Clear previous data

        String url = "jdbc:mysql://localhost:3306/skincare";
        String user = "root";
        String password = "4507";

        String sql = "SELECT * FROM Sunscreens WHERE (skin_type = ? OR ? = 'All') AND (spf_level = ? OR ? = 0) ORDER BY rating DESC LIMIT 5";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, skinType);
            pstmt.setString(2, skinType);
            pstmt.setInt(3, spf);
            pstmt.setInt(4, spf);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Sunscreen sunscreen = new Sunscreen(
                        rs.getString("product_name"),
                        rs.getString("brand"),
                        rs.getInt("spf_level"),
                        rs.getString("skin_type"),
                        rs.getDouble("rating")
                );
                sunscreenData.add(sunscreen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBarChart(List<Sunscreen> sunscreenData) {
        // Clear previous data
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Sunscreen sunscreen : sunscreenData) {
            String fullLabel = sunscreen.getBrand() + " " + sunscreen.getProductName();
            String[] labelLines = splitLabel(fullLabel, 20); // Split label into lines with max 20 characters

            XYChart.Data<String, Number> data = new XYChart.Data<>(String.join("\n", labelLines), sunscreen.getRating());
            Tooltip.install(data.getNode(), new Tooltip(fullLabel)); // Add tooltip to show full label

            series.getData().add(data);
        }

        barChart.getData().add(series);

    }

    private String[] splitLabel(String label, int maxLength) {
        // Split label into lines with max maxLength characters
        if (label.length() <= maxLength) {
            return new String[]{label};
        }

        // Split the label into multiple lines
        int numLines = (int) Math.ceil((double) label.length() / maxLength);
        String[] lines = new String[numLines];
        for (int i = 0; i < numLines; i++) {
            int start = i * maxLength;
            int end = Math.min((i + 1) * maxLength, label.length());
            lines[i] = label.substring(start, end);
        }
        return lines;
    }
    @FXML
    private void showTable() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tableview.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Sunscreen Table");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
