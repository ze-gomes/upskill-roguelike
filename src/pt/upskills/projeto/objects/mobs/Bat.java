package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Bat extends Enemy implements ImageTile {

    public Bat(Position position) {
        super(position, 1, 1, 10);
    }

    @Override
    public String getName() {
        return "Bat";
    }
}