package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Hammer extends FloorInteractables implements ImageTile {
    public Hammer(Position position) {
        super (position, 50, 4);
    }

    @Override
    public String getName() {
        return "Hammer";
    }

}