import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         Menu menu=new Menu();
         menu.menu();


    }
}
class Menu{
    private final Scanner scanner=new Scanner(System.in);
    private final  Manager manager=new Manager();
    public void menu() {
        User user = null;
        System.out.println("""
                welcome to Opinion Hub!
                   1.sign in
                   2.log in
                   3.exit
                """);
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            user = manager.registerUser();
        } else if (choice == 2) {
            String name = manager.getUserName();
            String password = manager.getPassword();
            manager.isValidUser(name, password);
            user = manager.findUserByName(name);
        } else {
            return;
        }

        System.out.println("Welcome " + user.getUsername());
        while (true) {
            System.out.println("""
                    what would you like to do:
                           1.create a poll
                           2.vote on active polls
                           3.remove a poll(admin only)
                           4.view all poles
                           5.exit
                    
                    
                    """);

            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    manager.createPoll(user);
                    break;
                case 2:
                    manager.vote(user);
                    break;
                case 3:
                    manager.closePoll(user);
                    break;
                case 4:
                    manager.viewAllPolls();
                    break;
                case 5:
                    return;

            }


        }
    }



}






class User implements Serializable{
     private String Username;
     private String password;
     private List<Poll> userCreatedPolls;
     public User(String username,String password){
         this.password=password; this.Username=username;
         userCreatedPolls=new ArrayList<>();
     }

    public List<Poll> getUserCreatedPolls() {
        return userCreatedPolls;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}





class Poll  implements Serializable{
    private  final String topic;
    private  String status;
    private final  HashMap<String, Integer> votePerChoice;
    private  final List<User> voters;

    public Poll(String topic) {
        status="active";                     //poll is active when created
        this.topic = topic;
        voters=new ArrayList<>();
        votePerChoice = new HashMap<>();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getTopic() {
        return topic;
    }

    public List<User> getVoters() {

        return voters;
    }

    public HashMap<String, Integer> getVotePerChoice() {
        return votePerChoice;
    }
}






class Manager {

    private  List<User> usersList;
    private  List<Poll> polls;

    private static final Scanner scanner = new Scanner(System.in);


    public Manager() {
         importUsers();
        importPolls();

    }
    //helper methods

    public void importUsers(){
          File file=new File("src/users.ser");
          if(!file.exists()||file.length()==0){
              usersList=new ArrayList<>();
              return;
          }
           try{
               ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream(file)) ;
               usersList=(List<User>)inputStream.readObject();
               inputStream.close();
           }catch(IOException e){
                System.exit(1);
        }
           catch (ClassNotFoundException e){
               System.exit(3);
           }
    }

