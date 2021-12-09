package adding_scene;

import Domain.DatabaseController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main_scene.MainSceneController;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class AddController {

    private String tableName;
    private ArrayList<String> attributes;
    private MainSceneController mController;
    private DatabaseController database;
    @FXML
    private HBox hBox;

    @FXML
    public void initialize(){
        database = new DatabaseController();
    }

    public void setAttributes(String tableName){
        this.tableName = tableName;
        this.attributes = database.getAttributeNames(tableName);
        for(String attribute : attributes){
            TextField tf = new TextField();
            tf.setPromptText(attribute);
            tf.setPrefWidth(800);
            hBox.getChildren().add(tf);
        }
    }

    public void setMainController(MainSceneController controller){
        this.mController = controller;
    }

    @FXML
    public void onClickApplyButton(){
        String values = "";
        for (Object tf : hBox.getChildren()){
            TextField t = (TextField) tf;
            if (t.getText().isEmpty()) {
                DatabaseController.showAlert("Input not valid", "Field cannot be empty.");
                return;
            } else
                values += "'" + t.getText().trim() + "', ";
        }
        values = values.substring(0, values.length() - 2);
        database.insert(tableName, values);
        Stage stage = (Stage) hBox.getScene().getWindow();
        mController.tableController.switchTable(tableName);
        stage.close();
    }

}
