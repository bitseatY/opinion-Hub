import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Poll {
    private  final String topic;
    public   enum  status{ACTIVE,EXPIRED};
    private status currentStatus;
    private final LocalDateTime creationDateTime;
    private  final LocalDateTime expiryDateTime;
    private final HashMap<String, Integer> votePerChoice;
    private  final List<User> voters;

    public Poll(String topic, Duration duration) {
        currentStatus=status.ACTIVE ;          //poll is active when created
        this.topic = topic;
        voters=new ArrayList<>();
        votePerChoice = new HashMap<>();
        creationDateTime=LocalDateTime.now();
        expiryDateTime=creationDateTime.plus(duration);
    }
    public  synchronized void  setStatus() {
        if(currentStatus==status.ACTIVE)
            currentStatus=status.EXPIRED;

    }

    public LocalDateTime getExpiryDateTime() {

        return expiryDateTime;
    }

    public   synchronized status getStatus() {

        return currentStatus;
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
