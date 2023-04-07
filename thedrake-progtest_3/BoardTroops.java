package thedrake;

import java.util.*;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition  = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        return Optional.ofNullable(this.troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide == PlayingSide.BLUE ? PlayingSide.BLUE : PlayingSide.ORANGE;
    }

    public TilePos leaderPosition() {
        return this.leaderPosition;
    }

    public int guards() {
        return this.guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition() != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards() < 2;
    }

    public Set<BoardPos> troopPositions() {
        Set<BoardPos> boardPosisSet = new HashSet<>();
        this.troopMap.forEach((key, value) -> {
            if (value.hasTroop()) boardPosisSet.add(key);
        });
        return boardPosisSet;
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (troopMap.containsKey(target))
            throw new IllegalArgumentException("Cannot place on tile.");

        TroopTile newtTile = new TroopTile(troop, playingSide, TroopFace.AVERS);
        Map<BoardPos, TroopTile> map = new HashMap<>(troopMap);
        map.put(target, newtTile);

        if (isPlacingGuards())
            return new BoardTroops(playingSide(), map, leaderPosition(), guards() + 1);
        else if (!isLeaderPlaced())
            return new BoardTroops(playingSide(), map, target, guards());
        else
            return new BoardTroops(playingSide(), map, leaderPosition(), guards());
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        BoardTroops boardTroops;
        if(this.isPlacingGuards())
        { throw new IllegalStateException("Cannot move troops before guards are placed."); }
        if(!this.isLeaderPlaced())
        { throw new IllegalStateException("Cannot move troops before the leader is placed."); }
        if( this.at(target).isPresent() )
        { throw new IllegalArgumentException(); }
        if(!at(origin).isPresent()) { throw new IllegalArgumentException(); }
        Map<BoardPos, TroopTile> newTroop = new HashMap<>(troopMap);
        if( !this.leaderPosition().equals(origin) )
        { boardTroops  = new BoardTroops(this.playingSide(), newTroop, this.leaderPosition(), this.guards()); } else { boardTroops  = new BoardTroops(this.playingSide(), newTroop, TilePos.OFF_BOARD, this.guards()); }
        boardTroops = boardTroops.placeTroop(boardTroops.troopMap.get(origin).troop(), target);
        if( boardTroops.troopMap.get(origin).face() == boardTroops.troopMap.get(target).face() )
        { boardTroops = boardTroops.troopFlip(target); }
        boardTroops = boardTroops.removeTroop(origin);
        return boardTroops;
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if(isPlacingGuards())
        {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        if(!isLeaderPlaced())
        {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }
        if(!at(origin).isPresent()) {
            throw new IllegalArgumentException();
        }
        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        TilePos newPosit;
        if(!this.isLeaderPlaced())
        {
            throw new IllegalStateException("Cannot move troops before the leader is placed.");
        }

        if(this.isPlacingGuards())
        {
            throw new IllegalStateException("Cannot move troops before guards are placed.");
        }

        if(!at(target).isPresent()) {
            throw new IllegalArgumentException();
        }

        Map<BoardPos, TroopTile> newTroop = new HashMap<>(troopMap);
        TilePos tilePos = (this.leaderPosition().equals(target)) ? (newPosit = TilePos.OFF_BOARD) : (newPosit = this.leaderPosition());
        newTroop.remove(target);
        return  new BoardTroops(this.playingSide(), newTroop, newPosit, this.guards());
    }
}

