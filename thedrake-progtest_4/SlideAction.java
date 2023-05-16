package thedrake;
import java.util.ArrayList;
import java.util.List;
public class SlideAction  extends  TroopAction
{
    protected SlideAction(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }
    public SlideAction(Offset2D offset) {
        super(offset);
    }
    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state)
    {
        List<Move> res = new ArrayList<>();
        TilePos aim = origin.stepByPlayingSide(offset(), side);
        while (true)
        {
            if (state.canStep(origin, aim))
            {
                res.add(new StepOnly(origin, (BoardPos) aim));
                aim = aim.stepByPlayingSide(offset(), side);
                continue;
            }
            else if (state.canCapture(origin, aim))
            {
                res.add(new StepAndCapture(origin, (BoardPos) aim));
            }
            break;
        }
        return res;
    }
}