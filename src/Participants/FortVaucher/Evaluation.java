package Participants.FortVaucher;

import Othello.Move;
import Participants.FortVaucher.Board.Color;
/**
 * @author : Danick Fort
 * This class is used to evaluate the strength of the position of a player in regards a certain situation within the game flow.
 */
public class Evaluation
{
	private static final int[][] boardWeights =
		{
		{120, -20, 20,  5,  5, 20, -20, 120},
		{-20, -40, -5, -5, -5, -5, -40, -20},
		{ 20,  -5, 15,  3,  3, 15,  -5,  20},
		{  5,  -5,  3,  3,  3,  3,  -5,   5},
		{  5,  -5,  3,  3,  3,  3,  -5,   5},
		{ 20,  -5, 15,  3,  3, 15,  -5,  20},
		{-20, -40, -5, -5, -5, -5, -40, -20},
		{120, -20, 20,  5,  5, 20, -20, 120}
			/*{500, -150, 30, 10, 10, 30, -150, 500},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{500, -150, 30, 10, 10, 30, -150, 500} */
		};

	private static final Move[] DIRECTIONS = {
			new Move(0,-1),
			new Move(0,1),
			new Move(1,0),
			new Move(-1,0),
			new Move(1,-1),
			new Move(-1,-1),
			new Move(-1,1),
			new Move(1,1)
		};


	public static double evaluateBoard(Board rawBoard, byte evaluatedPlayerColor)
	{
		byte[][] board = rawBoard.getBoard();
		int midGameThreshold = 13;
		int playedCells = getPlayedCells(board);

		double coeffWeightMatrix = 0.0;
		double coeffBorder = 0.0;
		double coeffNumberPawns = 0.0;
		double coeffParity = 0.0;

		// The coefficients change in accordance with the game stages
		if (playedCells < midGameThreshold) // Early game stage
		{
			coeffWeightMatrix = 0.3;
			coeffBorder = 1.2;
			coeffNumberPawns = -1.5;
			coeffParity = 0.0;
		}
		else if ((playedCells >= midGameThreshold) && (playedCells <= 56)) // Mid game stage
		{
			coeffWeightMatrix = 1.5;
			coeffBorder = 0.3;
			coeffNumberPawns = 0.0;
			coeffParity = 3.0;
		}
		else // Late game stage
		{
			coeffWeightMatrix = 0;
			coeffBorder = 0;
			coeffNumberPawns = 2.0;
			coeffParity = 5.0;
		}

		double evaluation = coeffWeightMatrix * evaluateWithWeightMatrix(board, evaluatedPlayerColor);
		evaluation += coeffBorder * evaluateBorder(board, evaluatedPlayerColor);
		evaluation += coeffNumberPawns * evaluateNumberOfPawns(board, evaluatedPlayerColor);
		evaluation += coeffParity * evaluateParity(rawBoard, evaluatedPlayerColor);

		return evaluation;
	}

	// #### Evaluation functions ####

	// Takes in account parity of empty cells and no moves left

	private static int  evaluateParity(Board rawBoard, byte evaluatedPlayerColor)
	{
		int emptyCells = 0;
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
				if (rawBoard.getBoard()[i][j] == Color.NOTHING) emptyCells++;
			}
		}
		if ((emptyCells % 2 == 0) && (rawBoard.getPossibleMoves(evaluatedPlayerColor == rawBoard.getOurColor()).size() == 0))
		{
			return 25;
		}
		else
		{
			return 0;
		}
	}

	// Takes in account the number of pawns captured

	private static int evaluateNumberOfPawns(byte[][] board, byte evaluatedPlayerColor)
	{
		int evaluation = 0;
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
				if (board[i][j] != Color.NOTHING) evaluation += (board[i][j] == evaluatedPlayerColor ? 1 : -1);
			}
		}
		return evaluation;
	}

	// Takes in account the number of pawns on a border

	private static int evaluateBorder(byte[][] board, byte evaluatedPlayerColor)
	{
		int evaluation = 0;
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
				if (board[i][j] != Color.NOTHING && isOnBorder(i, j, board))
				{
					evaluation += (board[i][j] == evaluatedPlayerColor ? 1 : -1);
				}
			}
		}
		return evaluation;
	}

	// Takes in account the weight matrix for each cell.

	private static int evaluateWithWeightMatrix(byte[][] board, byte evaluatedPlayerColor)
	{
		int evaluation = 0;
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
			   	byte piece = board[i][j];
			   	if (piece != Color.NOTHING)
			   	{
				   	int add = boardWeights[i][j];
					add *= (piece == evaluatedPlayerColor ? 1 : -1);

				   	evaluation += add;
			   	}
			}
		}
		return evaluation;
	}

	//Tools for evaluation

	private static boolean isOnBorder(int i, int j, byte[][] board)
	{
		for (Move dir : DIRECTIONS)
		{
			try {
				if (board[i+dir.i][j+dir.j] == Color.NOTHING) return true;
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
			}
		}
		return false;
	}
	private static int getPlayedCells(byte[][] board)
	{
		int empty = 0;
		for (int i = 0;i < 8;i++)
		{
			for (int j = 0;j < 8; j++)
			{
				if (board[i][j] == Color.NOTHING) empty++;
			}
		}
		return 60-empty; // The 4 starting pieces don't count, so we consider 64-4 cells.
	}
}
