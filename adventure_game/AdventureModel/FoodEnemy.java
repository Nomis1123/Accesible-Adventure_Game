package AdventureModel;
 import java.io.Serializable;
 import java.util.Random;
/**
 * class FoodEnemy
 */
public class FoodEnemy extends Enemy implements Serializable {

    public String getEnemyDescription() {
        return enemyDescription;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public Health getHealth() {
        return health;
    }

    private String enemyDescription = "You are facing a hostile creature formed by wasted food!!!";
    private String enemyName = "foodMonster"; //note the enemy's image will be accessed using this name
    private Health health = new Health(125, 125);

    private Random random = new Random();

    /**
     * food enemy performs one random action
     * @param player : player who might being hit
     * @return description of this attack by the food enemy
     */
    public String attack(Player player) {
        //0: food monster will stomp
        //1: food monster will putridSmell
        //2: food monster will feast
        int randomNum = random.nextInt(0, 3);
        if(randomNum == 0){
            return stomp(player);
        }else if(randomNum == 1){
            return putridSmell(player);
        }else{
            return feast();
        }
    }

    /**
     * food enemy performs an attack called stomp
     * will damage player's hp by 5~12
     * @param player: player who might being hit.
     * @return description of this attack stomp by the food enemy
     */
    public String stomp(Player player){
        int attackAmount = random.nextInt(5, 12);
        player.modifyPlayerHealth(-attackAmount);
        return "The beast lets out a stomach turning roar and stomps on the ground, damaging you for " + attackAmount + " hp";
    }

    /**
     * food enemy performs an attack called putridSmell
     * will damage player's hp by 3~15
     * @param player: player who might being hit.
     * @return description of this attack stomp by the food enemy
     */
    public String putridSmell(Player player){
        int attackAmount = random.nextInt(3, 15);
        player.modifyPlayerHealth(-attackAmount);
        return "The beast opens pores on its body and lets out a thick green gas, damaging you for " + attackAmount + " hp";
    }

    /**
     * food enemy use this action to recover some hp
     * will recover hp 30~60
     * @return description of the enemy's action
     */
    public String feast(){
        int recoverAmount = random.nextInt(30, 60);
        health.modifyHealth(recoverAmount);
        return "The beast takes a piece of himself and consumes it, healing the monster for  " + recoverAmount + " hp";
    }






}
