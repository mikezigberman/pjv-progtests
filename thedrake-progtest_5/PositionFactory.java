package thedrake;

public class PositionFactory {
  private final int dimension;
  
  public PositionFactory(int dimension) {
    if(dimension < 0)
      throw new IllegalArgumentException("The dimension needs to be positive.");
    
    this.dimension = dimension;
  }
  
  public int dimension() {
    return dimension;
  }
  
  public BoardPos pos(int i, int j) {
    return new BoardPos(dimension, i, j);
  }
  
  public BoardPos pos(char column, int row) {
    return pos(ifrCol(column), jfrCol(row));
  }
  
  public BoardPos pos(String pos) {
    return pos(pos.charAt(0), Integer.parseInt(pos.substring(1)));
  }
  
  private int ifrCol(char column) {
    return column - 'a';
  }
  
  private int jfrCol(int row) {
    return row - 1;
  }
}
