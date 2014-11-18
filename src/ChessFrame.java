

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class ChessFrame extends JFrame {
	
	public ChessFrame() {
		super("Doodley");
		final ChessCanvas canvas = new ChessCanvas();
		add(canvas);
		setResizable(false);
		pack();
		setVisible(true);
		createBufferStrategy(2);
		setLocationRelativeTo(null);
		canvas.setup();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				canvas.stop();
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		new ChessFrame();
    }
}
