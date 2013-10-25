package Participants.FortVaucher;


import Othello.Move;

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
	private Color[][] board;
	private final Color ourColor;
	private final Color theirColor;

	// Constants
	public final int nbBoxes;

	public enum Color
	{
		RED, BLUE, NOTHING
	}

	public Board(Color ourColor, int nbBoxes)
	{
		board = new Color[nbBoxes][nbBoxes];
		this.ourColor = ourColor;
		this.theirColor = ourColor == Color.BLUE ? Color.RED : Color.BLUE;
		this.nbBoxes = nbBoxes;

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
		Color flipColor = fromOurselves ? ourColor : theirColor;
		// The current color of the bubbles
		Color otherColor = fromOurselves ? theirColor : ourColor;

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
		Color flipColor = fromOurselves ? theirColor : ourColor;

		// Check in all 8 directions
		for(int i = -1; i <= 1; i++)
			for(int j = -1; j <= 1; j++)
				if(i != 0 || j != 0)
					amount += numberOfBoxesFlippedInDirection(move, i, j, flipColor);

		return amount;
	}

	private int numberOfBoxesFlippedInDirection(Move move, int colIncrement, int rowIncrement, Color flipColor)
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

	public Color[][] getBoard()
	{
		return board;
	}
}
