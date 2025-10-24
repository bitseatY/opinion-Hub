import java.util.ArrayList;
import java.util.List;

public class User {
    private String Username;
    private String password;
    private final List<Poll> userCreatedPolls;
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
