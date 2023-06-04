import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class League {
    Random random = new Random();
    private List<Team> teams;
    private List<Match>allMatches;
    private List<List<Integer>>goalsByTeamAndPlayer;
    private List<Integer>scoreTable;

    //TODO:צריך לעשות מחזוריות משחקים ולא רנדומליות
    public League() {
        this.allMatches = new ArrayList<>();
       this.teams = createTeams();
        createGoalsByTeamAndPlayer();
        IntStream.range(0, 5)
                .mapToObj(i -> {
                    Match match = new Match(teams.get(random.nextInt(9)), teams.get(random.nextInt(9)));
                    match.start();
                    try {
                        Thread.sleep(Constants.COUNTDOWN * 1000 + 50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    match.setFinalResult();
                    updateGoalsByTeamAndPlayer(match);
                    updateScoreTable(match);
                    return match;
                })
                .forEach(this.allMatches::add);
        printScoreTable();

    }


    private void printScoreTable() {
        sortScoreTable();
        String finisher =  "st";
        System.out.println("\n\nChampions League Score Table:\n --------------");
        for (int i = 0 ; i <scoreTable.size();i++){
            switch (i ){
                case 1 -> finisher = "nd";
                case 2-> finisher = "rd";
                case 3 -> finisher = "th";
            }
            System.out.println(i+1 + finisher + " ---> " +teams.get(i).getName() + ": "+ scoreTable.get(i)+ " pts");
        }
    }
    //TODO:צריך למיין את הטבלה לפני שאני מדפיס אותה ובשביל זה אני צריך להחליף את הגישה לשם של הקבוצה לא לפי אינדקס אלה לפי מיקום
    private void sortScoreTable() {
        List<Integer> temp = new ArrayList<>();
    }

    private void updateScoreTable(Match match) {
        if (this.scoreTable==null){
            this.scoreTable = IntStream.range(0, this.teams.size())
                    .mapToObj(i -> 0)
                    .collect(Collectors.toList());
        }else {
            switch (match.getFinalResult()){
                case Constants.HOME_TEAM -> this.scoreTable.set(match.getHomeTeam().getId(),this.scoreTable.get(match.getHomeTeam().getId())+Constants.WIN_PTS);
                case Constants.AWAY_TEAM -> this.scoreTable.set(match.getAwayTeam().getId(),this.scoreTable.get(match.getAwayTeam().getId())+Constants.WIN_PTS);
                case Constants.TIE -> {
                    this.scoreTable.set(match.getAwayTeam().getId(),this.scoreTable.get(match.getAwayTeam().getId())+Constants.TIE_PTS);
                    this.scoreTable.set(match.getHomeTeam().getId(),this.scoreTable.get(match.getHomeTeam().getId())+Constants.TIE_PTS);
                }
            }
        }
    }

    private void createGoalsByTeamAndPlayer() {
        this.goalsByTeamAndPlayer = Stream.generate(() -> new ArrayList<Integer>(Collections.nCopies(Constants.PLAYERS_IN_TEAM, 0)))
                .limit(teams.size())
                .collect(Collectors.toCollection(ArrayList::new));

    }


    private List<Team> createTeams() {
        return Stream.generate(Team::new).limit(Constants.AMOUNT_OF_TEAMS).toList();
    }

    private synchronized void updateGoalsByTeamAndPlayer(Match match){
        if (match.getGoals().size()>0 && match.getGoals()!=null){
           for (int i = 0 ; i < match.getGoals().size();i++){
               int teamIndex = match.getGoals().get(i).getScorer().getId()/Constants.PLAYERS_IN_TEAM;
               int playerIndex = match.getGoals().get(i).getScorer().getId()%Constants.PLAYERS_IN_TEAM;
                   List <Integer> temp = goalsByTeamAndPlayer.get(teamIndex);
                   temp.set(playerIndex, temp.get(playerIndex)+1);
                   goalsByTeamAndPlayer.set(teamIndex,temp);

           }
        }
        System.out.println(match);
    }
}



