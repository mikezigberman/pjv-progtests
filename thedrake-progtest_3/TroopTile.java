package thedrake;

public class TroopTile implements Tile {
    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;


    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side() {
        return side;
    }

    public TroopFace face() {
        return face;
    }

    public Troop troop() {
        return troop;
    }

    public boolean canStepOn() {
        return false;
    }

    public boolean hasTroop() {
        return true;
    }

    public TroopTile flipped() {
        if (this.face == TroopFace.AVERS) {
            return new TroopTile(this.troop(), this.side(), TroopFace.REVERS);
        } else {
            return new TroopTile(this.troop(), this.side(), TroopFace.AVERS);
        }
    }

}


