package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Sword extends FloorInteractables implements ImageTile {
    public Sword(Position position) {
        super (position, 10, 2);
    }

    @Override
    public String getName() {
        return "Sword";
    }

}