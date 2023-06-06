public class Constants {
    public static final int PLAYERS_IN_TEAM = 15;
    public static final int AMOUNT_OF_TEAMS = 10;
    public static final int NUM_ROUNDS = AMOUNT_OF_TEAMS-1;
    public static final int MAX_NUM_OF_GOALS_IN_MATCH = 7;
    public static final int HOME_TEAM = 1;
    public static final int AWAY_TEAM = 2;
    public static final int MATCH_LENGTH = 90;
    public static final int COUNTDOWN = 10;
    public static final int WIN_PTS = 3;
    public static final int TIE_PTS = 1;
    public static final int TIE = 0;

    public static final String KEEP_GOING = "~~~Next round start now~~~";
    public static final String LM_STRING_OUTPUT = "Options:\n" +
            "1)find matches by team id\n" +
            "2)find top scoring teams\n" +
            "3)find player with at least n goals\n" +
            "4)find team by position in table\n" +
            "5)find scorers with at least number of goals\n" +
            "" +
            "9)NEXT ROUND!!!";


    public static final int LM_OPT_1 = 1,LM_OPT_2=2,LM_OPT_3=3,LM_OPT_4=4,LM_OPT_5=5,LM_OPT_EXIT=9,LM_OPT_NULL=-1;
    public static final String STRT_MSG=" Starting In\n" +
            "  VVVVVVV";
}
