// App.java
package com.example.customermanager;

import javafx.application.Application;        // base class for every JavaFX program
import javafx.collections.FXCollections;      // helper that creates observable lists
import javafx.collections.ObservableList;     // a list the table can watch for changes
import javafx.geometry.Insets;                // padding around layouts
import javafx.scene.Scene;                    // the content shown inside a window
import javafx.scene.control.*;                // Button, Label, TextField, ComboBox, TableView, Alert...
import javafx.scene.layout.*;                 // GridPane, HBox, VBox, Priority
import javafx.stage.Stage;                    // the window itself

// "extends Application" is what turns this class into a JavaFX program.
public class App extends Application {

    // Step 2: the list that holds every customer.
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        // ---- Step 1: form ----

        Label nameLabel = new Label("_Name:");
        nameLabel.setMnemonicParsing(true);        // turns the underscore feature ON
        TextField nameField = new TextField();     // the box where the user types a name
        nameLabel.setLabelFor(nameField);          // Alt+N jumps into this box

        Label provinceLabel = new Label("_Province:");
        provinceLabel.setMnemonicParsing(true);
        ComboBox<String> provinceBox = new ComboBox<>(FXCollections.observableArrayList(
                "Central", "Copperbelt", "Eastern", "Luapula", "Lusaka",
                "Muchinga", "Northern", "North-Western", "Southern", "Western"));
        provinceBox.setPromptText("Select province");  // grey hint text
        provinceLabel.setLabelFor(provinceBox);        // Alt+P jumps to the drop-down

        Button addButton = new Button("_Add");
        addButton.setDefaultButton(true);          // Enter anywhere in the form clicks Add
        Button deleteButton = new Button("_Delete");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");   // make the text red

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, nameLabel, nameField);       // row 0
        form.addRow(1, provinceLabel, provinceBox); // row 1

        // ---- Step 3: TableView ----

        TableView<Customer> table = new TableView<>(customers);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());

        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(c -> c.getValue().provinceProperty());

        // FIX 2: add() one at a time instead of addAll(...).
        // addAll(col1, col2) creates a generic array under the hood and gives you an
        // "unchecked / varargs" warning. Two add() calls do exactly the same thing, cleanly.
        table.getColumns().add(nameCol);
        table.getColumns().add(provinceCol);

        // FIX 1: CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN only exists in JavaFX 20+.
        // On JavaFX 17/19 (what most courses use) it is "cannot find symbol".
        // CONSTRAINED_RESIZE_POLICY exists in every version and does the same job here.
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No customers yet."));  // shown when the list is empty

        // ---- Step 4: validate, then add ----

        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String province = provinceBox.getValue();

            // Validation check 1: the name must not be empty.
            if (name.isEmpty()) {
                errorLabel.setText("Name is required.");
                nameField.requestFocus();
                return;                                    // do NOT add the customer
            }
            // Validation check 2: a province must be selected.
            if (province == null) {
                errorLabel.setText("Please select a province.");
                provinceBox.requestFocus();
                return;
            }

            // Both checks passed: create the customer and add it.
            customers.add(new Customer(name, province));

            // Reset the form for the next customer.
            errorLabel.setText("");
            nameField.clear();
            provinceBox.setValue(null);
            nameField.requestFocus();
        });

        // ---- Step 5: confirm deletion ----

        // Delete is greyed out whenever no row is selected.
        deleteButton.disableProperty()
                .bind(table.getSelectionModel().selectedItemProperty().isNull());

        deleteButton.setOnAction(e -> {
            Customer selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;      // safety check

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Delete " + selected.nameProperty().get() + "?",
                    ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Confirm deletion");

            // Only remove the customer if the user actually clicked YES.
            confirm.showAndWait()
                    .filter(b -> b == ButtonType.YES)
                    .ifPresent(b -> customers.remove(selected));
        });

        // ---- Layout ----

        HBox buttons = new HBox(10, addButton, deleteButton);
        VBox root = new VBox(10, form, buttons, errorLabel, table);
        root.setPadding(new Insets(15));
        VBox.setVgrow(table, Priority.ALWAYS);   // let the table take all spare height

        stage.setTitle("Customer Manager");
        stage.setScene(new Scene(root, 480, 460));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}