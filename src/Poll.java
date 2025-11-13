
import java.time.Duration;
import java.time.LocalDateTime;


public class Poll  {
    private  final String topic;
    private final User creator;
    private   String currentStatus;
    private  final LocalDateTime expiryDateTime;

    public Poll(String topic,User creator,String currentStatus, Duration duration) {
        this.creator=creator;
        this.topic = topic;
        expiryDateTime=LocalDateTime.now().plus(duration);// expires at poll created time  plus duration
       this.currentStatus=currentStatus;
    }


    public  synchronized void  setStatus() {
        if(currentStatus.equals("ACTIVE"))
            currentStatus="EXPIRED";

    }

    public User getCreator() {
        return creator;
    }

    public LocalDateTime getExpiryDateTime() {

        return expiryDateTime;
    }


    public synchronized String getStatus(){
        return currentStatus;
    }

    public String getTopic() {

        return topic;
    }
}
