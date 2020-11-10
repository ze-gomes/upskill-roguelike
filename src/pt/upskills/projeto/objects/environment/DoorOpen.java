package pt.upskills.projeto.objects.environment;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class DoorOpen extends Door implements ImageTile {

    public DoorOpen(Position position, int numDoor, String destRoom, int destDoor) {
        super(position, numDoor, destRoom, destDoor);
    }

    @Override
    public String getName() {
        return "DoorOpen";
    }

}