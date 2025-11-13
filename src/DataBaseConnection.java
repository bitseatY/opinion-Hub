import java.sql.*;


public class DataBaseConnection {
     private    String url;
      private   String password;
     private  String username;
     public DataBaseConnection( String url,String password,String username){
         this.url=url;
         this.password=password;
         this.username=username;
     }


    public Connection getConnection() {
        try {
            return     DriverManager.getConnection(url,username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }






}
