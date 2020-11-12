package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.Floor;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.HashMap;
import java.util.List;

public class Room {
    private String nome;
    private List<ImageTile> roomImages;
    private HashMap<Integer, Door> listaPortas;
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

    public Hero getHero1() {
        for (ImageTile tile : roomImages) {
            if (tile instanceof Hero) {
                Hero hero = (Hero) tile;
                return hero;
            }
        }
        return null;
    }


    public ImageTile checkPosition(Position position) {
        for (ImageTile tile : roomImages) {
            if (tile.getPosition().equals(position)) {
                // If for a given position the object found is Not floor, return that object
                if (!(tile instanceof Floor)) {
                    return tile;
                }
            }
        } // Else if nothing found return null (Means it's floor)
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

    // Mexe todos os enemigos da sala
    public void moveEnemiesRoom() {
        for (ImageTile image : roomImages) {
            if (image instanceof Enemy) {
                ((Enemy) image).movement();
            }
        }
    }


    public void removeObject(ImageTile tile) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.removeImage(tile);
        roomImages.remove(tile);
    }

    public void addObject(ImageTile tile) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.addImage(tile);
        roomImages.add(tile);
    }
}