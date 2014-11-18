package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import objects.Board;
import objects.Piece;

public class BasicAI
{
	Board board;
	public BasicAI(Board b)
	{
		board = b;
	}
	
	public void makeMove()
	{
		Piece piece = null;
		Point move = null;
		double maxScore = Double.NEGATIVE_INFINITY;
		
		for (Piece p: board.getPieces(1))
		{
			for (Point loc : p.validActions())
			{
				Point tempLoc = p.loc;
				Board tempBoard = new Board(board);

				tempBoard.removePieceAt(loc);
				tempBoard.pieceAt(tempLoc).moveTo(loc);
//				System.out.println( calculateScore(tempBoard));
				double bestOpponentScore = Double.POSITIVE_INFINITY; 
				for (Board opponentMove : getPossibleMoves(tempBoard, 0))
				{
					double score = calculateScore(opponentMove);
					if (score < bestOpponentScore)
						bestOpponentScore = score;
				}
//				System.out.println(getPossibleMoves(tempBoard, 0).size());
				if (bestOpponentScore > maxScore)
				{
					piece = p;
					move = loc;
					maxScore = bestOpponentScore;
				}
				
			}
//			for (Point loc : p.validTakes())
//			{
//				Point tempLoc = p.loc;
//				Board tempBoard = new Board(board);
//				
//				tempBoard.removePieceAt(loc);
//				tempBoard.pieceAt(tempLoc).moveTo(loc);
//				
//				double score = calculateScore(tempBoard);
//				if (score > maxScore)
//				{
//					piece = p;
//					move = loc;
//					maxScore = score;
//				}
//			}
			
		}
		
		
		if (!board.movePiece(piece, move))
		{
			System.err.println("Failed to move");
		}
		
	}
	
	public double calculateScore(Board b)
	{
		double score = 0;
		if (b.checkCheckmate() == 1)
		{
			System.out.println("1 Move to win");
			return 10000;
		}
		if (b.checkCheckmate() == 0)
		{
			System.out.println("1 Move to lose");
			return -10000;
		}
		for (Piece p : b.getPieces())
		{
			if (p.team == 0)
			{
				score -= p.value();
				score -= .1/p.value() * p.numMoves();
			}
			else
			{
				score += p.value();
				score += .1/p.value() * p.numMoves();
			}
		}
		return score;
	}
	
	public ArrayList<Board> getPossibleMoves(Board b, int team)
	{
		ArrayList<Board> boards = new ArrayList<Board>();
		for (Piece p: b.getPieces(team))
		{
			for (Point loc : p.validActions())
			{
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
