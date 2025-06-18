package AdventureModel;

/**
 * abstract class enemy.
 * defines all the behaviors an enemy will have
 */
public abstract class Enemy implements Attack {


    private String enemyDescription;
    private String enemyName; //note the enemy's image will be accessed using this name
    private Health health;

    public String getEnemyDescription() {
        return enemyDescription;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public Health getHealth() {
        return health;
    }

    /**
     * Perform an attack on the player, after the attack returns the description of this attack
     * important: after the attack, remember to update the player's health display!!!
     * @param player : player who is being hit
     * @return the description of this attack, including the damage amount
     */
    public abstract String attack(Player player);





}
