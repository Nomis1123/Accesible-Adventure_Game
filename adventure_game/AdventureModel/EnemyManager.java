package AdventureModel;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * This class generate enemies and keep track with where enemy is located at.
 * If the enemy is slayed, then the system will remove the enemy from the enemyManager
 */
public class EnemyManager {
    //private ArrayList<Enemy> enemyList;
    private ArrayList<Integer> roomsWithEnemy;

    private HashMap<Integer, Enemy> roomToEnemy = new HashMap<Integer, Enemy>(); //key: integer representation of the room, value: enemy that is inside this room
    /**
     * constructor of the EnemyManager
     * will distribute enemies into the corresponding rooms
     * @param roomsWithEnemy: a list of room that need to be assigned with enemies, elements in this list should be unique
     */
    public EnemyManager(ArrayList<Integer> roomsWithEnemy){
        this.roomsWithEnemy = roomsWithEnemy;
        generateEnemyToRooms();
    }

    /**
     * This method generate enemies to the room whose number is inside in the list <roomsWithEnemy>
     */
    private void generateEnemyToRooms(){
        for(int roomNum: roomsWithEnemy){

            roomToEnemy.put(roomNum, generateSingleEnemy(roomNum));
        }
    }

    /**
     * randomly generate a single enemy
     * @return the enemy should be located in the room with <roomNum>
     */
    public Enemy generateSingleEnemy(int roomNum){
        if(roomNum == 16){
            return new FoodEnemy();
        }else if(roomNum == 24){
            return new EnemyMimic();
        }else if(roomNum == 25){
            return new EnemyDrone();
        }else {
            return new EnemyEntity();
        }
    }//which enemy do we assign, try here with any other enemy

    /**
     * this method returns whether a room has an enemy inside
     * @param roomNum: room that we want to check
     * @return return true if there is an enemy inside
     */
    public boolean roomHasEnemy(int roomNum){
        return roomToEnemy.containsKey(roomNum);
    }

    /**
     * precondition : roomHasEnemy(<roomNum>) is true
     * @param roomNum: enemy in which room we want to get
     * @return enemy in the room with <roomNum>
     */
    public Enemy getEnemyInRoom(int roomNum){
        return roomToEnemy.get(roomNum);
    }


    /**
     * precondition: there is an enemy in the room with <roomNum>
     * @param roomNum: enemy is slayed in the room with room number <roomNUm>
     */
    public void slayedEnemy(int roomNum){
        roomsWithEnemy.remove(Integer.valueOf(roomNum));
        roomToEnemy.remove(roomNum);
    }
}
