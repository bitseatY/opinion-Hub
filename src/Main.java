import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner scanner=new Scanner(System.in);
        System.out.println("enter url: ");
        String url=scanner.nextLine();
        System.out.println("enter username : ");
        String username=scanner.nextLine();
        System.out.println("enter password : ");
        String password=scanner.nextLine();

        Menu menu=new Menu(new DataBaseConnection(url,username,password));
         menu.menu();


    }
}































