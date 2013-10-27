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
		board.initBoard();
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

		// Compute the best move to play
		Move bestMove = AlphaBeta.alphaBeta(board, depth);

		// Apply our move to the board
		if(bestMove != null)
			board.applyMove(bestMove, true);

		return bestMove;
	}

}