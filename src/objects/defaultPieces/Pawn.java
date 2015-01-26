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

public class Pawn extends Piece {

    boolean enPassantable = false;

    public Pawn(int xLoc, int yLoc, int team, Board board) {
	super(xLoc, yLoc, team, board);
    }

    @Override
    public ArrayList<Point> validMoves() {
	ArrayList<Point> moves = new ArrayList<Point>();
	int dir = 1;
	if (team == 1)
	    dir = -1;
	Point move1 = new Point(loc.x, loc.y + dir * 1);
	if (!board.hasPieceAt(move1)) {
	    if (board.isUncheckedMove(this, move1)
		    && board.validLocation(move1))
		moves.add(move1);
	    if (firstMove) {
		Point move2 = new Point(loc.x, loc.y + dir * 2);
		if (!board.hasPieceAt(move2))
		    if (board.isUncheckedMove(this, move2)
			    && board.validLocation(move2))
			moves.add(move2);
	    }
	}

	return moves;
    }

    @Override
    public ArrayList<Point> threatens() {
	ArrayList<Point> moves = new ArrayList<Point>();
	int dir = 1;
	if (team == 1)
	    dir = -1;

	Point move1 = new Point(loc.x + 1, loc.y + dir);
	if (board.hasPieceAt(move1))
	    moves.add(move1);

	Point move2 = new Point(loc.x - 1, loc.y + dir);
	if (board.hasPieceAt(move2))
	    moves.add(move2);

	Point enPass1 = new Point(loc.x + 1, loc.y);
	if (board.hasPieceAt(enPass1)) {
	    Piece p = board.pieceAt(enPass1);
	    if (p instanceof Pawn && ((Pawn) p).enPassantable)
		moves.add(move1);
	}

	Point enPass2 = new Point(loc.x - 1, loc.y);
	if (board.hasPieceAt(enPass2)) {
	    Piece p = board.pieceAt(enPass2);
	    if (p instanceof Pawn && ((Pawn) p).enPassantable)
		moves.add(move2);
	}

	return moves;
    }

    @Override
    public ArrayList<Point> validTakes() {
	ArrayList<Point> moves = new ArrayList<Point>();
	int dir = 1;
	if (team == 1)
	    dir = -1;

	Point move1 = new Point(loc.x + 1, loc.y + dir);
	if (board.hasPieceAt(move1) && board.pieceAt(move1).team != team)
	    if (board.isUncheckedTake(this, move1))
		moves.add(move1);

	Point move2 = new Point(loc.x - 1, loc.y + dir);
	if (board.hasPieceAt(move2) && board.pieceAt(move2).team != team)
	    if (board.isUncheckedTake(this, move2))
		moves.add(move2);

	Point enPass1 = new Point(loc.x + 1, loc.y);
	if (board.hasPieceAt(enPass1)) {
	    Piece p = board.pieceAt(enPass1);
	    if (p instanceof Pawn && ((Pawn) p).enPassantable)
		if (board.isUncheckedTake(this, move1))
		    moves.add(move1);
	}

	Point enPass2 = new Point(loc.x - 1, loc.y);
	if (board.hasPieceAt(enPass2)) {
	    Piece p = board.pieceAt(enPass2);
	    if (p instanceof Pawn && ((Pawn) p).enPassantable)
		if (board.isUncheckedTake(this, move2))
		    moves.add(move2);
	}

	return moves;
    }

    @Override
    public void moveTo(Point newLoc) {
	if (firstMove) {
	    // if (newLoc.distance(loc)==2)
	    // enPassantable=true;
	    firstMove = false;
	}
	loc = newLoc;

	int dir = 1;
	if (team == 1)
	    dir = -1;
	Point move = new Point(loc.x, loc.y + dir);
	if (!board.inBounds(move)) {
	    board.promote(this);
	}

    }

    @Override
    public Piece takenPiece(Point newLoc) {
	int dir = 1;
	if (team == 1)
	    dir = -1;
	Piece p = null;
	if (board.hasPieceAt(newLoc))
	    p = board.pieceAt(newLoc);
	else {
	    Point enPassant = new Point(newLoc.x, newLoc.y - dir);
	    if (board.hasPieceAt(enPassant))
		p = board.pieceAt(enPassant);
	}
	return p;
    }

    @Override
    public void draw(Graphics2D g, int i, int j, int size) {
	if (team == 0)
	    g.drawImage(ImageHelper.getPicture("pieces/WPawn.png"), i, j, size,
		    size, null);
	else
	    g.drawImage(ImageHelper.getPicture("pieces/BPawn.png"), i, j, size,
		    size, null);
    }

    public Piece clone() {
	Pawn p = new Pawn(loc.x, loc.y, team, board);
	p.firstMove = firstMove;
	p.enPassantable = enPassantable;
	return p;
    }

    @Override
    public double value() {
	return 1;
    }

    @Override
    public int mobility() {
	if (firstMove)
	    return 2;
	return 1;
    }
}
