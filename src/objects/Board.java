package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import ai.BasicAI;
import objects.Piece;
import objects.defaultPieces.*;

public class Board
{
	private boolean grid[][];
	ArrayList<Piece> pieces = new ArrayList<Piece>();
	Piece selection;
	int turn = 0;
	Stack<ArrayList<Piece>> pastPositions = new Stack<ArrayList<Piece>>();
	
	BasicAI ai;
	
	boolean ignoreRules = false;
	
	public Board()
	{
		grid = new boolean[8][8];
		for (int i = 0; i < getGrid().length; i++)
			for (int j = 0; j < getGrid()[i].length; j++)
				getGrid()[i][j] = true;
		for (int i = 0; i < 8; i++)
		{
			pieces.add(new Pawn(i,1,0,this));
			pieces.add(new Pawn(i,6,1,this));
		}
		
		
		pieces.add(new Knight(1,0,0,this));
		pieces.add(new Knight(6,0,0,this));
		pieces.add(new Knight(1,7,1,this));
		pieces.add(new Knight(6,7,1,this));
		
		pieces.add(new Bishop(2,0,0,this));
		pieces.add(new Bishop(5,0,0,this));
		pieces.add(new Bishop(2,7,1,this));
		pieces.add(new Bishop(5,7,1,this));

		pieces.add(new Rook(0,0,0,this));
		pieces.add(new Rook(7,0,0,this));
		pieces.add(new Rook(0,7,1,this));
		pieces.add(new Rook(7,7,1,this));

		pieces.add(new Queen(3,0,0,this));
		pieces.add(new Queen(3,7,1,this));
		
		pieces.add(new King(4,0,0,this));
		pieces.add(new King(4,7,1,this));

//		updatePieces();
		
		ai = new BasicAI(this);
	}
	
	public Board(boolean[][] g, ArrayList<Piece> p)
	{
		grid = g;
		pieces = new ArrayList<Piece>();
		for (Piece piece : p)
		{
			Piece newPiece = piece.clone();
			newPiece.board = this;
			pieces.add(newPiece);
			
		}

//		updatePieces();
	}
	
	public Board(Board b)
	{
		this(b.grid,b.pieces);
	}

	public ArrayList<Piece> getPieces(int team)
	{
		ArrayList<Piece> teamPieces = new ArrayList<Piece>();
		for (Piece p : pieces)
		{
			if (p.team==team)
				teamPieces.add(p);
		}
		return teamPieces;
	}
	
	public ArrayList<Piece> getPieces()
	{
		return pieces;
	}
	
	public boolean hasPieceAt(Point loc)
	{
		for (int i = 0; i < pieces.size(); i++)
		{
			if (pieces.get(i).loc.equals(loc))
				return true;
		}
		return false;
	}
	
	public Piece pieceAt(Point loc)
	{
		for (int i = 0; i < pieces.size(); i++)
		{
			if (pieces.get(i).loc.equals(loc))
				return pieces.get(i);
		}
		return null;
	}
	
	public boolean validLocation(Point loc)
	{
		int x = loc.x;
		int y = loc.y;
		if (x < 0 || y < 0)
			return false;
		return !(x >= getGrid().length || y >= getGrid()[0].length);	
	}
	
	public void draw(Graphics2D g)
	{

		int size = 500/Math.max(getGrid().length, getGrid()[0].length);
		int gapx = (600 - size*getGrid().length)/2;
		int gapy = (600 - size*getGrid()[0].length)/2;
		for (int i = 0; i < getGrid().length; i++)
		{
			for (int j = 0; j < getGrid()[i].length; j++)
			{
				if (getGrid()[i][j])
				{
					if ((i + j) % 2 != 0)
						g.setColor(new Color(211, 141, 71));
					else
						g.setColor(new Color(251, 205, 156));
					g.fillRect(i * size + gapx, j * size + gapy, size, size);
				}
				else 
				{
					if ((i + j) % 2 != 0)
						g.setColor(new Color(150, 150, 150));
					else
						g.setColor(new Color(210, 210, 210));
					g.fillRect(i * size + gapx, j * size + gapy, size, size);
				}
			}
		}

		g.setColor(Color.black); //files
		for (int i = 0; i < getGrid().length; i++)
		{
			String let = "" + (char) ('a' + i);
			g.drawString(let, i * size + gapx
					+ (-TextHelper.textWidth(let, g) + size) / 2, gapy - 10);
		}
		for (int i = 0; i < getGrid()[0].length; i++) // ranks
		{
			String num = "" + (getGrid()[0].length - i);
			g.drawString(num, gapx - TextHelper.textWidth(num, g) - 10, i
					* size + gapy + (TextHelper.textHeight(g) + size) / 2);
		}
		
		for (int i = 0; i < pieces.size(); i++) //draw pieces
		{
			Piece p = pieces.get(i);
			p.draw(g, p.loc.x * size + gapx, (getGrid()[0].length-1-p.loc.y) * size + gapy, size);
		}
		
		if (selection!= null)
		{
				g.setColor(new Color(100,250,100,160));
				g.fillRect(selection.loc.x * size + gapx + 5,
						(getGrid()[0].length - 1 - selection.loc.y) * size + gapy + 5,
						size - 10, size - 10);
			for (Point loc : selection.validMoves())
			{
				g.setColor(new Color(255,140,140,160));
				g.fillRect(loc.x * size + gapx + 5,
						(getGrid()[0].length - 1 - loc.y) * size + gapy + 5,
						size - 10, size - 10);
			}
			for (Point loc : selection.validTakes())
			{
				g.setColor(new Color(255,0,0,160));
				g.fillRect(loc.x * size + gapx + 5,
						(getGrid()[0].length - 1 - loc.y) * size + gapy + 5,
						size - 10, size - 10);
			}
		}
		
	}
	
