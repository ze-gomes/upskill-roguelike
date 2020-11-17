package pt.upskills.projeto.objects;

import pt.upskills.projeto.game.FireBallThread;
import pt.upskills.projeto.game.LevelManager;
import pt.upskills.projeto.gui.FireTile;
import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.environment.DoorClosed;
import pt.upskills.projeto.objects.items.*;
import pt.upskills.projeto.objects.mobs.Enemy;
import pt.upskills.projeto.objects.status.*;
import pt.upskills.projeto.rogue.utils.Direction;
import pt.upskills.projeto.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.util.*;

public class Hero extends GameCharacter implements ImageTile, Observer {
    private String name;
    private int currentHP; // range 0-8
    private int maxHP = 8;
    private int fireballs = 3;
    private int damage;
    private List<ImageTile> statusImages; // Status Images
    private int score;
    private HashMap<Integer, FloorInteractables> currentItems; // Stores Items in the item slot
    private Integer lastKeycode = (Integer) KeyEvent.VK_UP; // Saves Default fireball direction
    private int lives =3;


    public Hero(Position position) {
        super(position);
        this.name = "Hero";
        this.currentHP = maxHP;
        this.damage = 1; // start with 1 damage
        this.statusImages = new ArrayList<ImageTile>();
        this.currentItems = new HashMap<Integer, FloorInteractables>();
        this.score = 0;
    }


    // Test function for game restart
    public void resetState(){
        this.currentHP = maxHP;
        this.damage = 1; // start with 1 damage
        this.currentItems = new HashMap<Integer, FloorInteractables>();
        this.score = 0;
        this.fireballs = 3;
    }


    // Changes hero score, prevents score from dropping below 0
    public void changeScore(int score) {
        if (this.score + score < 0) {
            this.score = 0;
        } else {
            this.score += score;
        }
    }

    // Changes hero damage, prevents damage from dropping below 1
    public void changeDamage(int damage) {
        if (this.damage + damage < 0) {
            this.damage = 1;
        } else {
            this.damage += damage;
        }
    }


