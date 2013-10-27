package Participants.FortVaucher;

import Othello.Move;

import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: Sébastien
 * Date: 25.10.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class AlphaBeta
{
	private int depth;

	public AlphaBeta(int depth)
	{
		this.depth = depth;
	}

	public Move alphaBeta(Board root)
	{
		Move move = new Move(-100, -100);
		max(root, Double.POSITIVE_INFINITY, depth, move);
		if(move.i != -100 && move.j != -100)
			return move;
		else
			return null;
	}

	public double max(Board root, double parentMin, int depth, Move outMove)
	{
		if(depth == 0 || root.isGameOver())
			return root.evaluation(true);

		double maxVal = Double.NEGATIVE_INFINITY;
		Move maxOp = null;

		for(Move op : root.getPossibleMoves(true))
		{
			Board newBoard = root.applyMoveToNewBoard(op, true);
			double val = min(newBoard, maxVal, depth -1, null);
			if(val > maxVal)
			{
				maxVal = val;
				maxOp = op;
				if(maxVal > parentMin)
					break;
			}
		}

		if(maxOp != null && outMove != null)
		{
			outMove.i = maxOp.i;
			outMove.j = maxOp.j;
		}
		return maxVal;
	}

	private double min(Board root, double parentMax, int depth, Move outMove)
	{
		if(depth == 0 || root.isGameOver())
			root.evaluation(false);

		double minVal = Double.POSITIVE_INFINITY;
		Move minOp = null;

		for(Move op : root.getPossibleMoves(false))
		{
			Board newBoard = root.applyMoveToNewBoard(op, false);
			double val = max(newBoard, minVal, depth -1, null);

			if(val < minVal)
			{
				minVal = val;
				minOp = op;
				if(minVal < parentMax)
					break;
			}
		}

		if(minOp != null && outMove != null)
		{
			outMove.i = minOp.i;
			outMove.j = minOp.j;
		}
		return minVal;
	}
}
