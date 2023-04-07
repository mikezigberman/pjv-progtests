package thedrake;

import java.util.ArrayList;
import java.util.List;

public class BoardPos implements TilePos {
  private final int dimension;
  private final int i;
  private final int j;

  public BoardPos(int dimension, int i, int j) {
    this.dimension = dimension;
    this.i = i;
    this.j = j;
  }

  @Override
  public int i() {
    return i;
  }

  @Override
  public int j() {
    return j;
  }

  @Override
  public char column() {
    return (char) ('a' + i);
  }

  @Override
  public int row() {
    return j + 1;
  }

  public TilePos step(int columnStep, int rowStep) {
    int newi = i + columnStep;
    int newj = j + rowStep;

    if ((newi >= 0 && newi < dimension) && (newj >= 0 && newj < dimension)) {
      return new BoardPos(dimension, newi, newj);
    }

    return TilePos.OFF_BOARD;
  }

  @Override
  public TilePos step(Offset2D step) {
    return step(step.x, step.y);
  }

  @Override
  public List<BoardPos> neighbours() {
    List<BoardPos> result = new ArrayList<>();
    TilePos pos = step(1, 0);
    if(pos != TilePos.OFF_BOARD)
      result.add((BoardPos)pos);

    pos = step(-1, 0);
    if(pos != TilePos.OFF_BOARD)
      result.add((BoardPos)pos);

    pos = step(0, 1);
    if(pos != TilePos.OFF_BOARD)
      result.add((BoardPos)pos);

    pos = step(0, -1);
    if(pos != TilePos.OFF_BOARD)
      result.add((BoardPos)pos);

    return result;
  }

  @Override
  public boolean isNextTo(TilePos pos) {
    if (pos == TilePos.OFF_BOARD) {
      return false;
    }

    int di = Math.abs(this.i - pos.i());
    int dj = Math.abs(this.j - pos.j());
    return (di == 1 && dj == 0) || (di == 0 && dj == 1);
  }

  @Override
  public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
    return side == PlayingSide.BLUE ? step(dir) : step(dir.yFlipped());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + i;
    result = prime * result + j;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    BoardPos other = (BoardPos) obj;
    return i == other.i && j == other.j;
  }

  @Override
  public boolean equalsTo(int i, int j) {
    return this.i == i && this.j == j;
  }

  @Override
  public String toString() {
    return String.format("%c%d", column(), row());
  }
}