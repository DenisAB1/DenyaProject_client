package sample;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by User on 19.03.2017.
 */
public class Row {
    private String code;
    private String manufacturer;
    private String name;
    private String price;

    private static ArrayList<String> whoList = new ArrayList<String>() {{
        add("Sergio Fontana");/*2*/ add("igaism@us.ibm.com");/*3*/ add("EMEA CENTRAL RCMS BTIT SUPPORT");/*4*/add("EMEA CENTRAL RCMS BTIT SUPPORT");/*5*/
        add("Italy ACM Support");/*6*/add("EMEA CENTRAL RCMS BTIT SUPPORT");/*7*/add("Norbert Michael Sommer");/*8*/add("Italy ACM Support");/*9*/
        add("Dzimitry Nazarchuk");/*10*/add("NR ACM ADM Technical Support");/*11*/add("Italy ACM Support");/*12*/add("Norbert Michael Sommer");/*13*/
    }};;
    private static ArrayList<String> subjectList = new ArrayList<String>() {{
        add("IN9406971 - RCMS Central Production + UAT - PROG755");/*2*/ add("IN9402672 - RCMS Central production - extra FL for SUI and AUT");/*3*/ add("Re: iERP project for Austria and Switzerland - ready to move to production for RCMS");/*4*/add("IGA SCCD IN9402672: Fw: iERP project for Austria and Switzerland - ready to move to production for RCMS");/*5*/
        add("PB750318-Data Privacy level not updated during OOS change by UPDV transaction");/*6*/add("Fw: iERP project for Austria and Switzerland - ready to move to production for RCMS");/*7*/add("Re: Fw: iERP project for Austria and Switzerland - ready to move to production for RCMS");/*8*/add("Re: PB750317 - Call Read does not work with ESN number");/*9*/
        add("Fw: Can you help with who i need to ask to have BMT capture a data tag in RCMS for LEnovo aging calls casue message?");/*10*/add("Re: Fw: Can you help with who i need to ask to have BMT capture a data tag in RCMS for LEnovo aging calls casue message?");/*11*/add("PB750317 - Call Read does not work with ESN number");/*12*/add("Fw: iERP project for Austria and Switzerland - ready to move to production for RCMS");/*13*/
    }};;
    private static ArrayList<String> dateList = new ArrayList<String>() {{
        add("19.05.2017");/*2*/ add("18.05.2017");/*3*/ add("18.05.2017");/*4*/add("18.05.2017");/*5*/
        add("18.05.2017");/*6*/add("18.05.2017");/*7*/add("18.05.2017");/*8*/add("18.05.2017");/*9*/
        add("18.05.2017");/*10*/add("18.05.2017");/*11*/add("18.05.2017");/*12*/add("18.05.2017");/*13*/
    }};;
    private static ArrayList<String> timeList = new ArrayList<String>() {{
        add("9:19:00");/*2*/ add("9:27:00");/*3*/ add("9:21:00");/*4*/add("9:29:00");/*5*/
        add("10:30:00");/*6*/add("18:30:00");/*7*/add("18:35:00");/*8*/add("15:45:00");/*9*/
        add("13:53:00");/*10*/add("14:46:00");/*11*/add("11:37:00");/*12*/add("11:08:00");/*13*/
    }};;

    public Row(JSONObject jsonObject) {
        try {
            this.code = (String) jsonObject.get("code");
            this.manufacturer = (String) jsonObject.get("manufacturer");
            this.name = (String) jsonObject.get("name");
            this.price = (String.valueOf(jsonObject.get("price")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Row(int i){
        this.code = whoList.get(i);
        this.manufacturer = subjectList.get(i);
        this.name = dateList.get(i);
        this.price = timeList.get(i);
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
