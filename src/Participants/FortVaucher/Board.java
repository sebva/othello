package Participants.FortVaucher;


import Othello.Move;

import java.util.List;
import java.util.LinkedList;

/**
 * This class represents the board of the game.
 * This class has been thoroughly tested !
 *
 * @author Sébastien Vaucher
 */
public class Board
{
	/**
	 * The board. Be careful with the order !
	 *
	 * /!\  /!\ /!\ /!\  /!\
	 * /!\ [Column][Row] /!\
	 * /!\  /!\ /!\ /!\  /!\
	 */
	private byte[][] board;
	private final byte ourColor;
	private final byte theirColor;

	// Constants
	public final int nbBoxes;

	public class Color
	{
		public static final byte RED = 0;
		public static final byte BLUE = 1;
		public static final byte NOTHING = 2;
	}

	/**
	 * Create a new Board
	 * @param ourColor The color from the player we represent. Either Color.BLUE or Color.RED
	 * @param nbBoxes Number of boxes in each dimension
	 */
	public Board(byte ourColor, int nbBoxes)
	{
		board = new byte[nbBoxes][nbBoxes];
		this.ourColor = ourColor;
		this.theirColor = ourColor == Color.BLUE ? Color.RED : Color.BLUE;
		this.nbBoxes = nbBoxes;
	}

	/**
	 * Initialize the board with Color.NOTHING and the four initial bubbles
	 */
	public void initBoard()
	{
		// Board initialization
		for (int i = 0; i < nbBoxes; i++)
			for (int j = 0; j < nbBoxes; j++)
				board[i][j] = Color.NOTHING;

		// Adding the 4 existing bubbles at the center
		board[nbBoxes / 2][nbBoxes / 2] = Color.BLUE;
		board[nbBoxes / 2 - 1][nbBoxes / 2 - 1] = Color.BLUE;
		board[nbBoxes / 2 - 1][nbBoxes / 2] = Color.RED;
		board[nbBoxes / 2][nbBoxes / 2 - 1] = Color.RED;
	}

	/**
	 * Can be used to print the current state of the board to the standard system output
	 */
	public void printBoard()
	{
		for (int i = 0; i < nbBoxes; i++)
		{
			System.out.println();
			for (int j = 0; j < nbBoxes; j++)
				System.out.print(board[j][i] + "\t");
		}
		System.out.println();
	}

	/**
	 * Apply a move to the board
	 * @param move The move to apply
	 * @param fromOurselves true = The player who realized this move is ourselves
	 */
	public void applyMove(Move move, boolean fromOurselves)
	{
		// The color to which the bubbles will be moved to
		byte flipColor = fromOurselves ? ourColor : theirColor;
		// The current color of the bubbles
		byte otherColor = fromOurselves ? theirColor : ourColor;

		// Flip the move bubble
		board[move.i][move.j] = flipColor;

		// Chain reaction in all 8 directions
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++)
				if(!(i == 0 && j == 0))
				{
					// If there is something to flip in the current direction
					if(numberOfBoxesFlippedInDirection(move, i, j, otherColor) > 0)
					{
						int col = move.i, row = move.j;

						while(board[col + i][row + j] == otherColor)
						{
							col += i;
							row += j;
							board[col][row] = flipColor;
						}
					}
				}
	}

	/**
	 * Clone the Board and apply the move to it. The this Board object is untouched
	 * @param move The move to apply
	 * @param fromOurselves true = The player who realized this move is ourselves
	 * @return The new Board with the move applied to it
	 */
	public Board applyMoveToNewBoard(Move move, boolean fromOurselves)
	{
		Board board2 = new Board(ourColor, nbBoxes);

		for (int i = 0; i < nbBoxes; i++)
			System.arraycopy(this.board[i], 0, board2.board[i], 0, nbBoxes);

		board2.applyMove(move, fromOurselves);
		return board2;
	}

	/**
	 * Get a List of every possible moves
	 * @param fromOurselves true = The player who realized this move is ourselves
	 * @return A List object of every possible moves
	 */
	public List<Move> getPossibleMoves(boolean fromOurselves)
	{
		List<Move> movesList = new LinkedList<Move>();
		Move currentMove = new Move();
		for(int i = 0; i < nbBoxes; i++)
			for(int j = 0; j < nbBoxes; j++)
			{
				currentMove.i = i;
				currentMove.j = j;

				if(numberOfBoxesFlipped(currentMove, fromOurselves) > 0)
					movesList.add(new Move(currentMove.i, currentMove.j));
			}

		return movesList;
	}

	/**
	 * Returns the number of bubbles which would be flipped if someone would play on this box
	 * @param move The coordinates of the box
	 * @param fromOurselves true = The move would be played by us
	 * @return A number of bubbles which would then belong to the player playing
	 */
	public int numberOfBoxesFlipped(Move move, boolean fromOurselves)
	{
		// Don't attempt to play where there already is a bubble
		if(board[move.i][move.j] != Color.NOTHING)
			return 0;

		int amount = 0;
		byte flipColor = fromOurselves ? theirColor : ourColor;

		// Check in all 8 directions
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++)
				if(i != 0 || j != 0)
					amount += numberOfBoxesFlippedInDirection(move, i, j, flipColor);

		return amount;
	}

	/**
	 * Compute the number of boxes that will be flipped in the specified direction
	 * @param move The move to simulate
	 * @param colIncrement Horizontal direction
	 * @param rowIncrement Vertical direction
	 * @param flipColor The opponent's color
	 * @return The number of boxes that we will gain from this move
	 */
	private int numberOfBoxesFlippedInDirection(Move move, int colIncrement, int rowIncrement, byte flipColor)
	{
		int col = move.i, row = move.j;
		int amount = 0;

		while(true)
		{
			col += colIncrement;
			row += rowIncrement;

			// Bounds check
			if(row < 0 || row >= nbBoxes || col < 0 || col >= nbBoxes)
				return 0;

			if(board[col][row] == flipColor)
				amount++;
			else
				break;
		}

		if(board[col][row] == Color.NOTHING)
			return 0;
		else
			return amount;
	}

	/**
	 * Is the game finished
	 * @return True = There are no possible moves
	 */
	public boolean isGameOver()
	{
		Move currentMove = new Move();
		for(int i = 0; i < nbBoxes; i++)
			for(int j = 0; j < nbBoxes; j++)
			{
				currentMove.i = i;
				currentMove.j = j;

				if(numberOfBoxesFlipped(currentMove, true) > 0)
					return false;
			}

		return true;
	}

	/**
	 * Evaluate the board
	 * @param fromOurselves true = The move would be played by us
	 * @return A number representing the evaluation
	 */
	public double evaluate(boolean fromOurselves)
	{
		return Evaluation.evaluateBoard(this, (fromOurselves ? ourColor : theirColor));
	}

	/**
	 *
	 * @return the board in a byte array form
	 */
	public byte[][] getBoard()
	{
		return board;
	}

	public byte getOurColor()
	{
		return ourColor;
	}
}
