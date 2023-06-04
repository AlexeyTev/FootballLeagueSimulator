import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeagueManager {
    //TODO: צריך לעשות את כל המטודות של המשימה
    List<List<Match>>allMatches;
    List<Match>currentRound;
    LeagueManager(List<Match>currentRound){
        this.currentRound= currentRound;
        allMatches.add(currentRound);
    }
    private List<Match> findMatchesByTeam(int teamId) {
        return allMatches.stream()
                .flatMap(List::stream)
                .filter(match -> match.getHomeTeam().getId() == teamId || match.getAwayTeam().getId() == teamId)
                .collect(Collectors.toList());
    }

}
