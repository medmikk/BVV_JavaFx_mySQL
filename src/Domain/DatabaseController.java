package Domain;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class DatabaseController {
    private final String HOST = "localhost";
    private final String PORT = "228"; //TODO make your port and login data
    private final String DB_NAME = "water";
    private final String LOGIN = "root";
    private final String PASSWORD = "7951078";

    private Connection connection = null;


    public DatabaseController() {
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connStr, LOGIN, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getCountOfColumns(String tableName) throws SQLException {
        String sql = "SELECT count(*) FROM information_schema.columns WHERE table_name = '" + tableName + "'";
        Statement statement = connection.createStatement();
        ResultSet res = statement.executeQuery(sql);
        Integer result = 0;
        if(res.next())
            result = Integer.parseInt(res.getString(1));
        return result;
    }

    public ArrayList<Entity> getTable(String tableName){
        ArrayList<Entity> items = null;
        try {
            Integer colsCount = getCountOfColumns(tableName);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + tableName;
            ResultSet res = statement.executeQuery(sql);
            items = new ArrayList<>();

            while(res.next()) {
                ArrayList<String> params = new ArrayList<>();
                for (int i = 0; i < colsCount; i++)
                    params.add(res.getString(i + 1));
                Entity item = new Entity(params);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<String> getAttributeNames(String tableName){
        ArrayList<String> result = new ArrayList<>();
        try {
        String sql = "SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE " +
                "`TABLE_SCHEMA`='" + DB_NAME + "' AND `TABLE_NAME`='" + tableName + "' order by ORDINAL_POSITION;";
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            while(res.next()) {
                result.add(res.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getAttributeFromTable(String attrName, String tableName){
        ArrayList<String> result = new ArrayList<>();
        try {
            String sql = "SELECT "+ attrName +" FROM " + tableName;
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            while(res.next()) {
                result.add(res.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public void insert(String tableName, String values){
        String sql = "INSERT INTO " + tableName + " VALUES (" + values + ")";

        PreparedStatement prSt;
        try {
            prSt = connection.prepareStatement(sql);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("SQL syntax error", e.toString());
        }
    }

    public void delete(String tableName, String attribute, String value){
        String sql = "DELETE FROM " +
                tableName +
                " WHERE " + attribute + " = " + value;

        PreparedStatement prSt;
        try {
            prSt = connection.prepareStatement(sql);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("SQL syntax error", e.toString());
        }
    }

    public String getIdName(String tableName){
        return getAttributeNames(tableName).get(0);
    }

    public ArrayList<String> getByIndex(Integer index, String tableName){
        String idName = getIdName(tableName);
        ArrayList<String> result = new ArrayList<>();
        try {
            int count = getCountOfColumns(tableName);
            if(index > count)
                return result;
            String sql = "SELECT * FROM " + tableName + " WHERE " + idName + " = " + index;
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            res.next();
            for (int i = 0; i < count; i++)
                result.add(res.getString(i + 1));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public static void showAlert(String header, String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
