package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static final LevelManager INSTANCE = new LevelManager();
    /**
     * @return Access to the Singleton instance of ImageMatrixGUI
     */
    public static LevelManager getInstance() {
        return INSTANCE;
    }


    List<Room> gameLevels = new ArrayList<Room>();
    Room currentRoom;

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public List<Room> getGameLevels() {
        return gameLevels;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public void setGameLevels(List<Room> gameLevels) {
        this.gameLevels = gameLevels;
    }

    public void addGameLevel(Room room){
        gameLevels.add(room);
    }
}
