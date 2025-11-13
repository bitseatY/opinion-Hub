public class Choice {
    private  String choice;
    private int totalVoters;
    public Choice(String choice,int totalVoters){
        this.choice=choice;
        this.totalVoters=totalVoters;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public void increment() {
       totalVoters +=1;
    }

    public int getTotalVoters() {
        return totalVoters;
    }
}