    // Changes hero HP, prevents HP from going over the MaxHP or below 0
    public void changeHP(int damage) {
        if (currentHP + damage > maxHP) {
            this.currentHP = maxHP;
        } else if (currentHP + damage < 0) {
            this.currentHP = 0;
        } else {
            this.currentHP += damage;
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    // Add item picked up to a HashMap<Integer, FloorInteractables> with keys 1-3 which represent the respective item slots
    public void addItem(FloorInteractables item) {
        // Adds item to the first slot available (checked with containsKey() )
        if (!(currentItems.containsKey(1))) {
            currentItems.put(1, item);
        } else if (!(currentItems.containsKey(2))) {
            currentItems.put(2, item);
        } else if (!(currentItems.containsKey(3))) {
            currentItems.put(3, item);
        }
    }

    // Check if there are item slots available, return TRUE if there are
    public boolean itemSlotsAvailable() {
        return (!(currentItems.containsKey(1) && currentItems.containsKey(2) && currentItems.containsKey(3)));
    }


    // Get Key from item slot
    public FloorInteractables getKeyfromSlot(String keycode) {
        for (int i = 1; i <= 3; i++) {
            if (currentItems.get(i) instanceof Key) {
                Key item = (Key) currentItems.get(i);
                // only if the key matches the door keycode
                if (item.getCode().equals(keycode)){
                    currentItems.remove(i);
                    return item;
                }
            }
        }
        return null;
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
        // Create fireballs
        for (int i = 0; i < fireballs; i++) {
            Position pos = new Position(i, 0);
            FireTile fire = new Fire(pos);
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
        // Display items in item slots from the hashmap of FloorInteractables
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }


    // Check object at status position (return Null if it's empty), return the object otherwise
    public ImageTile getStatusPosition(Position position) {
        for (ImageTile tile : statusImages) {
            if (tile.getPosition().equals(position)) {
                // If for a given position the object found is Not Black, return that object
                if (!(tile instanceof Black)) {
                    return tile;
                }
            }
        } // if nothing found return null, means it's Black (empty)
        return null;
    }

    // Check if there are any item pickups or traps on the floor
    // (no collision but requires an action when you go over it)
    public void checkIfGroundAction(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        ImageTile floorItem = currentRoom.checkPosition(newPos);
        // If the floorItem is a sword
        if (floorItem instanceof FloorInteractables) {
            if (floorItem instanceof Sword) {
                Sword floorSword = (Sword) floorItem;
                // Only pickup item if there are slots available
                if (itemSlotsAvailable()) {
                    // Remove item from the room and into the status bar
                    // Object remains the same
                    addItem(floorSword);
                    currentRoom.removeObject(floorSword);
                    // Apply item effects
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
            } else if (floorItem instanceof Axe) {
                Axe floorAxe = (Axe) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorAxe);
                    currentRoom.removeObject(floorAxe);
                    changeScore(floorAxe.getScore());
                    changeDamage(floorAxe.getDamage());
                }
                // HP items (GoodMeat and Potion) are stored in the status bar item slots and consumed when "dropped"
            } else if (floorItem instanceof GoodMeat) {
                GoodMeat floorGoodMeat = (GoodMeat) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorGoodMeat);
                    currentRoom.removeObject(floorGoodMeat);
                    changeScore(floorGoodMeat.getScore());
                }
            } else if (floorItem instanceof Potion) {
                Potion floorPotion = (Potion) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorPotion);
                    currentRoom.removeObject(floorPotion);
                    changeScore(floorPotion.getScore());
                }
                // Trap damages the player and decreases score
            } else if (floorItem instanceof Trap) {
                Trap floorTrap = (Trap) floorItem;
                changeScore(floorTrap.getScore());
                changeHP(-floorTrap.getDamage());
            }  else if (floorItem instanceof Key) {
                Key floorKey = (Key) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorKey);
                    currentRoom.removeObject(floorKey);
                    changeScore(floorKey.getScore());
                }
            }
            updateStatus();
        }
    }


    // Manages movement of the hero given a new position
    public void moveHero(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        setName("Hero");
        // Move if there is no collision and is inside map bounds
        if (!checkCollision(newPos) && checkInsideMapBounds(newPos)) {
            // Check if there are items on the floor to pickup
            checkIfGroundAction(newPos);
            // Decrease score on walking
            changeScore(-1);
            setPosition(newPos);
            // If collision is Enemy, do damage and check if enemy dies, increase score in that case
        } else if (getCollisionItem() instanceof Enemy) {
            Enemy enemy = (Enemy) getCollisionItem();
            enemy.takeDamage(damage);
            if (enemy.getMobHP() <= 0) {
                changeScore(enemy.getScore());
            }
        }
        // If new position is not inside map bounds, and the current position is a door
        // means we are entering a door to change level
        if (!checkInsideMapBounds(newPos)) {
            if (currentRoom.checkDoorPos(getPosition()) != null) {
                // Convert ImageTile from getCollisionItem() to Door
                Door door = (Door) currentRoom.checkDoorPos(getPosition());
                System.out.println("Changing to room " + door.getDestRoom() + " Door " + door.getDestDoor());
                // Changes the level (loads up the new room and all the necessary assets)
                levelManager.changeLevel(this, door);
            }

        } else if (getCollisionItem() instanceof Door) {
            // Only move to a door position if the door is open or a Doorway
            if (!(getCollisionItem() instanceof DoorClosed)) {
                setPosition(newPos);
                changeScore(-1);
            } else { // Otherwise it's a closed door
                DoorClosed doorClosed = (DoorClosed) getCollisionItem();
                // Interacting with closed door
                // Gets a key from item slot and check if it matches the door code with the key code.
                FloorInteractables floorItem = getKeyfromSlot(doorClosed.getKey());
                if (floorItem != null) {
                    Key key = (Key) floorItem;
                    if (key.getCode().equals(doorClosed.getKey())) {
                        // If it's the right key, use key and Opens door with key
                        System.out.println("Opening door " + doorClosed.getNumDoor() + " with " + key.getCode() + " to " + doorClosed.getDestRoom());
                        gui.removeStatusImage(key);
                        changeScore(50);
                        updateStatus();
                        // To prevent the hero becoming invisible (blocked by the new door tile) we have to do this
                        gui.removeImage(this);
                        // Creates a similar Door object (same attributes except key) but DoorOpen instead of DoorClosed
                        currentRoom.openDoorWithKey(doorClosed);
                        gui.addImage(this);
                    }
                }


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
        if (!levelManager.getGameOver()) {
            Room currentRoom = levelManager.getCurrentRoom();
            Integer keyCode = (Integer) arg;
            ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
            updateStatus();
            /// Check if Hero HP goes to 0 to trigger the end of the game
            if (currentHP == 0) {
                lives--;
                levelManager.gameOver(getScore());
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                Position newPos = getPosition().plus(Direction.DOWN.asVector());
                // Handles all the tests and collisions for the required position
                moveHero(newPos);
                // Moves all the enemies on the room when here moves as well
                currentRoom.moveEnemiesRoom();
                // Used for fireball direction, fireball is sent always in the last direction received
                // It's defaulted to VK_UP at the start if no direction has been received
                lastKeycode = (Integer) KeyEvent.VK_DOWN;
            }
            if (keyCode == KeyEvent.VK_UP) {
                Position newPos = getPosition().plus(Direction.UP.asVector());
                moveHero(newPos);
                currentRoom.moveEnemiesRoom();
                lastKeycode = (Integer) KeyEvent.VK_UP;
            }
            if (keyCode == KeyEvent.VK_LEFT) {
                Position newPos = getPosition().plus(Direction.LEFT.asVector());
                moveHero(newPos);
                currentRoom.moveEnemiesRoom();
                lastKeycode = (Integer) KeyEvent.VK_LEFT;
            }
            if (keyCode == KeyEvent.VK_RIGHT) {
                Position newPos = getPosition().plus(Direction.RIGHT.asVector());
                moveHero(newPos);
                currentRoom.moveEnemiesRoom();
                lastKeycode = (Integer) KeyEvent.VK_RIGHT;
            }
            if (keyCode == KeyEvent.VK_1) {
                // Check if respective item slot has any item (otherwise do nothing)
                if (currentItems.containsKey(1)) {
                    FloorInteractables item = currentItems.get(1);
                    currentItems.remove(1);
                    gui.removeStatusImage(item);
                    updateStatus();
                    // If the item is a healing item (GoodMeat or Potion), the item is consumed when the slot is selected
                    if (item instanceof GoodMeat) {
                        changeHP(item.getDamage());
                        updateStatus();
                    } else if (item instanceof Potion) {
                        // Potion heals and adds damage at the same time
                        changeHP(item.getDamage());
                        changeDamage(item.getDamage()/4);
                        updateStatus();
                    } else { // If it's not a healing item, it's a weapon,
                        // this means we have to take back the added damage and score
                        // This prevents the hero from "farming" score and damage
                        changeDamage(-item.getDamage());
                        changeScore(-item.getScore());
                        item.setPosition(getPosition());
                        // Add object to room again when dropped , but must remove and add Hero image so it always appears on top of item
                        gui.removeImage(this);
                        currentRoom.addObject(item);
                        gui.addImage(this);
                    }
                }
            }
            if (keyCode == KeyEvent.VK_2) {
                if (currentItems.containsKey(2)) {
                    FloorInteractables item = currentItems.get(2);
                    currentItems.remove(2);
                    gui.removeStatusImage(item);
                    updateStatus();
                    if (item instanceof GoodMeat) {
                        changeHP(item.getDamage());
                        updateStatus();
                    } else if (item instanceof Potion) {
                        changeHP(item.getDamage());
                        changeDamage(item.getDamage()/4);
                        updateStatus();
                    } else {
                        changeDamage(-item.getDamage());
                        changeScore(-item.getScore());
                        item.setPosition(getPosition());
                        gui.removeImage(this);
                        currentRoom.addObject(item);
                        gui.addImage(this);
                    }

                }
            }
            if (keyCode == KeyEvent.VK_3) {
                if (currentItems.containsKey(3)) {
                    FloorInteractables item = currentItems.get(3);
                    currentItems.remove(3);
                    gui.removeStatusImage(item);
                    updateStatus();
                    if (item instanceof GoodMeat) {
                        changeHP(item.getDamage());
                        updateStatus();
                    } else if (item instanceof Potion) {
                        changeHP(item.getDamage());
                        changeDamage(item.getDamage()/4);
                        updateStatus();
                    } else {
                        changeDamage(-item.getDamage());
                        changeScore(-item.getScore());
                        item.setPosition(getPosition());
                        gui.removeImage(this);
                        currentRoom.addObject(item);
                        gui.addImage(this);
                    }
                }
            }
            // Throws fireballs
            if (keyCode == KeyEvent.VK_SPACE) {
                if (fireballs > 0) {
                    Position pos = new Position(0, 0);
                    // Gets fireball objects from status bar
                    Fire fireball = (Fire) getStatusPosition(pos);
                    fireballs--;
                    fireball.setPosition(getPosition());
                    gui.addImage(fireball);
                    if (lastKeycode == KeyEvent.VK_UP) {
                        FireBallThread fireBallThread = new FireBallThread(Direction.UP, fireball);
                        fireBallThread.start();
                    } else if (lastKeycode == KeyEvent.VK_DOWN) {
                        FireBallThread fireBallThread = new FireBallThread(Direction.DOWN, fireball);
                        fireBallThread.start();
                    } else if (lastKeycode == KeyEvent.VK_LEFT) {
                        FireBallThread fireBallThread = new FireBallThread(Direction.LEFT, fireball);
                        fireBallThread.start();
                    } else if (lastKeycode == KeyEvent.VK_RIGHT) {
                        FireBallThread fireBallThread = new FireBallThread(Direction.RIGHT, fireball);
                        fireBallThread.start();
                    }
                    updateStatus();
                }

            }
        }
    }
}
