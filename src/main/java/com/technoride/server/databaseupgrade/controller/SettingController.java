package com.technoride.server.databaseupgrade.controller;

import com.technoride.server.databaseupgrade.client.RestClient;
import com.technoride.server.databaseupgrade.dto.Parameter;
import com.technoride.server.databaseupgrade.setting.ThreadSetting;
import com.technoride.server.databaseupgrade.utils.FileUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.util.Map;


@Controller
public class SettingController {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private RestClient restClient;
    @FXML
    private TableView<Parameter> settingTable;

    @FXML
    private TableColumn<?, ?> serialNumber;

    @FXML
    private TableColumn<?, ?> parameterName;

    @FXML
    private TableColumn<?, ?> paramValue;

    @FXML
    private TableColumn<?, ?> description;

    private Map<String,Parameter> parameterMap;

    @FXML
    public void applySetting(ActionEvent event) {
        fileUtil.saveParameter(parameterMap);
    }

    @FXML
    public void cancelSetting(ActionEvent event) {

    }


    @FXML
    public void initialize()
    {
        parameterMap = fileUtil.populateProperty();
        parameterMap.forEach((s, parameter) -> {
            if (parameter.getPropertyType().charAt(0) == 'C')

            {
                try {
                    ThreadSetting ts = new ThreadSetting(){};
                    Field [] allFields  = ts.getClass().getFields();
                    ObservableList<String> values = FXCollections.observableArrayList();
                    for (Field f : allFields)
                    {
                        String value = (String)f.get(ts);
                        values.add(value);
                    }
                    parameter.propertyValueProperty().setValue(values);
                }
                catch (IllegalAccessException iae)
                {
                   iae.printStackTrace();
                }
            }

        });

        int i=1;
        for (String key : parameterMap.keySet())
        {
            Parameter parameter = parameterMap.get(key);
            parameter.setSerialNumber(i);
            i++;
        }

        serialNumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        parameterName.setCellValueFactory(new PropertyValueFactory<>("propertyName"));
        paramValue.setCellValueFactory(new PropertyValueFactory<>("control"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        settingTable.getItems().addAll(parameterMap.values());
    }



}
