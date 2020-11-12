package pt.upskills.projeto.objects;

import com.sun.tools.javac.Main;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.Wall;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.rogue.utils.Position;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public abstract class GameCharacter implements ImageTile {
    private Position position;
    private ImageTile collisionObject;

    public GameCharacter(Position position) {
        this.position = position;
    }

    public abstract String getName();

    public Position getPosition() {
        return position;
    }

    public ImageTile getCollisionItem() {
        return collisionObject;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    //Check map bounds for a given position, return TRUE if inside map dimensions
    public boolean checkInsideMapBounds(Position pos) {
        return !(pos.getY() >= 10 || pos.getX() >= 10 || pos.getX() < 0 || pos.getY() < 0);
    }


    // Check collision for all game characters
    public boolean checkCollision(Position pos) {
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile foundImage = currentRoom.checkPosition(pos);
        if (foundImage instanceof Enemy) {
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Hero) {
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Door) {
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Wall) {
            collisionObject = foundImage;
            return true;
            // null means it's the Floor
        } else if (foundImage == null) {
            return false;
        }
        collisionObject = null;
        return false;
    }

    public synchronized void playSound(final String url) {
        String localDir = System.getProperty("user.dir");
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Main.class.getResourceAsStream("localDir + \\sounds\\" + url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

}
