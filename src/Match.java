import java.util.List;

public class Match extends Thread{
    private static int idCounter=0;
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private List<Goal> goals;

    public Match(Team homeTeam, Team awayTeam, List<Goal> goals) {
        idCounter++;
        this.id = idCounter;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.goals = goals;
    }
}
