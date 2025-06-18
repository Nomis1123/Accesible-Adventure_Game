
package AdventureModel;

public class HealthPotion extends AdventureObject {


    private int potionRecoveryAmount;

    /**
     * HealthPotion constructor
     * ___________________________
     * This constructor sets the name, description, and location of the object.
     *
     * @param location    The location of the health potion in the game.
     * @param potionRecoveryAmount Amount of health this potion can recover
     */
    public HealthPotion(Room location, int potionRecoveryAmount) {
        // note the name of the health potion should be exactly the same with its name in the objectImage directory
        super("BANDAGE", "a HP recovery bandage", location);
        this.potionRecoveryAmount = potionRecoveryAmount;
    }

    /**
     * getter method for the recoveryAmount
     * @return return potionRecoveryAmount
     */
    public int getPotionRecoveryAmount() {
        return potionRecoveryAmount;
    }


}