    public void exportUsers(){
        File file=new File("src/users.ser");

        try{
            ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(usersList);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  void  importPolls(){

        File file=new File("src/polls.ser");
        if(!file.exists()||file.length()==0){
            polls=new ArrayList<>();
            return;
        }
        try{
            ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream(file)) ;
            polls=(List<Poll>)inputStream.readObject();
            inputStream.close();
        }catch(IOException e){
            System.exit(1);
        }
        catch (ClassNotFoundException e){
            System.exit(3);
        }
    }

    public  void exportPolls(){
          File file=new File("src/polls.ser");

            try{
                ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(polls);
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }




    public String getUserName() {
        System.out.println("Enter user name : ");
        return scanner.nextLine();

    }

    public String getPassword() {
        System.out.println("Enter password: ");
        return scanner.nextLine();
    }

    public boolean isUserNameTaken(String name) {
        importUsers();
        for (User user : usersList) {
            if (user.getUsername().equals(name))
                return true;
        }
        return false;

    }

    public User findUserByName(String name) {
          importUsers();
        for (User user : usersList) {
            if (user.getUsername().equals(name))
                return user;
        }
        return null;
    }

    public boolean isValidUser(String name, String password) {
         importUsers();
        for (User user : usersList) {
            if (user.getPassword().equals(password) && user.getUsername().equals(name))
                return true;
        }
        return false;
    }

    public List<Poll> getActivePolls() {
        importPolls();
        List<Poll> activePolls = new ArrayList<>();
        for (Poll poll : polls) {
            if (poll.getStatus().equals("active"))
                activePolls.add(poll);
        }

        return activePolls;
    }

    public void viewAllPolls(){
        importPolls();
        viewPolls(polls);
    }



    public void viewPolls(List<Poll> pollList) {

        if (pollList.isEmpty())
            return;
        for (int i = 0; i < pollList.size(); i++) {
            System.out.println((i + 1) + " " + pollList.get(i).getTopic() + "(" + pollList.get(i).getVoters().size() + " participants)" +
                    "    Status= " + pollList.get(i).getStatus());
        }

    }

    public void viewChoices(Poll poll) {
        if (poll.getVotePerChoice().isEmpty())
            return;
        System.out.println("     "+poll.getVoters().size()+" votes");
        int i = 0;
        for (String key : poll.getVotePerChoice().keySet()) {
            if(poll.getVoters().isEmpty()){
                System.out.println((++i) + " " + key + "( 0 votes ) " );
            }
            else {
                System.out.println((++i) + " " + key + "(" + ((double) poll.getVotePerChoice().get(key) / poll.getVoters().size()) * 100 + "% )");
            }
        }
    }

    public Poll pickPoll(String message, List<Poll> polls1) {
        System.out.println(message);

        int choice = Integer.parseInt(scanner.nextLine());

        return polls1.get(choice - 1);

    }

    public String pickOption(String message, Poll poll) {
        viewChoices(poll);
        System.out.println(message);
        int choice = Integer.parseInt(scanner.nextLine());
        String key = null;
        int i = 0;
        for (String key1 : poll.getVotePerChoice().keySet()) {
            if (i++ == choice - 1) {
                key = key1;
                break;
            }

        }
        return key;
    }







//main functionalities

    public User registerUser() {
                                     //get valid username
        String name = getUserName();
        while (isUserNameTaken(name) || name.length() < 6) {
            if (name.length() < 6)
                System.out.println("user name length should be greater than 6 characters,try another.");
            else
                System.out.println("user name is taken,try another.");
            name = getUserName();
        }
                          //get valid password
        String password ;
        int digitCount;
        do {
            password = getPassword();
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
       usersList.add(user);
       exportUsers();
      System.out.println("you have successfully registered as "+user.getUsername()+" and your password is "+user.getPassword());
      return  user;
}


public void createPoll(User user){

            if(user==null){
                return;
            }


             System.out.println("Enter the topic for the poll: ");
            String topic =scanner.nextLine();
            Poll poll=new Poll(topic);
            polls.add(poll);
            user.getUserCreatedPolls().add(poll);



            System.out.println("enter  choices one by one and enter 1 when you finish: ");
            int i=1;
            while (true){
                System.out.print(i+" ");
                String option=scanner.nextLine();
                if(option.trim().equals("1"))
                    break;
                if (poll.getVotePerChoice().containsKey(option)) {
                    System.out.println("choice already present.");
                  }
                else{
                    poll.getVotePerChoice().put(option,0);
                    i++;
                  }
            }
            System.out.println("poll successfully created.\n"+"     "+topic);
            exportPolls();
            exportUsers();        //b/c user userCreatedPoll list  is updated
           viewChoices(poll);
  }



   public void   vote(User voter){

          if(voter==null){
              return;
          }
          List<Poll> activePolls=getActivePolls();

          if(activePolls.isEmpty()){
              System.out.println("There are no active polls for now. ");
              return;
          }
          System.out.println("here are a list of currently active polls: ");
          viewPolls(getActivePolls());

       Poll poll=pickPoll("enter the number of poll you want to vote : ", activePolls);
       for(User user:poll.getVoters()){
           if(user.getUsername().equals(voter.getUsername())){
               System.out.println("you already voted.");
               return;
           }

       }

       String key=pickOption("enter the number written before the  choice you want to vote: ",poll);

      poll.getVotePerChoice().put(key,poll.getVotePerChoice().get(key)+1);
      poll.getVoters().add(voter);
      exportPolls();
      System.out.println("☑ "+key+"\n");
      viewChoices(poll);

   }

    public void closePoll(User admin){

         if(admin==null)
             return;

         viewPolls(getActivePolls());
         Poll poll=pickPoll("choose poll you want to remove(only if you are admin of the poll): ",getActivePolls());
         if(poll==null){
             System.out.println("poll not found.");
             return;
         }
         if(!admin.getUserCreatedPolls().contains(poll)){
               System.out.println("you are not admin for the poll.");
               return;
         }
          poll.setStatus("inactive");
          exportPolls();
          exportUsers();   // b/c user userCreatedPolls  list is changed
          System.out.println(" poll successfully closed.");
          showResult(poll);

    }

   public  void showResult(Poll poll){
        if(poll.getVotePerChoice().isEmpty()){
            return;                   //poll closed without vote have no result .
        }
        viewChoices(poll);
       int  winnerVotes=0;
       String winnerOpinion="null";
        for(String key:poll.getVotePerChoice().keySet()){
            if(poll.getVotePerChoice().get(key)>winnerVotes){
                winnerVotes=poll.getVotePerChoice().get(key);
                winnerOpinion=key;
            }
        }

        System.out.println("\n\n       most popular opinion:   \" "+winnerOpinion+"\"   with "+winnerVotes +" votes.\n");



   }













}




