package sample;

import com.sun.javafx.fxml.builder.URLBuilder;
import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xml.internal.security.keys.keyresolver.implementations.PrivateKeyResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        TableView_Chosen = (TableView) anchorPane_chosen.getChildren().get(0);
        Button_AddToExcel = (Button) anchorPane_chosen.getChildren().get(1);

        TableView_Chosen.setItems(chosenList);

        ((TableColumn)TableView_Chosen.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("code"));

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
        System.out.println("ready");
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

        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", new FileBody(file));

        HttpPost request = new HttpPost("http://localhost:8080/component");
        request.setEntity(entity);

        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(request);

            if(response.getStatusLine().toString().contains("404")){
                //entity.consumeContent();
                //there is no mapping
                MultipartEntity entity1 = new MultipartEntity();
                entity1.addPart("name", new StringBody("Description 1+Description 2+Description 3"));
                entity1.addPart("manufacturer", new StringBody("\\\"XAL\\\""));
                entity1.addPart("price", new StringBody("EUR"));
                entity1.addPart("code", new StringBody("Code"));

                HttpPost request1 = new HttpPost("http://localhost:8080/mapping");
                request1.setEntity(entity1);

                HttpClient client1 = new DefaultHttpClient();

                try {
                    HttpResponse response1 = client1.execute(request1);
                    System.out.println(response1.getStatusLine());
                }  catch (IOException e) {
                    e.printStackTrace();
                }

                /*URL object = null;
                HttpURLConnection con = null;
                System.out.println("new mapping");
                try {
                    String stringURL = null;
                    try {
                        stringURL = "http://localhost:8080/mapping?" +
                                "name=" + URLEncoder.encode("Description 1+Description 2+Description 3", "UTF-8") +
                                "&manufacturer=" + URLEncoder.encode("\"XAL\"", "UTF-8") +
                                "&price=" + URLEncoder.encode("EUR", "UTF-8") +
                                "&code=" + URLEncoder.encode("Code", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    object = new URL(stringURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    con = (HttpURLConnection) object.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Accept", "application/json");
                } catch (IOException e) {
                    System.err.println("Can not open connection. " + e);
                }
                System.out.println("Con resp code: " + con.getResponseCode());*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Button_AddToExcel_Action(ActionEvent event){
         try {

             FileInputStream file = new FileInputStream(new File("C:\\Users\\User\\Desktop\\3.xlsx"));
             //FileInputStream file = new FileInputStream(new File("C:\\test.xlsx"));
             System.out.println(file);
             XSSFWorkbook workbook = new XSSFWorkbook (file);
             XSSFSheet sheet = workbook.getSheetAt(0);

             for (int i = 0; i < chosenList.size(); i++){
                 sheet.getRow(19 + i).getCell(0).setCellValue(chosenList.get(i).getManufacturer()); //производитель
                 sheet.getRow(19 + i).getCell(1).setCellValue(chosenList.get(i).getCode()); // код
                 sheet.getRow(19 + i).getCell(2).setCellValue(chosenList.get(i).getName()); // описание
                 sheet.getRow(19 + i).getCell(5).setCellValue(Double.parseDouble(chosenList.get(i).getPrice())); // цена
                 if(i >= chosenList.size() - 1)
                     break;
                 sheet.shiftRows(20 + i, sheet.getLastRowNum(), 1);
                 sheet.createRow(20 + i);
                 CopyRow.CopyRow1(workbook, sheet, 19 + i, 20 + i);
             }

             sheet.setForceFormulaRecalculation(true);

             file.close();

             FileOutputStream outFile =new FileOutputStream(new File("C:\\Users\\User\\Desktop\\1.xlsx"));
             workbook.write(outFile);
             outFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

}
