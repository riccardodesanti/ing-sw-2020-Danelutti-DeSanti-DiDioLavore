package it.polimi.ingsw.model;

import java.util.Objects;

/**
 * <h1>Position</h1>
 * The Position class is used as a Type to
 * represent the position of a pawn in the board
 */
public class Position {
    private int x;
    private int y;

    /**
     * Default constructor
     * @param x This is the abscissa
     * @param y  This is the ordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Abscissa
     */
    public int getX() {
        return x;
    }

    /**
     * @return Ordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @param x This is the abscissa
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y This is the ordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
