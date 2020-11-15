package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Axe extends FloorInteractables implements ImageTile {
    public Axe(Position position) {
        super (position, 25, 2);
    }

    @Override
    public String getName() {
        return "Axe";
    }

}