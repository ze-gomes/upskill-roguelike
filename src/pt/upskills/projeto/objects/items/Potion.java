package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Potion extends FloorInteractables implements ImageTile {

    public Potion(Position position) {
        super (position, 30, 8);
    }

    @Override
    public String getName() {
        return "Potion";
    }

}