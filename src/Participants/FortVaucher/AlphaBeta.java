package Participants.FortVaucher;

import Othello.Move;

public class AlphaBeta
{
	/**
	 * Apply the alpha-beta algorithm to find the best move to play
	 * @param root The Board
	 * @param depth Depth of the algorithm
	 * @return A Move object ready to be played
	 */
	public static Move alphaBeta(Board root, int depth)
	{
		Move move = new Move(-100, -100);
		alphaBeta2(root, 1, Double.POSITIVE_INFINITY, depth, move);
		if(move.i != -100 && move.j != -100)
			return move;
		else
			return null;
	}

	/**
	 * The Holy Alpha-Beta Algorithm logic lies in this method
	 * @param root The current Board object
	 * @param minOrMax 1 = Max, -1 = Min
	 * @param parentValue The optVal from the parent node
	 * @param depth Depth remaining
	 * @param outMove Output parameter, the optimal move computed will be stored in it. It has to be a valid Move object.
	 * @return The value of the outMove move
	 */
	private static double alphaBeta2(Board root, int minOrMax, double parentValue, int depth, Move outMove)
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
				val = alphaBeta2(newBoard, -minOrMax, optVal, depth - 1, null);
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
