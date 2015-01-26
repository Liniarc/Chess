package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import ai.AI;
import ai.MinMaxAI;
import ai.RandAI;
import ai.FastMinMaxAI;
import objects.Piece;
import objects.defaultPieces.*;

public class Board {
    private boolean grid[][];
    ArrayList<Piece> pieces = new ArrayList<Piece>();
    Piece selection;
    int turn = 0;
    Stack<ArrayList<Piece>> pastPositions = new Stack<ArrayList<Piece>>();

    AI ai;

    boolean ignoreRules = false;

    /**
     * Creates a default chessboard.
     */
    public Board() {
	grid = new boolean[8][8];
	for (int i = 0; i < grid.length; i++)
	    for (int j = 0; j < grid[i].length; j++) {
		grid[i][j] = true;
	    }
	for (int i = 0; i < 8; i++) {
	    pieces.add(new Pawn(i, 1, 0, this));
	    pieces.add(new Pawn(i, 6, 1, this));
	}

	pieces.add(new Knight(1, 0, 0, this));
	pieces.add(new Knight(6, 0, 0, this));
	pieces.add(new Knight(1, 7, 1, this));
	pieces.add(new Knight(6, 7, 1, this));

	pieces.add(new Bishop(2, 0, 0, this));
	pieces.add(new Bishop(5, 0, 0, this));
	pieces.add(new Bishop(2, 7, 1, this));
	pieces.add(new Bishop(5, 7, 1, this));

	pieces.add(new Rook(0, 0, 0, this));
	pieces.add(new Rook(7, 0, 0, this));
	pieces.add(new Rook(0, 7, 1, this));
	pieces.add(new Rook(7, 7, 1, this));

	pieces.add(new Queen(3, 0, 0, this));
	pieces.add(new Queen(3, 7, 1, this));

	pieces.add(new King(4, 0, 0, this));
	pieces.add(new King(4, 7, 1, this));

	ai = new MinMaxAI(this);
    }

    public Board(boolean[][] g, ArrayList<Piece> p) {
	grid = g;
	pieces = new ArrayList<Piece>();
	for (Piece piece : p) {
	    Piece newPiece = piece.clone();
	    newPiece.board = this;
	    pieces.add(newPiece);

	}
    }

    /**
     * Copies a board
     * 
     * @param b
     *            board to copy.
     */
    public Board(Board b) {
	this(b.grid, b.pieces);
    }

