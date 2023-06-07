import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class League {


    private List<Team> teams;
    private List<Match>allMatches;
    private List<List<Integer>>goalsByTeamAndPlayer;
    private HashMap<Integer,Integer> scoreTable;
    private   LeagueManager lm;
    public League() {
        leagueStartingActions();
        int numberOfTeams = Constants.AMOUNT_OF_TEAMS;
        List<Team> roundTeams = new ArrayList<>(teams);
        IntStream.rangeClosed(1, Constants.AMOUNT_OF_TEAMS - 1)
                .forEach(round -> {
                    IntStream.range(0, numberOfTeams / 2)
                            .mapToObj(i -> {
                                Team homeTeam = roundTeams.get(i);
                                Team awayTeam = roundTeams.get(numberOfTeams - 1 - i);
                                return new Match(homeTeam, awayTeam);
                            })
                            .peek(Match::start)
                            .forEach(match -> {
                                try {
                                    Thread.sleep(Constants.COUNTDOWN * 1000 + 50);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                matchFinishedAction(match);
                            });

                    roundFinishedAction(lm);
                    Team firstTeam = roundTeams.remove(1);
                    roundTeams.add(firstTeam);
                });
    }

    private void leagueStartingActions() {
        this.allMatches = new ArrayList<>();
        this.teams = createTeams();
        createGoalsByTeamAndPlayer();
        lm = new LeagueManager(teams,goalsByTeamAndPlayer);
        resetScoreTable();
    }

    private void matchFinishedAction(Match match) {
        match.setFinalResult();
        updateGoalsByTeamAndPlayer(match);
        updateScoreTable(match);
        allMatches.add(match);

    }

    private void roundFinishedAction(LeagueManager lm) {
        sortAndPrintScoreTable();
        lm.updateAllMatches(this.allMatches);
        lm.updateGoalsByTeamAndPlayer(this.goalsByTeamAndPlayer);
        choseOptionFromLm(lm);
    }




    private void resetScoreTable() {
        this.scoreTable = new HashMap<>();
        for (Team team : teams) {
            scoreTable.put(team.getId(), 0);
        }
    }

    private void choseOptionFromLm(LeagueManager lm) {
        int choice=Constants.LM_OPT_NULL;
        Scanner scanner = new Scanner(System.in);
        Map<Integer,Integer>topScorers;
        while (choice!=Constants.LM_OPT_EXIT) {
            System.out.println(Constants.LM_STRING_OUTPUT);
            choice = scanner.nextInt();
            switch (choice) {
                case Constants.LM_OPT_1 -> {
                    System.out.println("Enter team id");
                    int id = scanner.nextInt();
                    if (id >= 1 && id <= Constants.AMOUNT_OF_TEAMS) {
                       List<Match> result = lm.findMatchesByTeam(id);
                        System.out.println(result);
                    } else System.out.println("No such team");
                }
                case Constants.LM_OPT_2 -> {
                    System.out.println("Enter amount of teams");
                    int n = scanner.nextInt();
                    if (n>=0 &&n <= Constants.AMOUNT_OF_TEAMS) {
                       List<Team>result = lm.findTopScoringTeams(n);
                        System.out.println(result);
                    } else System.out.println("To many teams (Only " + Constants.AMOUNT_OF_TEAMS + ") Teams are in the league");
                }
                case Constants.LM_OPT_3 -> {
                    System.out.println("Enter amount of minimum goals");
                    int n = scanner.nextInt();
                    if (n >= 0) {
                       List<Player>result = lm.findPlayersWithAtLeastNGoals(n);
                        System.out.println(result);
                    }else System.out.println("number must be bigger than 0");
                }
                case Constants.LM_OPT_4 -> {
                    System.out.println("Enter the position in the table");
                    int position = scanner.nextInt();
                    if (position - 1 >= 0 && position - 1 < Constants.AMOUNT_OF_TEAMS) {
                        Team result = lm.getTeamByPosition(position - 1);
                        System.out.println("The team in " + position + " position is:" + result.getName());
                    }else System.out.println("No such position in the table");
                }
                case Constants.LM_OPT_5 -> {
                    System.out.println("Enter the number of highest scoring players");
                    int n = scanner.nextInt();
                    if (n>0 && n<=Constants.AMOUNT_OF_TEAMS*Constants.PLAYERS_IN_TEAM){
                        topScorers = lm.getTopScorers(n);
                    }else {
                        System.out.println("To many or less than one players");
                }}
            }
        }
        System.out.println(Constants.KEEP_GOING);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            lm.updateLocation(sorted);
        }


        System.out.println("-----------\nChampions League Round " + allMatches.size()/Constants.GAMES_PER_ROUND + "  Table\n-----------");
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


