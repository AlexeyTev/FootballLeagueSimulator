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

    Team (){
        idCounter++;
        this.id = idCounter;
        this.name = extractNameFromCsv();
        this.players=getPlayers();
    }

    private List<Player> getPlayers() {
        List<Player>playerList = new ArrayList<>();
        for (int i = 0 ; i <Constants.PLAYERS_IN_TEAM; i ++){
            playerList.add(new Player());
        }
        return playerList;
    }

    private String extractNameFromCsv() {
        String csvFilePath = "src/resources/team_names.csv";
        String teamName="";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    if (data.length==Constants.AMOUNT_OF_TEAMS) {
                        teamName = data[this.id - 1];
                    }else System.out.println("There are not enought teams to build a league");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return teamName;
    }

    public String getName() {
        return this.name;
    }
}
