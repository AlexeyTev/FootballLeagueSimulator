public class Goal {
    private static int idCounter=0;
        private int id;
        private int minute;
        private Player scorer;

        public Goal(int minute, Player scorer) {
            idCounter++;
            this.id = idCounter;
            this.minute = minute;
            this.scorer = scorer;
        }

    public Player getScorer() {
        return scorer;
    }
}
