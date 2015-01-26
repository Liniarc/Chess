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

public class King extends Piece {
    public King(int xLoc, int yLoc, int team, Board board) {
	super(xLoc, yLoc, team, board);
    }

    @Override
    public ArrayList<Point> validMoves() {

	ArrayList<Point> moves = new ArrayList<Point>();

	for (int i = -1; i <= 1; i++)
	    for (int j = -1; j <= 1; j++) {
		if (i == 0 && j == 0)
		    continue;
		Point move = new Point(loc.x + i, loc.y + j);
		if (!board.hasPieceAt(move) && board.inBounds(move))
		    if (board.isUncheckedMove(this, move)
			    && board.validLocation(move))
			moves.add(move);
	    }

	if (firstMove && board.checkedTeam() != team) // castling
	{
	    for (int dir = -1; dir <= 1; dir += 2) {
		int i = 3;
		Point move = new Point(loc.x + i * dir, loc.y);
		while (!board.hasPieceAt(move) && board.inBounds(move)) {
		    i++;
		    move = new Point(loc.x + i * dir, loc.y);
		}
		if (board.inBounds(move)) {
		    Piece p = board.pieceAt(move);
		    if (p instanceof Rook && ((Rook) p).firstMove) {
			if (board.isUncheckedMove(this, new Point(loc.x + dir,
				loc.y))
				&& board.isUncheckedMove(this, new Point(loc.x
					+ dir * 2, loc.y)))
			    moves.add(new Point(loc.x + dir * 2, loc.y));
		    }

		}
	    }
	}

	return moves;
    }

    @Override
    public ArrayList<Point> threatens() {
	ArrayList<Point> moves = new ArrayList<Point>();
	for (int i = -1; i <= 1; i++)
	    for (int j = -1; j <= 1; j++) {
		if (i == 0 && j == 0)
		    continue;
		Point move = new Point(loc.x + i, loc.y + j);
		if (board.hasPieceAt(move))
		    moves.add(move);
	    }
	return moves;
    }

    @Override
    public ArrayList<Point> validTakes() {
	ArrayList<Point> moves = new ArrayList<Point>();
	for (int i = -1; i <= 1; i++)
	    for (int j = -1; j <= 1; j++) {
		if (i == 0 && j == 0)
		    continue;
		Point move = new Point(loc.x + i, loc.y + j);
		if (board.hasPieceAt(move) && board.pieceAt(move).team != team)
		    if (board.isUncheckedTake(this, move))
			moves.add(move);
	    }
	return moves;
    }

    @Override
    public void moveTo(Point newLoc) {
	if (firstMove)
	    firstMove = false;
	if (newLoc.distance(loc) == 2) {
	    int dir = (int) Math.signum(newLoc.x - loc.x);
	    int i = 1;
	    Point rook = new Point(loc.x + i * dir, loc.y);
	    while (!board.hasPieceAt(rook) && board.inBounds(rook)) {
		i++;
		rook = new Point(loc.x + i * dir, loc.y);
	    }
	    board.pieceAt(rook).loc = new Point(loc.x + dir, loc.y);
	}
	loc = newLoc;
    }

    @Override
    public Piece takenPiece(Point newLoc) {
	Piece p = null;
	if (board.hasPieceAt(newLoc))
	    p = board.pieceAt(newLoc);
	return p;
    }

    @Override
    public void draw(Graphics2D g, int i, int j, int size) {
	if (team == 0)
	    g.drawImage(ImageHelper.getPicture("pieces/WKing.png"), i, j, size,
		    size, null);
	else
	    g.drawImage(ImageHelper.getPicture("pieces/BKing.png"), i, j, size,
		    size, null);
    }

    public Piece clone() {
	King k = new King(loc.x, loc.y, team, board);
	k.firstMove = firstMove;
	return k;
    }

    @Override
    public double value() {
	return 512;
    }

    @Override
    public int mobility() {

	int num = 0;

	for (int i = -1; i <= 1; i++)
	    for (int j = -1; j <= 1; j++) {
		if (i == 0 && j == 0)
		    continue;
		Point move = new Point(loc.x + i, loc.y + j);
		if (!board.hasPieceAt(move) && board.inBounds(move))
		    num++;
	    }

	if (firstMove && board.checkedTeam() != team) // castling
	{
	    num += 3;
	}

	return num;
    }
}
