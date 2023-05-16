package thedrake;

import java.util.ArrayList;
import java.util.List;

public class Board {
	BoardTile [][] boardA;
	int dimensionB;

	public Board(int dimension) {
		this.boardA = new BoardTile[dimension][dimension];
		this.dimensionB = dimension;
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
		this.dimensionB = dimension;
		this.boardA = board;
	}

	// Rozměr hrací desky
	public int dimension() {

		return this.dimensionB;
	}

	public BoardTile at(TilePos pos) {

		return boardA[pos.i()][pos.j()];
	}

	public Board withTiles(TileAt ... ats) {
		BoardTile[][] newBoard = new BoardTile[this.dimensionB][this.dimensionB];
		for (int i = 0 ; i < this.dimensionB ; i++){
			newBoard[i] = this.boardA[i].clone();
		}
		for (TileAt it : ats){
			newBoard[it.pos.i()][it.pos.j()] = it.tile;
		}
		return new Board(dimensionB, newBoard);
	}

	// Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
	public PositionFactory positionFactory() {

		return new PositionFactory(this.dimensionB);
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

