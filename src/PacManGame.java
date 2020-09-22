/**
* This class is used to create a new PacManGUI that will start a PacMan game.
* @author Mr.Fernandes
*/

public class PacManGame {
/**
* Main method to run program and create a new GUI
 * @param args not used
 * @throws Exception 
 */
	public static void main(String[] args) throws Exception {
		int test =0;
		
		new SplashScreen("PacManLogo.png");
		
		while (test !=100){
			test = SplashScreen.done();
			System.out.println();
		}
		
		new menu();
		
	}

}