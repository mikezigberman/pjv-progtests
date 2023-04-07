package thedrake;

import java.util.*;

public class Board {
	private final BoardTile[][] boardA;
	int dimension;

	public Board(int dimension) {
		this.boardA = new BoardTile[dimension][dimension];
		this.dimension = dimension;
		for (int i = 0 ; i < dimension ; i++)
		{
			for (int j = 0 ; j < dimension ; j++)
			{
				boardA[i][j] = BoardTile.EMPTY;
			}
		}
	}

	public Board(int dimension, BoardTile [][] board)
	{
		this.dimension = dimension;
		this.boardA = board;
	}

	public int dimension() {
		return this.dimension;
	}

	public BoardTile at(TilePos pos) {

		return boardA[pos.i()][pos.j()];
	}

	public Board withTiles(TileAt ... ats) {
		BoardTile[][] newBoard = new BoardTile[this.dimension][this.dimension];
		for (int i = 0 ; i < this.dimension ; i++){
			newBoard[i] = this.boardA[i].clone();
		}
		for (TileAt it : ats) {
			newBoard[it.pos.i()][it.pos.j()] = it.tile;
		}
		return new Board(dimension, newBoard);
	}


	public PositionFactory positionFactory() {

		return new PositionFactory(this.dimension);
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

