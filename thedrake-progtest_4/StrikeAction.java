package thedrake;
import java.util.ArrayList;
import java.util.List;
public class StrikeAction  extends  TroopAction
{
    protected StrikeAction(int offsetX, int offsetY)
    {
        super(offsetX, offsetY);
    }

    public StrikeAction(Offset2D offset)
    {
        super(offset);
    }
    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state)
    {
       List<Move> movjst = new ArrayList<>();
       TilePos target = origin.stepByPlayingSide(offset(), side);
       if (state.canCapture(origin, target))
       {
           movjst.add(new CaptureOnly(origin, (BoardPos) target));
       }
       return movjst;
    }
}
