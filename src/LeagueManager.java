import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LeagueManager {
    //TODO: צריך להפוך לסטרים
    private List<Match>allMatches;
    private List<Team>allTeams;
    private List<List<Integer>>goalsByTeamAndPlayer;
    private List<Integer>location;

    LeagueManager(List<Team>allTeams, List<List<Integer>>goalsByTeamAndPlayer){
      this.allMatches=new ArrayList<>();
      this.allTeams=allTeams;
      this.goalsByTeamAndPlayer=goalsByTeamAndPlayer;

    }

    public void updateAllMatches(List<Match>matches) {
        this.allMatches=matches;
    }
    public void updateGoalsByTeamAndPlayer(List<List<Integer>>goalsByTeamAndPlayer) {
        this.goalsByTeamAndPlayer=goalsByTeamAndPlayer;
    }
    public void updateLocation(List<Integer>location){
        this.location=location;
    }
    public List<Match> findMatchesByTeam(int teamId){
        List<Match> result = allMatches.stream()
                .filter(match -> match.matchesId(teamId))
                .collect(Collectors.toList());
        return result;
    }
    public List<Team> findTopScoringTeams(int n) {
        List<Team> teams = sortTeamsByGoals();
        List<Team> result = teams.stream()
                .sorted(Comparator.comparingInt(Team::getTotalGoals).reversed())
                .limit(n)
                .collect(Collectors.toList());
        return result;
    }

    private List<Team> sortTeamsByGoals() {
        List<Team> result = new ArrayList<>(allTeams);

        result.sort(Comparator.comparingInt(Team::getTotalGoals).reversed());

        return result;
    }
    public List<Player> findPlayersWithAtLeastNGoals(int n){
        List<Player>result = new ArrayList<>();
        for (int i = 0 ; i < Constants.AMOUNT_OF_TEAMS;i++){
            for (int j = 0 ; j<Constants.PLAYERS_IN_TEAM;j++){
                if (this.goalsByTeamAndPlayer.get(i).get(j)>=n){
                    result.add(allTeams.get(i).getScoringPlayer(j));
                }
            }
        }
        return result;
    }
    public  Team getTeamByPosition(int position){
        return allTeams.get(location.get(position));
    }
    public Map<Integer, Integer> getTopScorers(int n) {
        Map<Integer, Integer> result = new HashMap<>();
        int max = 0;
        for (int i = 0; i < this.goalsByTeamAndPlayer.size(); i++) {
            for (int j = 0; j < Constants.PLAYERS_IN_TEAM; j++) {
                if (this.goalsByTeamAndPlayer.get(i).get(j) > max) {
                    max = this.goalsByTeamAndPlayer.get(i).get(j);
                }
            }
        }


        while (max > 0 && result.size() <= n) {
            for (int i = 0; i < this.goalsByTeamAndPlayer.size(); i++) {
                for (int j = 0; j < Constants.PLAYERS_IN_TEAM; j++) {
                        if (this.goalsByTeamAndPlayer.get(i).get(j) == max && result.size()<n) {
                            int key = (j + (i * 10));
                            int goals = goalsByTeamAndPlayer.get(i).get(j);
                            result.put(key, goals);
                        }else if (result.size()>n){
                            break;
                        }
                    }
                }
            if (result.size()>=n){
                break;
            }
                max--;
            }
        return result;

    }
    }

