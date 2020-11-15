package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorOpen;
import pt.upskills.projeto.objects.environment.Floor;
import pt.upskills.projeto.objects.environment.Grass;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.objects.status.Fire;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.HashMap;
import java.util.List;

public class Room {
    private String nome;
    private List<ImageTile> roomImages; // Stores all the roomImages
    private HashMap<Integer, Door> listaPortas; // Stores all the doors when the map is read from the file on a hash map with their ids as keys.
    private Hero hero;


    public Room(String nome, List<ImageTile> readMapImages, HashMap<Integer, Door> listaPortas) {
        this.nome = nome;
        this.roomImages = readMapImages;
        this.listaPortas = listaPortas;
    }

    public List<ImageTile> getRoomImages() {
        return roomImages;
    }

    public String getNome() {
        return nome;
    }


    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }


    // Check a given position on the room, used for collision check and others
    public ImageTile checkPosition(Position position) {
        for (ImageTile tile : roomImages) {
            if (tile.getPosition().equals(position)) {
                // If for a given position the object found is Not floor and Not Grass, return that object
                if (!(tile instanceof Floor) && !(tile instanceof Grass)) {
                    return tile;
                }
            }
        } // Else if nothing found return null (Means it's Floor or Grass)
        return null;
    }

    public void addDoor(Door door, int numPorta) {
        listaPortas.put(numPorta, door);
    }

    public Door getDoor(int numPorta) {
        return listaPortas.get(numPorta);
    }

    //Check if a given position has a door, if it has, return that door, otherwise return null
    public Door checkDoorPos(Position pos) {
        for (Door door : listaPortas.values()) {
            if (door.getPosition().equals(pos)) {
                return door;
            }
        }
        return null;
    }


    // Opens a closed door with the respective Key
    public void openDoorWithKey(Door doorClosed){
        int numPortaFechada = doorClosed.getNumDoor();
        int destDoor = doorClosed.getDestDoor();
        String destRoom = doorClosed.getDestRoom();
        Position doorPos = doorClosed.getPosition();
        // Creates a new similar door with the same attributes (except key code) but OpenDoor instead of closed door
        Door portaAberta = new DoorOpen(doorPos, numPortaFechada, destRoom, destDoor);
        removeObject(doorClosed);
        addObject(portaAberta);
        addDoor(portaAberta, numPortaFechada);
    }

    // Moves all the enemies in the room
    public void moveEnemiesRoom() {
        for (ImageTile image : roomImages) {
            if (image instanceof Enemy) {
                ((Enemy) image).movement();
            }
        }
    }

    // Removes a object from both the gui and our room list
    public void removeObject(ImageTile tile) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.removeImage(tile);
        roomImages.remove(tile);
    }

    // Adds a object to both the gui and our room list
    public void addObject(ImageTile tile) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.addImage(tile);
        roomImages.add(tile);
    }

}
