import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

/*TODO:
  Finish Ending Text
  Restart Button */

public class Outlaw extends GraphicsProgram {

	//Sets Window
	public static final int APPLICATION_WIDTH = 1280;
	public static final int APPLICATION_HEIGHT = 720;

	//Space between Horse
	public static final int SPACE = 180;

	//Sets the Width of the Image
	public static final int PIC_WIDTH = 200;

	//Sets animation time
	public static final int PAUSE_TIME = 33;

	//Image space / padding
	public static final int IMG_SPACE = 20;

	//Keeps track of game
	public int LIVES = 3;
	public int POINTS = 0;
	public int HORSES = 0;

	//Tells the game to run or stop
	public int ACTIVE = 1;

	public GLabel startGame;
	public GLabel score;
	public GImage horse;
	public GImage background;
	public double dx, dy;

	//Methods
	public void run() {
		//Game End / Just Opened
		while (ACTIVE >= 0){
			//Game Starts
			while (ACTIVE == 1){
				//Detects Mouse
				addMouseListeners();

				//Sets Application Size
				setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);

				//Background
				background();

				//Ending
				ending();
			}
			//Resets the game
			reset();
		}
	}

	public void background() {
		while (LIVES > 0) {
			background = new GImage ("Desert1.jpg");
			background.setLocation(0,0);
			add(background);
			
			//Gives game a title
			title(getWidth() / 4, 70);
			title(getWidth() / 4 - 7, 70);

			startGame();

			//Waits to start game
			waitForClick();
			remove(startGame);

			//Runs game
			while (LIVES > 0){
				lifeTrack();
				moveHorse();
				keepScore();
			}
		}
	}

	public void startGame() {
		//Informs the Player to start the game
		startGame = new GLabel ("Click to Start");
		startGame.setLocation(getWidth() / 4, getHeight() / 2 + 20);
		startGame.setColor(Color.BLACK);
		startGame.setFont("Monospaced-70");
		add(startGame);
	}

	public void title(int x, int y) {
		//Game Title
		GLabel title = new GLabel ("Outlaw");
		title.setLocation(x, y);
		title.setColor(Color.BLACK);
		title.setFont("Monospaced-70");
		add(title);
	}

	public void getHorse(double x, int y) {
		//Sets up dog-horse image
		horse = new GImage ("Horse.jpg");
		horse.setLocation(x, y);
		add(horse);
	}

	public void drawHorses(int horseNum) {
		//Placement of three possible horses
		if (horseNum == 1){
			getHorse(0, getHeight() / 4);
		} else if (horseNum == 2) {
			getHorse(0, getHeight() / 4 + SPACE);
		} else if (horseNum== 3) {
			getHorse(0, getHeight() / 4 + SPACE * 2);
		}
	}

	public void moveHorse() {
		//Random Generator
		RandomGenerator rgen = RandomGenerator.getInstance();

		//Randomized the placement of the dog-horse
		if (HORSES == 0) {
			drawHorses(2);
			HORSES++;
		} else if (horse.getX() + horse.getWidth() > getWidth()) {
			drawHorses(rgen.nextInt(1, 3));
			HORSES++;
		} else if (horse == null){
			drawHorses(rgen.nextInt(1, 3));
			HORSES++;
		}
		
		//Moves the dog-horse
		dx = (1.0 + HORSES);
		dy = 0;
		horse.move(dx, dy);

		//Removes dog-horse if hits the other end of the window
		if (horse.getX() + horse.getWidth() > getWidth()){
			remove(horse);
			LIVES--;
		}

		pause(PAUSE_TIME);
	}

	public void keepScore() {
		//Sets initial score text
		if (POINTS == 0){
			score = new GLabel("Current Score:");
			score.setLocation(getWidth() / 8, 140);
			score.setColor(Color.BLACK);
			score.setFont("Monospaced-70");
			add(score);
		}
		
		//Updates score text
		if (POINTS > 0) {
			remove(score);
			score = new GLabel("Current Score:" + POINTS);
			score.setLocation(getWidth() / 8, 140);
			score.setColor(Color.BLACK);
			score.setFont("Monospaced-70");
			add(score);
		}
	}

	public void lifeTrack() {
		//Display lives
		if (LIVES == 3){
			GImage max = new GImage ("Horse1.jpg");
			max.setLocation(getWidth() * 3 / 4, IMG_SPACE);
			add(max);
		}
		if (LIVES == 2) {
			GImage mid = new GImage ("Horse2.jpg");
			mid.setLocation(getWidth() * 3 / 4, IMG_SPACE);
			add(mid);
		} 
		if (LIVES == 1) {
			GImage low = new GImage ("Horse3.jpg");
			low.setLocation(getWidth() * 3 / 4, IMG_SPACE);
			add(low);
		}
	}

	@Override public void mousePressed(MouseEvent e) {
		//Gets coordinates of mouse click
		shootingHorse(e.getX(), e.getY());
	}

	public void shootingHorse (int x, int y) {
		//Random Generator
		RandomGenerator rgen = RandomGenerator.getInstance();

		//Detects Object Selected
		GObject shotHorse = getElementAt(x, y);

		//Removes object if a horse
		if (shotHorse == horse) {
			remove(shotHorse);
			POINTS++;
			drawHorses(rgen.nextInt(1, 3));
			HORSES++;
		}
	}

	public void ending () {
		if (LIVES == 0){
			//End active game
			ACTIVE--;

			//Sets Ending screen
			background = new GImage ("Desert3.jpg");
			background.setLocation(0,0);
			add(background);

			//Gives game a title
			title(getWidth() / 4, 70);
			title(getWidth() / 4 - 7, 70);

			//Displays ending score
			GLabel results = new GLabel ("Your Score Was: " + POINTS);
			results.setLocation(getWidth() / 6, getHeight() / 4);
			results.setColor(Color.BLACK);
			results.setFont("Monospaced-70");
			add(results);

			//Gives option to play again
			GLabel playAgain = new GLabel("Play again?");
			playAgain.setLocation(getWidth() / 4 + 50, getHeight() / 4 + 70);
			playAgain.setColor(Color.BLACK);
			playAgain.setFont("Monospaced-70");
			add(playAgain);
		}

	}

	public void reset() {
		//Resets the game
		while (LIVES == 0){
			waitForClick();
			ACTIVE++;
			LIVES = 3;
			HORSES = 0;
			POINTS = 0;
		}
	}
}