

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import objects.Board;
import objects.Piece;

public class ChessCanvas extends Canvas implements MouseListener
{

	private BufferStrategy strategy;
	private final Timer timer;
	private TimerTask renderTask;

	Board board;
	ArrayList<MouseEvent> clickBuffer = new ArrayList<MouseEvent>();
	
	double dt;
	
	public ChessCanvas() {
		this.setIgnoreRepaint(true);
		this.setPreferredSize(new Dimension(800,600));
		timer = new Timer();
		board = new Board();
		addMouseListener(this);
	}


	public void start() {
		if (renderTask != null) {
			renderTask.cancel();
		}


		renderTask = new TimerTask() {

			@Override
			public void run() {
				long lasttime = System.currentTimeMillis();
				render();
				calculate();
				doMouse();
				long time = System.currentTimeMillis();
				dt = (time - lasttime)/1000.;
			}
		};
		timer.scheduleAtFixedRate(renderTask, 0, 16);
	}

	protected void stop() {
		renderTask.cancel();
	}
	
	public void setup() {
		this.createBufferStrategy(2);
		strategy = this.getBufferStrategy();

		
		start();
	}	
	

	private void render() {
		
		Graphics2D g2D = (Graphics2D) strategy.getDrawGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		drawBackground(g2D);
		drawGrid(g2D);
		drawText(g2D);
		
		
		g2D.dispose();
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
	}

	private void calculate() {
		board.calculate();
	}
	private void doMouse()
	{
		while(clickBuffer.size()>0)
		{
			MouseEvent e = clickBuffer.get(0);
			clickBuffer.remove(0);
			if (e.getX()<600)
				board.doMouse(e);
			else
				board.undoMove();
		}
	}

	private void drawBackground(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 600);
		g.setColor(new Color(255,240,200));
		g.fillRect(0, 0, 600, 600);
	}
	
	private void drawGrid(Graphics2D g)
	{
		board.draw(g);
	}

	private void drawText(Graphics2D g)
	{
		if (board.checkCheckmate()>=0)
		{
			g.drawString("CHECKMATE!", 0, 50);
		}
	}



	@Override
	public void mouseClicked(MouseEvent e)
	{
	}


	@Override
	public void mouseEntered(MouseEvent e)
	{
	}


	@Override
	public void mouseExited(MouseEvent e)
	{
	}


	@Override
	public void mousePressed(MouseEvent e)
	{
		clickBuffer.add(e);
	}


	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
}
