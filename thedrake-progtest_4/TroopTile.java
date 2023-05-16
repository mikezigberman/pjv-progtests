package thedrake;

import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile {
    Troop ptroop;
    PlayingSide pside;
    TroopFace pface;

    // Konstruktor
    public TroopTile(Troop troop, PlayingSide side, TroopFace face){
        this.ptroop = troop;
        this.pside = side;
        this.pface = face;
    }

    // Vrací barvu, za kterou hraje jednotka na této dlaždici
    public PlayingSide side(){
        return this.pside;
    }

    // Vrací stranu, na kterou je jednotka otočena
    public TroopFace face(){
        return this.pface;
    }

    // Jednotka, která stojí na této dlaždici
    public Troop troop(){
        return this.ptroop;
    }

    // Vrací False, protože na dlaždici s jednotkou se nedá vstoupit
    public boolean canStepOn(){
        return false;
    }

    // Vrací True
    public boolean hasTroop()
    {
        return true;
    }

    // Vytvoří novou dlaždici, s jednotkou otočenou na opačnou stranu
    // (z rubu na líc nebo z líce na rub)

    public List<Move> movesFrom(BoardPos pos, GameState state)
    {
        List<Move> result = new ArrayList<>();
        List<TroopAction> actions = ptroop.actions(pface);
        for (TroopAction it: actions)
            result.addAll(it.movesFrom(pos, pside, state));
        return result;
    }
    public TroopTile flipped() {
        if (this.pface == TroopFace.AVERS) {
            return new TroopTile(this.troop(), this.pside, TroopFace.REVERS);
        } else {
            return new TroopTile(this.troop(), this.pside, TroopFace.AVERS);
        }
    }
}
