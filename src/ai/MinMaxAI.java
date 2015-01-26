package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import objects.Board;
import objects.Piece;
/**
 * http://redd.it/2d55fx
 * @author Thomas Liu
 *
 */
public class MinMaxAI extends AI {

    public MinMaxAI(Board b) {
	super(b);
    }

    long listTime;
    long calcTime;
    long countMoveTime;

    public void makeMove() {
	listTime = 0;
	calcTime = 0;
	countMoveTime = 0;
	Piece piece = null;
	Point move = null;
	double maxScore = Double.NEGATIVE_INFINITY;

	long totTime = System.currentTimeMillis();
	for (Piece p : board.getPieces(1)) {
	    for (Point loc : p.validActions()) {
		Point tempLoc = p.loc;
		Board tempBoard = new Board(board);
		tempBoard.removePieceAt(loc);
		tempBoard.pieceAt(tempLoc).moveTo(loc);
		double score = alphabeta(tempBoard, 2, maxScore,
			Double.POSITIVE_INFINITY, 0);

		if (score > maxScore) {
		    maxScore = score;
		    piece = p;
		    move = loc;
		}

	    }
	}

	//shit's slow man.
	System.out.println("Total time: "
		+ (System.currentTimeMillis() - totTime));
	System.out.println("List moves time: " + listTime);
	System.out.println("Calc score time: " + calcTime);
	System.out.println("num moves time: " + countMoveTime);
	if (!board.movePiece(piece, move)) {
	    System.err.println("Failed to move");
	}
    }

    /**
     * Calculates the min max value of a board using alpha-beta pruning.
     * 
     * @param node - the board to calculate value
     * @param depth
     * @param a
     * @param b 
     * @param player
     * @return double value representing the value of the board.
     */
    private double alphabeta(Board node, int depth, double a, double b,
	    int player) {
	int checkmate = node.checkCheckmate();
	if (checkmate == 1)
	    return -10000;
	if (checkmate == 0)
	    return 10000;
	if (depth == 0) {
	    long s = System.currentTimeMillis();
	    double ret = calculateScore(node);
	    calcTime += (System.currentTimeMillis() - s);
	    return ret;
	}
	if (player == 1) {
	    double v = Double.NEGATIVE_INFINITY;
	    for (Board aiMove : getPossibleMoves(node, 1)) {
		v = Math.max(v, alphabeta(aiMove, depth - 1, a, b, 0));
		a = Math.max(a, v);
		if (b <= a) {
		    break;
		}
	    }
	    return v;
	} else {
	    double v = Double.POSITIVE_INFINITY;
	    for (Board opponentMove : getPossibleMoves(node, 0)) {
		v = Math.min(v, alphabeta(opponentMove, depth - 1, a, b, 1));
		b = Math.min(b, v);
		if (b <= a) {
		    break;
		}
	    }
	    return v;
	}
    }

    /**
     * Calculates a score using piece value and mobility.
     * A higher score is better for AI.
     * 
     * @param b
     * @return
     */
    public double calculateScore(Board b) {
	double score = 0;
	for (Piece p : b.getPieces()) {
	    long s = System.currentTimeMillis();
	    int moves = p.mobility();
	    if (p.team == 0) {
		score -= p.value();
		score -= .1 / p.value() * moves;
	    } else {
		score += p.value();
		score += .1 / p.value() * moves;
	    }
	    countMoveTime += (System.currentTimeMillis() - s);
	}
	return score;
    }

    /**
     * Creates an Arraylists of boards that result from possible moves.
     * 
     * @param b - the current board position
     * @param team - the team to move.
     * @return
     */
    public ArrayList<Board> getPossibleMoves(Board b, int team) {
	long s = System.currentTimeMillis();
	ArrayList<Board> boards = new ArrayList<Board>();
	for (Piece p : b.getPieces(team)) {
	    for (Point loc : p.validActions()) {
		System.out.println(p.toString()+" " + p.validActions().size());
		Point tempLoc = p.loc;
		Board tempBoard = new Board(b);
		tempBoard.removePieceAt(loc);
		tempBoard.pieceAt(tempLoc).moveTo(loc);

		boards.add(tempBoard);
	    }
	}
	listTime += (System.currentTimeMillis() - s);
	return boards;
    }
}
