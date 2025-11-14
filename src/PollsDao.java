import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PollsDao {
     private  final Connection connection;
     public PollsDao(Connection connection){
         this.connection=connection;

     }
     public  void addPoll(Poll poll) throws SQLException {
            String query="insert into polls(creator_id,topic,status,ex_date) values(?,?,?,?)";
            try(PreparedStatement ps=connection.prepareStatement(query)){
                ps.setInt(1,new UsersDao(connection).getUserId(poll.getCreator()));
                ps.setString(2,poll.getTopic());
                ps.setString(3,poll.getStatus());
                ps.setTimestamp(4, Timestamp.valueOf(poll.getExpiryDateTime()));
                ps.executeUpdate();
            }
     }
     public Poll getPollById(int id) throws SQLException{
         String query="select * from polls where id=?";
         try (PreparedStatement ps= connection.prepareStatement(query)){
             ps.setInt(1,id);
             ResultSet rs=ps.executeQuery();
             if(rs.next())
                 return new Poll(rs.getString("topic"),new UsersDao(connection).findUserById(rs.getInt("creator_id"))
                         ,rs.getString("status"),
                         Duration.between(LocalDateTime.now(),rs.getTimestamp("ex_date").toLocalDateTime())
                         );

         }
          return null;

     }



     public  int getPollId(Poll poll) throws SQLException{
         String query="select * from polls where topic=?";
         try (PreparedStatement ps= connection.prepareStatement(query)){
             ps.setString(1,poll.getTopic());
             ResultSet rs=ps.executeQuery();
             if(rs.next())
                 return rs.getInt("id");

         }
         return 0;
     }

     public void removePoll(Poll poll) throws SQLException{
         String query="delete from polls where topic=?";
         try (PreparedStatement ps=connection.prepareStatement(query)){
             ps.setString(1,poll.getTopic());
             ps.executeUpdate();

         }

     }
     public List<Poll> getActivePolls() throws SQLException{
         return  getAllPolls().stream().filter(poll -> poll.getStatus().equals("ACTIVE")).toList();
     }
     public  List<Poll> getClosedPolls() throws SQLException{
         return getAllPolls().stream().filter(poll -> poll.getStatus().equals("EXPIRED")).toList();
     }
     public List<Poll> getAllPolls() throws SQLException{
         List<Poll> pollList=new ArrayList<>();
         String query="select * from polls ";
         try(PreparedStatement ps= connection.prepareStatement(query)){
             ResultSet rs= ps.executeQuery();
             while (rs.next())
                 pollList.add(new Poll(rs.getString("topic"),
                          new UsersDao(connection).findUserById(rs.getInt("creator_id")),rs.getString("status"),
                                  Duration.between(LocalDateTime.now(),rs.getTimestamp("ex_date").toLocalDateTime())));
         }
        return pollList;

     }

     public List<Poll> getUserCreatedPolls(User user) throws SQLException{
          List<Poll> pollList=new ArrayList<>();
         String query="select * from polls where creator_id=? ";
         try(PreparedStatement ps= connection.prepareStatement(query)){
             ps.setInt(1,new UsersDao(connection).getUserId(user));
             ResultSet rs= ps.executeQuery();
             while (rs.next())
                 pollList.add(new Poll(rs.getString("topic"),
                         new UsersDao(connection).findUserById(rs.getInt("creator_id")),rs.getString("status"),
                         Duration.between(LocalDateTime.now(),rs.getTimestamp("ex_date").toLocalDateTime())));
         }
         return pollList;


     }
      public void setPollStatus(Poll poll) throws SQLException{
          String query="update polls set status='EXPIRED' where id=?";
          try (PreparedStatement ps=connection.prepareStatement(query)){
              ps.setInt(1,new PollsDao(connection).getPollId(poll));
              ps.executeUpdate();

          }



      }








}
