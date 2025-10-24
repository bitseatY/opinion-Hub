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
            String name = manager.getUserName();
            String password = manager.getPassword();
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
