package sample;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Created by User on 20.05.2017.
 */
public class FileWorker {
    private static File file = null;
    public static File openFile(Stage primaryStage){
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
        return file;
    }
    public static int getSizeOfFile(){
        /*XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);
        int n = header.getLastCellNum();
        String header1 = header.getCell(0).getStringCellValue();
        String header2 = header.getCell(1).getStringCellValue();
        String header3 = header.getCell(2).getStringCellValue();
        String header4 = header.getCell(3).getStringCellValue();
        if (header1.equals("Who") && header2.equals("Date")
                && header3.equals("TIme") && header4.equals("Subject")) {
            if(sheet.getLastRowNum()<1){
                //return 0;
            }
        }else{
            SOP("invalid format");
            //return sheet.getLastRowNum();
        }*/
        return 12;
    }
}
