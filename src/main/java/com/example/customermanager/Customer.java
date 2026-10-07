// Customer.java: one customer = one name + one province.
// This is the "model": it holds data only and has no screen code.

// Must match the folder path src/main/java/com/example/customermanager
package com.example.customermanager;

// A "Property" is a value that can tell other code when it changes.
// StringProperty is the property version of a String.
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    // Private fields: only this class can touch them directly.
    // "final" means the property object is never replaced (its value can still change).
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty province = new SimpleStringProperty();

    // Constructor: runs when you write new Customer("Mwamba", "Copperbelt").
    // It fills in the two properties with the values you give it.
    public Customer(String name, String province) {
        this.name.set(name);          // "this.name" is the field, "name" is the parameter
        this.province.set(province);
    }

    // Getter methods for the properties. The TableView calls these to read the data.
    // The names MUST end in "Property" (nameProperty, provinceProperty).
    public StringProperty nameProperty() { return name; }
    public StringProperty provinceProperty() { return province; }
}