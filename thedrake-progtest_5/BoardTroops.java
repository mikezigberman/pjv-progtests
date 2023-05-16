package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
	private final PlayingSide plSide;
	private final Map<BoardPos, TroopTile> tmap;
	private final TilePos leadPos;
	private final int guards;

	public BoardTroops(PlayingSide playingSide) {
		this.plSide = playingSide;
		this.tmap = Collections.emptyMap();
		this.leadPos = TilePos.OFF_BOARD;
		this.guards = 0;

	}

	public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards)
	{
		this.plSide = playingSide;
		this.tmap = troopMap;
		this.leadPos  = leaderPosition;
		this.guards = guards;
		}

	public Optional<TroopTile> at(TilePos pos) {
		Optional<TroopTile>troopTile =Optional.ofNullable(this.tmap.get(pos));
		return (this.tmap.containsKey(pos)) ? troopTile :  Optional.empty();
	}

	public PlayingSide playingSide() {
		if (plSide == PlayingSide.ORANGE)
		{
			return PlayingSide.ORANGE;
		}
		return PlayingSide.BLUE;
	}

	public TilePos leaderPosition() {
		return this.leadPos;
	}

	public int guards() {
		return this.guards;
	}

	public boolean isLeaderPlaced() {
		if ( this.leaderPosition() != TilePos.OFF_BOARD)
		{
			return true;
		}
		else
			return false;
	}

	public boolean isPlacingGuards() {
		if (this.isLeaderPlaced() && this.guards() < 2)
			return true;
		else
			return false;
	}

	public Set<BoardPos> troopPositions() {
		Set<BoardPos>  boardPosSet = new HashSet<>();
		this.tmap.forEach((key, valeu) -> {if (valeu.hasTroop()) {boardPosSet.add(key); } });

		return boardPosSet;
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		if (this.tmap.containsKey(target))
		{
			throw new IllegalArgumentException("To nelze umístit na dlaždici");
		}

		TroopTile newtroopTile = new TroopTile(troop,this.plSide, TroopFace.AVERS);
		Map<BoardPos, TroopTile> map =new HashMap<>(tmap);
		map.put(target, newtroopTile);

		if (!this.isLeaderPlaced())
		{
			return new BoardTroops(this.playingSide(), map,target, this.guards());
		}
		else if (this.isPlacingGuards())
		{
			return new BoardTroops(this.playingSide(), map, this.leaderPosition(), this.guards() +1);
		}
		else {
			return new BoardTroops(this.playingSide(), map, this.leaderPosition(), this.guards());
		}
	}

	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if(!this.isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");
		}

		if(this.isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");
		}

		if(!at(origin).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroop = new HashMap<>(tmap);
		if( this.at(target).isPresent() ) {
			throw new IllegalArgumentException();
		}

		BoardTroops boardTroops;

		if( this.leaderPosition().equals(origin) ) {
			boardTroops  = new BoardTroops(this.playingSide(), newTroop, TilePos.OFF_BOARD, this.guards());
		} else {
			boardTroops  = new BoardTroops(this.playingSide(), newTroop, this.leaderPosition(), this.guards());
		}

		boardTroops = boardTroops.placeTroop(boardTroops.tmap.get(origin).troop(), target);
		if( boardTroops.tmap.get(origin).face() == boardTroops.tmap.get(target).face() ) {
			boardTroops = boardTroops.troopFlip(target);
		}
		boardTroops = boardTroops.removeTroop(origin);

		return boardTroops;

	}

	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");
		}

		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");
		}

		if(!at(origin).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(tmap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leadPos, guards);
	}

	public BoardTroops removeTroop(BoardPos target) {
		if(!this.isLeaderPlaced()) throw new IllegalStateException(
				"Cannot move troops before the leader is placed.");

		if(this.isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");
		}

		if(!at(target).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroop = new HashMap<>(tmap);

		TilePos newPos;
		if( this.leaderPosition().equals(target) ) {
			newPos = TilePos.OFF_BOARD;
		} else {
			newPos = this.leaderPosition();
		}

		newTroop.remove(target);

		return  new BoardTroops(this.playingSide(), newTroop, newPos, this.guards());

	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"side\":");
		plSide.toJSON(writer);
		writer.print(",\"leaderPosition\":");
		leadPos.toJSON(writer);
		writer.printf(",\"guards\":%d", guards);
		writer.printf(",\"troopMap\":{");
		int count = 0;
		for (BoardPos boardPos : new TreeSet<>(tmap.keySet())) {
			boardPos.toJSON(writer);
			writer.printf(":");
			tmap.get(boardPos).toJSON(writer);
			count++;
			if (count < tmap.size())
				writer.printf(",");
		}
		writer.printf("}}");
	}
}
