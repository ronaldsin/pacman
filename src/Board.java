import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener, ActionListener {
	//added loading bar,menu,sound effects,ai,blue mode,scatter mode,score display,changed maze,gate to stop ghost from leaving house
	//timer for  game movement
	private Timer gameTimer = new Timer(55,this);
	
	public Timer ghostTimer = new Timer(150,this);//ghost speed
	
	//timer for pacman mouth
	private Timer animateTimer = new Timer(55,this);
	
	private Timer startTimer = new Timer (4000,this);//stops player from moving before the intro is complete
	
	private Timer gateTimer = new Timer (5000,this);
	
	private Timer blueModeTimer = new Timer (10000,this);
	
	private Timer scatterTimer = new Timer (30000,this);//every 30 sec the ghost will stop chasing
	
	private Timer scatterEndTimer = new Timer (5000,this);
	
	boolean blueMode=false;
	
	boolean start=false;
	
	boolean gate = true;//if the ghost room is open
	
	boolean scatter = false;//if the ghost are in scatter mode
	
	int gateOpened;
	
	//images 
	private static final ImageIcon WALL = new ImageIcon("images/StdWall.bmp");
	
	private static final ImageIcon FOOD = new ImageIcon("images/StdFood.bmp");
	
	private static final ImageIcon BLUE = new ImageIcon("images/Cherry.bmp");
	
	private static final ImageIcon BLANK = new ImageIcon("images/StdBlank.bmp");
	
	private static final ImageIcon DOOR = new ImageIcon("images/StdDoor.bmp");
	
	private static final ImageIcon SKULL = new ImageIcon("images/StdSkull.bmp");
	
	private char[][] maze = new char[25][27];
	
	//array for board game char from txt file
	private JLabel[][] cell = new JLabel[25][27];
	
	//pacman object
	private PacMan pacMan;
	
	private Ghost[] ghost = new Ghost[3];
	
	private int pellets = 0;
	
	private int eaten = 0;
	
	private int score =0;
	
	private int pStep;
	
	public Board() {
		
		setLayout(new GridLayout(25,27));
		setBackground(Color.BLACK);
		
		pacMan = new PacMan();
		
		ghost[0]=new Ghost(0);
		ghost[1]=new Ghost(1);
		ghost[2]=new Ghost(2);

		loadBoard();
		
		startTimer.start();
		startTimer.setRepeats(false);
	}
	
	
	private void loadBoard() {
		
		int r = 0;
		
		Scanner input;
		
		try {
			input = new Scanner(new File("maze.txt"));
			
			while(input.hasNext()){
			
			maze[r] = input.nextLine().toCharArray();
			for (int c = 0; c < maze[r].length; c++) {				
				cell[r][c]=new JLabel();
				if ( maze[r][c] == 'W' )
					cell[r][c].setIcon(WALL);
				
				else if (maze[r][c]=='F'||maze[r][c] == 'I') {
					cell[r][c].setIcon(FOOD);
					pellets++;
				}
				else if (maze[r][c] == 'T'||maze[r][c] == 'G'){
					cell[r][c].setIcon(BLANK);
				}
				else if (maze[r][c] == 'B'){
					cell[r][c].setIcon(BLUE);
				}
				else if (maze[r][c]=='P') {
					cell[r][c].setIcon(pacMan.getIcon());
					pacMan.setRow(r);
					pacMan.setColumn(c);
					pacMan.setDirection(0);
				}
				else if (maze[r][c]=='0'||maze[r][c]=='1'||maze[r][c]=='2') {
					int gNum = Character.getNumericValue(maze[r][c]);
					cell[r][c].setIcon(ghost[gNum].getIcon());
					ghost[gNum].setRow(r);
					ghost[gNum].setColumn(c);
				}
				else if (maze[r][c]=='D') {
					cell[r][c].setIcon(DOOR);
				}
				add(cell[r][c]);
			}
			r++;

		}
		input.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
			
	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		
		if (gameTimer.isRunning()==false && pacMan.isDead()==false&&start==true){
			gameTimer.start();
			ghostTimer.start();
			scatterTimer.start();
		}
		if (pacMan.isDead()==false && score!=pellets) {
			int direction = key.getKeyCode()-37;
			if (direction==0 && maze[pacMan.getRow()][pacMan.getColumn()-1] != 'W')
				pacMan.setDirection(0);
			else if (direction==1 && maze[pacMan.getRow()-1][pacMan.getColumn()] != 'W')
				pacMan.setDirection(1);
				else if (direction==2 && maze[pacMan.getRow()][pacMan.getColumn()+1] != 'W')
				pacMan.setDirection(2);
				else if (direction==3 && maze[pacMan.getRow()+1][pacMan.getColumn()] != 'W')
					pacMan.setDirection(3);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

		
	}
	private void performMove(Mover mover) {

		if (mover.getColumn()==1){
			mover.setColumn(24);
			cell[12][1].setIcon(DOOR);
		} 
			else if (mover.getColumn()==25){

			mover.setColumn(2);

			cell[12][25].setIcon(DOOR);

			}

		

		if (maze[mover.getNextRow()][mover.getNextColumn()] != 'W') {

			if (mover==pacMan)
				animateTimer.start();

			else{ 
				if (maze[mover.getRow()][mover.getColumn()]=='F'||maze[mover.getRow()][mover.getColumn()]=='I')

					cell[mover.getRow()][mover.getColumn()].setIcon(FOOD);
				
				else if(maze[mover.getRow()][mover.getColumn()]=='B')
					cell[mover.getRow()][mover.getColumn()].setIcon(BLUE);
				
				else

					cell[mover.getRow()][mover.getColumn()].setIcon(BLANK);

				mover.move();
			

					if (collided())

						death();

					else

						cell[mover.getRow()][mover.getColumn()].setIcon(mover.getIcon());
			}
		}
	}

		
	private boolean collided(){


		for (Ghost g: ghost) {

			if (g.getRow()==pacMan.getRow() && g.getColumn()==pacMan.getColumn())
				return true;
			
		}
		return false;
	}
		
	private void death() {
		if(!blueMode){//kill pacman if blue mode isnt on
			pacMan.setDead(true);
			try {
				new playAudio("sounds/killed.wav");
			} catch (Exception e) {
				System.out.println("error");
			}

			stopGame();

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);
		}
		else if(blueMode){//eat ghost instead if bluemode is active
			for (Ghost g: ghost) {

				if (g.getRow()==pacMan.getRow() && g.getColumn()==pacMan.getColumn()){
					//System.out.println("blueMode end");
					score=score+50;
					new score(score);
					g.setRow(13);
					g.setColumn(14);
					cell[13][14].setIcon(g.getIcon());
					g.setDirection(1);
					g.setHouse(true);
					gateTimer.start();
					gateTimer.setRepeats(false);
				}
				
			}
		}

		}
	private void stopGame() {

		if (pacMan.isDead() || pellets==eaten) {
			animateTimer.stop();
			gameTimer.stop();
			ghostTimer.stop();

		}
	}
	private void moveGhosts(){

		Random rnd = new Random();
		
		int chase = 1;//which direction the ghost will chase
		
		int tried =0;//how many times the AI has failed to move

		int done =0;//if AI has decided to move yet
		

		for (Ghost g: ghost) {//ai
			if(!scatter&&!blueMode){
				g.setGoalX(pacMan.getColumn());
				g.setGoalY(pacMan.getRow());
			}
			else if(scatter||blueMode){
				ghost[0].setGoalX(4);
				ghost[0].setGoalY(3);
				
				ghost[2].setGoalX(6);
				ghost[2].setGoalY(3);
				
				ghost[0].setGoalX(4);
				ghost[0].setGoalY(23);
			}
			
		int dir = rnd.nextInt(5);
			
		if(!g.inHouse()){
			
			dir = g.getDirection();
			
			if(maze[g.getNextRow()][g.getNextColumn()]=='W'){

				if(maze[g.getRow()+1][g.getColumn()]!='W'&&g.getDirection()!=1){
					dir =3;
				}
				else if(maze[g.getRow()-1][g.getColumn()]!='W'&&g.getDirection()!=3){
					dir =1;
				}
				else if(maze[g.getRow()][g.getColumn()+1]!='W'&&g.getDirection()!=0){
					dir =2;
				}
				else if(maze[g.getRow()][g.getColumn()-1]!='W'&&g.getDirection()!=2){
					dir =0;
				}
			}
			
			
				
			while((maze[g.getRow()][g.getColumn()]=='I'||maze[g.getRow()][g.getColumn()]=='i')&&done==0){
				if(chase == 1){
					if(g.getRow()<g.getGoalY()&&maze[g.getRow()+1][g.getColumn()]!='W'){
						//System.out.println("try down");
						if(g.getDirection()!=1){
							dir=3;
							chase=0;
							done=1;
							//System.out.println(chase);
							//System.out.println("down");
						}
						else{
							chase=0;
							tried++;
							//System.out.println(chase);
							//System.out.println("failed down");
						}
					}
					else if(g.getRow()>g.getGoalY()&&maze[g.getRow()-1][g.getColumn()]!='W'){
						//System.out.println("try up");
						if(g.getDirection()!=3){
							dir=1;
							chase=0;
							done=1;
							//System.out.println(chase);
							//System.out.println("up");
						}
						else{
							chase=0;
							tried++;
							//System.out.println(chase);
							//System.out.println("failed up");
						}
					}
					else if(g.getRow()==g.getGoalY()){
						chase=0;
						tried++;
						//System.out.println(chase);
					}
					else tried++;
				}
			
				if(chase == 0){
				
					if(g.getColumn()>g.getGoalX()&&maze[g.getRow()][g.getColumn()-1]!='W'){
						//System.out.println("try left");
						if(g.getDirection()!=2||tried==1){
							dir=0;
							chase=1;
							done=1;
							//System.out.println(chase);
							//System.out.println("left");
						}
					
						else{
							chase =1;
							tried++;
							//System.out.println(chase);
							//System.out.println("failed left");
						}
					}
					else if(g.getColumn()<g.getGoalX()&&maze[g.getRow()][g.getColumn()+1]!='W'){
						//System.out.println("try right");
						if(g.getDirection()!=0){
							dir=2;
							chase=1;
							done=1;
							//System.out.println(chase);
							//System.out.println("right");
							}
						else{
							chase=1;
							tried++;
							//System.out.println(chase);
							//System.out.println("failed right");
						}
					}
					
					else if(g.getColumn()==g.getGoalX()){
							chase=1;
							tried++;
							//System.out.println(chase);
		
					}
					else
						tried++;
				}
				if(tried==2){
					if(g.getColumn()==g.getGoalX()&&g.getColumn()==g.getGoalX()&&scatter||blueMode){
						if(scatter){
						scatter=false;
						//System.out.println("Goal reached Scatter end");
						g.setGoalX(pacMan.getColumn());
						g.setGoalY(pacMan.getRow());
						}
						else if(blueMode){
							g.setGoalX(25);
							g.setGoalY(23);
							if(g.getColumn()==g.getGoalX()&&g.getColumn()==g.getGoalX())
								g.setGoalY(3);
								g.setGoalX(4);
						}
					}
					while(done==0){
						dir=rnd.nextInt(5);
						if(dir==0){
							if(maze[g.getRow()][g.getColumn()-1]!='W'&&g.getDirection()!=2){
								//System.out.println("rng left");
								done=1;
								tried=0;
							}
						}
						else if(dir==2){
							if(maze[g.getRow()][g.getColumn()+1]!='W'&&g.getDirection()!=0){
								//System.out.println("rng right");
								done=1;
								tried=0;
							}
						}
						else if(dir==1){
							if(maze[g.getRow()-1][g.getColumn()]!='W'&&g.getDirection()!=3){
								//System.out.println("rng up");
								done=1;
								tried=0;
							}
						}
						else if(dir==3){
							if(maze[g.getRow()+1][g.getColumn()]!='W'&&g.getDirection()!=1){
								//System.out.println("rng down");
								done=1;
								tried=0;
							}
						}
					}
					
				}
			}//while loop end
			
			}
		else if (maze[g.getRow()][g.getColumn()]=='T'&&gate){
			dir = 1;
		}
			
		else if (maze[g.getRow()][g.getColumn()]=='G'){
			if(gateOpened>3){
				dir = 1;
				gate = false;
				gateOpened++;
				g.setHouse(false);
				if(!ghost[0].inHouse()&&!ghost[1].inHouse()&&!ghost[2].inHouse()){
					gateTimer.stop();
					gateTimer.setRepeats(false);
				}
				else{
					gateTimer.start();
				}
					
			}
			dir = 1;
			gate = false;
			gateOpened++;
			g.setHouse(false);
			//System.out.println("gate closed");
			gateTimer.start();
		}
		else if(maze[g.getRow()][g.getColumn()]=='G'&&!gate){
			dir=3;
		}
			g.setDirection(dir);

			performMove(g);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==startTimer) {
			start=true;
		}
			
		if (e.getSource()==gameTimer) {
			performMove(pacMan);
		}
		
		else if (e.getSource()==animateTimer) {
			animatePacMan();
			pStep++;
			if (pStep==3)
				pStep=0;
		}
		
		if (e.getSource()==ghostTimer) {	
			moveGhosts();
		}
		
		if (e.getSource()==gateTimer) {
			gate=true;
			//System.out.println("gate open");
		}
		
		if (e.getSource()==scatterTimer) {
			scatter=true;
			scatterEndTimer.start();
			scatterEndTimer.setRepeats(false);
			scatterTimer.stop();
			//System.out.println("scatter");
		}
		
		if(e.getSource()==scatterEndTimer){
			scatter=false;
			scatterTimer.start();
			scatterTimer.setRepeats(false);
			scatterEndTimer.stop();
			//System.out.println("scatter end");
		}
		
		if(e.getSource()==blueModeTimer){
			blueMode=false;
			blueModeTimer.stop();
			ghost[0].setIcon("images/Ghost1.bmp");
			ghost[1].setIcon("images/Ghost2.bmp");
			ghost[2].setIcon("images/Ghost3.bmp");
			ghostTimer.setDelay(150);
		}

	}
	private void animatePacMan(){

		if (pStep == 0) {

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon

			(PacMan.IMAGE[pacMan.getDirection()][1]);

			animateTimer.setDelay(50);
			try {
				new playAudio("sounds/pacchomp.wav");
			} catch (Exception e) {
				System.out.println("Error");

			}
		}

		else if (pStep==1){

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(BLANK);
		}

		else if (pStep ==2) {

			pacMan.move();

		if (maze[pacMan.getRow()][pacMan.getColumn()]=='F'){
			score=score+10;
			eaten++;
			if(pellets==eaten)
			stopGame();
			new score(score);
			maze[pacMan.getRow()][pacMan.getColumn()]='E';
		}
		if (maze[pacMan.getRow()][pacMan.getColumn()]=='B'){
			//System.out.println("blueMode");
			score=score+50;
			new score(score);
			ghostTimer.setDelay(300);
			blueModeTimer.start();
			blueModeTimer.setRepeats(false);
			blueMode=true;
			maze[pacMan.getRow()][pacMan.getColumn()]='b';
			for(Ghost g:ghost){
				g.setIcon("images/BlueGhost.bmp");
			}
		}
		if (maze[pacMan.getRow()][pacMan.getColumn()]=='I'){
			score=score+10;
			new score(score);
			maze[pacMan.getRow()][pacMan.getColumn()]='i';
		}
		

		animateTimer.stop();

		if (pacMan.isDead())
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);

		else

			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(PacMan.IMAGE[pacMan.getDirection()][0]);

		}

	}

}

