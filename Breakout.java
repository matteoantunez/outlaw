import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 600;
	public static final int APPLICATION_HEIGHT = 800;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
			(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private int NTURNS = 3;

	//Pause time
	private static int PAUSE_TIME = 5;

	//Bricks left
	private int BRICKS_LEFT = NBRICKS_PER_ROW * NBRICK_ROWS;

	private GRect paddle;
	private GOval ball;
	private GRect brick;
	private double dx, dy;

	public void run() {
		//Sets window size
		setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);

		//Adds mouse Commands
		addMouseListeners();

		//Draw Grid of Bricks
		brickGrid();

		//Draws Paddle
		insertPaddle();

		//Draws Ball
		insertBall();

		//Displays Overall Win
		if (NTURNS == 0 && BRICKS_LEFT != 0){
			lose();
		}

		//Displays Overall Lose
		if (BRICKS_LEFT == 0) {
			win();
		}
	}

	public void brickGrid () {
		for (int i = 0; i < NBRICK_ROWS; i++){
			for(int j = 0; j < NBRICKS_PER_ROW; j++){
				double x = i * BRICK_WIDTH;
				double y = j * BRICK_HEIGHT;

				brick = new GRect (x + (i * BRICK_SEP), y + BRICK_Y_OFFSET + (j * BRICK_SEP), BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				if (j == 0 || j == 1){
					brick.setColor(Color.RED);
				} else if (j == 2 || j == 3){
					brick.setColor(Color.ORANGE);
				} else if (j == 4 || j == 5){
					brick.setColor(Color.YELLOW);
				} else if (j == 6 || j == 7){
					brick.setColor(Color.GREEN);
				} else {
					brick.setColor(Color.CYAN);
				}

				add(brick);
			}
		}
	}

	public void insertPaddle () {
		paddle = new GRect (0, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}

	public void insertBall() {
		//Ball Coordinates
		int ballX = getWidth() / 2 - BALL_RADIUS / 2;
		int ballY = getHeight() / 2 - BALL_RADIUS / 2;

		while (NTURNS > 0){
			waitForClick();
			drawBall(ballX, ballY);
			NTURNS--;
		}
	}

	public void drawBall(int x, int y){
		//Generates ball
		ball = new GOval (x, y, BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.BLUE);
		add(ball);

		//Random Generates Numbers
		RandomGenerator rgen = RandomGenerator.getInstance();

		//Makes the ball move
		dx = rgen.nextDouble(1.0, 3.0);
		dy = 2;
		while (ball.getY() < getHeight() - BALL_RADIUS){

			//Detects if a ball hits something
			GObject collider = getCollidingObject();

			//Moves the Ball
			ball.move(dx, dy);
			if (ball.getX() + BALL_RADIUS > getWidth()){
				dx = -dx;
			} else if (ball.getX() < 0){
				dx = -dx;
			} else if (ball.getY() < 0){
				dy = -dy;
				//Detects if the ball hits something
			} else if (collider == paddle && dy > 0){
				dy = -dy;
			} else if (collider != null && collider != paddle){
				remove(collider);
				dy = -dy;
				BRICKS_LEFT--;
			} else if (ball.getY() + BALL_RADIUS > getHeight() - BALL_RADIUS){
				remove(ball);
			} else if (BRICKS_LEFT == 0) {
				remove(ball);
			}/*else if (rgen.nextBoolean(0.5)) {
			}
					dx = -dx;
			}*/
			pause(PAUSE_TIME);
		}
	}

	public GObject getCollidingObject() {
		GObject colision = getElementAt(ball.getX(), ball.getY());
		if (colision == null){
			colision = getElementAt(ball.getX() + BALL_RADIUS, ball.getY());
		}
		if (colision == null){
			colision = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS);
		}
		if (colision == null){
			colision = getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS);
		}
		return colision;

	}

	@Override public void mouseMoved (MouseEvent e) {
		//Sets Paddle location
		double x = e.getX() - paddle.getWidth()/2;

		//Moves Paddle
		if (x > 0){
			if (x < getWidth() - paddle.getWidth()){
				paddle.setLocation(x, getHeight() - PADDLE_Y_OFFSET);
			}
		}
	}

	public void lose() {
		GLabel lose = new GLabel ("YOU LOSE");
		lose.setLocation(getWidth() / 2 - lose.getWidth() * 3, getHeight() / 2 - lose.getHeight() * 3);
		lose.setColor(Color.BLACK);
		lose.setFont("Monospaced-70");
		add(lose);
	}

	public void win() {
		GLabel win = new GLabel ("YOU WIN! :)");
		win.setLocation(getWidth() / 2 - win.getWidth() * 3, getHeight() / 3 - win.getHeight() * 3);
		win.setColor(Color.BLACK);
		win.setFont("Monospaced-70");
		add(win);
	}

}

