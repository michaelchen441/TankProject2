import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.Timer;

public class Arena
{
	int level; // Specifies which level and a specific arena 
	// is drawn based on which level is passed in
	int levelCount;
	public Wall[][] walls; //List of all walls in the arena
	//Every cell in the arena is can be made into a wall
	// Remains null if no wall is created in the cell
	PlayerTank playerTank; // Tank controlled by player


	int numTanksKilled;
	int recentNumTanksKilled = 0;

	//Note about TankList
	//New Tanklist is created with the creation of a new arena
	//This prevents tanks from previous levels from being drawn in later levels
	ArrayList<Tank> tankList; // List of all tanks to keep track of
	ArrayList<Explosion> explosionList;

	int[] inputMoveInfo; // Information for how to change x and y locations
	// Dependent on keypressed
	int numWallsAcross; // Dimensions across
	int numWallsDown;	// Dimensions down

	private int timer;
	public boolean transition;
	private int timerStartTransition;
	private boolean startingTransition;
	public boolean advanceLevel;
	public Wall[][] transitionWalls;

	// Arena Constructor
	public Arena(int inLevel, int inNumWallsAcross, int  inNumWallsDown) {
		level = inLevel; // Sets up which level
		levelCount = inLevel; 
		

		// Sets up arena dimensions
		numWallsAcross = inNumWallsAcross; 
		numWallsDown = inNumWallsDown;

		//Construct 2D Area of walls
		walls = new Wall[numWallsDown][numWallsAcross];

		timer = 0;
		transition = false;
		timerStartTransition = 0;
		advanceLevel = false;
		startingTransition = false;
		transitionWalls = new Wall[numWallsDown][numWallsAcross];


		for(int r = 0; r < numWallsDown; r++){
			transitionWalls[r][0] = new Wall(r, 0, false, false);
		}
		for(int r = 0; r < numWallsDown; r++){
			transitionWalls[r][numWallsAcross - 1] = new Wall(r, numWallsAcross - 1, false, false);
		}
		for(int c = 0; c < numWallsAcross; c++){
			transitionWalls[0][c] =  new Wall(0, c, false, false);
		}
		for(int c = 0; c < numWallsAcross; c++){
			transitionWalls[numWallsDown - 1][c] =  new Wall(numWallsDown - 1, c, false, false);
		}



		// Constructs a player tank with location (3,10)
		playerTank = new PlayerTank(3,10, this);
		// Constructs list of tanks
		tankList = new ArrayList<Tank>();
		// Adds player tank to arraylist of tanks to keep track of
		tankList.add(playerTank);

		explosionList = new ArrayList<Explosion>();

	}



	public void moveTanks(ResourceLibrary l){
		if(!transition){
			for(Tank tank: tankList){
				tank.move(l);
				if(tank.type != TankType.GREEN) {

					tank.fire(l);

				}
			}
		}
	}

