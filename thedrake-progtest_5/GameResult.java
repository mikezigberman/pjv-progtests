package thedrake;

import java.io.PrintWriter;
public enum GameResult {
	 DRAW,VICTORY,IN_PLAY;
    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", toString());
    }
}
