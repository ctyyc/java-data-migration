import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InsertExcel {

    private Connection conn = null;

    public InsertExcel() {}

    public boolean getConnection(String url, String user, String pwd) {
        try {
            this.conn = DriverManager.getConnection(url, user, pwd);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String executeSql(String... str) {

        String sql = "INSERT INTO db.table\r\n"
                + "(col1, col2, col3, col4, col5)\r\n"
                + "VALUES(?, ?, ?, ?, ?);\r\n"
                + "";

        try {
            PreparedStatement psmt = this.conn.prepareStatement(sql);
            psmt.setInt(1, Integer.parseInt(str[0]));
            psmt.setInt(2, Integer.parseInt(str[1]));
            psmt.setInt(3, Integer.parseInt(str[2]));
            psmt.setString(4, str[3]);
            psmt.setString(5, str[4]);

            // System.out.println(psmt + ";");
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringValue(Cell cell) {
        String rtnValue = "";

        try {
            rtnValue = cell.getStringCellValue();
        } catch(IllegalStateException e) {
            rtnValue = Integer.toString((int) cell.getNumericCellValue());
        }

        return rtnValue;
    }

    public static void main(String args[]) throws ClassNotFoundException {

        InsertExcel ie = new InsertExcel();

        FileInputStream fis = null;
        XSSFWorkbook hfwb = null;

        try {
            String user = "";
            String password = "";
            String url = "jdbc:mariadb://{host}:{port}/{db-name}";

            if(ie.getConnection(url, user, password)) {
                fis = new FileInputStream("C:\\{dir-name}\\{file-name}.xlsx");
                hfwb = new XSSFWorkbook(fis);

                XSSFSheet sheet = null;
                XSSFRow row = null;
                XSSFCell cell = null;

                for(int sheetIndex = 0; sheetIndex < hfwb.getNumberOfSheets(); sheetIndex++) {
                    sheet = hfwb.getSheetAt(sheetIndex);

                    for(int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                        System.out.println("rowIndex : " + rowIndex);
                        row = sheet.getRow(rowIndex);

                        ie.executeSql(
                                getStringValue(row.getCell(0)),
                                getStringValue(row.getCell(1)),
                                getStringValue(row.getCell(2)),
                                getStringValue(row.getCell(3)),
                                getStringValue(row.getCell(4))
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (ie != null && ie.conn != null) {
                    System.out.println("=== Close");
                    ie.conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (hfwb != null) {
                try {
                    hfwb.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
