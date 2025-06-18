package AdventureModel;

import java.io.Serializable;

/**
 * represents the health of the player or the enemy
 */
public class Health implements Serializable {
    private int healthAmount;
    private final int maxMumHealth;

    /**
     * getter method for the maxMumHealth
     * @return maximum health can reach
     */
    public int getMaxMumHealth() {
        return maxMumHealth;
    }

    /**
     * setter method for the health
     * @param healthAmount: current amount of health of the player or enemy
     */
    public void setHealthAmount(int healthAmount) {
        this.healthAmount = healthAmount;
    }

    /**
     * getter method for the health
     * @return current amount of the health
     */
    public int getHealthAmount() {
        return healthAmount;
    }

    /**
     * if the changeAmount is positive then health is recovering, otherwise  health is losing
     * @param changeAmount : amount of the health needs to change
     */
    public void modifyHealth(int changeAmount) {
        this.healthAmount += changeAmount;
        if(healthAmount > maxMumHealth){
            healthAmount = maxMumHealth;
        }
        if(healthAmount<0){ //health can not go below than 0
            healthAmount = 0;
        }
    }


    /**
     * constructor of the Health
     * @param healthAmount: current amount of health
     * @param maxMumHealth: max health can reach
     */
    public Health(int healthAmount, int maxMumHealth){
        this.healthAmount = healthAmount;
        this.maxMumHealth = maxMumHealth;
    }
}