	public King getKing(int team)
	{
		for (Piece p : pieces)
		{
			if (p instanceof King && p.team==team)
				return (King)p;
		}
		return null;
	}
	
	
	public void doMouse(MouseEvent e)
	{
		int size = 500/Math.max(getGrid().length, getGrid()[0].length);
		int gapx = (600 - size*getGrid().length)/2;
		int gapy = (600 - size*getGrid()[0].length)/2;
		int gridX = (e.getX()-gapx)/size;
		int gridY = getGrid()[0].length-1-(e.getY()-gapy)/size;
		
		Point gridLoc = new Point(gridX,gridY);
		
		if (selection!= null && turn%2==0)
		{
			if (movePiece(selection,gridLoc))
			{
				turn++;
			}
			selection = null;
		}
		else
		{
			Piece selectionTemp = selection;
			for (Piece p : pieces)
			{
				if (p.loc.equals(gridLoc))
				{
					selection = p;
					break;
				}
			}
			if (selection == selectionTemp)
				selection = null;
		}
		System.out.println((char)('a'+gridX) +", "+ (getGrid().length-gridY));
	}
	
	/**
	 * Tries to move a piece to a new location
	 * 
	 * @param p - the piece to be moved
	 * @param loc - the new location to move the piece
	 * @return whether a move was made or not
	 */
	public boolean movePiece(Piece p, Point loc)
	{
		if (!ignoreRules)
		{
			if (p.canMoveTo(loc))
			{
				addPosition();
				p.moveTo(loc);
//				updatePieces();
				return true;
			}
			if (p.canTakeTo(loc))
			{
//				pastPositions.push((ArrayList<Piece>) pieces.clone());
				addPosition();
				pieces.remove(p.takenPiece(loc));
				p.moveTo(loc);
//				updatePieces();
				return true;
			}
			return false;
		}
		else
		{

			if (loc != p.loc)
			{
				if (hasPieceAt(loc))
				{
					pieces.remove(p.takenPiece(loc));
					addPosition();
					p.moveTo(loc);
				}
				else
				{
					addPosition();
					p.moveTo(loc);
				}
//				updatePieces();
				return true;
			}
			return false;
		}
	}
	
	public void removePieceAt(Point loc)
	{
		pieces.remove(pieceAt(loc));
	}
	
	public void addPosition()
	{
		ArrayList<Piece> pos = new ArrayList<Piece>();
		for (Piece p: pieces)
		{
			pos.add(p.clone());
		}
		pastPositions.add(pos);
	}
	
	public void undoMove()
	{
		selection = null;
		if (!pastPositions.isEmpty())
			pieces = pastPositions.pop();
	}
	
	public int checkedTeam()
	{
		for (Piece p : pieces)
		{
			for (Point move : p.threatens())
			{
				if (pieceAt(move) instanceof King && !pieceAt(move).sameTeam(p))
					return p.team;
			}
		}
		return -1;
	}
	
	public boolean isUncheckedMove(Piece movedPiece, Point loc)
	{
		Point tempLoc = movedPiece.loc;
		Board tempBoard = new Board(grid, pieces);
		
		tempBoard.pieceAt(tempLoc).moveTo(loc);
		
		for (Piece p : tempBoard.pieces)
		{
			if (p.team != movedPiece.team)
				for (Point move : p.threatens())
				{
					if (tempBoard.pieceAt(move) instanceof King && tempBoard.pieceAt(move).team!=p.team)
					{
						return false;
					}
				}
		}
		return true;
	}
	
	public boolean isUncheckedTake(Piece movedPiece, Point loc)
	{
		Point tempLoc = movedPiece.loc;
		Board tempBoard = new Board(grid, pieces);
		
		tempBoard.pieces.remove(tempBoard.pieceAt(loc));
		tempBoard.pieceAt(tempLoc).moveTo(loc);
		
		
		for (Piece p : tempBoard.pieces)
		{
			if (p.team != movedPiece.team )
				for (Point move : p.threatens())
				{
					if (tempBoard.hasPieceAt(move))
					{
						Piece king = tempBoard.pieceAt(move);
						if (king instanceof King && king.team!=p.team)
						{
							return false;
						}
					}
				}
		}
		
		return true;
	}
	
	public void promote(Piece promotedPiece)
	{
		Point loc = promotedPiece.loc;
		Piece newPiece = new Queen(loc.x, loc.y, promotedPiece.team, this);

		pieces.remove(promotedPiece);
		pieces.add(newPiece);
	}
	
	/**
	 * @return returns -1 if no checkmate. Otherwise, returns the winning team.
	 */
	public int checkCheckmate()
	{
		int team = checkedTeam();
		if (team == -1)
			return -1;
		for (Piece p : pieces)
		{
			if (p.team == team)
			{
				if (p.numMoves() != 0)
					return -1;
			}
		}
		return team;
	}

	public void calculate()
	{
		if (turn%2==1 && checkCheckmate()<0)
		{
//			System.out.println( checkCheckmate());
			ai.makeMove();
			turn++;
		}
	}

	public boolean[][] getGrid()
	{
		return grid;
	}
}
