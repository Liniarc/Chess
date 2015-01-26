package objects;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Piece {
    public Point loc;
    public int team; // 0 = white, 1 = black
    protected Board board;
    public boolean firstMove = true;

    public Piece(int xLoc, int yLoc, int team, Board b) {
	loc = new Point(xLoc, yLoc);
	this.team = team;
	board = b;
    }

    public ArrayList<Point> validActions() {
	ArrayList<Point> actions = validMoves();
	actions.addAll(validTakes());
	return actions;
    }

    public abstract ArrayList<Point> validMoves();

    public abstract ArrayList<Point> validTakes();

    public abstract void moveTo(Point loc);

    public abstract Piece takenPiece(Point loc);

    public boolean canMoveTo(Point loc) {
	return validMoves().contains(loc);
    }

    public boolean canTakeTo(Point loc) {
	return validTakes().contains(loc);
    }

    public int numMoves() {
	return validMoves().size() + validTakes().size();
    }

    /**
     * Calculate approximately number of legal moves. Ignores check/checkmate
     * and legal takes. A faster than numMoves/numTakes. Used in MinMax
     * 
     * @return the number of squares that can be reached by piece.
     */
    public abstract int mobility();

    public abstract ArrayList<Point> threatens();

    public boolean sameTeam(Piece p) {
	return p.team == team;
    }

    public abstract double value();

    public abstract void draw(Graphics2D g, int i, int j, int size);

    public abstract Piece clone();
}
