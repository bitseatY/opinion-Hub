import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDao {
    private final   Connection  connection;
    public UsersDao(Connection connection){
        this.connection=connection;

    }

   public   void addUser(User user) throws SQLException {
       String query = "insert into users(username,password) values(?,?)";
       try (PreparedStatement ps = connection.prepareStatement(query)) {
           ps.setString(1, user.getUsername());
           ps.setString(2, user.getPassword());
           ps.executeUpdate();
       }
   }
  public  User findUserByName(String name) throws SQLException {
      String query = "select * from users where username=?";
      try (PreparedStatement ps = connection.prepareStatement(query)) {
          ps.setString(1, name);
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
              return new User(rs.getString("username"), rs.getString("password"));
          }

      } return null;
  }
  public int getUserId(User user) throws SQLException{
        String query="select * from users where username=?";
      try (PreparedStatement ps = connection.prepareStatement(query)) {
          ps.setString(1, user.getUsername());
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
              return rs.getInt("id");
          }

      } return 0;


  }
  public  User findUserById(int id) throws SQLException{
        String query="select * from users where id=?";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                return new User(rs.getString("username"),rs.getString("password"));
        }

        return null;


  }







  public   boolean isUsernameAvailable(String name) throws SQLException {
      String query = "select * from users where username=?";
      try (PreparedStatement ps = connection.prepareStatement(query)) {
          ps.setString(1, name);
          ResultSet rs = ps.executeQuery();
          return !rs.next();

      }
  }

  public  boolean isUserValid(String username, String password) throws SQLException{
        String query= "select * from users where username=? and password=?";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs=ps.executeQuery();
            return  rs.next();


        }


  }







}
