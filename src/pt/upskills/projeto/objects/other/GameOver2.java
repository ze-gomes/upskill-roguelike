package pt.upskills.projeto.objects.other;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

public class GameOver2 implements ImageTile {

    private Position position;

    public GameOver2(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "GameOver2";
    }

    @Override
    public Position getPosition() {
        return position;
    }

}
