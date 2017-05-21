package sample;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 19.03.2017.
 */
public class DataRow {
    private String code;
    private String manufacturer;
    private String name;
    private String price;
    private String who;
    private String date;
    private String time;
    private String subject;

    public DataRow(String who, String date, String time, String subject) {

    }
        public DataRow(JSONObject jsonObject) {
            try {
                this.code = (String) jsonObject.get("code");
                this.manufacturer = (String) jsonObject.get("manufacturer");
                this.name = (String) jsonObject.get("name");
                this.price = (String.valueOf(jsonObject.get("price")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
