import java.util.ArrayList;
import java.util.List;

public class League {
    private List<Team> teams;

    public League() {
       this.teams = createTeams();
    }

    private List<Team> createTeams() {
        List<Team>result = new ArrayList<>();
        for (int i = 0 ; i <Constants.AMOUNT_OF_TEAMS;i++){
            result.add(new Team());
        }
        return result;
    }
}



