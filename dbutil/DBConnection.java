package evoting.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;
    static{
            try
            {
                Class.forName("oracle.jdbc.OracleDriver");
                conn=DriverManager.getConnection("jdbc:oracle:thin:@//DESKTOP-V0BN5VQ:1521/xe","evoting","evoting");
                System.out.println("Driver loaded and connection opened successfully");
            }
            catch(ClassNotFoundException cnf){
                cnf.printStackTrace();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        public static Connection getConnection(){
            return conn;
        }
        public static void closeConnection(){
            try{
                conn.close();
                System.out.println("Disconnected From DB");
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
}

