import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class SplashScreen extends JWindow {//makes loading screen


	private static JProgressBar progressBar;
	private static int counter = 1;
	private static int TimerSpeed = 10;
	private static int maximum = 100;
	private static Timer progressBarTimer;
	static int done = 0;

	public SplashScreen(String image) {
		createSplash(image);
	}


	private void createSplash(String image) {



		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JLabel splashImage = new JLabel(new ImageIcon((image)));
		panel.add(splashImage);


		progressBar = new JProgressBar();
		progressBar.setMaximum(maximum);


		progressBar.setForeground(new Color(50,150,200));
		progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(progressBar, BorderLayout.SOUTH);


		this.setContentPane(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);


		startProgressBar();


	}


	private void startProgressBar(){

		progressBarTimer = new Timer(TimerSpeed, new ActionListener(){


			@Override
			public void actionPerformed(ActionEvent arg0) {
				progressBar.setValue(counter);


				if (maximum == counter){
					SplashScreen.this.dispose();
					progressBarTimer.stop();
					done = 1;


				}
				counter++;
			}
	
			});
		progressBarTimer.start();
		
		}

	public static int done(){
		return counter;
		
	}


}