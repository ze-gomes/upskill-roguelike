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
    private Position position;
    private int currentHP; // range 0-8
    private int maxHP = 8;
    private int damage;
    private List<ImageTile> statusImages;
    private int score;
    private HashMap<Integer, FloorInteractables> currentItems;
    private int fireballs;
    private Integer lastKeycode = (Integer) KeyEvent.VK_UP; // Default fireball direction


    public Hero(Position position) {
        super(position);
        this.currentHP = maxHP;
        this.damage = 1;
        this.statusImages = new ArrayList<ImageTile>();
        this.currentItems = new HashMap<Integer, FloorInteractables>();
        this.score = 0;
        this.fireballs = 3;
    }


    // Prevents score from dropping below 0
    public void changeScore(int score) {
        if (this.score + score < 0) {
            this.score = 0;
        } else {
            this.score += score;
        }
    }

    // Prevents damage from dropping below 1
    public void changeDamage(int damage) {
        if (this.damage + damage < 0) {
            this.damage = 1;
        } else {
            this.damage += damage;
        }
    }


    public void changeHP(int damage) {
        if (currentHP + damage > maxHP) {
            this.currentHP = maxHP;
        } else if (currentHP + damage < 0) {
            this.currentHP = 0;
        } else {
            this.currentHP += damage;
        }
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

    public FloorInteractables getKeyfromSlot() {
        for (int i = 1; i <= 3; i++) {
            if (currentItems.get(i) instanceof Key) {
                FloorInteractables item = currentItems.get(i);
                currentItems.remove(i);
                return item;
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
        } // Else if nothing found return null, means it's Black (empty)
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
            } else if (floorItem instanceof Axe) {
                Axe floorAxe = (Axe) floorItem;
                if (itemSlotsAvailable()) {
                    addItem(floorAxe);
                    currentRoom.removeObject(floorAxe);
                    changeScore(floorAxe.getScore());
                    changeDamage(floorAxe.getDamage());
                }
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

    public void moveHero(Position newPos) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        LevelManager levelManager = LevelManager.getInstance();
        Room currentRoom = levelManager.getCurrentRoom();
        // Move if there is no collision and is inside bounds
        if (!checkCollision(newPos) && checkInsideMapBounds(newPos)) {
            // Check if there are items on the floor to pickup
            checkIfGroundAction(newPos);
            // Decrease score on walking
            changeScore(-1);
            setPosition(newPos);
            // If collision is Enemy, do damage and check if enemy dies
        } else if (getCollisionItem() instanceof Enemy) {
            Enemy enemy = (Enemy) getCollisionItem();
            enemy.takeDamage(damage);
            if (enemy.getMobHP() <= 0) {
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
                setPosition(newPos);
                changeScore(-1);
            } else {
                DoorClosed doorClosed = (DoorClosed) getCollisionItem();
                FloorInteractables floorItem = getKeyfromSlot();
                if (floorItem != null) { // Interacting with closed door
                    Key key = (Key) floorItem;
                    if (key.getCode().equals(doorClosed.getKey())) {
                        System.out.println("Opening door " + doorClosed.getNumDoor() + " with " + key.getCode() + " to " + doorClosed.getDestRoom());
                        gui.removeStatusImage(key);
                        changeScore(50);
                        updateStatus();
                        // Para o heroi nao ficar invisivel (debaixo da porta) por esta ter sido adicionada por cima à tiles
                        gui.removeImage(this);
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
            //System.out.println("O Score atual é: " + score);
            updateStatus();
            //System.out.println("Current HP: " + currentHP);
            ///Game Over
            if (currentHP == 0) {
                levelManager.gameOver(getScore());
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                // Posicao a mover dada o input
                Position newPos = getPosition().plus(Direction.DOWN.asVector());
                // Testa se a pos é parede antes de mover, só move se não for parede
                moveHero(newPos);
                currentRoom.moveEnemiesRoom();
                // Used for fireball direction
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
                if (currentItems.containsKey(1)) {
                    FloorInteractables item = currentItems.get(1);
                    currentItems.remove(1);
                    gui.removeStatusImage(item);
                    updateStatus();
                    // Add object to room again when dropped , but must remove and add Hero image so it always appears on top of item
                    if (item instanceof GoodMeat) {
                        changeHP(item.getDamage());
                        updateStatus();
                    } else if (item instanceof Potion) {
                        changeHP(item.getDamage());
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
                        // Adds life and more damage
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
            if (keyCode == KeyEvent.VK_SPACE) {
                if (fireballs > 0) {
                    Position pos = new Position(0, 0);
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
