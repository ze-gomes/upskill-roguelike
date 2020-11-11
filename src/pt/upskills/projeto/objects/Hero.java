package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorClosed;
import pt.upskills.projeto.objects.environment.Wall;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.objects.status.Black;
import pt.upskills.projeto.objects.status.Fire;
import pt.upskills.projeto.objects.status.Green;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Hero extends GameCharacter implements ImageTile, Observer {

    private Position position;
    private int life;
    private List<ImageTile> statusImages;

    public Hero(Position position) {
        super(position);
        this.life = 5;
        this.statusImages = new ArrayList<ImageTile>();
    }

    public void updateStatus() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        statusImages.clear();
        for (int i = 0; i < 10; i++) {
            Position pos = new Position(i, 0);
            ImageTile black = new Black(pos);
            statusImages.add(black);
        }
        for (int i = 0; i < 3; i++) {
            Position pos = new Position(i, 0);
            ImageTile fire = new Fire(pos);
            statusImages.add(fire);
        }
        for (int i = 0; i < life; i++) {
            Position posLife = new Position(i + 3, 0);
            ImageTile green = new Green(posLife);
            statusImages.add(green);

        }
        gui.newStatusImages(statusImages);
    }

    @Override
    public String getName() {
        return "Hero";
    }


    public void moveHero(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        // Move if there is no collision and is inside bounds
        if (!checkCollision(newPos) && checkInsideMapBounds(newPos)) {
            gui.removeImage(this);
            setPosition(newPos);
            gui.addImage(this);
            System.out.println("Nao colidiu");
            System.out.println("move");
        } // Se a nova posicao é fora do mapa, e a posicao actual é uma porta
          // quer dizer que estamos a entrar numa porta para mudar de nivel
        if (!checkInsideMapBounds(newPos)) {
            System.out.println("outside bounds");
            if (currentRoom.checkDoorPos(getPosition()) != null) {
                // Convert ImageTile from getCollisionItem() to Door
                Door door = (Door) currentRoom.checkDoorPos(getPosition());
                System.out.println("Changing to room " + door.getDestRoom() + door.getDestDoor() + "porta");
                levelManager.changeLevel(this, door);
                System.out.println("open door");
            }
        } else if (getCollisionItem() instanceof Door) {
            // Only move through a door if the door is open or Doorway
            if (!(getCollisionItem() instanceof DoorClosed)) {
                System.out.println("A entrar em porta");
                gui.removeImage(this);
                setPosition(newPos);
                gui.addImage(this);

            } else {
                System.out.println("door closed");
            }
        }
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
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        Integer keyCode = (Integer) arg;
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        updateStatus();
        System.out.println(this.getPosition());
        if (keyCode == KeyEvent.VK_DOWN) {
            // Posicao a mover dada o input
            Position newPos = getPosition().plus(Direction.DOWN.asVector());
            // Testa se a pos é parede antes de mover, só move se não for parede
            moveHero(newPos);
            currentRoom.moveEnemiesRoom();
        }
        if (keyCode == KeyEvent.VK_UP) {
            Position newPos = getPosition().plus(Direction.UP.asVector());
            moveHero(newPos);
            currentRoom.moveEnemiesRoom();
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            Position newPos = getPosition().plus(Direction.LEFT.asVector());
            moveHero(newPos);
            currentRoom.moveEnemiesRoom();
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            Position newPos = getPosition().plus(Direction.RIGHT.asVector());
            moveHero(newPos);
            currentRoom.moveEnemiesRoom();
        }
    }
}
