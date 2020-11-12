package pt.upskills.projeto.rogue.utils;

/**
 * 2D integer position.
 */
public class Position {

    private int x;
    private int y;

    /**
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return horizontal coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return vertical coordinate
     */
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }


    public double calculateDistance(Position p2) {
            return Math.sqrt((p2.getY() - this.getY()) * (p2.getY() - this.getY()) + (p2.getX() - this.getX()) * (p2.getX() - this.getX()));
    }

    public Position plus(Vector2D vector2d) {
        return new Position(getX()+ vector2d.getX(), getY()+ vector2d.getY());
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
