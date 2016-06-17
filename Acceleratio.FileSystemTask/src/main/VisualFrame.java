package main;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import data.*;

/**
 * The {@link JFrame} initialized on program execution.
 * <p>
 * Contains the main method.
 * 
 * @author Petar Kovacevic
 * @version 1.0
 */
public class VisualFrame extends JFrame {
	
	private static final long 	serialVersionUID = 1L;	// ignore
	
	private static final int 	FRAME_HEIGHT = 350;
	private static final int 	FRAME_WIDTH = 650;
	private static final String TITLE = "Files path";

	private VisualFrame(IDataAccessObject data) {
		this.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	data.saveToStorage();
		}});
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle(TITLE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        Container contentPane = this.getContentPane();
        VisualPanel panel = new VisualPanel(data);
        panel.initGUI();
        contentPane.add(panel);
        setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		IDataAccessObject data = initializeData();
		SwingUtilities.invokeLater(() -> {
            new VisualFrame(data).setVisible(true);
        });
	}
	
	private static IDataAccessObject initializeData() {
		try {
			Path path = Paths.get("").toAbsolutePath().resolve("res/favorites.txt");
			return new TextFileData(path);
		} catch (IOException e) {
			System.err.println("data initialization error!");
			System.exit(1);
			return null;
		}
	}

}
