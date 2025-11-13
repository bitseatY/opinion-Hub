
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Scanner;
import java.sql.*;

public class Manager {
    private final  DataBaseConnection dataBaseConnection;
    private  final PollsDao pollsDao;
    private final UsersDao usersDao;
    private final ChoicesDao choicesDao;
    private final VotersDao votersDao;

    private static final Scanner scanner = new Scanner(System.in);


    public Manager(DataBaseConnection dataBaseConnection)  {
        this.dataBaseConnection=dataBaseConnection;
        pollsDao =new PollsDao(dataBaseConnection.getConnection());
        usersDao=new UsersDao(dataBaseConnection.getConnection());
        choicesDao=new ChoicesDao(dataBaseConnection.getConnection());
        votersDao=new VotersDao(dataBaseConnection.getConnection());

    }
    //helper methods

    public String getInfo(String message){
        System.out.println(message);
        return scanner.nextLine();
    }

    public boolean isUserNameAvailable(String name)  throws SQLException{    //username is unique for every user
         return usersDao.isUsernameAvailable(name);
    }

    public User findUserByName(String name) throws SQLException {
        return usersDao.findUserByName(name);
    }

    public boolean isValid(String name,String password) throws SQLException{   // password must match user username
        return usersDao.isUserValid(name,password);
    }



    public List<Poll> getClosedPolls() throws SQLException{   //only closed polls have result ,active polls are still in progress
        return pollsDao.getClosedPolls();
    }

    public void viewPolls(List<Poll> pollList) throws SQLException {

        if (pollList==null||pollList.isEmpty())
            return;
        for (int i = 0; i < pollList.size(); i++) {
            Poll poll=pollList.get(i);
            System.out.println((i + 1) + " " + poll.getTopic() + "(" + votersDao.getTotalVoters(poll) + " participants)" +
                    "    Status= " + pollList.get(i).getStatus());
        }

    }

    public void viewChoices(Poll poll)  throws SQLException{
             System.out.println(poll.getTopic());
             int totalVotes=(choicesDao.getTotalVotesForPoll(poll)==0?1:choicesDao.getTotalVotesForPoll(poll));
             List<Choice> choices=choicesDao.getChoicesForPoll(poll);
             int i=0;
             for(Choice c:choices){
                   System.out.printf("%d. %s(%.0f%% )%n",++i,c.getChoice(),((double) c.getTotalVoters() / totalVotes) * 100 );

             }
    }



