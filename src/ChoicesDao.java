import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoicesDao {
    private final Connection connection;
    public ChoicesDao(Connection connection){
        this.connection=connection;

    }
    public void addChoice(Poll poll,Choice choice) throws SQLException {
        String query = "insert into choices(poll_id,choice,total_votes) values(?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, new PollsDao(connection).getPollId(poll));
            ps.setString(2, choice.getChoice());
            ps.setInt(3, 0);
            ps.executeUpdate();

        }
    }
   public  Choice getChoiceById( int id) throws SQLException {
       String query = "select * from choices where id=?";
       try (PreparedStatement ps = connection.prepareStatement(query)) {
           ps.setInt(1, id);
           ResultSet rs = ps.executeQuery();
           if (rs.next())
               return new Choice(rs.getString("choice"), rs.getInt("total_votes"));
       }
       System.out.println("nulllll");

       return null;
   }

    public int getId(Choice choice) throws SQLException{
        String query="select * from choices where choice=?";
        try (PreparedStatement ps=connection.prepareStatement(query)){
             ps.setString(1,choice.getChoice());
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                return  rs.getInt("id");
        }
        return 0;

    }




    public void updateNumberOFVotes(Choice choice) throws SQLException{
        String query="update choices set total_votes=? where id=?";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ps.setInt(1,choice.getTotalVoters()+1);
            ps.setInt(2,new ChoicesDao(connection).getId(choice));
            ps.executeUpdate();
        }



    }

     public List<Choice> getChoicesForPoll(Poll poll) throws SQLException{
         List<Choice> choices=new ArrayList<>();
         String query="select * from choices where poll_id=?";
         try (PreparedStatement ps= connection.prepareStatement(query)){
             ps.setInt(1, new PollsDao(connection).getPollId(poll));
             ResultSet rs= ps.executeQuery();
             while (rs.next())
                 choices.add(new Choice(rs.getString("choice"),rs.getInt("total_votes")));

         }
          return choices;


     }

     public int getTotalVotesForPoll(Poll poll) throws SQLException{
         String query="select sum(total_votes) as sum from choices where poll_id=?";
         try (PreparedStatement ps= connection.prepareStatement(query)){
             ps.setInt(1,new PollsDao(connection).getPollId(poll));
             ResultSet rs= ps.executeQuery();
             if (rs.next())
                 return rs.getInt("sum");


         }
            return 0;

     }

     public boolean isChoicePresent(String choice) throws SQLException{
         String query="select * from  choices where choice=?";
         try (PreparedStatement ps= connection.prepareStatement(query)) {
             ps.setString(1, choice);
             ResultSet rs = ps.executeQuery();
             return rs.next();
         }
     }

    public Choice getWinnerOpinionTotalVotes(Poll poll) throws SQLException{
        String query="select total_votes,choice  from choices   where  poll_id =? order by total_votes desc";
        try (PreparedStatement ps= connection.prepareStatement(query)){
            ps.setInt(1,new PollsDao(connection).getPollId(poll));
            ResultSet rs= ps.executeQuery();
            if(rs.next())
                return new Choice(rs.getString("choice"),rs.getInt("total_votes"));

        }


        return null;

    }





}