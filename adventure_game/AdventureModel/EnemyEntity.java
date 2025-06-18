package AdventureModel;
import java.io.Serializable;
import java.util.Random;

/**
 * class EnemyEntity
 */
public class EnemyEntity extends Enemy implements Serializable {
    public String getEnemyDescription() {
        return enemyDescription;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public Health getHealth() {
        return health;
    }

    private String enemyDescription = "You encounter with an indescribable fearsome entity!!!";
    private String enemyName = "theEntity"; //note the enemy's image will be accessed using this name
    private Health health = new Health(200, 200);

    private Random random = new Random();

    /**
     * the entity performs one random action.
     * note that the entity will only switch health if the entity's health is less than 100
     * @param player : player who might being hit.
     * @return description of this attack by the entity.
     */
    public String attack(Player player) {
        //0: the entity will shiftReality, if the entity's hp is greater than 100 then 0 will trigger confusion
        //1: the entity will use confusion
        //2: the entity will use Giga - Ray
        int randomNum = random.nextInt(0, 3);
        if(randomNum == 0){
            if(health.getHealthAmount() <= 100){
                return shiftReality(player); // only shifts the reality when enemy's health is less than 100
            }else {
                return confusion(player);
            }
        }else if(randomNum == 1){
            return confusion(player);
        }else{
            return giGaRay(player);
        }
    }

    /**
     * The entity performs an attack called shift reality.
     * This should make the enemies hp the players hp and vice versa.
     * @param player: player who might being hit.
     * @return description of this attack fire by the drone.
     */
    public String shiftReality(Player player){
        int playerCurrHealth = player.getPlayerHealth();

        //swap the health
        player.setPlayerHealth(this.health.getHealthAmount());
        this.health.setHealthAmount(playerCurrHealth);

        return "The entity magically shifted your timeline as you 2 swap health";
    }

    /**
     * The entity performs an attack called Confusion.
     * damage in this case will be a random int between 5 - 25.
     * @param player: player who might being hit.
     * @return description of this attack taze by the drone.
     */
    public String confusion(Player player){
        int attackAmount = random.nextInt(5, 26);
        player.modifyPlayerHealth(-attackAmount);
        return "The entity emits rays which confuse you, hitting yourself  " + attackAmount + " hp";
    }

    /**
     * The entity performs an attack called Giga - Ray
     * damage in this case will be a random int between 25 - 55.
     * @param player: player who might being hit.
     * @return description of this attack taze by the drone.
     */
    public String giGaRay(Player player){
        int attackAmount = random.nextInt(25, 56);
        player.modifyPlayerHealth(-attackAmount);
        return "The entity charges a dark purple ray and blasts you with it, damaging you for  " + attackAmount + " hp";
    }
}