	// Arena draw method
	public void draw(Graphics g, ResourceLibrary l) {
		timer++;
		// draws wood panel background image
		g.drawImage(l.background,0,0,null);

		if (transition){

			if(startingTransition == true){
				timerStartTransition = timer;//start timer so transition only lasts so long
				startingTransition = false;		
			}
			for(int r = 0; r < walls.length; r++){ //draws all of the walls in transition screen
				for(int c = 0; c < walls[r].length; c++){
					if(transitionWalls[r][c] != null){
						transitionWalls[r][c].draw(g, l);	
					}
				}
			}
			drawTransition(g, l);
			System.out.println(timerStartTransition);
			if(timer - timerStartTransition > 750){ //check if transition should end
				System.out.println("timer ran out");
				advanceLevel = true;//tells arena to start next level
			}


		}
		else{
			// If a cell in the arena is not null, it is considered to be a wall
			// We call the wall's draw function here to make our wall
			for(int r = 0; r < walls.length; r++){
				for(int c = 0; c < walls[r].length; c++){
					if(walls[r][c] != null){
						walls[r][c].draw(g, l);	
					}
				}
			}

			//Draws all the tanks in the tanklist
			for(Tank tank: tankList){
				tank.draw(g, l);
			}
			//Draws all the explosions in the explosionList
			for(Explosion explosion: explosionList){
				explosion.draw(g, l);
			}

			if(level == 0){
				if(numTanksKilled < 10){
					g.setColor(Color.BLACK); //Red colored rectangle
					g.fillRect(650, 0, 50, 50); //Makes rectangle for text
				}
				else if(numTanksKilled < 100){
					g.setColor(Color.BLACK); //Red colored rectangle
					g.fillRect(650, 0, 70, 50); //Makes rectangle for text
				}
				else {
					g.setColor(Color.BLACK); //Red colored rectangle
					g.fillRect(650, 0, 100, 50); //Makes rectangle for text
				}

				g.setFont(new Font("TimesRoman", Font.PLAIN, 50)); //Times New Roman font; size 50
				g.setColor(Color.WHITE); //white colored text
				g.drawString(""+numTanksKilled, 660, 45); //displays score



				//check each tank for dead
				int numTanksAlive = 0;
				numTanksKilled = 0;		// always recalc
				for(int i = 1; i < tankList.size(); i++){
					if (tankList.get(i).alive != true){
						numTanksKilled++;
					}
					else{
						numTanksAlive++;
					}
				}
				if(numTanksKilled < 10){
					for(int i = 0; i < 1 - numTanksAlive; i++){
						survivalAddTank();
					}
				}
				else{
					for(int i = 0; i < 2 - numTanksAlive; i++){
						survivalAddTank();
					}
				}

				//TODO gradually delete inner walls
				if(numTanksKilled > 23 && numTanksKilled%2 == 0 && recentNumTanksKilled != numTanksKilled){
					recentNumTanksKilled = numTanksKilled;
					deleteWall();				
				}

			}
			else{
				//Determines if all enemy tanks are dead
				//If condition is met, level is incremented
				//Doesn't check index 0 because it is a playertank
				boolean allDead = true;
				for(int i = 1; i < tankList.size(); i++){
					if (tankList.get(i).alive == true){
						allDead = false;
					}
				}
				if(allDead){
					transition = true;
					startingTransition = true;
					l.playClip(l.K_progressLevel);
				}
			}
		}


	}

	private void deleteWall()
	{
		int row = (int) (Math.random()*numWallsDown);
		int col = (int) (Math.random()*numWallsAcross);
		
		while(walls[row][col] == null || !walls[row][col].deletable){
			row = (int) (Math.random()*numWallsDown);
			col = (int) (Math.random()*numWallsAcross);
		}
		
		walls[row][col] = null;
		
	}



	public void addExplosion(int inX, int inY, ExplosionType inType){
		explosionList.add(new Explosion(inX, inY, inType));
	}
	private void survivalAddTank()
	{
		//use tanklist to find quadrant to add to killed to get random
		boolean Q1Free = true;
		boolean Q2Free = true;
		boolean Q3Free = true;
		boolean Q4Free = true;
		for(Tank t: tankList){
			if(t.alive){
				if(t.xLoc < 700 && t.yLoc < 400){
					Q2Free = false;
				}
				if(t.xLoc > 700 && t.yLoc < 400){
					Q1Free = false;
				}
				if(t.xLoc < 700 && t.yLoc > 400){
					Q3Free = false;
				}
				if(t.xLoc > 700 && t.yLoc > 400){
					Q4Free = false;
				}
			}
		}
		int x = 0;
		int y = 0;
		//multiples if statements based on quadrant to set x and y
		int quadrant = 0;
		if(Q1Free && !Q2Free && !Q3Free && !Q4Free){
			quadrant = 1;
		}
		else if(!Q1Free && Q2Free && !Q3Free && !Q4Free){
			quadrant = 2;
		}
		else if(!Q1Free && !Q2Free && Q3Free && !Q4Free){
			quadrant = 3;
		}
		else if(!Q1Free && !Q2Free && !Q3Free && Q4Free){
			quadrant = 4;
		}
		else if(!Q1Free && !Q2Free && !Q3Free && !Q4Free){
			quadrant = getRandomQuadrant();
		}
		else{
			quadrant = getRandomQuadrant();
			while(checkQuadrant(quadrant, Q1Free, Q2Free, Q3Free, Q4Free) == false){
				quadrant = getRandomQuadrant();
			}
		}

		switch(quadrant){
		case 1:
			y = 2;
			x = 25;
			break;
		case 2:
			y = 2;
			x = 2;
			break;
		case 3:
			y = 13;
			x = 2;
			break;
		case 4:
			y = 13;
			x = 25;
			break;
		}

		TankType type = survivalRandomType();

		tankList.add(new AITank(type, x, y, this));

	}

