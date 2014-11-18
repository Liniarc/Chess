package objects.defaultPieces;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import objects.Board;
import objects.ImageHelper;
import objects.Piece;
import objects.TextHelper;

public class Knight extends Piece
{

	public Knight(int xLoc, int yLoc, int team, Board board)
	{
		super(xLoc, yLoc, team, board);
	}

	@Override
	public ArrayList<Point> validMoves()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int i = 0; i < 8; i++)
		{
			int xDir = 1;
			int yDir = 1;
			int xLen = 1;
			int yLen = 1;
			
			if (i%2==0)
				xDir = -1;
			if (i%4<2)
				yDir = -1;
			if (i < 4)
				xLen = 2;
			else
				yLen = 2;
			
			Point move = new Point(loc.x+xDir*xLen,loc.y+yDir*yLen);
			if (!board.hasPieceAt(move) && board.validLocation(move))
				if (board.isUncheckedMove(this, move))
					moves.add(move);
		}
		
		return moves;
	}
	
	@Override
	public ArrayList<Point> threatens()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int i = 0; i < 8; i++)
		{
			int xDir = 1;
			int yDir = 1;
			int xLen = 1;
			int yLen = 1;
			
			if (i%2==0)
				xDir = -1;
			if (i%4<2)
				yDir = -1;
			if (i < 4)
				xLen = 2;
			else
				yLen = 2;
			
			Point move = new Point(loc.x+xDir*xLen,loc.y+yDir*yLen);
			if (board.hasPieceAt(move) && board.pieceAt(move).team!=team)
				moves.add(move);
		}
		
		return moves;
	}

	@Override
	public ArrayList<Point> validTakes()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int i = 0; i < 8; i++)
		{
			int xDir = 1;
			int yDir = 1;
			int xLen = 1;
			int yLen = 1;
			
			if (i%2==0)
				xDir = -1;
			if (i%4<2)
				yDir = -1;
			if (i < 4)
				xLen = 2;
			else
				yLen = 2;
			
			Point move = new Point(loc.x+xDir*xLen,loc.y+yDir*yLen);
			if (board.hasPieceAt(move) && board.pieceAt(move).team!=team)
				if (board.isUncheckedTake(this, move))
					moves.add(move);
		}
		
		return moves;
	}

	@Override
	public void moveTo(Point newLoc)
	{
		loc = newLoc;
	}

	@Override
	public Piece takenPiece(Point newLoc)
	{		
		Piece p = null;
		if (board.hasPieceAt(newLoc))
			 p = board.pieceAt(newLoc);
		return p;
	}
	
	@Override
	public void draw(Graphics2D g, int i, int j, int size)
	{
		if (team == 0)
			g.drawImage(ImageHelper.getPicture("pieces/WKnight.png"), i, j,
					size, size, null);
		else
			g.drawImage(ImageHelper.getPicture("pieces/BKnight.png"), i, j,
					size, size, null);
	}
	
	public Piece clone()
	{
		Knight n = new Knight(loc.x, loc.y, team, board);
		n.firstMove = firstMove;
		return n;
	}

	@Override
	public double value()
	{
		return 3;
	}
}
