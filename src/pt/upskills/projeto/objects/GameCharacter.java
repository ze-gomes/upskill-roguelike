package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.Wall;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.rogue.utils.Position;

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

    public boolean checkCollision(Position pos) {
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile foundImage = currentRoom.checkPosition(pos);
        if (foundImage instanceof Enemy) {
            System.out.println("Enemy");
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Hero) {
            System.out.println("Hero");
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Door) {
            System.out.println("Door");
            collisionObject = foundImage;
            return true;
        } else if (foundImage instanceof Wall) {
            System.out.println("Wall");
            collisionObject = foundImage;
            return true;
            // Floor
        } else if (foundImage == null) {
            return false;
        }
        collisionObject = null;
        return false;
    }
}