    public int pickPoll(String message, List<Poll> pollslist) {
        System.out.println(message);
        int index=0;
        while (index==0) {
            try {
                index = Integer.parseInt(scanner.nextLine());
                if (index > pollslist.size()  || index  <1){
                    System.out.println("invalid input. try again.");
                   index=0;
                }

            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return index;

    }

    public int  pickOption(String message,Poll poll) throws SQLException {
        viewChoices(poll);
        List<Choice> choices=choicesDao.getChoicesForPoll(poll);
        System.out.println(message);
        int index=0;
        while (index==0) {
            try {
                index = Integer.parseInt(scanner.nextLine());
                if (index > choices.size() || index <1){
                    System.out.println("invalid input. try again.");
                    index=0;
                }

            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return index;


    }

    //main functionalities

    public  void seeProfile(User user) throws SQLException{
        List<Poll> userVotedPolls=votersDao.getUserVotedPolls(user);
        List<Poll> userCreatedPolls=pollsDao.getUserCreatedPolls(user);
         if(userVotedPolls.isEmpty()){
             System.out.println("you haven't voted to any polls yet.");


         }else {
             System.out.println(user.getUsername() + " \nmost recent voted polls : ");

             for (int i = 0; i < userVotedPolls.size()  && i < 10; i++) {
                 Poll poll = userVotedPolls.get(i);
                 System.out.println("        " + poll.getTopic() + "[" + (poll.getStatus().equals("ACTIVE") ? "Active" : "Closed") + "]");

             }
         }
         if(userCreatedPolls.isEmpty()){
             System.out.println("you haven't created any polls yet.");
         }
         else {
             System.out.println( " \nmost recent created  polls : ");
             for (int i = 0; i < userCreatedPolls.size() && i < 10; i++) {
                 Poll poll = userCreatedPolls.get(i);
                 System.out.println("        " + poll.getTopic() + "[" + (poll.getStatus().equals("ACTIVE") ? "Active" : "Closed") + "]");
             }
         }
    }



    public User registerUser()  throws SQLException{
        //get valid username
        String name = getInfo("enter user name: ");
        while (!isUserNameAvailable(name) || name.length() < 6) {
            if (name.length() < 6)
                System.out.println("user name length should be greater than 6 characters,try another.");
            else
                System.out.println("user name is taken,try another.");
            name = getInfo("enter user name: ");
        }

        //get valid password
        String password ;
        int digitCount;
        do {
            password = getInfo("enter password: ");
            digitCount = 0;
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c))
                    digitCount++;
            }
            if (password.length() < 6)
                System.out.println("password should contain at least 6 characters,try again");
            else if(digitCount<3)
                System.out.println("password should contain at least  3 digits.try again ");

        } while (password.length() < 6 || digitCount < 3);

        User user = new User(name,password);
        usersDao.addUser(user);     //userList in the database should be modified as well

        System.out.println("you have successfully registered as "+user.getUsername()+" and your password is "+user.getPassword());
        return  user;
    }

    public synchronized  void createPoll(User user) throws SQLException {

        if (user == null) {
            return;
        }
        String topic ;
        int days ;
        int hours ;
        int minutes ;
        while (true) {
            try {
                System.out.println("Enter the topic for the poll: ");
                topic = scanner.nextLine();
                System.out.println("How long will the poll last? (duration can't be more than 10 days.):  ");
                System.out.println("how many days? (only digit) ");
                days = Integer.parseInt(scanner.nextLine());
                System.out.println(days + "days + how many hours? (only digit) ");
                hours = Integer.parseInt(scanner.nextLine());
                System.out.println(days + " days + " + hours + " hours how many minutes? (only digit) ");
                minutes = Integer.parseInt(scanner.nextLine());
                System.out.println("poll will last  " + days + "days and " + hours + " hours and " + minutes + " minutes.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("in valid answer ,try again.\n");
            }
        }
        Duration duration = Duration.ofDays(days).plusHours(hours).plusMinutes(minutes);

        Poll poll = new Poll(topic, user,"ACTIVE",duration);     //every poll will have a timer that automatically close it when expiry date is reached.
        pollsDao.addPoll(poll);

        new Thread (() -> {
            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            poll.setStatus();
            try {
                pollsDao.setPollStatus(poll);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } ).start();

        System.out.println("enter  choices one by one and enter 1 when you finish: ");
        int i = 1;
        while (true) {
            System.out.print(i + " ");
            String option = scanner.nextLine();
            if (option.trim().equals("1"))
                break;
            if (choicesDao.isChoicePresent(option)) {
                System.out.println("choice already present.");
            }
            else {
                choicesDao.addChoice(poll,new Choice(option,0));
                i++;
            }
        }
        System.out.println("poll  created , expires at " + poll.getExpiryDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss")));
        viewChoices(poll);

    }

    public synchronized  void   vote(User voter) throws SQLException{      // vote is Synchronized b/c  we don't want poll timer thread to interrupt in the middle leading to inconsistent state of a poll
        //but this also prevents multiple users from voting on the same poll at the same time , they have to take turns
        //future fix, apply concurrency concept

        if(voter==null){
            return;
        }
        List<Poll> activePolls=pollsDao.getActivePolls();

        if(activePolls.isEmpty()){
            System.out.println("There are no active polls for now. ");
            return;
        }

        System.out.println("here are a list of currently active polls: ");
        viewPolls(activePolls);
        Poll poll=activePolls.get(pickPoll("enter the number of poll you want to vote : ", activePolls)-1);

        if(votersDao.hasVoted(voter,poll)){
            System.out.println("you already voted.");
            return;

        }
        List<Choice> choices=choicesDao.getChoicesForPoll(poll);
        int index=pickOption("enter the number written before the  choice you want to vote: ",poll);
        Choice choice=choices.get(index-1);

        if(LocalDateTime.now().isAfter(poll.getExpiryDateTime())){  // we have to make sure poll expiry time hasn't passed before recording the vote
            poll.setStatus(); //change poll status to expired
            pollsDao.setPollStatus(poll);
            System.out.println("poll has expired.");
            return;
        }
        votersDao.recordVote(poll,voter);
        choicesDao.updateNumberOFVotes(choice);
        choice.increment();

        System.out.println("☑ "+choice.getChoice()+"\n");
        viewChoices(poll);

    }

    public  synchronized  void closePoll(User admin) throws SQLException{// same issue as vote method

        if(admin==null)
            return;
        List<Poll> pollList=pollsDao.getUserCreatedPolls(admin);

        if(pollList.isEmpty()){
            System.out.println("no active poll present to close. ");
              return;
        }
        viewPolls(pollList);

        Poll poll=pollList.get(pickPoll("choose poll you want to remove ",pollList)-1);

        pollsDao.setPollStatus(poll);
        poll.setStatus();
        System.out.println("poll is closed. showing result of the poll...");
        showResult(poll);
    }


    public List<Poll>  top5MostVotedActivePolls() throws SQLException{  //uses insertion sort for ordering polls in decreasing order in the array
        return votersDao.getTop5MostVotedPolls();
    }

     public List<User>  top5MostActiveUsers() throws SQLException{
         return votersDao.getTop5MostActiveUsers();
    }

    public void Dashboard() throws SQLException{
        System.out.println("\nwhat is trending on Opinion Hub?    \n");
        List<Poll> pollList=top5MostVotedActivePolls();
        if(pollList!=null&&!pollList.contains(null)){

            System.out.println("\nhere are the top 5 most voted active  polls on Opinion Hub right now , vote for your opinion now. \n");
            viewPolls(pollList);
        }
        List<User> usersList1=top5MostActiveUsers();
        if(usersList1!=null&&!usersList1.contains(null)){

            System.out.println("\nhere are the top 5 most active users  on Opinion Hub right now, you can be one of them.\n ");
            int i=1;
            for(User user:usersList1){
                System.out.println((i++)+". "+user.getUsername()+" with "+votersDao.getUserVotedPolls(user).size()+" votes.");
            }
        }
    }

    public void seeResultOfClosedPolls() throws SQLException{ //choosing is much easier than searching by topic plus user may input wrong topic
        List<Poll> pollList=getClosedPolls();
        if(pollList.isEmpty()){
            System.out.println("no closed polls yet.");
            return;
        }
        System.out.println("Here are a list of closed polls: ");
        viewPolls(pollList);
        Poll poll=pollList.get(pickPoll("pick a poll to view result: ",pollList)-1);
        showResult(poll);

    }

    public  void showResult(Poll poll) throws SQLException{
        Choice choice=choicesDao.getWinnerOpinionTotalVotes(poll);
        if(choice==null){
            return;                   //poll closed without vote have no result .
        }
        viewChoices(poll);

        System.out.println("\n\n       most popular opinion: \" "+choice.getChoice()+"\"  with "+choice.getTotalVoters() +" votes.\n");
    }
}
