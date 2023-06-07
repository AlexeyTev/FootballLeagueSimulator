import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private static int idCounter = 0;
    private int id;
    private String name;
    private List<Player>players;
    private int totalGoals;
    private int goalsAgainst;

    Team (){
        idCounter++;
        this.id = idCounter;
        this.name = extractNameFromCsv();
        this.players=getPlayers();
        this.totalGoals=0;

    }

    private List<Player> getPlayers() {
        List<Player>playerList = new ArrayList<>();
        for (int i = 0 ; i <Constants.PLAYERS_IN_TEAM; i ++){
            Player newPlayer = new Player();
            playerList.add(newPlayer);
        }
        return playerList;
    }

    private String extractNameFromCsv() {
        String csvFilePath = "src/resources/team_names.csv";
        List<String> teamNames = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            teamNames = br.lines()
                    .map(line -> line.split(","))
                    .filter(data -> data.length == Constants.AMOUNT_OF_TEAMS)
                    .findFirst()
                    .map(List::of)
                    .orElseGet(ArrayList::new);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (teamNames.size() < Constants.AMOUNT_OF_TEAMS) {
            System.out.println("There are not enough teams to build a league");
        }

        return teamNames.get(id - 1);
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
    public Player getScoringPlayer (int num){
        return this.players.get(num);
    }
    public void updateGoals(){
        this.totalGoals++;
    }
    public void updateGoalsAgainst (int n){
        this.goalsAgainst+=n;
    }

    public int getTotalGoals() {
        return totalGoals;
    }
    public int getGoalDifference(){
        return this.totalGoals-this.goalsAgainst;
    }

    @Override
    public String toString() {
        return
                "ID " + id +
                ":'" + name + '\'' +
                " totalGoals:" + totalGoals+" Goal difference:" +this.getGoalDifference()+"\n";
    }
}
