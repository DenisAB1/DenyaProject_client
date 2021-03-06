package sample;

import com.sun.javafx.fxml.builder.URLBuilder;
import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.PrivateKeyResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;


import java.io.*;
import java.net.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;

public class Controller {
    private final static int LIMIT = 25;

    @FXML
    private static TabPane TabPane;
    @FXML
    private static Tab TabSchedule;
    @FXML
    private static AnchorPane anchorPane_tabSch;

    @FXML
    private static Button Button_Login;
    @FXML
    private static TextField TextFieldLogin;
    @FXML
    private static SplitPane RegSplitPane;
    @FXML
    private static AnchorPane RegAnchorPane_main;

    @FXML
    private static SplitPane splitPane;
    @FXML
    private static ProgressBar ProgressBar;
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
    private static Button Button_AddToExcel;
    @FXML
    private static Pagination pagination;

    @FXML
    private static HBox hBox_params;
    @FXML
    private static TextField TextField_Code;
    @FXML
    private static TextField TextField_Manufacturer;
    @FXML
    private static TextField TextField_Name;
    @FXML
    private static TextField TextField_Price;


    @FXML
    private static TableView TableView_Main;
    @FXML
    private static TableView TableView_Chosen;


    private static ObservableList<Row> chosenList = FXCollections.observableArrayList();
    private static Stage primaryStage;
    public static void init2(Parent root, Stage stage){
        /*TabPane = (TabPane) root.getChildrenUnmodifiable().get(0);
        TabSchedule = (Tab) TabPane.getTabs().get(0);
        anchorPane_tabSch = (AnchorPane) TabSchedule.getContent();
        Butto*/
    }
    public static void init1(Parent root, Stage stage) {
        primaryStage = stage;
        RegSplitPane = (SplitPane) root.getChildrenUnmodifiable().get(0);
        RegAnchorPane_main = (AnchorPane) RegSplitPane.getItems().get(1);
        TextFieldLogin = (TextField) RegAnchorPane_main.getChildren().get(2);
        Button_Login = (Button) RegAnchorPane_main.getChildren().get(5);
    }
    public static void init(Parent root, Stage stage){
        primaryStage = stage;

        splitPane = (SplitPane) root.getChildrenUnmodifiable().get(0);
        anchorPane_main = (AnchorPane) splitPane.getItems().get(0);
        anchorPane_chosen = (AnchorPane) splitPane.getItems().get(1);

        TableView_Main = (TableView) anchorPane_main.getChildren().get(0);
        Button_GetRow = (Button) anchorPane_main.getChildren().get(1);
        //TextField_Code = (TextField) anchorPane_main.getChildren().get(2);
        Button_AddToChosen = (Button) anchorPane_main.getChildren().get(2);
        pagination = (Pagination) anchorPane_main.getChildren().get(3);
        hBox_params = (HBox) anchorPane_main.getChildren().get(4);

        TextField_Code = (TextField) hBox_params.getChildren().get(0);
        TextField_Manufacturer = (TextField) hBox_params.getChildren().get(1);
        TextField_Name = (TextField) hBox_params.getChildren().get(2);
        TextField_Price = (TextField) hBox_params.getChildren().get(3);

        ProgressBar = (ProgressBar) anchorPane_main.getChildren().get(5);

        TableView_Chosen = (TableView) anchorPane_chosen.getChildren().get(0);
        Button_AddToExcel = (Button) anchorPane_chosen.getChildren().get(1);

        TableView_Chosen.setItems(chosenList);

        ((TableColumn)TableView_Chosen.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

        ((TableColumn)TableView_Main.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("code"));
        ((TableColumn)TableView_Main.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        ((TableColumn)TableView_Main.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("name"));
        ((TableColumn)TableView_Main.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("price"));
        TableView_Main.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        pagination.setVisible(false);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });

    }

