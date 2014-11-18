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

public class Queen extends Piece
{

	public Queen(int xLoc, int yLoc, int team, Board board)
	{
		super(xLoc, yLoc, team, board);
	}

	@Override
	public ArrayList<Point> validMoves()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		

		for (int xd = -1; xd <= 1; xd++)
			for (int yd = -1; yd <=1; yd++)
			{
				if (xd == 0 && yd == 0)
					continue;
				
				int i = 1;
				Point move = new Point(loc.x+xd*i,loc.y+yd*i);
				while (!board.hasPieceAt(move) && board.validLocation(move))
				{
					if (board.isUncheckedMove(this, move))
						moves.add(move);
					i++;
					move = new Point(loc.x+xd*i,loc.y+yd*i);
				}
			}
		
		return moves;
	}

	@Override
	public ArrayList<Point> threatens()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int xd = -1; xd <= 1; xd++)
			for (int yd = -1; yd <=1; yd++)
			{
				if (xd == 0 && yd == 0)
					continue;
				
				int i = 1;
				Point move = new Point(loc.x+xd*i,loc.y+yd*i);
				while (!board.hasPieceAt(move) && board.validLocation(move))
				{
					i++;
					move = new Point(loc.x+xd*i,loc.y+yd*i);
				}
				if (board.hasPieceAt(move))
					moves.add(move);
			}
		return moves;
	}
	
	@Override
	public ArrayList<Point> validTakes()
	{
		ArrayList<Point> moves = new ArrayList<Point>();
		for (int xd = -1; xd <= 1; xd++)
			for (int yd = -1; yd <=1; yd++)
			{
				if (xd == 0 && yd == 0)
					continue;
				
				int i = 1;
				Point move = new Point(loc.x+xd*i,loc.y+yd*i);
				while (!board.hasPieceAt(move) && board.validLocation(move))
				{
					i++;
					move = new Point(loc.x+xd*i,loc.y+yd*i);
				}
				if (board.hasPieceAt(move) && board.pieceAt(move).team != team)
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
			g.drawImage(ImageHelper.getPicture("pieces/WQueen.png"), i, j,
					size, size, null);
		else
			g.drawImage(ImageHelper.getPicture("pieces/BQueen.png"), i, j,
					size, size, null);
	}

	public Piece clone()
	{
		Queen q = new Queen(loc.x, loc.y, team, board);
		q.firstMove = firstMove;
		return q;
	}

	@Override
	public double value()
	{
		return 8.6;
	}
}
