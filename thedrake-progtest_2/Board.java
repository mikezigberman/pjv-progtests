package thedrake;

public class Board {
    private final int dimension;
    private final BoardTile[][] tiles;

    // Constructor. Creates a square board of the specified size where all tiles are empty, i.e. BoardTile.EMPTY
    public Board(int dimension) {
        this.dimension = dimension;
        tiles = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                tiles[i][j] = BoardTile.EMPTY;
            }
        }
    }

    // Playing field dimensions
    public int dimension() {
        return dimension;
    }

    // Returns the tile to the selected position.
    public BoardTile at(TilePos pos) {
        return tiles[pos.i()][pos.j()];
    }

    // Creates a new playing field with new tiles. All other tiles stay the same
    public Board withTiles(TileAt... ats) {
        Board copy = new Board(dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                copy.tiles[i][j] = tiles[i][j];
            }
        }
        for (TileAt tileAt : ats) {
            copy.tiles[tileAt.pos.i()][tileAt.pos.j()] = tileAt.tile;
        }
        return copy;
    }

    // Creates a PositionFactory instance to create positions on this board
    public PositionFactory positionFactory() {
        return new PositionFactory(this.dimension());
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}
