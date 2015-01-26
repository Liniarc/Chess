package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import objects.Board;
import objects.Piece;

public class FastMinMaxAI extends AI {

    public FastMinMaxAI(Board b) {
	super(b);
    }

    /**
     * Calculates the best move via min max. 2 deep.
     */
    public void makeMove() {
	Piece piece = null;
	Point move = null;
	double maxScore = Double.NEGATIVE_INFINITY;

	for (Piece p : board.getPieces(1)) {
	    for (Point loc : p.validActions()) {
		Point tempLoc = p.loc;
		Board tempBoard = new Board(board);
		tempBoard.removePieceAt(loc);
		tempBoard.pieceAt(tempLoc).moveTo(loc);
		if (calculateScore(tempBoard) <= maxScore) { //
		    continue;
		}
		double bestOpponentScore = Double.POSITIVE_INFINITY;
		for (Board opponentMove : getPossibleMoves(tempBoard, 0)) {
		    double score = calculateScore(opponentMove);
		    if (score < bestOpponentScore)
			bestOpponentScore = score;
		}

		if (bestOpponentScore > maxScore) {
		    piece = p;
		    move = loc;
		    maxScore = bestOpponentScore;
		}

	    }
	}

	System.out.println(maxScore);
	if (!board.movePiece(piece, move)) {
	    System.err.println("Failed to move");
	}

    }

    /**
     * Calculates a score based only on sum of value of pieces.
     * Not very smart AI, but very fast.
     * 
     * @param b
     * @return
     */
    public double calculateScore(Board b) {
	double score = 0;
	if (b.checkCheckmate() == 0) {
	    System.out.println("1 Move to win");
	    return 10000;
	}
	if (b.checkCheckmate() == 1) {
	    System.out.println("1 Move to lose");
	    return -10000;
	}
	for (Piece p : b.getPieces()) {
	    if (p.team == 0) {
		score -= p.value();
	    } else {
		score += p.value();
	    }
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
	ArrayList<Board> boards = new ArrayList<Board>();
	for (Piece p : b.getPieces(team)) {
	    for (Point loc : p.validActions()) {
		Point tempLoc = p.loc;
		Board tempBoard = new Board(b);

		tempBoard.removePieceAt(loc);
		tempBoard.pieceAt(tempLoc).moveTo(loc);
		boards.add(tempBoard);
	    }
	}
	return boards;
    }
}
