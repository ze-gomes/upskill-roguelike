package pt.upskills.projeto.objects.items;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class GoodMeat extends FloorInteractables implements ImageTile {
    private int lifePoints = 2;

    public GoodMeat(Position position) {
        super (position, 10);
    }

    @Override
    public String getName() {
        return "GoodMeat";
    }

    public int getLifePoints() {
        return lifePoints;
    }
}