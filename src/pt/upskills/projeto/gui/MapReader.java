package pt.upskills.projeto.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class MapReader {


    public void readMap(String nomeFicheiro){
            // Obtem a directoria do project para aceder ao ficheiro
            String localDir = System.getProperty("user.dir");
            // Obtem a localizaçao correcta do ficheiro juntando o caminho localDir + o caminho relativo do projecto em que se está a trabalhar
            // Este caminho é totalmente variável e depende da organizaçao do projecto de cada um e tambem se usam Mac, Windows ou Linux
            File f = new File(localDir + "\\rooms\\" + nomeFicheiro);
            try {
                // Abre o ficheiro com o Scanner
                Scanner fileScanner = new Scanner(f);
                // Enqaunto houver linhas pra fazer scan, continua
                while (fileScanner.hasNextLine()) {
                    String linha = fileScanner.nextLine();
                    for (MapCodes dir : MapCodes.values()) {
                        System.out.println(dir.getCodigo());
                        // do what you want
                    }
                } // Fecha o fileScanner
                fileScanner.close();
            } catch (FileNotFoundException e) {
                // Apanha e trata o erro
                System.out.println("Ficheiro não encontrado");
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {
        MapReader map = new MapReader();
        map.readMap("room0.txt");
    }



}
