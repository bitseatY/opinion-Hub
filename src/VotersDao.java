import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VotersDao {
    private final Connection connection;
    public VotersDao(Connection connection){
        this.connection=connection;
    }



    public void recordVote(Poll poll, User user) throws SQLException {
         String query="insert into  voters values (?,?)";
         try(PreparedStatement ps=connection.prepareStatement(query)){
             ps.setInt(1,new PollsDao(connection).getPollId(poll) );
             ps.setInt(2,new UsersDao(connection).getUserId(user));
            ps.executeUpdate();
         }
    }

    public List<Poll> getUserVotedPolls(User user) throws SQLException{
        List<Poll> polls=new ArrayList<>();
        String query="select * from voters where user_id=?";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ps.setInt(1,new UsersDao(connection).getUserId(user));
            ResultSet rs= ps.executeQuery();
            while (rs.next())
                polls.add(new PollsDao(connection).getPollById(rs.getInt("poll_id")));
        }
      return  polls;

    }
    public boolean hasVoted(User user, Poll poll) throws SQLException{
        String query="select *  from  voters where poll_id=? and user_id=?";
        try(PreparedStatement ps=connection.prepareStatement(query)){
            ps.setInt(1,new PollsDao(connection).getPollId(poll) );
            ps.setInt(2,new UsersDao(connection).getUserId(user));
           ResultSet rs= ps.executeQuery();
            return rs.next();
        }



    }
    public  List<Poll> getTop5MostVotedPolls() throws SQLException{
        List<Poll> polls=new ArrayList<>();
        String query="select count(user_id),poll_id  from voters group by poll_id   order by count(user_id) desc limit 5 ";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ResultSet rs= ps.executeQuery();
            while (rs.next())
                polls.add(new PollsDao(connection).getPollById(rs.getInt("poll_id")));
        }
        return  polls;
    }

    public  List<User> getTop5MostActiveUsers() throws SQLException{
        List<User> users=new ArrayList<>();
        String query="select count(poll_id),user_id  from voters group by user_id order by count(poll_id) desc limit 5  ";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ResultSet rs= ps.executeQuery();
            while (rs.next())
                users.add(new UsersDao(connection).findUserById(rs.getInt("user_id")));
        }
        return  users;



    }
    public int getTotalVoters(Poll poll) throws SQLException{
        String query="select count(user_id) from   voters where poll_id=?";
        try(PreparedStatement ps=connection.prepareStatement(query)){
            ps.setInt(1,new PollsDao(connection).getPollId(poll) );
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                return rs.getInt("count(user_id)");
        }
         return 0;


    }




}
