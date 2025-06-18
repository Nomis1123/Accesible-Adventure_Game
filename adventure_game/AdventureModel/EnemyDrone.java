package AdventureModel;
import java.io.Serializable;
import java.util.Random;

/**
 * class EnemyDrone
 */
public class EnemyDrone extends Enemy implements Serializable {
    public String getEnemyDescription() {
        return enemyDescription;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public Health getHealth() {
        return health;
    }

    private String enemyDescription = "You encounter with a fully weaponed automatic drone!!!";
    private String enemyName = "drone"; //note the enemy's image will be accessed using this name
    private Health health = new Health(50, 50);

    private Random random = new Random();

    /**
     * drone performs one random action.
     * @param player : player who might being hit.
     * @return description of this attack by the drone.
     */
    public String attack(Player player) {
        //0: Drone will fire
        //1: Drone will taze
        //2: Drone will Self - Destruct
        int randomNum = random.nextInt(0, 3);
        if(randomNum == 0){
            return fire(player);
        }else if(randomNum == 1){
            return taze(player);
        }else{
            return selfDestruct(player);
        }
    }

    /**
     * Drone performs an attack called fire.
     * damage in this case will be a random int between 10 - 30.
     *
     * @param player: player who might being hit.
     * @return description of this attack fire by the drone.
     */
    public String fire(Player player){
        int attackAmount = random.nextInt(10, 31);
        player.modifyPlayerHealth(-attackAmount);
        return "The drone peppers you in rubber bullets, damaging you for  " + attackAmount + " hp";
    }

    /**
     * Drone performs an attack called taze.
     * damage in this case will be a random int between 5 - 25.
     * @param player: player who might being hit.
     * @return description of this attack taze by the drone.
     */
    public String taze(Player player){
        int attackAmount = random.nextInt(5, 26);
        player.modifyPlayerHealth(-attackAmount);
        return "The drone fires a shock dart at you, shocking you for  " + attackAmount + " hp";
    }

    /**
     * Drone performs an attack called selfDestruct.
     * damage in this case will be a random int between 35 - 55.
     * after the attack set the drone's health to 1.
     * @param player: player who might being hit.
     * @return description of this attack taze by the drone.
     */
    public String selfDestruct(Player player){
        int attackAmount = random.nextInt(35, 56);
        player.modifyPlayerHealth(-attackAmount);
        health.setHealthAmount(1); // after the attack set the drone's health to 1.
        return "The drone flies at you in an attempt to explode, it damages you for  " + attackAmount + " hp, while damaging itself as well" ;
    }
}
