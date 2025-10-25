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
                           1.create a poll
                           2.vote on active polls
                           3.close a poll(admin only)
                           4.view all poles
                           5.see user profile
                           6.see top polls
                           7.show result for poll
                    
                    
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
                    manager.seeProfile(user);
                    break;

                case 6:
                    manager.viewPolls(manager.mostVotedActivePolls());
                    break;
                case 7:
                    manager.seeResultOfClosedPolls();
                    break;
            }


        }

    }
}
