package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.Floor;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.rogue.utils.Position;


import java.util.HashMap;
import java.util.List;

public class Room {
    private String nome;
    private Position heroPos;
    private List<ImageTile> roomImages;
    private HashMap<Integer, Door> listaPortas;
    private List<Enemy> roomEnemies;



    public Room(String nome, List<ImageTile> readMapImages, Position heroPos, HashMap<Integer, Door> listaPortas, List<Enemy> roomEnemies) {
        this.nome = nome;
        this.heroPos = heroPos;
        this.roomImages = readMapImages;
        this.listaPortas = listaPortas;
        this.roomEnemies = roomEnemies;
    }

    public List<ImageTile> getRoomImages() {
        return roomImages;
    }

    public String getNome() {
        return nome;
    }

    public Position getHeroPos() {
        return heroPos;
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
    public Door checkDoorPos(Position pos){
        for (Door door: listaPortas.values()) {
            if (door.getPosition().equals(pos)){
                return door;
            }
        }
        return null;
    }


    public void setHeroPos(Position heroPos) {
        this.heroPos = heroPos;
    }

    public void addEnemy(Enemy enemy) {
        roomEnemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return roomEnemies;
    }

    // Mexe todos os enemigos da sala
    public void moveEnemiesRoom() {
        // Only move if there are enemies in the room
        if (!(roomEnemies == null || roomEnemies.size() == 0)) {
            // move each enemy in the room
            for (Enemy enemy : roomEnemies) {
                enemy.movement();
            }
        }
    }

}
