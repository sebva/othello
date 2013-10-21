package Participants.FortVaucher;

import Othello.Move;

/**
 * @author Sébastien Vaucher
 */
public class Joueur extends Othello.Joueur
{

	private Board board;

	/**
	 * @param depth Alpha-beta algorithm depth
	 * @param playerID 0 = Red, 1 = Blue
	 */
	public Joueur(int depth, int playerID)
	{
		super(depth, playerID);

		board = new Board(playerID == 0 ? Board.Color.RED : Board.Color.BLUE, super.size);
	}

	/**
	 * This method is called whenever we have to play
	 * @param move The opponent's move, which has just happened
	 * @return Our next move
	 */
	@Override
	public Move nextPlay(Move move)
	{
		// Apply the opponent's move to the board
		if(move != null)
			board.applyMove(move, false);



		// BEGIN Temp dumb algorithm
		Move bestMove = null;
		int bestAmount = -1;
		Move currentMove = new Move();
		for(int i = 0; i < super.size; i++)
			for(int j = 0; j < super.size; j++)
			{
				currentMove.i = i;
				currentMove.j = j;

				int amount = board.numberOfBoxesFlipped(currentMove, true);
				if(amount > bestAmount)
				{
					bestAmount = amount;
					bestMove = new Move(i, j);
				}
			}

		if(bestAmount <= 0)
			// This method has to return null when it's not possible to play
			return null;
		// END Temp dumb algorithm

		// Apply our move to the board
		board.applyMove(bestMove, true);

		return bestMove;
	}

}