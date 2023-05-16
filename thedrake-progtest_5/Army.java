package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Army implements JSONSerializable {
	private final BoardTroops boardT;
	private final List<Troop> stack;
	private final List<Troop> captur;
	public Army(PlayingSide playingSide, List<Troop> stack) {
		this(
				new BoardTroops(playingSide),
				stack,
				Collections.emptyList());
	}
	public Army(
			BoardTroops boardTroops,
			List<Troop> stack,
			List<Troop> captured)
	{
		this.boardT = boardTroops;
		this.stack = stack;
		this.captur = captured;
	}
	public PlayingSide side() {
		return boardT.playingSide();
	}
	public BoardTroops boardTroops() {
		return boardT;
	}

	public List<Troop> stack() {
		return stack;
	}

	public List<Troop> captured() {
		return captur;
	}
	public Army placeFromStack(BoardPos target) {
		if(target == TilePos.OFF_BOARD)
			throw new IllegalArgumentException();

		if(stack.isEmpty())
			throw new IllegalStateException();

		if(boardT.at(target).isPresent())
			throw new IllegalStateException();

		List<Troop> newStack = new ArrayList<Troop>(
				stack.subList(1, stack.size()));

		return new Army(
				boardT.placeTroop(stack.get(0), target),
				newStack,
				captur);
	}
	public Army troopStep(BoardPos origin, BoardPos target)
	{
		return new Army(boardT.troopStep(origin, target), stack, captur);
	}
	public Army troopFlip(BoardPos origin) {
		return new Army(boardT.troopFlip(origin), stack, captur);
	}
	public Army removeTroop(BoardPos target) {
		return new Army(boardT.removeTroop(target), stack, captur);
	}
	public Army capture(Troop troop)
	{
		List<Troop> newCaptured = new ArrayList<Troop>(captur);
		newCaptured.add(troop);

		return new Army(boardT, stack, newCaptured);
	}


	@Override
	public void toJSON(PrintWriter writer) {
		writer.printf("{\"boardTroops\":");
		boardT.toJSON(writer);
		writer.printf(",\"stack\":[");
		listToJSON(writer, stack);
		writer.printf("],\"captured\":[");
		listToJSON(writer, captur);
		writer.printf("]}");
	}
	private void listToJSON(PrintWriter writer, List<Troop> list) {
		for (int i = 0; i < list.size(); i++)
		{
			list.get(i).toJSON(writer);
			if (i != list.size() - 1)
				writer.printf(",");
		}
	}
}
