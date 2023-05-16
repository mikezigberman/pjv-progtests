package thedrake;

import java.util.*;

public class BoardTroops {
    private final PlayingSide plSide;
    private final Map<BoardPos, TroopTile> tMap;
    private final TilePos leadPos;
    private final int guards;
    public BoardTroops(PlayingSide playingSide)
    {
        this.plSide = playingSide;
        this.tMap = Collections.emptyMap();
        this.leadPos = TilePos.OFF_BOARD;
        this.guards = 0;
    }
    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards)
    {
        this.plSide = playingSide;
        this.tMap = troopMap;
        this.leadPos  = leaderPosition;
        this.guards = guards;
        // Místo pro váš kód
    }
    public Optional<TroopTile> at(TilePos pos)
    {
        Optional<TroopTile>troopTile = Optional.ofNullable(this.tMap.get(pos));
        //return (this.tMap.containsKey(pos)) ? troopTile :  Optional.empty();
        if (this.tMap.containsKey(pos))
            return troopTile;
        else
            return Optional.empty();
    }
    public PlayingSide playingSide() {
        if (plSide == PlayingSide.BLUE)
        {
            return PlayingSide.BLUE;
        }
        else
            return PlayingSide.ORANGE;
        // Místo pro váš kód
    }
    public TilePos leaderPosition() {
        return this.leadPos;
        // Místo pro váš kód
    }
    public int guards() {
        return this.guards;
        // Místo pro váš kód
    }
    public boolean isLeaderPlaced() {
        return this.leaderPosition() != TilePos.OFF_BOARD;
        // Místo pro váš kód
    }
    public boolean isPlacingGuards() {
        return this.isLeaderPlaced() && this.guards() < 2;
        // Místo pro váš kód
    }
    public Set<BoardPos> troopPositions() {
        Set<BoardPos>  boardPosisSet = new HashSet<>();
        this.tMap.forEach((key, valeu) -> {if (valeu.hasTroop())
        {
            boardPosisSet.add(key);
        } });
        return boardPosisSet;
    }
    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (this.tMap.containsKey(target))
        {
            throw new IllegalArgumentException("To nelze umístit na dlaždici");
        }
        TroopTile newtTile = new TroopTile(troop,this.plSide, TroopFace.AVERS);
        Map<BoardPos, TroopTile> map =new HashMap<>(tMap);
        map.put(target, newtTile);
        if (this.isPlacingGuards())
        {
            return new BoardTroops(this.playingSide(), map, this.leaderPosition(), this.guards() +1);
        }
        else if (!this.isLeaderPlaced())
        {
                return new BoardTroops(this.playingSide(), map,target, this.guards());
        }
        else {
            return new BoardTroops(this.playingSide(), map, this.leaderPosition(), this.guards());
        }
    }
    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        BoardTroops boardTroops;
        if(this.isPlacingGuards())
        {
            throw new IllegalStateException("Cannot move troops before guards are placed.");
        }
        if(!this.isLeaderPlaced())
        {
            throw new IllegalStateException("Cannot move troops before the leader is placed.");
        }
        if( this.at(target).isPresent() )
        {
            throw new IllegalArgumentException();
        }
        if(!at(origin).isPresent()) {
            throw new IllegalArgumentException();
        }
        Map<BoardPos, TroopTile> newTroop = new HashMap<>(tMap);
        if( !this.leaderPosition().equals(origin) )
        {
            boardTroops  = new BoardTroops(this.playingSide(), newTroop, this.leaderPosition(), this.guards());

        } else {
            boardTroops  = new BoardTroops(this.playingSide(), newTroop, TilePos.OFF_BOARD, this.guards());
        }
        boardTroops = boardTroops.placeTroop(boardTroops.tMap.get(origin).troop(), target);
        if( boardTroops.tMap.get(origin).face() == boardTroops.tMap.get(target).face() )
        {
            boardTroops = boardTroops.troopFlip(target);
        }

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
        Map<BoardPos, TroopTile> newTroops = new HashMap<>(tMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leadPos, guards);
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

        Map<BoardPos, TroopTile> newTroop = new HashMap<>(tMap);
        TilePos tilePos = (this.leaderPosition().equals(target)) ? (newPosit = TilePos.OFF_BOARD) : (newPosit = this.leaderPosition());
        newTroop.remove(target);
        return  new BoardTroops(this.playingSide(), newTroop, newPosit, this.guards());
    }
}

