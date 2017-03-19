package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controller {
    @FXML
    private static SplitPane splitPane;
    @FXML
    private static AnchorPane anchorPane_main;
    @FXML
    private static AnchorPane anchorPane_chosen;
    @FXML
    private static TabPane tabPane;

    @FXML
    private static Button Button_GetRow;
    @FXML
    private static Button Button_AddToChosen;
    @FXML

    private static TableView TableView_Main;
    @FXML
    private static TableView TableView_Chosen;
    @FXML

    private static TextField TextField_Code;

    private static ObservableList<Row> chosenList = FXCollections.observableArrayList();

    public static void init(Parent root){
        /*Button_GetRow = (Button) root.getChildrenUnmodifiable().get(0);
        tableView = (TableView) root.getChildrenUnmodifiable().get(1);
        TextField_Code = (TextField) root.getChildrenUnmodifiable().get(2);
        ((TableColumn)tableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("code"));
        ((TableColumn)tableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        ((TableColumn)tableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("name"));
        ((TableColumn)tableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("price"));
        //((TableColumn)tableView.getColumns().get(4)).setCellValueFactory(nwe );
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);*/
        //System.err.println(root.getChildrenUnmodifiable().get(0));
        /*Button_GetRow = (Button) root.lookup("Button_GetRow");
        TextField_Code = (TextField) root.lookup("TextField_Code");
        tableView = (TableView) root.lookup("TableView_Rows");*/

        splitPane = (SplitPane) root.getChildrenUnmodifiable().get(0);
        anchorPane_main = (AnchorPane) splitPane.getItems().get(0);
        anchorPane_chosen = (AnchorPane) splitPane.getItems().get(1);

        TableView_Main = (TableView) anchorPane_main.getChildren().get(0);
        Button_GetRow = (Button) anchorPane_main.getChildren().get(1);
        TextField_Code = (TextField) anchorPane_main.getChildren().get(2);
        Button_AddToChosen = (Button) anchorPane_main.getChildren().get(3);

        TableView_Chosen = (TableView) anchorPane_chosen.getChildren().get(0);

        TableView_Chosen.setItems(chosenList);

        ((TableColumn)TableView_Chosen.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("code"));

        ((TableColumn)TableView_Main.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("code"));
        ((TableColumn)TableView_Main.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        ((TableColumn)TableView_Main.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("name"));
        ((TableColumn)TableView_Main.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("price"));
        TableView_Main.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

    public void Button_GetRow_Action(ActionEvent event){
        URL object = null;
        HttpURLConnection con = null;

        try {
            object = new URL("http://localhost:8080/component/" + TextField_Code.getText().trim());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            con = (HttpURLConnection) object.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
        } catch (IOException e1) {
            System.err.println("Can not open connection. " + e1);
        }

        JSONArray jsonArray = null;

        try {
            if(con.getResponseCode() != HttpURLConnection.HTTP_OK){

            }
            else{
                jsonArray = getJSONobjectResponse(con);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //List<Row> newRows = new ArrayList<Row>();
        ObservableList<Row> rowList = FXCollections.observableArrayList();
        //rowList.removeAll();
        TableView_Main.setItems(rowList);
        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++){
                try {
                    Row row = new Row((JSONObject)jsonArray.get(i));
                    rowList.add(row);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //tableView.getItems().addAll(rowList);
            //System.err.println(rowList.get(0));
            //System.err.println(rowList.get(1));
            //System.err.println(rowList.get(2));
            //tableView.getColumns().get(0).toString();

        }




    }

    public void Button_AddToChosen_Action(ActionEvent event){
        chosenList.addAll(TableView_Main.getSelectionModel().getSelectedItems());
    }

    private static JSONArray getJSONobjectResponse(HttpURLConnection con){
        try {
            StringBuilder sb = new StringBuilder();
            String jsonObjects;
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                try {
                    return new JSONArray(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(con.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
