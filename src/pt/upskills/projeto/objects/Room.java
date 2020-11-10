package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room {
    private String nome;
    private Position heroPos;
    private List<ImageTile> roomImages = new ArrayList<ImageTile>();
    private HashMap<Integer, Door> listaPortas;

    public Room(String nome, List<ImageTile> readMapImages, Position heroPos, HashMap<Integer, Door> listaPortas){
        this.nome = nome;
        this.heroPos = heroPos;
        this.roomImages = readMapImages;
        this.listaPortas = listaPortas;
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

    public void addDoor(Door door, int numPorta){
        listaPortas.put(numPorta, door);
    }

    public Door getDoor(int numPorta){
        return listaPortas.get(numPorta);
    }
}
