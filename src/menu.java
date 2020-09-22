import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class menu extends JFrame implements ActionListener{
	JButton startButton = new JButton(new ImageIcon("StartButton.png"));
	JButton quitButton = new JButton(new ImageIcon("QuitButton.png"));
	JLabel titleLabel = new JLabel(new ImageIcon("PacManLogo.png"));
	JFrame menuFrame = new JFrame();
	
	public menu(){
		
		menuFrame.setSize(600,600);
		menuFrame.setLayout(null);
		menuFrame.getContentPane().setBackground(new Color(250,100,0));
		menuFrame.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		titleLabel.setBounds(50,50,500,150);//title
		menuFrame.add(titleLabel);
		titleLabel.setVisible(true);
		
		startButton.setBounds(150, 250, 250, 100);//start button, starts game
		menuFrame.add(startButton);
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(false);
		startButton.setBorderPainted(false);
		startButton.setVisible(true);
		startButton.addActionListener(this);
		
		
		quitButton.setBounds(150, 400, 250, 100);//quit button
		menuFrame.add(quitButton);
		quitButton.setVisible(true);
		quitButton.setContentAreaFilled(false);
		quitButton.setBorderPainted(false);
		quitButton.setVisible(true);
		quitButton.addActionListener(this);
			
			
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton){
			
			new PacManGui();
			menuFrame.dispose();
		}
		if(e.getSource() == quitButton){
			menuFrame.dispose();
		}
		
	}
		
		
}
		
		
		

