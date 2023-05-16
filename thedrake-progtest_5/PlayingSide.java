package thedrake;

import java.io.PrintWriter;

public enum PlayingSide {
     BLUE,ORANGE;

    public void toJSON(PrintWriter writer) {
        writer.printf("\"%s\"", toString());
    }
}
