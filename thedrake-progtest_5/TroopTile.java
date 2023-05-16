package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile, JSONSerializable{

    private Troop ptroop;
    private PlayingSide pside;
    private TroopFace pface;


    public TroopTile(Troop troop, PlayingSide side, TroopFace face)
    {
        this.ptroop = troop;
        this.pside = side;
        this.pface = face;

    }

    public PlayingSide side()
    {
        return pside;
    }

    public TroopFace face()
    {
        return pface;
    }

    public Troop troop()
    {
        return ptroop;
    }

    @Override
    public boolean canStepOn() {
        return false;
    }

    @Override
    public boolean hasTroop() {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> result = new ArrayList<>();
        List<TroopAction> actions = ptroop.actions(pface);

        for (TroopAction it: actions)
            result.addAll(it.movesFrom(pos, pside, state));

        return result;
    }
    public TroopTile flipped()
    {
       if (pface == TroopFace.AVERS) {
            return new TroopTile(this.ptroop, this.pside, TroopFace.REVERS);
       }
       return new TroopTile(this.ptroop, this.pside, TroopFace.AVERS);
    }
    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{\"troop\":");
        ptroop.toJSON(writer);
        writer.printf(",\"side\":");
        pside.toJSON(writer);
        writer.printf(",\"face\":");
        pface.toJSON(writer);
        writer.printf("}");
    }

}
