package Participants.FortVaucher;

import Participants.FortVaucher.Board.Color;
/**
 * @author : Danick Fort
 * This class is used to evaluate the strength of the position of a player in regards a certain situation within the game flow.
 */
public class Evaluation
{
	private static int[][] boardWeights =
		{
		{120, -20, 20,  5,  5, 20, -20, 120},
		{-20, -40, -5, -5, -5, -5, -40, -20},
		{ 20,  -5, 15,  3,  3, 15,  -5,  20},
		{  5,  -5,  3,  3,  3,  3,  -5,   5},
		{  5,  -5,  3,  3,  3,  3,  -5,   5},
		{ 20,  -5, 15,  3,  3, 15,  -5,  20},
		{-20, -40, -5, -5, -5, -5, -40, -20},
		{120, -20, 20,  5,  5, 20, -20, 120}
		};


	public static int evaluateBoard(Board board, Color evaluatedPlayerColor)
	{
		int evaluation = 0;
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
			   	Color piece = board.getBoard()[j][i];
			   	if (piece != Color.NOTHING)
			   	{
				   	int add = boardWeights[j][i];
					add *= (piece == evaluatedPlayerColor ? 1 : -1);
				   	evaluation += add;
					System.out.print(piece + "" + add + " ");
			   	}
			}
			System.out.println("");
		}
		return evaluation;
	}
}
