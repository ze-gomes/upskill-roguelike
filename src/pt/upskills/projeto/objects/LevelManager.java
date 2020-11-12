package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.*;

public class LevelManager {
    private static final LevelManager INSTANCE = new LevelManager();
    Map<String, Room> gameLevels = new HashMap<String, Room>();
    Room currentRoom;

    /**
     * @return Access to the Singleton instance of ImageMatrixGUI
     */
    public static LevelManager getInstance() {
        return INSTANCE;
    }

    public void changeLevel(Hero hero, Door door){
        Room destRoom = getGameLevel(door.getDestRoom());
        Door destDoor = destRoom.getDoor(door.getDestDoor());
        // Init gui and populate level
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        hero.setPosition(destDoor.getPosition());
        gui.clearImages();
        List<ImageTile> tiles = destRoom.getRoomImages();
        tiles.add(hero);
        gui.newImages(tiles);
        setCurrentRoom(door.getDestRoom());
    }


    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String room) {
        currentRoom = gameLevels.get(room);
    }

    public Room getGameLevel(String name) {
        return gameLevels.get(name);
    }


    public void addGameLevel(String name, Room room) {
        gameLevels.put(name, room);
    }

}
