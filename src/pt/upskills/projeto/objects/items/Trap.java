package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Trap extends FloorInteractables implements ImageTile {

    public Trap(Position position) {
        super (position, 10);
    }

    @Override
    public String getName() {
        return "Trap";
    }


}