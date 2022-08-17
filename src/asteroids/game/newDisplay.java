package asteroids.game;

import javax.swing.*;
import static asteroids.game.Constants.*;
import java.awt.*;

/**
 * Defines the top-level appearance of an Asteroids game.
 */
@SuppressWarnings("serial")
public class newDisplay extends JFrame {
	/** The area where the action takes place */
	private newScreen screen;

	/**
	 * Lays out the game and creates the enhanced_controller
	 */
	public newDisplay(Enhanced_controller enhanced_controller) {
		// Title at the top
		setTitle(TITLE);

		// Default behavior on closing
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// The main playing area and the enhanced_controller
		screen = new newScreen(enhanced_controller);

		// This panel contains the screen to prevent the screen from being
		// resized
		JPanel screenPanel = new JPanel();
		screenPanel.setLayout(new GridBagLayout());
		screenPanel.add(screen);

		// This panel contains buttons and labels
		JPanel controls = new JPanel();

		// The button that starts the game
		JButton startGame = new JButton(START_LABEL);
		controls.add(startGame);

		// Organize everything
		JPanel mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(controls, "North");
		mainPanel.add(screenPanel, "Center");
		JOptionPane.showMessageDialog(mainPanel,
				"Use the UP Arrow, W, I to move up \n" + "Use the DOWN Arrow, S, K, or SPACE to shoot \n"
						+ "Use the LEFT Arrow, A, J to rotate left\n" + "Use the RIGHT Arrow, D, L to rotate right\n"
						+ "Use T to teleport the ship\n" + "Click the  N or Start Game button to play a new Game");

		setContentPane(mainPanel);
		pack();

		// Connect the enhanced_controller to the start button
		startGame.addActionListener(enhanced_controller);
	}

	/**
	 * Called when it is time to update the screen display. This is what drives the
	 * animation.
	 */
	public void refresh() {
		screen.repaint();

	}

	/**
	 * Sets the large legend
	 */
	public void setLegend(String s, String a) {

		screen.setLegend(s);
		screen.finalScoreString = a;

	}

	/**
	 * Sets the score
	 */
	public void setScore(int score) {
		screen.setScore(score);

	}

	public void switchLifeLight(boolean lifeLight) {
		screen.flickerLife = lifeLight;
	}

	/**
	 * Sets the lives
	 */
	public void setLives(int lives) {
		screen.setLives(lives);

	}

	/**
	 * Sets the levels
	 */
	public void setLevel(int level) {
		screen.setLevel(level);

	}
	
	
	public void recordasteroids(int count)
	{
		screen.setasteroidcount(count);
	}
	
	
	public void recordaliens(int count)
	{
		screen.setaliencount(count);
	}
}
