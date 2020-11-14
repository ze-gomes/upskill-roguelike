package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.Room;
import pt.upskills.projeto.objects.environment.Door;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LevelManager {
    private static final LevelManager INSTANCE = new LevelManager();
    Map<String, Room> gameLevels = new HashMap<String, Room>();
    Room currentRoom;
    Hero heroInstance;
    TreeMap<String, Integer> highscores = new TreeMap<String, Integer>();

    /**
     * @return Access to the Singleton instance of ImageMatrixGUI
     */
    public static LevelManager getInstance() {
        return INSTANCE;
    }

//    public void startGame(String startLevel){
//        // Read all maps to LevelManager
//        MapReader mapReader  = new MapReader();
//        mapReader.readMaps();
//        // Set start level
//        setCurrentRoom(startLevel);
//        // Init gui and populate level
//        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
//        List<ImageTile> tiles = getCurrentRoom().getRoomImages();
//        Hero hero = new Hero(mapReader.getStartHeroPos());
//        getCurrentRoom().setHero(hero);
//        tiles.add(hero);
//        gui.addObserver(hero);
//        hero.updateStatus();
//        gui.newImages(tiles);
//        gui.go();
//        while (true){
//            gui.update();
//        }
//    }

    public void changeLevel(Hero hero, Door door) {
        Room destRoom = getGameLevel(door.getDestRoom());
        Door destDoor = destRoom.getDoor(door.getDestDoor());
        // Init gui and populate level
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        hero.setPosition(destDoor.getPosition());
        gui.clearImages();
        List<ImageTile> tiles = destRoom.getRoomImages();
        tiles.add(hero);
        gui.newImages(tiles);
        setCurrentRoom(door.getDestRoom());
        getCurrentRoom().setHero(hero);
        getHighScore();
    }

    // To save Hero instance for score writing purposes
    public void setHeroInstance(Hero heroInstance) {
        this.heroInstance = heroInstance;
    }

    public void getHighScore() {
        // Obtem a directoria do project para aceder ao ficheiro
        String localDir = System.getProperty("user.dir");
        // Correct path using localDir + relative path to the project
        File file = new File(localDir + "\\highscores\\highscores.txt");
        try {// carrega todos os ficheiros
            Scanner fileScanner = new Scanner(file);
            int numLinha = 0;
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                // Read only highScore lines
                System.out.println(linha);
                if (!(linha.charAt(0) == '-' || linha.charAt(0) == ' ')) {
                    String[] score = linha.split(" ");
                    int pos = Integer.parseInt(String.valueOf(score[0].charAt(0)));
                    String nome = score[1];
                    int highscore = Integer.parseInt(score[3]);
                    highscores.put(nome, highscore);
                }
            }
            // Fecha o fileScanner
            fileScanner.close();

        } catch (FileNotFoundException e) {
            // Apanha e trata o erro
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
        System.out.println(highscores);
    }

    public void updateHighScore() {
        // Obtem a directoria do project para aceder ao ficheiro
        String localDir = System.getProperty("user.dir");
        // Correct path using localDir + relative path to the project
        File file = new File(localDir + "\\highscores\\highscores.txt");
        try {// carrega todos os ficheiros
            Scanner fileScanner = new Scanner(file);
            int numLinha = 0;
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                // Read only highScore lines
                System.out.println(linha);
                if (!(linha.charAt(0) == '-' || linha.charAt(0) == ' ')) {
                    String[] score = linha.split(" ");
                    int pos = Integer.parseInt(String.valueOf(score[0].charAt(0)));
                    System.out.println(pos);
                    String nome = score[1];
                    System.out.println(nome);
                    int highscore = Integer.parseInt(score[3]);
                    System.out.println(highscore);
                    highscores.put(nome, highscore);
                }
            }
            // Fecha o fileScanner
            fileScanner.close();

        } catch (FileNotFoundException e) {
            // Apanha e trata o erro
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
    }




    public int getTotalScore() {
        return heroInstance.getScore();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String room) {
        currentRoom = gameLevels.get(room);
    }

    public Room getGameLevel(String name) {
        return gameLevels.get(name);
    }


    public void addGameLevel(String name, Room room) {
        gameLevels.put(name, room);
    }

}
