package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import objects.Board;
import objects.Piece;

public class RandAI extends AI{
    Board board;
    int seed = 1;

    public RandAI(Board b) {
	super(b);
    }
    
    public RandAI(Board b, int seed) {
	super(b);
	this.seed = seed;
    }
    
    /**
     * Makes a "predictable" random move from a seed.
     * Selects a random piece and if it has valid moves, makes a random move.
     * 
     * Not every move has an equal chance of being made.
     */
    public void makeMove() {
	seed += 4201;
	Piece piece = board.getPieces(1).get(seed % board.getPieces(1).size());
	while (piece.numMoves() == 0)
	{
	    seed++;
	    piece = board.getPieces(1).get(seed % board.getPieces(1).size());
	}
	seed += 2161;
	seed %= 7103;
	
	Point move = piece.validActions().get(seed % piece.validActions().size());
	
	if (!board.movePiece(piece, move)) {
	    System.err.println("Failed to move");
	}

    }
}
