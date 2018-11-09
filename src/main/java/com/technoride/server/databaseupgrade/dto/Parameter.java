package com.technoride.server.databaseupgrade.dto;

import javafx.beans.property.*;


import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class Parameter
{
    private IntegerProperty serialNumber = new SimpleIntegerProperty();
    private StringProperty propertyName = new SimpleStringProperty();
    private ObjectProperty propertyValue = new SimpleObjectProperty();
    private StringProperty description = new SimpleStringProperty();
    private String value;
    private Control control;
    private String propertyType;

    public int getSerialNumber() {
        return serialNumber.get();
    }

    public IntegerProperty serialNumberProperty() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber.set(serialNumber);
    }

    public String getPropertyName() {
        return propertyName.get();
    }

    public StringProperty propertyNameProperty() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName.set(propertyName);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Control getControl()
    {
        System.out.println(propertyType.charAt(0));
        propertyValue.unbind();
        propertyValue.addListener(((observable, oldValue, newValue) -> {
            value=(String) newValue;
        }));
        switch (propertyType.charAt(0))
        {
            case 'T':
                TextField textField = new TextField();
                textField.textProperty().bindBidirectional(propertyValue);
                this.control = textField;
                break;
            case 'C':
                ComboBox<String> comboBox = new ComboBox<>();
                comboBox.itemsProperty().bindBidirectional(propertyValue);
                comboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
                    value=newValue;
                    System.out.println(value);
                }));
                this.control=comboBox;
                break;
        }
        return control;
    }

    /*public void setControl(Control control) {
        this.control = control;
    }*/

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Object getPropertyValue() {
        return propertyValue.get();
    }

    public ObjectProperty propertyValueProperty() {
        return propertyValue;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue.set(propertyValue);
    }

    public String getValue() {
        if(value == null)
            return (String) propertyValue.get();
        else
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
