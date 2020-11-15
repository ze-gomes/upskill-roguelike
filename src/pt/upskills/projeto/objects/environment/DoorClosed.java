package pt.upskills.projeto.objects.environment;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class DoorClosed extends Door implements ImageTile {
    String key; // Closed door has a keyCode associated. It's only opened with the right key

    public DoorClosed(Position position, int numDoor, String destRoom, int destDoor, String key) {
        super(position, numDoor, destRoom, destDoor);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return "DoorClosed";
    }

}