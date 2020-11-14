package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.objects.Hero;
import pt.upskills.projeto.objects.Room;
import pt.upskills.projeto.objects.environment.Door;
import pt.upskills.projeto.objects.other.GameOver1;
import pt.upskills.projeto.objects.other.GameOver2;
import pt.upskills.projeto.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LevelManager {
    private static final LevelManager INSTANCE = new LevelManager();
    Map<String, Room> gameLevels = new HashMap<String, Room>();
    Room currentRoom;
    Hero heroInstance;
    List<Score> highscores = new ArrayList<Score>();
    boolean gameOverStatus = false;

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
    }

    // To save Hero instance for score writing purposes
    public void setHeroInstance(Hero heroInstance) {
        this.heroInstance = heroInstance;
    }

    public void getHighScores() {
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
                    Score s = new Score(nome, highscore);
                    highscores.add(s);
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

    public boolean getGameOver(){
        return gameOverStatus;
    }

    public void updateHighScore() {
        // Obtem a directoria do project para aceder ao ficheiro
        String localDir = System.getProperty("user.dir");
        Path path = Paths.get(localDir + "\\highscores\\highscores.txt");
        try {// carrega todos os ficheiros
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 3, u = 1, x = 0; i < 8; i++, u++, x++) {
                String data = u + ". " + highscores.get(x).name + " - " + highscores.get(x).score;
                lines.set(i, data);
                Files.write(path, lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            // Apanha e trata o erro
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
    }



    // Check if score made the high score list
    public boolean checkifHighScore(int score) {
        getHighScores();
        Collections.sort(highscores);
        System.out.println(highscores);
        for (int i = 0; i < 5; i++) {
            if (score > highscores.get(i).score) {
                return true;
            }
        }
        return false;
    }


    public void gameOver(int score) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        if (checkifHighScore(score)){
            System.out.println("Parabéns! Conseguiste ficar no Top5 de scores deste jogo!");
            System.out.println("Introduz o teu nome para a ser gravado na lista:");
            try {
                Scanner scanner = new Scanner(System.in);
                //  No spaces allowed
                String nome = scanner.nextLine();
                nome = nome.replace(" ", "-");
                Score s = new Score(nome, score);
                highscores.add(s);
                Collections.sort(highscores);
            } catch (NoSuchElementException e){
                System.out.println("Nao introduziste nenhum nome, o resultado não foi gravado");
            }
        }
        updateHighScore();
        Position pos1 = new Position(4,4);
        Position pos2 = new Position(5,4);
        ImageTile ga1 = new GameOver1(pos1);
        ImageTile ga2 = new GameOver2(pos2);
        gui.addImage(ga1);
        gui.addImage(ga2);
        gameOverStatus = true;


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
