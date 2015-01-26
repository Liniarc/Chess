package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import objects.Board;
import objects.Piece;

public abstract class AI {
    Board board;

    public AI(Board b) {
	board = b;
    }

    public abstract void makeMove();
}
