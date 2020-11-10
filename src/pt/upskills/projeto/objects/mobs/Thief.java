package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Thief extends Enemy implements ImageTile {

    public Thief(Position position) {
        super(position);
    }

    @Override
    public String getName() {
        return "Thief";
    }
}