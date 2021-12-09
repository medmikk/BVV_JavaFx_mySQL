package main_scene;

import Domain.DatabaseController;
import Domain.Entity;
import adding_scene.AddController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainSceneController {

    public TableController tableController;

    @FXML
    private Text textDistributionPoint;
    @FXML
    private Text textFactProduction;
    @FXML
    private Text textEmployee;
    @FXML
    private Text textMachine;
    @FXML
    private Text textPlant;
    @FXML
    private Text textPosition;
    @FXML
    private Text textProduct;
    @FXML
    private Text textProductType;
    @FXML
    private Text textProvider;
    @FXML
    private Text textRegion;
    @FXML
    private Text textWater;


    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<String[]> tableView;

    @FXML
    public void initialize(){
        this.tableController = new TableController();
        textDistributionPoint.setOnMouseClicked(new TextClickHandler(textDistributionPoint));
        textFactProduction.setOnMouseClicked(new TextClickHandler(textFactProduction));
        textEmployee.setOnMouseClicked(new TextClickHandler(textEmployee));
        textMachine.setOnMouseClicked(new TextClickHandler(textMachine));
        textPlant.setOnMouseClicked(new TextClickHandler(textPlant));
        textPosition.setOnMouseClicked(new TextClickHandler(textPosition));
        textProduct.setOnMouseClicked(new TextClickHandler(textProduct));
        textProductType.setOnMouseClicked(new TextClickHandler(textProductType));
        textProvider.setOnMouseClicked(new TextClickHandler(textProvider));
        textRegion.setOnMouseClicked(new TextClickHandler(textRegion));
        textWater.setOnMouseClicked(new TextClickHandler(textWater));
        setTableRowFactory();
    }

    @FXML
    public void onClickInsert(){
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("adding_scene/add_scene.fxml"));
            Parent root = loader.load();

            AddController controller = loader.getController();
            controller.setAttributes(tableController.currentTable);
            controller.setMainController(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tableView.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickFind(){
        String word = tfSearch.getText().trim();
        if(word.isEmpty())
            DatabaseController.showAlert("Input error", "Field cannot be empty");
        else {
            tableController.database.find(tableController.currentTable, word);
        }
    }

    @FXML
    public void onClickTerminal(){
        try {
            FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("terminal_scene/terminal_scene.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tableView.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTableRowFactory(){
        tableView.setRowFactory(tableView->{
            TableRow<String[]> row = new TableRow<>();
            row.emptyProperty().addListener((obs, wasEmpty, isEmpty) -> {
                if (!isEmpty) {
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem removeItem = new MenuItem("Delete");

                    String[] item = row.getItem();
                    removeItem.setOnAction(event -> {
                        tableController.database.delete(tableController.currentTable,
                                tableController.database.getIdName(tableController.currentTable),
                                item[0]);
                        tableController.switchTable(tableController.currentTable);
                    });

                    contextMenu.getItems().add(removeItem);
                    row.setContextMenu(contextMenu);
                }
            });
            return row;
        });
    }

    private class TextClickHandler implements EventHandler<Event> {
        private final String tableName;
        private Text text;

        TextClickHandler(Text text){
            this.tableName = text.getText();
            this.text = text;
        }

        @Override
        public void handle(Event event) {
            tableController.switchTable(this.tableName);
            textDistributionPoint.setFill(Color.BLACK);
            textFactProduction.setFill(Color.BLACK);
            textEmployee.setFill(Color.BLACK);
            textMachine.setFill(Color.BLACK);
            textPlant.setFill(Color.BLACK);
            textPosition.setFill(Color.BLACK);
            textProduct.setFill(Color.BLACK);
            textProductType.setFill(Color.BLACK);
            textProvider.setFill(Color.BLACK);
            textRegion.setFill(Color.BLACK);
            textWater.setFill(Color.BLACK);
            text.setFill(Color.color(0.207, 0.415, 0.941));

        }
    }

    public class TableController {

        private DatabaseController database;
        private String currentTable;

        public TableController(){
            database = new DatabaseController();
            switchTable("employee");
        }

        public void switchTable(String tableName){
            currentTable = tableName;
            tableView.getItems().clear();
            tableView.getColumns().clear();
            ArrayList<String> attributeNames = database.getAttributeNames(tableName);
            ArrayList<Entity> table = database.getTable(tableName);
            ArrayList<String[]> tableList = new ArrayList<>();
            for(int i = 0; i < table.size(); i ++){
                String[] item = table.get(i).getAttributesArray();
                tableList.add(item);
            }
            for (int i = 0; i < attributeNames.size(); i++){
                String attrName = attributeNames.get(i);
                TableColumn<String[], String> tableColumn = new TableColumn<>(attrName);
                tableView.getColumns().add(tableColumn);

//                ArrayList<String> alItem = database.getByIndex(i + 1, tableName);
//                String[] item = new String[alItem.size()];
//                alItem.toArray(item);

                int finalI = i;
                tableColumn.setCellValueFactory((p) -> {
                    String[] x = p.getValue();
                    return new SimpleStringProperty(x != null && x.length > finalI ? x[finalI] : "<no data>");
                });

            }
            tableView.setItems(FXCollections.observableArrayList(tableList));
        }
    }
}
