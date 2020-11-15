package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Rat extends Enemy implements ImageTile {

    public Rat(Position position) {
        super(position, 1, 2, 15);
    }

    @Override
    public String getName() {
        return "Rat";
    }
}