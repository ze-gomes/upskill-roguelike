package pt.upskills.projeto.gui;

public enum MapCodes {
    // Enum com codigos e nomes de cada objecto
    Wall('W'), DoorOpen('0'), Hero('h');
    private char codigo;


    // Contructor com codigo da imagem
    private MapCodes(char codigo) {
        this.codigo  = codigo;
    }


    //Getter do codigo
    public char getCodigo() {
        return codigo;
    }


}