    public static VBox createPage(int pageIndex) {
        if(CachedPages.getTotalComponentNumber() != 0) {
            if(CachedPages.getPageElements(pageIndex) == null) {
                HttpURLConnection con = sendRequest(LIMIT, pageIndex * LIMIT);

                getJSONobjectResponse(con, pageIndex);
            }

            ObservableList<Row> rowList = FXCollections.observableArrayList();

            TableView_Main.setItems(rowList);

            if (CachedPages.getPageElements(pageIndex) != null) {
                for (int i = 0; i < CachedPages.getPageElements(pageIndex).length(); i++) {
                    try {
                        Row row = new Row((JSONObject) CachedPages.getPageElements(pageIndex).get(i));
                        rowList.add(row);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return new VBox();
    }

    public static HttpURLConnection sendRequest(int limit, int startFrom){
        URL object = null;
        HttpURLConnection con = null;
        System.out.println("new request");
        try {
            String stringURL = null;
            try {
                stringURL = "http://localhost:8080/component?" +
                        "name=" + URLEncoder.encode(TextField_Name.getText().trim(), "UTF-8") +
                        "&manufacturer=" + URLEncoder.encode(TextField_Manufacturer.getText().trim(), "UTF-8") +
                        "&price=" + URLEncoder.encode(TextField_Price.getText().trim(), "UTF-8") +
                        "&code=" + URLEncoder.encode(TextField_Code.getText().trim(), "UTF-8") +
                        "&limit=" + URLEncoder.encode(String.valueOf(limit).trim(), "UTF-8") +
                        "&startFrom=" + URLEncoder.encode(String.valueOf(startFrom).trim(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //System.out.println(stringURL);
            object = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            con = (HttpURLConnection) object.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
        } catch (IOException e) {
            System.err.println("Can not open connection. " + e);
        }
        return con;
    }

    public void Button_GetRow_Action(ActionEvent event){
        CachedPages.clearMap();
        CachedPages.setTotalComponentNumber(0);

        HttpURLConnection con = sendRequest(LIMIT,0);

        getJSONobjectResponse(con, 0);

        if(CachedPages.getTotalComponentNumber() > LIMIT)
            pagination.setVisible(true);
        else
            pagination.setVisible(false);

        pagination.setPageCount(CachedPages.getTotalComponentNumber()/LIMIT + 1);

        System.out.println("Total" + CachedPages.getTotalComponentNumber());

        ObservableList<Row> rowList = FXCollections.observableArrayList();

        TableView_Main.setItems(rowList);

        if(CachedPages.getPageElements(0) != null) {
            for (int i = 0; i < CachedPages.getPageElements(0).length(); i++) {
                try {
                    Row row = new Row((JSONObject) CachedPages.getPageElements(0).get(i));
                    rowList.add(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Button_AddToChosen_Action(ActionEvent event){
        chosenList.addAll(TableView_Main.getSelectionModel().getSelectedItems());
    }

    public void  Menu_LoadNewFile_Action(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Excel", "*.xls"),
                new FileChooser.ExtensionFilter("Excel new format", "*.xlsx")
        );
        File file = fileChooser.showOpenDialog(primaryStage);

        if(file != null) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                //System.out.println(sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObservableList<Row> rowList = FXCollections.observableArrayList();

        TableView_Main.setItems(rowList);

        for (int i = 0; i < FileWorker.getSizeOfFile(); i++) {
            try {
                Row row = new Row(i);
                rowList.add(row);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        MultipartEntity entity = new MultipartEntity();
//        entity.addPart("file", new FileBody(file));
//
//        HttpPost request = new HttpPost("http://localhost:8080/component");
//        request.setEntity(entity);
//
//        HttpClient client = new DefaultHttpClient();
//        try {
//            HttpResponse response = client.execute(request);
//
//            if(response.getStatusLine().toString().contains("404")){
//                //entity.consumeContent();
//                //there is no mapping
//                MultipartEntity entity1 = new MultipartEntity();
//                entity1.addPart("name", new StringBody("Description 1+Description 2+Description 3"));
//                entity1.addPart("manufacturer", new StringBody("\\\"XAL\\\""));
//                entity1.addPart("price", new StringBody("EUR"));
//                entity1.addPart("code", new StringBody("Code"));
//
//                HttpPost request1 = new HttpPost("http://localhost:8080/mapping");
//                request1.setEntity(entity1);
//
//                HttpClient client1 = new DefaultHttpClient();
//
//                try {
//                    HttpResponse response1 = client1.execute(request1);
//                    System.out.println(response1.getStatusLine());
//                }  catch (IOException e) {
//                    e.printStackTrace();
//                }
//
////                /*URL object = null;
////                HttpURLConnection con = null;
////                System.out.println("new mapping");
////                try {
////                    String stringURL = null;
////                    try {
////                        stringURL = "http://localhost:8080/mapping?" +
////                                "name=" + URLEncoder.encode("Description 1+Description 2+Description 3", "UTF-8") +
////                                "&manufacturer=" + URLEncoder.encode("\"XAL\"", "UTF-8") +
////                                "&price=" + URLEncoder.encode("EUR", "UTF-8") +
////                                "&code=" + URLEncoder.encode("Code", "UTF-8");
////                    } catch (UnsupportedEncodingException e) {
////                        e.printStackTrace();
////                    }
////                    object = new URL(stringURL);
////                } catch (MalformedURLException e) {
////                    e.printStackTrace();
////                }
////
////                try {
////                    con = (HttpURLConnection) object.openConnection();
////                    con.setRequestMethod("POST");
////                    con.setRequestProperty("Accept", "application/json");
////                } catch (IOException e) {
////                    System.err.println("Can not open connection. " + e);
////                }
////                System.out.println("Con resp code: " + con.getResponseCode());*/
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public void Menu_Schedule_Action(ActionEvent event){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ScheduleScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));
        Controller.init2(root, primaryStage);
    }

    public void Button_AddToExcel_Action(ActionEvent event){
        new Thread() {
            public void run() {
                for (int i = 0; i < FileWorker.getSizeOfFile(); i++) {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1001));
                        ProgressBar.setProgress(((double)i+1) / 12);
                        System.out.println(((double)i+1) / 12);
                        if(i == 11){
                            TableView_Chosen.getItems().clear();
                            TableView_Main.getItems().clear();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("I have a great message for you!");

                            alert.showAndWait();

                            ProgressBar.setProgress((double) 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();

//        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Information Dialog");
//        alert.setHeaderText(null);
//        alert.setContentText("I have a great message for you!");
//
//        alert.showAndWait();*/
//
//         /*try {
//
//             FileInputStream file = new FileInputStream(new File("C:\\Users\\User\\Desktop\\3.xlsx"));
//             //FileInputStream file = new FileInputStream(new File("C:\\test.xlsx"));
//             System.out.println(file);
//             XSSFWorkbook workbook = new XSSFWorkbook (file);
//             XSSFSheet sheet = workbook.getSheetAt(0);
//
//             for (int i = 0; i < chosenList.size(); i++){
//                 sheet.getRow(19 + i).getCell(0).setCellValue(chosenList.get(i).getManufacturer()); //производитель
//                 sheet.getRow(19 + i).getCell(1).setCellValue(chosenList.get(i).getCode()); // код
//                 sheet.getRow(19 + i).getCell(2).setCellValue(chosenList.get(i).getName()); // описание
//                 sheet.getRow(19 + i).getCell(5).setCellValue(Double.parseDouble(chosenList.get(i).getPrice())); // цена
//                 if(i >= chosenList.size() - 1)
//                     break;
//                 sheet.shiftRows(20 + i, sheet.getLastRowNum(), 1);
//                 sheet.createRow(20 + i);
//                 CopyRow.CopyRow1(workbook, sheet, 19 + i, 20 + i);
//             }
//
//             sheet.setForceFormulaRecalculation(true);
//
//             file.close();
//
//             FileOutputStream outFile =new FileOutputStream(new File("C:\\Users\\User\\Desktop\\1.xlsx"));
//             workbook.write(outFile);
//             outFile.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }*/

    }

    private static JSONArray getJSONobjectResponse(HttpURLConnection con, int page){
        try {
            StringBuilder sb = new StringBuilder();
            int totalComponentNumber = 0;
            JSONArray components;
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                JSONObject jsonResponse = null;
                try{
                    jsonResponse = new JSONObject(sb.toString());
                }
                catch (JSONException e){
                    System.err.println("Can not create JSONObject. Server returned String in wrong format");
                    //e.printStackTrace();
                }
                try {
                    //totalComponentNumber = (int)jsonResponse.get("totalComponentNumber");
                    if(CachedPages.getTotalComponentNumber() == 0)
                        CachedPages.setTotalComponentNumber((int)jsonResponse.get("totalComponentNumber"));
                    CachedPages.setMap(page, (JSONArray)jsonResponse.get("components"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(jsonResponse);
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


    public void Button_SaveSchedule_Action(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Creation of workitems was scheduled!");

        alert.showAndWait();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));
    }

    public void Button_BackToMainPage_Action(ActionEvent event){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));
    }


    public void Button_Login_Action(ActionEvent event){
        try {
            if(TextFieldLogin.getText().trim().equals("dzianis.bredneu@sk.ibm.com")) { // dzianis.bredneu@sk.ibm.com
                Thread.sleep(4000);
                Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
                primaryStage.setMinHeight(480);
                primaryStage.setMinWidth(750);
                primaryStage.setResizable(true);
                primaryStage.setScene(new Scene(root, 750, 480));
                Controller.init(root, primaryStage);
            }
            else {
                Thread.sleep(4000);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unable to log in to server");
                alert.setContentText("Invalid login or password!");

                alert.showAndWait();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
