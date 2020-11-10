package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class DoorWay extends Door implements ImageTile {

    public DoorWay(Position position, int numDoor, String destRoom, int destDoor) {
        super(position, numDoor, destRoom, destDoor);
    }

    @Override
    public String getName() {
        return "DoorWay";
    }

}