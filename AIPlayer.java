import java.util.Random;

public class AIPlayer {

    public static String turn() {
        Random random = new Random();
        int direction = random.nextInt(4); 

        switch(direction) {
            case 0:
                return "W";
            case 1:
                return "S";
            case 2:
                return "A";
            case 3:
                return "D";
            default:
                return "Invalid direction";
        }
    }
}
