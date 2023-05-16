package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction  extends  TroopAction{
    protected SlideAction(int offsetX, int offsetY)
    {
        super(offsetX, offsetY);
    }
    public SlideAction(Offset2D offset)
    {
        super(offset);
    }
    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> res = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);
        while (true) {
            if (state.canStep(origin, target)) {
                res.add(new StepOnly(origin, (BoardPos) target));
                target = target.stepByPlayingSide(offset(), side);
                continue;
            }
            else if (state.canCapture(origin, target))
                res.add(new StepAndCapture(origin, (BoardPos) target));
            break;
        }
        return res;
    }
}