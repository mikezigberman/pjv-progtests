package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {


	private final BoardTile[][] boardA;
	private final int dimensionB;

	public Board(int dimension) {
		this.dimensionB = dimension;
		this.boardA = new BoardTile[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				boardA[i][j] = BoardTile.EMPTY;
			}
		}
	}


	public int dimension() {
		return dimensionB;
	}


	public BoardTile at(TilePos pos) {
		return this.boardA[pos.i()][pos.j()];
	}


	public Board withTiles(TileAt ...ats) {
		Board newBoard = new Board(dimensionB);
		for (int i = 0; i < dimensionB; i++) {
			System.arraycopy(boardA[i], 0, newBoard.boardA[i], 0, dimensionB);
		}

		for (TileAt tile : ats)
			newBoard.boardA[tile.pos.i()][tile.pos.j()] = tile.tile;

		return newBoard;
	}


	public PositionFactory positionFactory() {
		return new PositionFactory(dimensionB);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"dimension\":%d", dimensionB);
		writer.printf(",\"tiles\":[");
		int count = 0;
		for (int i = 0; i < dimensionB; i++) {
			for (int j = 0; j < dimensionB; j++) {
				boardA[j][i].toJSON(writer);
				count++;
				if (count < dimensionB * dimensionB)
					writer.printf(",");
			}
		}
		writer.printf("]}");
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

