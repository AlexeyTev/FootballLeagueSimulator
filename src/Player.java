import java.util.Random;

public class Player {
    private static final String [] FIRST_NAMES = {
            "Eduardo", "Lucas", "Gabriel", "Anderson", "Rafael", "Douglas",
            "Matheus", "Fernando", "Rodrigo", "Henrique", "Pablo", "Alan",
            "Felipe", "Jorge", "Diego", "Tomer", "Samuel", "Tiago",
            "Gustavo", "Bruno", "Ricardo", "Vinicius", "Yossi", "Jair",
            "Alex", "Fábio", "Marcelo", "César", "Luis", "João"
    };
    private static final String [] LAST_NAMES = {
            "Silva", "Pereira", "Santos", "Oliveira", "Rodrigues", "Fernandes",
            "Gomes", "Martins", "Costa", "Almeida", "Nascimento", "Lima",
            "Barbosa", "Carvalho", "Mendes", "Ben Haim", "Monteiro", "Melo",
            "Lopes", "Ferreira", "Ribeiro", "Sousa", "Cohen", "Sampaio",
            "Nunes", "Pinto", "Gonçalves", "Ramos", "Correia", "Coelho"
    };
    Random random = new Random();
    private static int idCounter = 0;
    private int id;
    private String firstName;
    private String lastName;

    Player (){
        idCounter++;
        this.id=idCounter;
        this.firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        this.lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public int getId() {
        return id;
    }

    public String toString() {
      return "ID " + this.id +" :" + this.firstName + " " + this.lastName+"\n";
    }
}
