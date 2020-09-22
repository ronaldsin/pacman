/**

* This class s used to create Ghost objects. 

* It includes a constant ImageIcon array to hold the various ghost pictures

* and a constructor method that sets the Ghosts' images

*/



import javax.swing.ImageIcon;



@SuppressWarnings("serial")



public class Ghost extends Mover{


	public static final ImageIcon[] IMAGE = {



			new ImageIcon("images/Ghost1.bmp"),

			new ImageIcon("images/Ghost2.bmp"),

			new ImageIcon("images/Ghost3.bmp")



	};

	public Ghost(int gNum) {



		this.setIcon(IMAGE[gNum]);



	}
	public void setIcon(String file) {



		this.setIcon(new ImageIcon(file));



	}



	}