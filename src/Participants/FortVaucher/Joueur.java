package Participants.FortVaucher;

import Othello.Move;

/**
 * @author Sébastien Vaucher
 */
public class Joueur extends Othello.Joueur
{

	private Board board;
	private AlphaBeta alphaBeta;

	/**
	 * @param depth Alpha-beta algorithm depth
	 * @param playerID 0 = Red, 1 = Blue
	 */
	public Joueur(int depth, int playerID)
	{
		super(depth, playerID);

		board = new Board(playerID == 0 ? Board.Color.RED : Board.Color.BLUE, super.size);
		alphaBeta = new AlphaBeta(super.size);
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

		Move bestMove = alphaBeta.alphaBeta(board);

		// Apply our move to the board
		if(bestMove != null)
			board.applyMove(bestMove, true);

		return bestMove;
	}

}