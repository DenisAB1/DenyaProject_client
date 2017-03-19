package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controller {
    private static TabPane tabPane;
    private static Button Button_GetRow;
    private static TableView tableView;
    private static TextField TextField_Code;

    public static void init(Parent root){
        Button_GetRow = (Button) root.getChildrenUnmodifiable().get(0);
        tableView = (TableView) root.getChildrenUnmodifiable().get(1);
        TextField_Code = (TextField) root.getChildrenUnmodifiable().get(2);
        ((TableColumn)tableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("code"));
        ((TableColumn)tableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        ((TableColumn)tableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("name"));
        ((TableColumn)tableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("price"));
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
        tableView.setItems(rowList);
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
