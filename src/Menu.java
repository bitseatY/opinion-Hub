import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner=new Scanner(System.in);
    private final  Manager manager;
    private final DataBaseConnection dataBaseConnection;
    public Menu(DataBaseConnection dataBaseConnection){
         this.dataBaseConnection=dataBaseConnection;
         manager=new Manager(dataBaseConnection);

    }
    public void menu() throws SQLException  {
       System.out.println("welcome to Opinion Hub!");
        User user;
        do {
           int choice=acceptUserChoice();
           user = getUser(choice);

       } while (user==null);
        System.out.println("Welcome " + user.getUsername()+"\n");
        while (true) {
            int  choice=0;

                System.out.println("""
                        what would you like to do:
                               1.see what's trending
                               2.see profile
                               3.vote on active poll
                               4.create a poll
                               5.close a poll(admin only)
                               6.see result of a poll
                               7.exit
                        
                        
                    
                        
                        """);
                try {

                choice = Integer.parseInt(scanner.nextLine());
                if (choice>7||choice<1)
                    throw new NumberFormatException("invalid option number, try again");


            } catch (NumberFormatException e) {
               System.out.println("invalid option number, try again");
            }
            switch (choice) {
                case 1:
                     manager.Dashboard();
                     break;
                case 2:
                   manager.seeProfile(user);
                   break;
                case 3:
                    manager.vote(user);
                    break;
                case 4:
                    manager.createPoll(user);
                    break;
                case 5:
                    manager.closePoll(user);
                    break;

                case 6:
                    manager.seeResultOfClosedPolls();
                    break;
                case 7:
                    System.exit(1);
            }


        }

    }
    public int acceptUserChoice(){    //user either sign in as a new user or login using name and password
        int choice ;
        while(true) {
            System.out.println("""
                    
                       1.sign in
                       2.log in
                       3.exit
                    """);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("invalid input, try again.");
            }
        }
       return choice;

    }
    public User getUser(int choice) throws SQLException{
        User user;
        if (choice == 1) {
            user = manager.registerUser();        //new user created and added to userList

        } else if (choice == 2) {                //user retrieved from userList

            do {
                String name = manager.getInfo("enter user name: ");
                String password = manager.getInfo("enter password: ");
                if(manager.isValid(name,password)){                  //  password must match username
                    user=manager.findUserByName(name);
                    break;
                }
                System.out.println("user name or password is incorrect ,try again. ");    //future fix, user shouldn't be able to try infinitely
            }while (true);

        } else {
            return null;
        }

       return user;

    }

}
