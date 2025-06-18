package AdventureModel;

import java.io.Serializable;
import java.util.Random;

/**
 * class EnemyMimic
 */
public class EnemyMimic extends Enemy implements Serializable {

    public String getEnemyDescription() {
        return enemyDescription;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public Health getHealth() {
        return health;
    }

    private String enemyDescription = "You are facing a mimic monster who can copy other's action !!!";
    private String enemyName = "mimic"; //note the enemy's image will be accessed using this name
    private Health health = new Health(100, 100);

    private Random random = new Random();
    /**
     * Mimic enemy performs one random action
     * @param player : player who might being hit
     * @return description of this attack by the food enemy
     */
    public String attack(Player player) {
        //0: Mimic will Copy - Cat
        //1: Mimic will Screech
        //2: Mimic will Shapeshift
        int randomNum = random.nextInt(0, 3);
        if(randomNum == 0){
            return copyCat(player);
        }else if(randomNum == 1){
            return screech(player);
        }else{
            return shapeShift(player);
        }
    }

    /**
     * Mimic performs an attack called Copy - Cat
     * damage in this case will be a random int between 20 - 60, just like the player
     * @param player : player who might being hit
     * @return description of this attack Copy - Cat by mimic
     */
    public String copyCat(Player player){
        int attackAmount = random.nextInt(20, 61);
        player.modifyPlayerHealth(-attackAmount);
        return "The mimic mirrors your actions and hits you back for  " + attackAmount + " hp";
    }

    /**
     * Mimic performs an attack called screech
     * damage in this case will be a random int between 10 ~ 20
     * @param player : player who might being hit
     * @return description of this attack Copy - Cat by mimic
     */
    public String screech(Player player){
        int attackAmount = random.nextInt(10, 21);
        player.modifyPlayerHealth(-attackAmount);
        return "The mimic let’s out a ear piercing screech, damaging you for  " + attackAmount + " hp";
    }

    /**
     * Mimic performs an attack called shapeShift
     * damage in this case will be a random int between 8 ~ 16
     * @return description of this attack Copy - Cat by mimic
     * @param player : player who might being hit
     */
    public String shapeShift(Player player){
        int attackAmount = random.nextInt(8, 17);
        player.modifyPlayerHealth(-attackAmount);
        return "The mimic turns into one of your crewmates, as you hesitate it attacks you for " + attackAmount + " hp";
    }
}
