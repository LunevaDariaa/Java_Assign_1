package com.example.skincareapp;


import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TableViewController {

    @FXML
    private TableView<Sunscreen> tableView;
    @FXML
    private TableColumn<Sunscreen, String> nameCol;
    @FXML
    private TableColumn<Sunscreen, String> brandCol;
    @FXML
    private TableColumn<Sunscreen, Integer> spfCol;
    @FXML
    private TableColumn<Sunscreen, String> skinTypeCol;
    @FXML
    private TableColumn<Sunscreen, Float> ratingCol;

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        spfCol.setCellValueFactory(new PropertyValueFactory<>("spfLevel"));
        skinTypeCol.setCellValueFactory(new PropertyValueFactory<>("skinType"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        loadSunscreens();
    }

    //Populate table
    private void loadSunscreens() {
        ObservableList<Sunscreen> sunscreens = FXCollections.observableArrayList();
        try (Connection connection = DBConnector.getConnection()) {
            String query = "SELECT product_name, brand, spf_level, skin_type, rating FROM Sunscreens";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Sunscreen sunscreen = new Sunscreen(
                        resultSet.getString("product_name"),
                        resultSet.getString("brand"),
                        resultSet.getInt("spf_level"),
                        resultSet.getString("skin_type"),
                        resultSet.getFloat("rating")
                );
                sunscreens.add(sunscreen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tableView.setItems(sunscreens);
    }
}
