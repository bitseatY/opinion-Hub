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
                    manager.createPoll();
                    break;
                case 2:
                    manager.vote();
                    break;
                case 3:
                    manager.removePoll();
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






class User{
     private String Username;
     private String password;
     public User(String username,String password){
         this.password=password; this.Username=username;
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





class Poll {
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

    private final List<User> usersList;
    private final List<Poll> polls;
    private final HashMap<User, Poll> adminPerPoll;
    private static final Scanner scanner = new Scanner(System.in);


    public Manager() {
        usersList = new ArrayList<>();
        polls = new ArrayList<>();
        adminPerPoll = new HashMap<>();
    }
    //helper methods

    public String getUserName() {
        System.out.println("Enter user name : ");
        return scanner.nextLine();

    }

    public String getPassword() {
        System.out.println("Enter password: ");
        return scanner.nextLine();
    }

    public boolean isUserNameTaken(String name) {

        for (User user : usersList) {
            if (user.getUsername().equals(name))
                return true;
        }
        return false;

    }

    public User findUserByName(String name) {

        for (User user : usersList) {
            if (user.getUsername().equals(name))
                return user;
        }
        return null;
    }

    public boolean isValidUser(String name, String password) {

        for (User user : usersList) {
            if (user.getPassword().equals(password) && user.getUsername().equals(name))
                return true;
        }
        return false;
    }

    public List<Poll> getActivePolls() {
        List<Poll> activePolls = new ArrayList<>();
        for (Poll poll : polls) {
            if (poll.getStatus().equals("active"))
                activePolls.add(poll);
        }

        return activePolls;
    }

    public void viewAllPolls(){
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
        int i = 0;
        for (String key : poll.getVotePerChoice().keySet()) {

            System.out.println((++i) + " " + key + "(" + ((double) poll.getVotePerChoice().get(key) / poll.getVoters().size()) * 100 + "% )");
        }
    }

    public Poll pickPoll(String message, List<Poll> polls) {
        System.out.println(message);

        int choice = Integer.parseInt(scanner.nextLine());

        return polls.get(choice - 1);

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

    public User getUser(){
        String name;             //only users can create poll
        String password;
        User user=null;
        int choice=1;
        do {
            name = getUserName();
            password = getPassword();
            if (isValidUser(name, password)) {
                break;
            }
            else{
                System.out.println("user not found,would you like to\n1 try again\n2 register as a new user\n3 return to menu");
                choice=Integer.parseInt(scanner.nextLine());

            }
        }while (choice==1);
        if(choice==2){

            return registerUser();
        }
        if(choice==3)
            return null;

        else{
            return findUserByName(name);
        }

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
            else
                System.out.println("password should contain at least  3 digits.try again ");

        } while (password.length() < 6 || digitCount < 3);

        User user = new User(name,password);
       usersList.add(user);
      System.out.println("you have successfully registered as "+user.getUsername()+" and your password is "+user.getPassword());
      return  user;
}


public void createPoll(){
            User user=getUser();
            if(user==null){
                return;
            }


             System.out.println("Enter the topic for the poll: ");
            String topic =scanner.nextLine();
            Poll poll=new Poll(topic);
            polls.add(poll);
            adminPerPoll.put(user,poll);


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
  }



   public void   vote(){
          User voter=getUser();
          if(voter==null){
              return;
          }
          System.out.println("here are a list of currently active polls: ");

          List<Poll> activePolls=getActivePolls();
          viewPolls(activePolls);

       Poll poll=pickPoll("enter the number of poll you want to vote : ", activePolls);

      if(poll.getVoters().contains(voter)){
          System.out.println("you already voted.");
          return;
      }
         String key=pickOption("enter the number written before the  choice you want to vote: ",poll);

      poll.getVotePerChoice().put(key,poll.getVotePerChoice().get(key)+1);

   }

    public void removePoll(){
         User admin=getUser();
         if(admin==null)
             return;

         viewPolls(getActivePolls());
         Poll poll=pickPoll("choose poll you want to remove(only if you are admin of the poll): ",getActivePolls());
         if(poll==null){
             System.out.println("poll not found.");
             return;
         }
         if(adminPerPoll.get(admin)==null||!adminPerPoll.get(admin).equals(poll)){
               System.out.println("you are not admin for the poll.");
               return;
         }
         poll.setStatus("inactive");

    }















}




