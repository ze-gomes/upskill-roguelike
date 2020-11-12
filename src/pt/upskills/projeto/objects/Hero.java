package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorClosed;
import pt.upskills.projeto.objects.items.FloorInteractables;
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
    private int score;

    public Hero(Position position) {
        super(position);
        this.currentHP = maxHP;
        this.damage = 2;
        this.statusImages = new ArrayList<ImageTile>();
        this.sword = false;
        this.score = 0;
    }


    // Prevents score from dropping below 0
    public void addScore(int score) {
        if (this.score + score < 0) {
            this.score += 0;
        } else {
            this.score += score;
        }
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
            Sword sword = new Sword(posSword);
            statusImages.add(sword);
            addScore(sword.getScore());
        }
        gui.newStatusImages(statusImages);
        System.out.println("Current Damage: " + damage + " Has sword: " + sword);
    }

    @Override
    public String getName() {
        return "Hero";
    }

    public FloorInteractables getStatusPosition(Position pos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        for (ImageTile statusImage : statusImages) {
            if (statusImage.getPosition().equals(pos) && (statusImage instanceof FloorInteractables)) {
                System.out.println(statusImage.getName());
                return (FloorInteractables) statusImage;
            }
        }
        return null;
    }

    // Check if there are any pickups or traps on the floor
    // (no collision but requires an action when you go over it)
    public void checkIfGroundAction(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile floor = currentRoom.checkPosition(newPos);
        // Se o chao é sword e nao tem espada ainda
        if (floor instanceof FloorInteractables && (!sword)) {
            FloorInteractables floorInteractable = (FloorInteractables) floor;
            this.sword = true;
            this.damage += 2;
            currentRoom.removeObject(floorInteractable);
            updateStatus();
        }
    }

    public void moveHero(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        // Move if there is no collision and is inside bounds
        if (!checkCollision(newPos) && checkInsideMapBounds(newPos)) {
            checkIfGroundAction(newPos);
            addScore(-1);
            setPosition(newPos);
            // If collision is Enemy, do damage and check if enemy dies
        } else if (getCollisionItem() instanceof Enemy) {
            Enemy enemy = (Enemy) getCollisionItem();
            enemy.takeDamage(damage);
            if (enemy.getMobHP() <= 0) {
                currentRoom.removeObject(enemy);
                addScore(enemy.getScore());
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
                setPosition(newPos);
                addScore(-1);
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
        System.out.println("O Score atual é: " + score);
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
        if (keyCode == KeyEvent.VK_1) {
            Position pos = new Position(7, 0);
            if (getStatusPosition(pos) != null) {
                FloorInteractables statusImage1 = getStatusPosition(pos);
                gui.removeStatusImage(statusImage1);
                statusImage1.setPosition(getPosition());
                gui.removeImage(this);
                currentRoom.addObject(statusImage1);
                gui.addImage(this);
                sword = false;
                damage -=2;
            }
        }
        if (keyCode == KeyEvent.VK_2) {
            Position pos = getPosition();
        }
        if (keyCode == KeyEvent.VK_3) {
            Position pos = getPosition();

        }
    }
}
