package pt.upskills.projeto.objects.environment;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class StairsUp implements ImageTile {

    private Position position;

    public StairsUp(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "StairsUp";
    }

    @Override
    public Position getPosition() {
        return position;
    }

}
