package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Scorpion extends Enemy implements ImageTile {

    public Scorpion(Position position) {
        super(position, 3, 1, 30);
    }

    @Override
    public String getName() {
        return "Scorpion";
    }
}