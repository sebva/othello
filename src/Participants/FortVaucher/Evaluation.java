package Participants.FortVaucher;

import Participants.FortVaucher.Board.Color;
/**
 * @author : Danick Fort
 * This class is used to evaluate the strength of the position of a player in regards a certain situation within the game flow.
 */
public class Evaluation
{
	private static final int[][] boardWeights =
		{
/*		{120, -20, 20,  5,  5, 20, -20, 120},
		{-20, -40, -5, -5, -5, -5, -40, -20},
		{ 20,  -5, 15,  3,  3, 15,  -5,  20},
		{  5,  -5,  3,  3,  3,  3,  -5,   5},
		{  5,  -5,  3,  3,  3,  3,  -5,   5},
		{ 20,  -5, 15,  3,  3, 15,  -5,  20},
		{-20, -40, -5, -5, -5, -5, -40, -20},
		{120, -20, 20,  5,  5, 20, -20, 120}*/
			{500, -150, 30, 10, 10, 30, -150, 500},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{10, 0, 2, 16, 16, 2, 0, 10},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{500, -150, 30, 10, 10, 30, -150, 500}
		};


	public static double evaluateBoard(byte[][] board, byte evaluatedPlayerColor)
	{
		int evaluation = 0;
		int totalPieces = 0;
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
			   	byte piece = board[j][i];
			   	if (piece != Color.NOTHING)
			   	{
				   	int add = boardWeights[j][i];
					add *= (piece == evaluatedPlayerColor ? 1 : -1);

					totalPieces += (piece == evaluatedPlayerColor ? 1 : -1);

				   	evaluation += add;
			   	}
			}
		}

		return evaluation + 0*totalPieces;
	}
}
