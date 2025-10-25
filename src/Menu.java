import java.util.Scanner;

public class Menu {
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

            do {
                String name = manager.getInfo("enter user name: ");
                String password = manager.getInfo("enter password: ");
                if(manager.isValid(name,password)){
                    user=manager.findUserByName(name);
                    break;
                }
                System.out.println("user name or password is incorrect ,try again. ");
            }while (true);

        } else {
            return;
        }
        System.out.println("Welcome " + user.getUsername());
        while (true) {
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



            choice = Integer.parseInt(scanner.nextLine());
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

                     return;
            }


        }

    }
}
