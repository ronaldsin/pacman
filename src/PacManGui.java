/**
* This class creates a PacMan GUI that extends the JFrame class. It has a Board (JPanel) and
* includes a constructor method that sets up the frame and adds a key listener to the board.
*/
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;
@SuppressWarnings("serial")
public class PacManGui extends JFrame {

 // Board panel
	private Board board = new Board();
	JFrame main = new JFrame();
	static JLabel score = new JLabel("0000000");
	JLabel scoreTitle = new JLabel("Score");
	static JLabel lives = new JLabel();
	//JLabel L

 /**
 * PacMan GUI constructor
 */
	public PacManGui()  {

	JFrame main = new JFrame();
	//1. Setup the GUI
	main.setSize(600, 800);
	main.setTitle("PacMan");
	main.getContentPane().setBackground( Color.BLACK );
	main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	main.setVisible(true);
	main.setLayout(null);
	
	main.add(scoreTitle);
	scoreTitle.setBounds(40,20,100,20);
	scoreTitle.setFont(new Font("Arial", Font.BOLD, 20));
	scoreTitle.setForeground(Color.WHITE);
	scoreTitle.setVisible(true);
	
	main.add(score);//displays the score
	score.setBounds(30,50,80,30);
	score.setFont(new Font("Arial", Font.BOLD, 20));
	score.setForeground(Color.WHITE);
	score.setVisible(true);
	
	//2. Listen for events on the board and add the board to the GUI
	main.addKeyListener(board);
	main.add(board);
	board.setBounds(-10, 60, 600, 600);

	//3. Make GUI visible
	board.setVisible(true);
	
	try {//play intro music
		new playAudio("sounds/pacmanintro.wav");

	} 
	catch (Exception i) {
		System.out.println("error");
	}


	}
	
}