package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Controller {
    private static Button Button_GetRow;

    public void Button_GetRow_Action(ActionEvent event){
        URL object = null;
        HttpURLConnection con = null;

        try {
            object = new URL("http://localhost:8080/component/110");
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
        if(jsonArray != null){
            for (int i = 0; i < jsonArray.length(); i++){
                try {
                    System.out.println(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
