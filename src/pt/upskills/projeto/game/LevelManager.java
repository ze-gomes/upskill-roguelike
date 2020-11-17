package pt.upskills.projeto.game;

import pt.upskills.projeto.gui.ImageMatrixGUI;
import pt.upskills.projeto.gui.ImageTile;
import pt.upskills.projeto.gui.MapReader;
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
    private static final LevelManager INSTANCE = new LevelManager(); // Level Manager Singleton to handle the game operations and states
    Map<String, Room> gameLevels = new HashMap<String, Room>(); // Stores all the loaded rooms
    Room currentRoom;
    Hero heroInstance;
    List<Score> highscores = new ArrayList<Score>(); // List of Scores read from files
    boolean gameOverStatus = false; // Store if game has ended
    String savedLevel = "room0.txt";

    /**
     * @return Access to the Singleton instance of ImageMatrixGUI
     */
    public static LevelManager getInstance() {
        return INSTANCE;
    }


    // Test function for game restart
    public void restartGame() {
        System.out.println("Game Restarting");
        gameOverStatus = false;
        // Read all maps to LevelManager again
        MapReader mapReader = new MapReader();
        mapReader.readMaps();
        // Set start level
        setCurrentRoom("room0.txt");
        savedLevel = "room0.txt";
        // Init gui and populate level
        List<ImageTile> tiles = getCurrentRoom().getRoomImages();
        currentRoom.setHero(heroInstance);
        setHeroInstance(heroInstance);
        // Resets all hero defaults (score, life, etc)
        heroInstance.resetState();
        heroInstance.setLives(3);
        heroInstance.setPosition(mapReader.getStartHeroPos());
        tiles.add(heroInstance);
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gui.update();
        heroInstance.updateStatus();
        gui.newImages(tiles);
    }

    // Restarts to saved level stored to door 0 . Subtracts score because of the death
    public void restartSavedLevel() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        gameOverStatus = false;
        heroInstance.resetState();
        heroInstance.updateStatus();
        if (heroInstance.getLives() > 0) {
            if (savedLevel.equals("room0.txt")) {
                changeLevel(heroInstance, getGameLevel(savedLevel).getDoor(0));
            }
            else {
                heroInstance.changeScore(-200);
                changeLevel(heroInstance, getGameLevel(savedLevel).getDoor(1));
            }
        }
        gui.update();

    }

    public List<Score> getHighscoresList() {
        return highscores;
    }

    // Changes level when going through a door.
    // Receives the hero and a door
    public void changeLevel(Hero hero, Door door) {
        // If it's the last level, game is over when you enter the door
        if (door.getDestRoom().equals("gameOver")) {
            // Add 200 score for completing the game and trigger the Game Over
            hero.changeScore(200);
            gameOver(hero.getScore());
        } else {  // Init gui and populate level if it's a normal level change
            Room destRoom = getGameLevel(door.getDestRoom());
            Door destDoor = destRoom.getDoor(door.getDestDoor());
            ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
            gui.update();
            // Places hero in the correct destination door in the new room
            hero.setPosition(destDoor.getPosition());
            gui.clearImages();
            List<ImageTile> tiles = destRoom.getRoomImages();
            tiles.add(hero);
            gui.newImages(tiles);
            setCurrentRoom(door.getDestRoom());
            getCurrentRoom().setHero(hero);
        }
        // Saves every 2 levels depending on the door dest Room
        switch (door.getDestRoom()) {
            case "room2.txt":
                savedLevel = "room2.txt";
                break;
            case "room4.txt":
                savedLevel = "room4.txt";
                break;
            default:
                break;
        }
    }

    // To save Hero instance for score writing purposes
    public void setHeroInstance(Hero heroInstance) {
        this.heroInstance = heroInstance;
    }

    public Hero getHeroInstance() {
        return heroInstance;
    }

    public void setGameOverStatus(boolean gameOverStatus) {
        this.gameOverStatus = gameOverStatus;
    }

    // Gets highscores from files
    public void getHighScores() {
        highscores = new ArrayList<>();
        String localDir = System.getProperty("user.dir");
        // Correct path using localDir + relative path to the project
        File file = new File(localDir + "\\highscores\\highscores.txt");
        try {
            Scanner fileScanner = new Scanner(file);
            int numLinha = 0;
            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();
                // Read only highScore lines
                if (!(linha.charAt(0) == '-' || linha.charAt(0) == ' ')) {
                    String[] score = linha.split(" ");
                    int pos = Integer.parseInt(String.valueOf(score[0].charAt(0)));
                    String nome = score[1];
                    int highscore = Integer.parseInt(score[3]);
                    // Parses the info from the scores and creates a Score object for each high score, adds the object to a list of highscores to help sort them
                    Score s = new Score(nome, highscore);
                    highscores.add(s);
                }
            }
            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
    }


    public boolean getGameOver() {
        return gameOverStatus;
    }


    // Updates highscores (rewrites over the file with the updated info from the ordered Score array)
    public void updateHighScore() {
        // Obtem a directoria do project para aceder ao ficheiro
        String localDir = System.getProperty("user.dir");
        Path path = Paths.get(localDir + "\\highscores\\highscores.txt");
        try {// carrega todos os ficheiros
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            // Need 3 different counters, for the Highscore position, array index and line number
            for (int i = 3, u = 1, x = 0; i < 8; i++, u++, x++) {
                String data = u + ". " + highscores.get(x).name + " - " + highscores.get(x).score;
                lines.set(i, data);
                Files.write(path, lines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.out.println("Ficheiro não encontrado");
            e.printStackTrace();
        }
    }


    // Check if a score made the high score list
    public boolean checkifHighScore(int score) {
        // Reads the high scores and stores them in an Score object list
        getHighScores();
        // Orders the list according to score
        Collections.sort(highscores);
        for (int i = 0; i < 5; i++) {
            // if the score is bigger than any of those in the top5, it's a highscore
            if (score > highscores.get(i).score) {
                return true;
            }
        }
        return false;
    }


    // Triggers game over actions
    public void gameOver(int score) {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        // Draws game over visual feedback on the screen
        Position pos1 = new Position(4, 4);
        Position pos2 = new Position(5, 4);
        ImageTile ga1 = new GameOver1(pos1);
        ImageTile ga2 = new GameOver2(pos2);
        gui.addImage(ga1);
        gui.addImage(ga2);
        gameOverStatus = true;
        // Check if a score made it to the list, create score object and add it if so
//        if (checkifHighScore(score)) {
//            System.out.println("Parabéns! Conseguiste ficar no Top5 deste jogo com " + score + " pontos!");
//            System.out.println("Introduz o teu nome para a ser gravado na lista:");
//            try {
//                Scanner scanner = new Scanner(System.in);
//                String nome = scanner.nextLine();
//                //  No spaces allowed for name
//                nome = nome.replace(" ", "-");
//                Score s = new Score(nome, score);
//                highscores.add(s);
//                System.out.println("Obrigado!");
//                scanner.close();
//            } catch (NoSuchElementException e) {
//                System.out.println("Nao introduziste nenhum nome, o resultado não foi gravado");
//            } finally {
//                // Sort the list and update the highscores file with the new order (prints top5 only)
//                Collections.sort(highscores);
//                updateHighScore();
//            }
//        } else {
//            System.out.println("Acabaste o jogo com " + score + " pontos! Não conseguiste ficar no Top5");
//        }
//        try {
//            String localDir = System.getProperty("user.dir");
//            // Print the whole high scores file on the console for user feedback
//            System.out.println(new String(Files.readAllBytes(Paths.get(localDir + "\\highscores\\highscores.txt"))));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