    /**
     * Loads Board from file
     * 
     * @param b
     *            board to copy.
     */
    public Board(String file) {

	try {
	    BufferedReader br = new BufferedReader(new FileReader(
		    new File(file)));
	    String line = br.readLine();
	    String[] size = line.split(",");
	    int cols = Integer.parseInt(size[0]);
	    int rows = Integer.parseInt(size[1]);
	    grid = new boolean[cols][rows];
	    for (int i = 0; i < rows; i++) {
		line = br.readLine();
		for (int j = 0; j < cols; j++) {
		    grid[i][j] = line.charAt(j) == '1';
		}
	    }
	    line = br.readLine();
	    while (line != null) {
		String[] split = line.split(":");
		System.out.println(line);
		// split[0] is piece type. i.e. "Rook"
		// split[1] is team number.
		// split[2] is coords. i.e. "3,6"
		String name = split[0];
		int team = Integer.parseInt(split[1]);
		String[] loc = split[2].split(",");
		try {
		    pieces.add((Piece) Class
			    .forName("objects.defaultPieces." + name)
			    .getConstructor(int.class, int.class, int.class,
				    Board.class)
			    .newInstance(Integer.parseInt(loc[0]),
				    Integer.parseInt(loc[1]), team, this));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		line = br.readLine();
	    }
	    br.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	ai = new MinMaxAI(this);
    }

    /**
     * Finds all the pieces for a specific team number.
     * 
     * @param team
     * @return
     */
    public ArrayList<Piece> getPieces(int team) {
	ArrayList<Piece> teamPieces = new ArrayList<Piece>();
	for (Piece p : pieces) {
	    if (p.team == team)
		teamPieces.add(p);
	}
	return teamPieces;
    }

    public ArrayList<Piece> getPieces() {
	return pieces;
    }

    public boolean hasPieceAt(Point loc) {
	return pieceAt(loc) != null;
    }

    /**
     * Finds a piece with a given location. If no piece exists at the location,
     * returns null.
     * 
     * @param loc
     *            location to check
     * @return the piece at the location
     */
    public Piece pieceAt(Point loc) {
	for (Piece p : pieces)
	    if (p.loc.equals(loc))
		return p;
	return null;
    }

    /**
     * Checks if a piece is in bounds of the board. Does not check whether piece
     * is on a "valid" square. Use validLocation(Point) instead.
     * 
     * @param loc
     * @return
     */
    public boolean inBounds(Point loc) {
	int x = loc.x;
	int y = loc.y;
	if (x < 0 || y < 0)
	    return false;
	if (x >= grid.length || y >= grid[0].length)
	    return false;
	return true;
    }

    public boolean validLocation(Point loc) {
	return grid[loc.x][loc.y];
    }

    public void draw(Graphics2D g) {

	int size = 500 / Math.max(grid.length, grid[0].length);
	int gapx = (600 - size * grid.length) / 2;
	int gapy = (600 - size * grid[0].length) / 2;
	
	for (int i = 0; i < grid.length; i++) { //draw board
	    for (int j = 0; j < grid[i].length; j++) {
		if (grid[i][j]) {
		    if ((i + j) % 2 != 0)
			g.setColor(new Color(211, 141, 71));
		    else
			g.setColor(new Color(251, 205, 156));
		    g.fillRect(i * size + gapx, j * size + gapy, size, size);
		} else {
		    if ((i + j) % 2 != 0)
			g.setColor(new Color(150, 150, 150));
		    else
			g.setColor(new Color(210, 210, 210));
		    g.fillRect(i * size + gapx, j * size + gapy, size, size);
		}
	    }
	}

	g.setColor(Color.black);
	for (int i = 0; i < grid.length; i++) { // files letters
	    String let = "" + (char) ('a' + i);
	    g.drawString(let, i * size + gapx
		    + (-TextHelper.textWidth(let, g) + size) / 2, gapy - 10);
	}
	for (int i = 0; i < grid[0].length; i++) { // ranks
	    String num = "" + (grid[0].length - i);
	    g.drawString(num, gapx - TextHelper.textWidth(num, g) - 10, i
		    * size + gapy + (TextHelper.textHeight(g) + size) / 2);
	}

	for (int i = 0; i < pieces.size(); i++) { // draw pieces
	    Piece p = pieces.get(i);
	    p.draw(g, p.loc.x * size + gapx, (grid[0].length - 1 - p.loc.y)
		    * size + gapy, size);
	}

	if (selection != null) { // draw selection/legal moves
	    g.setColor(new Color(100, 250, 100, 160));
	    g.fillRect(selection.loc.x * size + gapx + 5,
		    (grid[0].length - 1 - selection.loc.y) * size + gapy + 5,
		    size - 10, size - 10);
	    for (Point loc : selection.validMoves()) {
		g.setColor(new Color(255, 140, 140, 160));
		g.fillRect(loc.x * size + gapx + 5,
			(grid[0].length - 1 - loc.y) * size + gapy + 5,
			size - 10, size - 10);
	    }
	    for (Point loc : selection.validTakes()) {
		g.setColor(new Color(255, 0, 0, 160));
		g.fillRect(loc.x * size + gapx + 5,
			(grid[0].length - 1 - loc.y) * size + gapy + 5,
			size - 10, size - 10);
	    }
	}

    }

    public King getKing(int team) {
	for (Piece p : pieces) {
	    if (p instanceof King && p.team == team)
		return (King) p;
	}
	return null;
    }

    public void doMouse(MouseEvent e) {
	int size = 500 / Math.max(grid.length, grid[0].length);
	int gapx = (600 - size * grid.length) / 2;
	int gapy = (600 - size * grid[0].length) / 2;
	int gridX = (e.getX() - gapx) / size;
	int gridY = grid[0].length - 1 - (e.getY() - gapy) / size;

	Point gridLoc = new Point(gridX, gridY);

	if (selection != null && turn % 2 == 0) {
	    if (movePiece(selection, gridLoc)) {
		turn++;
	    }
	    selection = null;
	} else {
	    Piece selectionTemp = selection;
	    for (Piece p : pieces) {
		if (p.loc.equals(gridLoc)) {
		    selection = p;
		    break;
		}
	    }
	    if (selection == selectionTemp)
		selection = null;
	}
    }

    /**
     * Tries to move a piece to a new location
     * 
     * @param p
     *            - the piece to be moved
     * @param loc
     *            - the new location to move the piece
     * @return whether a move was made or not
     */
    public boolean movePiece(Piece p, Point loc) {
	if (!ignoreRules) {
	    if (p.canMoveTo(loc)) {
		addPosition();
		p.moveTo(loc);
		return true;
	    }
	    if (p.canTakeTo(loc)) {
		addPosition();
		pieces.remove(p.takenPiece(loc));
		p.moveTo(loc);
		return true;
	    }
	    return false;
	} else {

	    if (loc != p.loc) {
		if (hasPieceAt(loc)) {
		    pieces.remove(p.takenPiece(loc));
		    addPosition();
		    p.moveTo(loc);
		} else {
		    addPosition();
		    p.moveTo(loc);
		}
		return true;
	    }
	    return false;
	}
    }

    public void removePieceAt(Point loc) {
	pieces.remove(pieceAt(loc));
    }

    private void addPosition() {
	ArrayList<Piece> pos = new ArrayList<Piece>();
	for (Piece p : pieces) {
	    pos.add(p.clone());
	}
	pastPositions.add(pos);
    }

    public void undoMove() {
	selection = null;
	if (!pastPositions.isEmpty())
	    pieces = pastPositions.pop();
	if (!pastPositions.isEmpty())
	    pieces = pastPositions.pop();
    }

    /**
     * @return -1 if no team is in check. Otherwise, returns the team in check.
     */
    public int checkedTeam() {
	for (Piece p : pieces) {
	    for (Point move : p.threatens()) {
		if (pieceAt(move) instanceof King && !pieceAt(move).sameTeam(p))
		    return pieceAt(move).team;
	    }
	}
	return -1;
    }

    /**
     * Checks whether moving a piece to a location will leave own king
     * vulnerable.<br>
     * <br>
     * If taking a piece at a location, use isUncheckedTake instead
     * 
     * @param movedPiece
     *            The Piece to move
     * @param loc
     *            The location to move to
     * @return Whether the performed move will leave own king vulnerable.
     */
    public boolean isUncheckedMove(Piece movedPiece, Point loc) {
	Point tempLoc = movedPiece.loc;
	Board tempBoard = new Board(grid, pieces);

	tempBoard.pieceAt(tempLoc).moveTo(loc);

	for (Piece p : tempBoard.pieces) {
	    if (p.team != movedPiece.team)
		for (Point move : p.threatens()) {
		    if (tempBoard.pieceAt(move) instanceof King
			    && tempBoard.pieceAt(move).team != p.team) {
			return false;
		    }
		}
	}
	return true;
    }

    /**
     * Checks whether taking a piece at a location will leave own king
     * vulnerable.<br>
     * <br>
     * If not taking a piece, use isUncheckedMove instead
     * 
     * @param movedPiece
     *            The Piece to move
     * @param loc
     *            The location to move to
     * @return Whether the performed move will leave own king vulnerable.
     */
    public boolean isUncheckedTake(Piece movedPiece, Point loc) {
	Point tempLoc = movedPiece.loc;
	Board tempBoard = new Board(grid, pieces);

	tempBoard.pieces.remove(tempBoard.pieceAt(loc));
	tempBoard.pieceAt(tempLoc).moveTo(loc);

	for (Piece p : tempBoard.pieces) {
	    if (p.team != movedPiece.team)
		for (Point move : p.threatens()) {
		    if (tempBoard.hasPieceAt(move)) {
			Piece king = tempBoard.pieceAt(move);
			if (king instanceof King && king.team != p.team) {
			    return false;
			}
		    }
		}
	}

	return true;
    }

    /**
     * TODO: add capability to choose pieces
     * 
     * @param promotedPiece
     */
    public void promote(Piece promotedPiece) {
	Point loc = promotedPiece.loc;
	Piece newPiece = new Queen(loc.x, loc.y, promotedPiece.team, this);

	pieces.remove(promotedPiece);
	pieces.add(newPiece);
    }

    /**
     * @return returns -1 if no checkmate. Otherwise, returns the losing team.
     */
    public int checkCheckmate() {
	int team = checkedTeam();
	if (team == -1)
	    return -1;
	for (Piece p : pieces) {
	    if (p.team == team) {
		if (p.numMoves() != 0) {
		    return -1;
		}
	    }
	}
	return team;
    }

    /**
     * Calculation per frames.
     * TODO: Let ai do processing while waiting for player to move.
     */
    public void calculate() {
	if (turn % 2 == 1 && checkCheckmate() < 0) {
	    ai.makeMove();
	    turn++;
	}
    }
}
