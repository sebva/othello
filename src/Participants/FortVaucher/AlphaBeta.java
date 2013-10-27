package Participants.FortVaucher;

import Othello.Move;

/**
 * Created with IntelliJ IDEA.
 * User: Sébastien
 * Date: 25.10.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class AlphaBeta
{
	public static Move alphaBeta(Board root, int depth)
	{
		Move move = new Move(-100, -100);
		minmax(root, 1, Double.POSITIVE_INFINITY, depth, move);
		if(move.i != -100 && move.j != -100)
			return move;
		else
			return null;
	}

	private static double minmax(Board root, int minOrMax, double parentValue, int depth, Move outMove)
	{
		final boolean fromOurselves = (minOrMax == 1);

		if(depth == 0 || root.isGameOver())
			return root.evaluate(true);

		double optVal = minOrMax * Double.NEGATIVE_INFINITY;
		Move optOp = null;

		for(Move op : root.getPossibleMoves(fromOurselves))
		{
			double val;
			{
				Board newBoard = root.applyMoveToNewBoard(op, fromOurselves);
				val = minmax(newBoard, -minOrMax, optVal, depth - 1, null);
			}
			if(val * minOrMax > optVal * minOrMax)
			{
				optVal = val;
				optOp = op;
				if(optVal * minOrMax > parentValue * minOrMax)
					break;
			}
		}

		if(optOp != null && outMove != null)
		{
			outMove.i = optOp.i;
			outMove.j = optOp.j;
		}
		return optVal;
	}
}
