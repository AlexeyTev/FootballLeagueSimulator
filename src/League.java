import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class League {

    Random random = new Random();
    private List<Team> teams;
    private List<Match>allMatches;
    private List<List<Integer>>goalsByTeamAndPlayer;
    private HashMap<Integer,Integer> scoreTable;

    public League() {
        this.allMatches = new ArrayList<>();
        this.teams = createTeams();
        createGoalsByTeamAndPlayer();
        LeagueManager lm = new LeagueManager(teams,goalsByTeamAndPlayer);
        int numberOfTeams = Constants.AMOUNT_OF_TEAMS;
        resetScoreTable();
        for (int round = 1; round <= Constants.AMOUNT_OF_TEAMS-1; round++) {
            List<Team> roundTeams = new ArrayList<>(teams);

            for (int i = 0; i < numberOfTeams / 2; i++) {
                Team homeTeam = roundTeams.get(i);
                Team awayTeam = roundTeams.get(numberOfTeams - 1 - i);

                Match match = new Match(homeTeam, awayTeam);
                match.start();

                try {
                    Thread.sleep(Constants.COUNTDOWN * 1000 + 50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                match.setFinalResult();
                updateGoalsByTeamAndPlayer(match);
                updateScoreTable(match);
                allMatches.add(match);


            }
            sortAndPrintScoreTable();
            lm.updateAllMatches(this.allMatches);
            lm.updateGoalsByTeamAndPlayer(this.goalsByTeamAndPlayer);
            choseOptionFromLm(lm);
            roundTeams.add(1, roundTeams.remove(numberOfTeams - 1));
        }


    }

    private void resetScoreTable() {
        this.scoreTable = new HashMap<>();
        for (Team team : teams) {
            scoreTable.put(team.getId(), 0);
        }
    }

    private void choseOptionFromLm(LeagueManager lm) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Options:\n " +
                "1) find matches by team id\n" +
                "2)find top scoring teams\n" +
                "3)find player with at least n goals\n" +
                "9)NEXT ROUND!!!");
        int choice = scanner.nextInt();
        switch (choice){
            case 1 -> {
                System.out.println("Enter team id");
                int id = scanner.nextInt();
                if (id>=1 && id <=Constants.AMOUNT_OF_TEAMS){
                lm.findMatchesByTeam(id);
            }else choseOptionFromLm(lm);
        }
        case 2-> {
            System.out.println("Enter amount of teams");
            int n = scanner.nextInt();
            if (n<=Constants.AMOUNT_OF_TEAMS){
                lm.findTopScoringTeams(n);
            }else choseOptionFromLm(lm);
            }
            case 3-> {
                System.out.println("Enter amount of minimum goals");
                int n = scanner.nextInt();
                if (n>=0){
                    lm.findPlayersWithAtLeastNGoals(n);
                }
            }
            case 9-> System.out.println("Starting next round\n\n");
        }
    }




    private void updateScoreTable(Match match) {
        int finalResult = match.getFinalResult();
        int currentHomeTeamPoint = this.scoreTable.get(match.getHomeTeam().getId());
        int currentAwayTeamPoint = this.scoreTable.get(match.getAwayTeam().getId());
        switch (finalResult){
            case Constants.HOME_TEAM -> this.scoreTable.put(match.getHomeTeam().getId(), currentHomeTeamPoint+Constants.WIN_PTS);
            case Constants.AWAY_TEAM -> this.scoreTable.put(match.getAwayTeam().getId(), currentAwayTeamPoint+Constants.WIN_PTS);
            case Constants.TIE -> {
                this.scoreTable.put(match.getAwayTeam().getId(), currentAwayTeamPoint+Constants.TIE_PTS);
                this.scoreTable.put(match.getHomeTeam().getId(), currentHomeTeamPoint+Constants.TIE_PTS);
            }
        }
    }

    private void sortAndPrintScoreTable() {
        List<Integer> sorted = new ArrayList<>();
        for (int i = 0; i < this.scoreTable.size(); i++) {
            int currentTeamIndex = i;
            int currentTeamPoints = this.scoreTable.get(currentTeamIndex+1);
            int currentTeamGoals = this.teams.get(currentTeamIndex).getTotalGoals();
            boolean added = false;

            for (int j = 0; j < sorted.size(); j++) {
                int sortedTeamIndex = sorted.get(j);
                int sortedTeamPoints = this.scoreTable.get(sortedTeamIndex+1);
                int sortedTeamGoals = this.teams.get(sortedTeamIndex).getTotalGoals();

                if (currentTeamPoints > sortedTeamPoints ||
                        (currentTeamPoints == sortedTeamPoints && currentTeamGoals > sortedTeamGoals)) {
                    sorted.add(j, currentTeamIndex);
                    added = true;
                    break;
                }
            }

            if (!added) {
                sorted.add(currentTeamIndex);
            }
        }


        System.out.println("-----------\nChampions League Round " + allMatches.size()/5 + "  Table\n-----------");
        IntStream.range(0, sorted.size())
                .forEach(index -> {
                    int integer = sorted.get(index);
                    String name = teams.get(integer).getName();
                    int pts = scoreTable.get(integer + 1);
                    int goals = teams.get(integer).getTotalGoals();
                    int place = index + 1;
                    System.out.println("|(" + place + ")|" + name + " ," + pts + " pts , " + goals + " goals");
                });

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
               int teamIndex = (match.getGoals().get(i).getScorer().getId()-1)/Constants.PLAYERS_IN_TEAM;
               int playerIndex = (match.getGoals().get(i).getScorer().getId()-1)%Constants.PLAYERS_IN_TEAM;
                   List <Integer> temp = goalsByTeamAndPlayer.get(teamIndex);
                   temp.set(playerIndex, temp.get(playerIndex)+1);
                   goalsByTeamAndPlayer.set(teamIndex,temp);
                   teams.get(teamIndex).updateGoals();
           }
        }
        System.out.println(match);
    }

}



