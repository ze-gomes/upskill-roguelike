package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Hero implements ImageTile, Observer {

    private Position position;

    public Hero(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * This method is called whenever the observed object is changed. This function is called when an
     * interaction with the graphic component occurs {{@link ImageMatrixGUI}}
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Integer keyCode = (Integer) arg;
        if (keyCode == KeyEvent.VK_DOWN){
            // do something
        }
        if (keyCode == KeyEvent.VK_UP){
            // do something
        }
        if (keyCode == KeyEvent.VK_LEFT){
            // do something
        }
        if (keyCode == KeyEvent.VK_RIGHT){
            // do something
        }
    }
}
