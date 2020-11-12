package pt.upskills.projeto.objects;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorClosed;
import pt.upskills.projeto.objects.items.FloorInteractables;
import pt.upskills.projeto.objects.items.GoodMeat;
import pt.upskills.projeto.objects.items.Hammer;
import pt.upskills.projeto.objects.items.Sword;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.objects.status.*;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.*;

public class Hero extends GameCharacter implements ImageTile, Observer {
    private Position position;
    private int currentHP; // range 0-8
    private int maxHP = 8;
    private int damage;
    private List<ImageTile> statusImages;
    private int score;
    private HashMap<Integer, FloorInteractables> currentItems;

    public Hero(Position position) {
        super(position);
        this.currentHP = maxHP;
        this.damage = 2;
        this.statusImages = new ArrayList<ImageTile>();
        this.currentItems = new HashMap<Integer, FloorInteractables>();
        this.score = 0;
    }


    // Prevents score from dropping below 0
    public void changeScore(int score) {
        if (this.score + score < 0) {
            this.score += 0;
        } else {
            this.score += score;
        }
    }

    // Prevents damage from dropping below 0
    public void changeDamage(int score) {
        if (this.score + score < 0) {
            this.score += 0;
        } else {
            this.score += score;
        }
    }


    public void takeDamage(int damage) {
        this.currentHP -= damage;
    }


    // Check object at status position (return Null if it's empty), return the object otherwise
    public ImageTile checkStatusPosition(Position position) {
        for (ImageTile tile : statusImages) {
            if (tile.getPosition().equals(position)) {
                // If for a given position the object found is Not Black, return that object
                if (!(tile instanceof Black)) {
                    return tile;
                }
            }
        } // Else if nothing found return null, means it's Black (empty)
        return null;
    }

    public void addItem(FloorInteractables item) {
        if (!(currentItems.containsKey(1))) {
            currentItems.put(1, item);
        } else if (!(currentItems.containsKey(2))) {
            currentItems.put(2, item);
        } else if (!(currentItems.containsKey(3))) {
            currentItems.put(3, item);
        }
    }

    public boolean itemSlotsAvailable() {
        return (!(currentItems.containsKey(1) && currentItems.containsKey(2) && currentItems.containsKey(3)));
    }

    // Returns first position of items Free on the status bar
    public Position checkFirstItemFree() {
        for (int i = 7; i < 10; i++) {
            Position posItems = new Position(i, 0);
            if (checkStatusPosition(posItems) == null) {
                return posItems; //Item Position Free
            }
        } // No items free
        return null;
    }

    //Update game status bar
    public void initStatus() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        statusImages.clear();
        // Create black bars on the background
        for (int i = 0; i < 10; i++) {
            Position pos = new Position(i, 0);
            ImageTile black = new Black(pos);
            statusImages.add(black);
        }
        for (int i = 0; i < 3; i++) {
            Position pos = new Position(i, 0);
            ImageTile fire = new Fire(pos);
            statusImages.add(fire);
        } // Update HP bars
        for (int i = 3; i < 7; i++) {
            Position posLife = new Position(i, 0);
            ImageTile green = new Green(posLife);
            statusImages.add(green);
        }
    }

    //Update game status bar
    public void updateStatus() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        statusImages.clear();
        // Create black bars on the background
        for (int i = 0; i < 10; i++) {
            Position pos = new Position(i, 0);
            ImageTile black = new Black(pos);
            statusImages.add(black);
        }
        for (int i = 0; i < 3; i++) {
            Position pos = new Position(i, 0);
            ImageTile fire = new Fire(pos);
            statusImages.add(fire);
        } // Update HP bars
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
        int u = 1;
        for (int i = 7; i < 10; i++) {
            if (currentItems.containsKey(u)) {
                Position posItems = new Position(i, 0);
                FloorInteractables floorItem = currentItems.get(u);
                floorItem.setPosition(posItems);
                statusImages.add(floorItem);

            }
            u++;
        }
        gui.newStatusImages(statusImages);
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
        ImageTile floorItem = currentRoom.checkPosition(newPos);
        // Se o chao é sword e nao tem espada ainda
        if (floorItem instanceof FloorInteractables) {
            if (floorItem instanceof Sword) {
                Sword floorSword = (Sword) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorSword);
                    currentRoom.removeObject(floorSword);
                    changeScore(floorSword.getScore());
                    changeDamage(floorSword.getDamage());
                }
            } else if (floorItem instanceof Hammer) {
                Hammer floorHammer = (Hammer) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorHammer);
                    currentRoom.removeObject(floorHammer);
                    changeScore(floorHammer.getScore());
                    changeDamage(floorHammer.getDamage());
                }
            } else if (floorItem instanceof GoodMeat) {
                GoodMeat floorGoodMeat = (GoodMeat) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorGoodMeat);
                    currentRoom.removeObject(floorGoodMeat);
                    changeScore(floorGoodMeat.getScore());
                    currentHP += floorGoodMeat.getLifePoints();
                }
            }
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
            changeScore(-1);
            setPosition(newPos);
            // If collision is Enemy, do damage and check if enemy dies
        } else if (getCollisionItem() instanceof Enemy) {
            Enemy enemy = (Enemy) getCollisionItem();
            enemy.takeDamage(damage);
            if (enemy.getMobHP() <= 0) {
                currentRoom.removeObject(enemy);
                changeScore(enemy.getScore());
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
                changeScore(-1);
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
            if (currentItems.containsKey(1)) {
                FloorInteractables item = currentItems.get(1);
                currentItems.remove(1);
                gui.removeStatusImage(item);
                updateStatus();
                item.setPosition(getPosition());
                // Add object to room again when dropped , but must remove and add Hero image so it always appears on top of item
                gui.removeImage(this);
                currentRoom.addObject(item);
                gui.addImage(this);
            }
        }
        if (keyCode == KeyEvent.VK_2) {
            if (currentItems.containsKey(2)) {
                FloorInteractables item = currentItems.get(2);
                currentItems.remove(2);
                gui.removeStatusImage(item);
                updateStatus();
                item.setPosition(getPosition());
                gui.removeImage(this);
                currentRoom.addObject(item);
                gui.addImage(this);

            }
        }
        if (keyCode == KeyEvent.VK_3) {
            if (currentItems.containsKey(3)) {
                FloorInteractables item = currentItems.get(3);
                currentItems.remove(3);
                gui.removeStatusImage(item);
                updateStatus();
                item.setPosition(getPosition());
                gui.removeImage(this);
                currentRoom.addObject(item);
                gui.addImage(this);
            }
        }
    }
}