	private boolean checkQuadrant(int quadrant, boolean q1Free, boolean q2Free, boolean q3Free, boolean q4Free)
	{
		switch(quadrant){
		case 1:
			if (q1Free){
				return true;
			}
			else{
				return false;
			}
		case 2:
			if (q2Free){
				return true;
			}
			else{
				return false;
			}
		case 3:
			if (q3Free){
				return true;
			}
			else{
				return false;
			}
		case 4:
			if (q4Free){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}

	private int getRandomQuadrant()
	{
		return ((int)(Math.random()*4))+1;
	}

	private TankType survivalRandomType()
	{
		//do a bunch of ifs to get a random number (the higher the numTanksKilled, the more difficult tanks included)
		//use switch case to return tanktype based on number
		int numTankType;
		if(numTanksKilled < 31){
			numTankType = (int) (Math.random()*((numTanksKilled/5)+1));
		}
		else{
			numTankType = (int) (Math.random()*7);
		}

		switch(numTankType){
		case 0:
			return TankType.BLUE;
		case 1:
			return TankType.RED;
		case 2:
			return TankType.BLACK;
		case 3:
			return TankType.WHITE;
		case 4:
			return TankType.PINK;
		case 5:
			return TankType.YELLOW;
		case 6:
			return TankType.INVISIBLE;
		}

		return null;
	}

	private void drawTransition(Graphics g, ResourceLibrary l)
	{
		g.drawImage(l.blueTank, 350, 230, null);	
		Graphics2D	g2D = (Graphics2D)g;
		AffineTransform	backupAT = g2D.getTransform();
		AffineTransform	theAT = new AffineTransform();

		int xTurretImageLoc = 350 + 15;
		int yTurretImageLoc = 230 - 15;
		int	xTurretRotateOffset = 10;
		int yTurretRotateOffset = 40;

		theAT.rotate((Math.PI * 0.5),	xTurretImageLoc + xTurretRotateOffset,
				yTurretImageLoc + yTurretRotateOffset); //add PI/2 because turret image is upwards so that starts it horizontal

		g2D.transform(theAT);
		g.drawImage(l.blueTurret, xTurretImageLoc, yTurretImageLoc, null);

		g2D.setTransform(backupAT);


		g.drawImage(l.redTank, 350, 300, null);	
		Graphics2D	g2D2 = (Graphics2D)g;
		AffineTransform	backupAT2 = g2D2.getTransform();
		AffineTransform	theAT2 = new AffineTransform();

		int xTurretImageLoc2 = 350 + 15;
		int yTurretImageLoc2 = 300 - 15;

		theAT2.rotate((Math.PI * 0.5),	xTurretImageLoc2 + xTurretRotateOffset,
				yTurretImageLoc2 + yTurretRotateOffset); //add PI/2 because turret image is upwards so that starts it horizontal

		g2D2.transform(theAT2);
		g.drawImage(l.redTurret, xTurretImageLoc2, yTurretImageLoc2, null);

		g2D2.setTransform(backupAT2);

		g.drawImage(l.blackTank, 350, 370, null);	
		Graphics2D	g2D3 = (Graphics2D)g;
		AffineTransform	backupAT3 = g2D3.getTransform();
		AffineTransform	theAT3 = new AffineTransform();

		int xTurretImageLoc3 = 350 + 15;
		int yTurretImageLoc3 = 370 - 15;

		theAT3.rotate((Math.PI * 0.5),	xTurretImageLoc3 + xTurretRotateOffset,
				yTurretImageLoc3 + yTurretRotateOffset); //add PI/2 because turret image is upwards so that starts it horizontal

		g2D3.transform(theAT3);
		g.drawImage(l.blackTurret, xTurretImageLoc3, yTurretImageLoc3, null);

		g2D3.setTransform(backupAT3);

		g.drawImage(l.whiteTank, 350, 440, null);	
		Graphics2D	g2D4 = (Graphics2D)g;
		AffineTransform	backupAT4 = g2D4.getTransform();
		AffineTransform	theAT4 = new AffineTransform();

		int xTurretImageLoc4 = 350 + 15;
		int yTurretImageLoc4 = 440 - 15;

		theAT4.rotate((Math.PI * 0.5),	xTurretImageLoc4 + xTurretRotateOffset,
				yTurretImageLoc4 + yTurretRotateOffset); //add PI/2 because turret image is upwards so that starts it horizontal

		g2D4.transform(theAT4);
		g.drawImage(l.whiteTurret, xTurretImageLoc4, yTurretImageLoc4, null);

		g2D4.setTransform(backupAT4);



		g.drawImage(l.pinkTank, 350, 510, null);	
		Graphics2D	g2D5 = (Graphics2D)g;
		AffineTransform	backupAT5 = g2D5.getTransform();
		AffineTransform	theAT5 = new AffineTransform();

		int xTurretImageLoc5 = 350 + 15;
		int yTurretImageLoc5 = 510 - 15;

		theAT5.rotate((Math.PI * 0.5),	xTurretImageLoc5 + xTurretRotateOffset,
				yTurretImageLoc5 + yTurretRotateOffset); //add PI/2 because turret image is upwards so that starts it horizontal

		g2D5.transform(theAT5);
		g.drawImage(l.pinkTurret, xTurretImageLoc5, yTurretImageLoc5, null);

		g2D5.setTransform(backupAT5);




		g.drawImage(l.yellowTank, 350, 580, null);	
		Graphics2D	g2D6 = (Graphics2D)g;
		AffineTransform	backupAT6 = g2D6.getTransform();
		AffineTransform	theAT6 = new AffineTransform();

		int xTurretImageLoc6 = 350 + 15;
		int yTurretImageLoc6 = 580 - 15;

		theAT6.rotate((Math.PI * 0.5),	xTurretImageLoc6 + xTurretRotateOffset,
				yTurretImageLoc6 + yTurretRotateOffset); //add PI/2 because turret image is upwards so that starts it horizontal

		g2D6.transform(theAT6);
		g.drawImage(l.yellowTurret, xTurretImageLoc6, yTurretImageLoc6, null);

		g2D5.setTransform(backupAT6);


		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 45)); 

		int blueDestroyed = 0;
		int redDestroyed = 0;
		int blackDestroyed = 0;
		int whiteDestroyed = 0;
		int pinkDestroyed = 0;
		int yellowDestroyed = 0;
		int invisibleDestroyed = 0;

		if(levelCount == 1) {
			blueDestroyed = 1;
			redDestroyed = 0;
			blackDestroyed = 0;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 2) {
			blueDestroyed = 0;
			redDestroyed = 1;
			blackDestroyed = 0;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 3) {
			blueDestroyed = 1;
			redDestroyed = 2;
			blackDestroyed = 0;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;

		}
		else if(levelCount == 4) {
			blueDestroyed = 2;
			redDestroyed = 2;
			blackDestroyed = 0;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 5) {
			blueDestroyed = 0;
			redDestroyed = 0;
			blackDestroyed = 2;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 6) {
			blueDestroyed = 2;
			redDestroyed = 0;
			blackDestroyed = 3;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 7) {
			blueDestroyed = 2;
			redDestroyed = 0;
			blackDestroyed = 3;
			whiteDestroyed = 0;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 8) {
			blueDestroyed = 0;
			redDestroyed = 0;
			blackDestroyed = 2;
			whiteDestroyed = 2;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 9) {
			blueDestroyed = 0;
			redDestroyed = 0;
			blackDestroyed = 2;
			whiteDestroyed = 2;
			pinkDestroyed = 0;
			yellowDestroyed = 0;
			invisibleDestroyed = 0;
		}
		else if(levelCount == 10) {
			blueDestroyed = 0;
			redDestroyed = 0;
			blackDestroyed = 2;
			whiteDestroyed = 0;
			pinkDestroyed = 2;
			yellowDestroyed = 1;
			invisibleDestroyed = 0; 
		}
		else if(levelCount == 11) {
			blueDestroyed = 0;
			redDestroyed = 0;
			blackDestroyed = 0;
			whiteDestroyed = 1;
			pinkDestroyed = 2;
			yellowDestroyed = 1;
			invisibleDestroyed = 2; 
		}
		else if(levelCount == 12) {
			blueDestroyed = 0;
			redDestroyed = 0;
			blackDestroyed = 1;
			whiteDestroyed = 2;
			pinkDestroyed = 2;
			yellowDestroyed = 2;
			invisibleDestroyed = 2; 
		}


		g.drawString(Integer.toString(blueDestroyed), 650, 272);
		g.drawString(Integer.toString(redDestroyed), 650, 342);
		g.drawString(Integer.toString(blackDestroyed), 650, 412);
		g.drawString(Integer.toString(whiteDestroyed), 650, 482);
		g.drawString(Integer.toString(pinkDestroyed), 650, 552);
		g.drawString(Integer.toString(yellowDestroyed), 650, 622);
		g.drawString(Integer.toString(invisibleDestroyed), 650, 692);

		g.drawString("x", 500, 272);
		g.drawString("x", 500, 342);
		g.drawString("x", 500, 412);
		g.drawString("x", 500, 482);
		g.drawString("x", 500, 552);
		g.drawString("x", 500, 622);
		g.drawString("x", 500, 692);

		g.drawString("=", 800, 272);
		g.drawString("=", 800, 342);
		g.drawString("=", 800, 412);
		g.drawString("=", 800, 482);
		g.drawString("=", 800, 552);
		g.drawString("=", 800, 622);
		g.drawString("=", 800, 692);

		g.drawString(Integer.toString(blueDestroyed * 10), 930, 272);
		g.drawString(Integer.toString(redDestroyed * 50), 930, 342);
		g.drawString(Integer.toString(blackDestroyed * 125), 930, 412);
		g.drawString(Integer.toString(whiteDestroyed * 150), 930, 482);
		g.drawString(Integer.toString(pinkDestroyed * 75), 930, 552);
		g.drawString(Integer.toString(yellowDestroyed * 100), 930, 622);
		g.drawString(Integer.toString(invisibleDestroyed * 200), 930, 692);



		int totalScore = (blueDestroyed * 10) + (redDestroyed * 50) + (blackDestroyed * 125) + 
				(whiteDestroyed * 150) + (pinkDestroyed * 75) + (yellowDestroyed * 100) + (invisibleDestroyed * 200);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
		g.drawString(Integer.toString(totalScore), 1133, 460);
		g.drawString("Total Score", 1018, 400);
		g.drawLine(1025, 410, 1305, 410);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 150)); 
		String levelCompletion = "Level " + levelCount + " Completed";
		g.drawString(levelCompletion, 100, 200);


	}


	//Sets amount a certain tank has to move based on keypress
	//Calls the player tanks setInputMoveArr which takes the information of how to move
	//Everytime tank is moved, actual x and y loc changes provided are executed
	public void setInputMoveArr(int[] inInputMoveArr){
		playerTank.setInputMoveArr(inInputMoveArr);
	}

	//Returns x location of playerTank
	public int playerTankLocX() {return playerTank.getX();}
	//Returns y location of playerTank
	public int playerTankLocY() {return playerTank.getY();}
	public ArrayList<Tank> getTanks() { return tankList;}

	public void addTank(int inRow, int inCol, TankType inType){
		if(inType.equals(TankType.GREEN)){
			playerTank.setX(inCol);
			playerTank.setY(inRow);
		}
		else{
			tankList.add(new AITank(inType, inCol, inRow, this));
		}
	}

	public void addWall(int inRow, int inCol, boolean destructable, boolean deletable){
		walls[inRow][inCol] = new Wall(inRow, inCol, destructable, deletable);
	}


}
