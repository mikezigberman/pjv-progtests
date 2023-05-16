package thedrake;

import java.util.Optional;

public class GameState {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }
    public Tile tileAt(TilePos pos) {
        // Místo pro váš kód
        if (pos.equals(TilePos.OFF_BOARD))
        {
            return BoardTile.MOUNTAIN;
        }
        if (orangeArmy.boardTroops().at(pos).isPresent())
        {
            return orangeArmy.boardTroops().at(pos).get();
        }
        if (blueArmy.boardTroops().at(pos).isPresent())
        {
            return blueArmy.boardTroops().at(pos).get();
        }
        return board.at(pos);
    }
    private boolean canStepFrom(TilePos origin) {
        if (origin.equals(TilePos.OFF_BOARD))
        {
            return false;
        }
        if (result != GameResult.IN_PLAY)
        {
            return false;
        }
        if (orangeArmy.boardTroops().isPlacingGuards())
        {
            return false;
        }
        if (blueArmy.boardTroops().isPlacingGuards())
        {
            return false;
        }
        return armyOnTurn().boardTroops().at(origin).isPresent();
    }

    private boolean canStepTo(TilePos target) {
        // Místo pro váš kód
        if(!result.equals(GameResult.IN_PLAY)) {
            return false;
        }
        if(armyOnTurn().boardTroops().at(target).isPresent() || armyNotOnTurn().boardTroops().at(target).isPresent()) {
            return false;
        }
        if(target.equals(TilePos.OFF_BOARD)) {
            return false;
        }
        if(!board.at(target).canStepOn())
        {
            return false;
        }

        return true;

    }
    //tady
    private boolean canCaptureOn(TilePos target) {
        return result == GameResult.IN_PLAY &&
                armyOnTurn().boardTroops().isLeaderPlaced() &&
                !armyOnTurn().boardTroops().isPlacingGuards() &&
                armyOnTurn().boardTroops().at(target).isEmpty() &&
                armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        // Místo pro váš kód
        if (result != GameResult.IN_PLAY || target.equals(TilePos.OFF_BOARD) || armyOnTurn().stack().isEmpty() || !tileAt((BoardPos)target).canStepOn()) {
            return false;
        }
        if (!armyOnTurn().boardTroops().isLeaderPlaced() &&
                ((sideOnTurn == PlayingSide.BLUE) ? (target.j() == 0) : (target.j() == board.dimension() - 1)))
            return true;
        if (armyOnTurn().boardTroops().isPlacingGuards() &&
                armyOnTurn().boardTroops().leaderPosition().neighbours().contains(target)) {
            return true;
        }
        if (armyOnTurn().boardTroops().isLeaderPlaced() && !armyOnTurn().boardTroops().isPlacingGuards())
        {
            for (BoardPos pos : armyOnTurn().boardTroops().troopPositions())
            {
                if (pos.neighbours().contains(target))
                    return true;
            }
        }
        return false;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }
}
