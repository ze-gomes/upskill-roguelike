package pt.upskills.projeto.game;

import pt.upskills.projeto.game.exceptions.MapDoorFormatException;
import pt.upskills.projeto.game.exceptions.MapFormatException;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Room;
import pt.upskills.projeto.objects.environment.*;
import pt.upskills.projeto.objects.items.*;
import pt.upskills.projeto.objects.mobs.*;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MapReader {
    private List<ImageTile> readMapObjects;
    private Position heroPos;  // Stores hero position for first map load
    private HashMap<Integer, Door> listaPortas;
    private String doorKey; // Stores keyCode
    private List<ImageTile> readMapImages; // Final list of objects to load the room


    // Reads all maps in /rooms directory
    public void readMaps() {
        String localDir = System.getProperty("user.dir");
        // Correct path using localDir + relative path to the project
        File[] fileArray = new File(localDir + "\\rooms\\").listFiles();
        // Gets levelManager singleton to load all the game levels
        LevelManager levelManager = LevelManager.getInstance();
        try {
            for (File f : fileArray) {
                // For each file, creates and stores a list of objects ImageTile and a HashMap of doors
                readMapImages = new ArrayList<ImageTile>();
                readMapObjects = new ArrayList<ImageTile>();
                listaPortas = new HashMap<Integer, Door>();
                Scanner fileScanner = new Scanner(f);
                // Adds the floor to the whole room first (to avoid cases where the floor overlaps items/enemies)
                addFloor();
                int numLinha = 0;
                while (fileScanner.hasNextLine()) {
                    String linha = fileScanner.nextLine();
                    // Read and parses info for each of the doors in the room
                    if (linha.charAt(0) == '#') {
                        String[] porta = linha.split(" ");
                        try {
                            lePortas(porta);
                        } catch (MapDoorFormatException e) {
                            System.err.println("Error reading doors for map: " + f.getName());
                            e.printStackTrace();
                        }
                    } else { // Read and parses info for the map
                        try {
                            readLine(linha, numLinha);
                        } catch (MapFormatException e) {
                            System.err.println("Error reading map: " + f.getName());
                            e.printStackTrace();
                        }
                        numLinha++;
                    }
                }
                // Append all objects that are not Floor or Grass to the final list
                readMapImages.addAll(readMapObjects);
                // Create room for each file and add to levelManager
                levelManager.addGameLevel(f.getName(), new Room(f.getName(), readMapImages, listaPortas));
                fileScanner.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
    }


    public Position getStartHeroPos() {
        return heroPos;
    }


    // Parses door info
    public void lePortas(String[] porta) throws MapDoorFormatException {
        // Create exception
        MapDoorFormatException mapDoorFormatException = new MapDoorFormatException("Incorrect format for door provided with map!");
        // Skips initial character separating door info
        if (porta.length > 1) {
            // Accepted string formats
            if (porta.length == 5 || porta.length == 6) {
                int numDoor = Integer.parseInt(porta[1]);
                String doorType = porta[2];
                String destRoom = porta[3];
                int destDoor = Integer.parseInt(porta[4]);
                // Open/Closed Doors
                if (doorType.equals("D")) {
                    // verifies if the door has a key associated
                    // Only if the door has a key associated it creates a closed door
                    if (porta.length == 6) { // Door closed
                        doorKey = porta[5];
                        // Create doors initially with (0,0) positions and update positions later when reading map
                        // DoorClosed
                        Door portaFechada = new DoorClosed(new Position(0, 0), numDoor, destRoom, destDoor, doorKey);
                        listaPortas.put(numDoor, portaFechada);
                    } else { //Door Open
                        Door portaAberta = new DoorOpen(new Position(0, 0), numDoor, destRoom, destDoor);
                        listaPortas.put(numDoor, portaAberta);
                    }
                    // Doorway
                } else if (doorType.equals("E") && porta.length == 5) {
                    Door semPorta = new DoorWay(new Position(0, 0), numDoor, destRoom, destDoor);
                    listaPortas.put(numDoor, semPorta);
                } else {
                    throw mapDoorFormatException;
                }
            }
            // Map Door format exception handling Door string split can only have length up to 6
            else {
                throw mapDoorFormatException;
            }
        }
    }


    //Adds floor below things
    public void addFloor() {
        Position pos;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                pos = new Position(x, y);
                ImageTile f = new Floor(pos);
                readMapImages.add(f);
            }
        }
    }


    // Create the corresponding objects for each line of the map read and add them to the list
    public void readLine(String line, int coordY) throws MapFormatException {
        Position pos;
        // Handle exceptions for wrong map format, the maps should still load fine
        // because it doesn't actually render to the GUI any out of bounds position)
        if (line.length() != 10) {
            throw new MapFormatException("Incorrect format for map provided, it exceeds the game bounds!");
        } else if (coordY >= 10) {
            throw new MapFormatException("Incorrect format for map provided, it exceeds the game bounds!");
        }
        for (int i = 0; i < line.length(); i++) {
            char elementToConvert = line.charAt(i);
            switch (elementToConvert) {
                case 'W':
                    pos = new Position(i, coordY);
                    ImageTile w = new Wall(pos);
                    readMapObjects.add(w);
                    break;
                case 'H':
                    heroPos = new Position(i, coordY);
                    break;
                case 'S':
                    pos = new Position(i, coordY);
                    Enemy skeleton = new Skeleton(pos);
                    readMapObjects.add(skeleton);
                    break;
                case 'G':
                    pos = new Position(i, coordY);
                    Enemy badGuy = new BadGuy(pos);
                    readMapObjects.add(badGuy);
                    break;
                case 'B':
                    pos = new Position(i, coordY);
                    Enemy bat = new Bat(pos);
                    readMapObjects.add(bat);
                    break;
                case 'R':
                    pos = new Position(i, coordY);
                    Enemy rat = new Rat(pos);
                    readMapObjects.add(rat);
                    break;
                case 'P':
                    pos = new Position(i, coordY);
                    Enemy scorpion = new Scorpion(pos);
                    readMapObjects.add(scorpion);
                    break;
                case 'T':
                    pos = new Position(i, coordY);
                    Enemy thief = new Thief(pos);
                    readMapObjects.add(thief);
                    break;
                case 's':
                    pos = new Position(i, coordY);
                    FloorInteractables sword = new Sword(pos);
                    readMapObjects.add(sword);
                    break;
                case 'h':
                    pos = new Position(i, coordY);
                    FloorInteractables hammer = new Hammer(pos);
                    readMapObjects.add(hammer);
                    break;
                case 'g':
                    pos = new Position(i, coordY);
                    ImageTile grass = new Grass(pos);
                    // adds grass to the floor list, so it doesnt overlap items and enemies
                    readMapImages.add(grass);
                    break;
                case 'k':
                    pos = new Position(i, coordY);
                    FloorInteractables key = new Key(pos, doorKey);
                    readMapObjects.add(key);
                    break;
                case 'a':
                    pos = new Position(i, coordY);
                    FloorInteractables axe = new Axe(pos);
                    readMapObjects.add(axe);
                    break;
                case 't':
                    pos = new Position(i, coordY);
                    FloorInteractables trap = new Trap(pos);
                    readMapObjects.add(trap);
                    break;
                case 'm':
                    pos = new Position(i, coordY);
                    FloorInteractables goodMeat = new GoodMeat(pos);
                    readMapObjects.add(goodMeat);
                    break;
                case 'p':
                    pos = new Position(i, coordY);
                    FloorInteractables potion = new Potion(pos);
                    readMapObjects.add(potion);
                    break;
                case '0':
                    pos = new Position(i, coordY);
                    Door porta0 = listaPortas.get(0);
                    porta0.setPosition(pos);
                    readMapObjects.add(porta0);
                    break;
                case '1':
                    pos = new Position(i, coordY);
                    Door porta1 = listaPortas.get(1);
                    porta1.setPosition(pos);
                    readMapObjects.add(porta1);
                    break;
                case '2':
                    pos = new Position(i, coordY);
                    Door porta2 = listaPortas.get(2);
                    porta2.setPosition(pos);
                    readMapObjects.add(porta2);
                    break;
                default: // if character is not recognized/added, create no object
                    break;
            }
        }
    }


}
