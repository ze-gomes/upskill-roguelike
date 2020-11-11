package pt.upskills.projeto.objects.environment;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public abstract class Door implements ImageTile {
    private Position position;
    private int numDoor;
    private String destRoom;
    private int destDoor;

    public Door(Position position, int numDoor, String destRoom, int destDoor) {
        this.position = position;
        this.numDoor = numDoor;
        this.destRoom = destRoom;
        this.destDoor = destDoor;
    }

    public String getName() {
        return "Door";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public int getDestDoor() {
        return destDoor;
    }

    public int getNumDoor() {
        return numDoor;
    }

    public String getDestRoom() {
        return destRoom;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}