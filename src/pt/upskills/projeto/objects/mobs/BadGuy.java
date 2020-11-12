package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class BadGuy extends Enemy implements ImageTile {

    public BadGuy(Position position) {
        super(position, 2, 8);
    }

    @Override
    public String getName() {
        return "BadGuy";
    }
}