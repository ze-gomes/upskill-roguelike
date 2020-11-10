package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.Engine;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.gui.MapReader;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Hero implements ImageTile, Observer {

    private Position position;
    private int life;
    private List<ImageTile> statusImages;

    public Hero(Position position) {
        this.position = position;
        this.life = 5;
        this.statusImages = new ArrayList<ImageTile>();
    }

    public void updateStatus(){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        statusImages.clear();
        for (int i = 0; i<10; i++){
            Position pos = new Position(i, 0);
            ImageTile black = new Black(pos);
            statusImages.add(black);
        }
        for (int i = 0; i<3; i++){
            Position pos = new Position(i, 0);
            ImageTile fire = new Fire(pos);
            statusImages.add(fire);
        }
        for (int i = 0; i<life; i++){
                Position posLife = new Position(i+3, 0);
            ImageTile green = new Green(posLife);
            statusImages.add(green);

        }
        gui.newStatusImages(statusImages);
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    //Verifica se uma dada pos é uma parede recorrendo ao currentRoom
    public boolean checkIfWall(Position pos) {
        LevelManager levelManager = LevelManager.getInstance();
        List<ImageTile> roomTiles = levelManager.getCurrentRoom().getRoomImages();
        for (ImageTile tile : roomTiles) {
            if (tile.getPosition().equals(pos)) {
                if (tile instanceof Wall) {
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }

    /**
     * This method is called whenever the observed object is changed. This function is called when an
     * interaction with the graphic component occurs {{@link ImageMatrixGUI}}
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Integer keyCode = (Integer) arg;
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        updateStatus();
        if (keyCode == KeyEvent.VK_DOWN) {
            // Posicao a mover dada o input
            Position newPos = position.plus(Direction.DOWN.asVector());
            // Testa se a pos é parede antes de mover, só move se não for parede
            if (!checkIfWall(newPos)) {
                ImageTile novaPosHero = new Hero(newPos);
                gui.removeImage(this);
                this.position = newPos;
                gui.addImage(this);
            }
        }
        if (keyCode == KeyEvent.VK_UP) {
            Position newPos = position.plus(Direction.UP.asVector());
            if (!checkIfWall(newPos)) {
                gui.removeImage(this);
                this.position = newPos;
                gui.addImage(this);
            }
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            Position newPos = position.plus(Direction.LEFT.asVector());
            if (!checkIfWall(newPos)) {
                gui.removeImage(this);
                this.position = newPos;
                gui.addImage(this);
            }
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            Position newPos = position.plus(Direction.RIGHT.asVector());
            if (!checkIfWall(newPos)) {
                gui.removeImage(this);
                this.position = newPos;
                gui.addImage(this);
            }
        }
    }
}
