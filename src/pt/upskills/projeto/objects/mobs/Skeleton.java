package pt.upskills.projeto.objects.mobs;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class Skeleton extends Enemy implements ImageTile {

    public Skeleton(Position position) {
        super(position, 1, 4, 20);
    }

    @Override
    public String getName() {
        return "Skeleton";
    }
}