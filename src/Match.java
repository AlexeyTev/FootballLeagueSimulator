
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Match extends Thread{
    private Random random = new Random();
    private static int idCounter=0;
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private List<Goal> goals;
    private int homeTeamScore;
    private int awayTeamScore;
    private int finalResult;

    public Match(Team homeTeam, Team awayTeam) {
        idCounter++;
        this.id = idCounter;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.goals = new ArrayList<>();
        this.homeTeamScore=0;
        this.awayTeamScore=0;

    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    @Override
    public void run() {
        super.run();
        System.out.println(this.id + ")" +homeTeam.getName() + " vs " + awayTeam.getName() );
        int numGoals = generateRandomNumberOfGoals();
        this.goals = IntStream.range(0, numGoals)
                .mapToObj(i -> new Goal(generateRandomTime(), generateRandomScorerFromTeam()))
                .collect(Collectors.toList());
        System.out.println(Constants.STRT_MSG);
        for (int i = Constants.COUNTDOWN; i > 0; i--) {
            System.out.println("     " +i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private int generateRandomNumberOfGoals (){
        return random.nextInt(0, Constants.MAX_NUM_OF_GOALS_IN_MATCH+1);
    }
    private  Player generateRandomScorerFromTeam (){
        int whatTeamScores = random.nextInt(Constants.HOME_TEAM,Constants.AWAY_TEAM+1);
        if (whatTeamScores==Constants.HOME_TEAM){
            this.homeTeamScore++;
            return this.homeTeam.getScoringPlayer(random.nextInt(Constants.PLAYERS_IN_TEAM));
        }else {
            this.awayTeamScore++;
            return this.awayTeam.getScoringPlayer(random.nextInt(Constants.PLAYERS_IN_TEAM));
        }
    }
    private int generateRandomTime (){
        return random.nextInt(Constants.MATCH_LENGTH+1);
    }
    public void setFinalResult(){
        if (this.awayTeamScore<this.homeTeamScore){
            this.finalResult= Constants.HOME_TEAM;
        }else if (this.awayTeamScore>this.homeTeamScore){
            this.finalResult=Constants.AWAY_TEAM;
        }
    }
    public int getFinalResult(){
        return this.finalResult;
    }

    @Override
    public String toString() {
        String winner = "tie";
        if (this.finalResult == Constants.AWAY_TEAM){
            winner = awayTeam.getName();
        }else if (this.finalResult == Constants.HOME_TEAM){
            winner= homeTeam.getName();
        }
        return "Final Result: \n"+this.homeTeam.getName() + ", " + this.homeTeamScore + " :: " + this.awayTeam.getName() + ", "+this.awayTeamScore+
                "  The winner is: " + winner;
    }

    public List<Goal> getGoals() {
        return goals;
    }
    public  boolean matchesId(int id){
        return this.homeTeam.getId()==id || this.awayTeam.getTotalGoals()==id;
    }
}