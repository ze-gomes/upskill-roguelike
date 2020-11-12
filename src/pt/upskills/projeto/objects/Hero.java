package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorClosed;
import pt.upskills.projeto.objects.items.Sword;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.objects.status.*;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Hero extends GameCharacter implements ImageTile, Observer {

    private Position position;
    private int currentHP; // range 0-8
    private int maxHP = 8;
    private int damage;
    private List<ImageTile> statusImages;
    private boolean sword;

    public Hero(Position position) {
        super(position);
        this.currentHP = maxHP;
        this.damage = 2;
        this.statusImages = new ArrayList<ImageTile>();
        this.sword = false;
    }


    public void takeDamage(int damage) {
        this.currentHP -= damage;
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
        int hpLoss = maxHP - currentHP;
        for (int i = 3; i < 7; i++) {
            Position posLife = new Position(i, 0);
            if (hpLoss > 0) {
                if (hpLoss == 1) {
                    ImageTile redgreen = new RedGreen(posLife);
                    statusImages.add(redgreen);
                    hpLoss--;
                } else if (hpLoss > 1) {
                    ImageTile red = new Red(posLife);
                    statusImages.add(red);
                    hpLoss -= 2;
                } else if (hpLoss == 0) {
                    ImageTile green = new Green(posLife);
                    statusImages.add(green);
                }
            } else {
                ImageTile green = new Green(posLife);
                statusImages.add(green);
            }
        }
        if (sword) {
            Position posSword = new Position(7, 0);
            ImageTile sword = new Sword(posSword);
            statusImages.add(sword);
        }
        gui.newStatusImages(statusImages);
        System.out.println("Current Damage: " + damage + " Has sword: " + sword);
    }

    @Override
    public String getName() {
        return "Hero";
    }

    // Check if there are any pickups or traps on the floor
    // (no collision but requires an action when you go over it)
    public void checkIfGroundAction(){
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile floor = currentRoom.checkPosition(getPosition());
        System.out.println(floor.getName());
        if (floor instanceof Sword){
            this.sword = true;
            this.damage += 2;
            gui.removeImage(floor);
        }
    }

    public void moveHero(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        // Move if there is no collision and is inside bounds
        if (!checkCollision(newPos) && checkInsideMapBounds(newPos)) {
            checkIfGroundAction();

            gui.removeImage(this);
            setPosition(newPos);
            gui.addImage(this);
            // If collision is Enemy, do damage and check if enemy dies
        } else if (getCollisionItem() instanceof Enemy) {
            Enemy enemy = (Enemy) getCollisionItem();
            enemy.takeDamage(damage);
            if (enemy.getMobHP()<=0){
                gui.removeImage(enemy);
            }
        }
        // Se a nova posicao é fora do mapa, e a posicao actual é uma porta
        // quer dizer que estamos a entrar numa porta para mudar de nivel
        if (!checkInsideMapBounds(newPos)) {
            if (currentRoom.checkDoorPos(getPosition()) != null) {
                // Convert ImageTile from getCollisionItem() to Door
                Door door = (Door) currentRoom.checkDoorPos(getPosition());
                System.out.println("Changing to room " + door.getDestRoom() + " porta " + door.getDestDoor());
                levelManager.changeLevel(this, door);
            }
        } else if (getCollisionItem() instanceof Door) {
            // Only move through a door if the door is open or Doorway
            if (!(getCollisionItem() instanceof DoorClosed)) {
                System.out.println("A entrar em porta aberta");
                gui.removeImage(this);
                setPosition(newPos);
                gui.addImage(this);
            } else {
                // Closed door
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
