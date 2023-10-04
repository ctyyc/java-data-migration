import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InsertJson {

    public static void main(String[] args) throws ParseException {

        java.sql.Statement stmt = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String filePath = "C:\\{dir-name}\\{file-name}.json";

        try {
            String driverClassName = "org.mariadb.jdbc.Driver";
            String url = "jdbc:mariadb://{host}:{port}/{db-name}";
            String user	= "";
            String password = "";

            //JDBC Driver Loading
            Class.forName(driverClassName);

            //JDBC Connection getting
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("== Driver : " + driverClassName + ", Connection : " + conn);

            FileInputStream input = new FileInputStream(filePath);
            InputStreamReader in = new InputStreamReader(input, "UTF-8");
            BufferedReader reader = new BufferedReader(in);

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(reader);
            JSONArray jsonArr = (JSONArray) obj;

            String SQL = "INSERT INTO db.table\r\n"
                    + "(col1, col2, col3, col4, col5)\r\n"
                    + "VALUES(?, ?, ?, ?, ?);\r\n"
                    + "";

            pstmt = conn.prepareStatement(SQL);

            if(jsonArr.size() > 0) {
                for(int i=0; i<jsonArr.size(); i++) {
                    JSONObject jsonObj = (JSONObject) jsonArr.get(i);

                    pstmt.setLong(1,(Long) jsonObj.get("col1"));
                    pstmt.setLong(2,(Long) jsonObj.get("col2"));
                    pstmt.setString(3,(String) jsonObj.get("col3"));
                    pstmt.setString(4,(String) jsonObj.get("col4"));
                    pstmt.setString(5,(String) jsonObj.get("col5"));

                    // System.out.println("== execute SQL : " + i);
                    pstmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                }catch(SQLException se) {
                    se.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                }catch(SQLException se) {
                    se.printStackTrace();
                }
            }
        }

    }

}
