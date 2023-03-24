package thedrake;

public interface Tile {
    /**
     * Returns true if the tile is free and can be stepped on.
     */
    boolean canStepOn();

    /**
     * Returns true if there is an army on the tile.
     */
    boolean hasTroop();
}