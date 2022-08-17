package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import javax.swing.*;
import asteroids.participants.*;

/**
 * The area of the display in which the game takes place.
 */
@SuppressWarnings("serial")
public class newScreen extends JPanel {
	/** Legend that is displayed across the newScreen */
	private String legend;
	/** String that represents the finalScore */
	public String finalScoreString;
	/** Instance variable to represent the score */
	private int score;
	/** Instance variable to represent the lives */
	private int lives;
	/** Instance variable to represent the level */
	private int level;

	/** Instance variable to represent the highest score **/
	private int highscores;

	/** Instance variable to represent the count of asteroid **/
	private int counting;
	/**
	 * Boolean that flickers the color of the heart representing the lives, for
	 * representing an animation
	 */
	public boolean flickerLife;

	/** Game enhanced_controller */
	private Enhanced_controller enhanced_controller;

	/**
	 * Creates an empty newScreen
	 */
	public newScreen(Enhanced_controller enhanced_controller) {
		this.enhanced_controller = enhanced_controller;
		legend = "";
		finalScoreString = "";
		setPreferredSize(new Dimension(SIZE, SIZE));
		setMinimumSize(new Dimension(SIZE, SIZE));
		setBackground(Color.black);
		setForeground(Color.white);
		setFocusable(true);

	}

	/**
	 * Set the legend
	 */
	public void setLegend(String legend) {
		this.legend = legend;
	}

	/**
	 * Sets the score
	 */
	public void setScore(int score) {
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		score = enhanced_controller.getScore();
		this.score = score;
	}

	/**
	 * sets the highest score
	 * 
	 */
	public void setfinalScore(int highscores) {
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		highscores = enhanced_controller.getfinalScore();
		this.highscores = highscores;
	}

	/**
	 * sets the total asteroids destroyed
	 * 
	 */
	public void setasteroidcount(int counting) {
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		counting = enhanced_controller.getfinalScore();
		this.counting = counting;
	}

	/**
	 * sets the total aliens destroyed
	 * 
	 */
	public void setaliencount(int counting) {
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		counting = enhanced_controller.getfinalScore();
		this.counting = counting;
	}

	/**
	 * Sets the lives
	 */
	public void setLives(int life) {
		this.lives = life;
	}

	/**
	 * Sets the levels
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Paint the participants onto this panel
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		// Use better resolution
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// Do the default painting
		super.paintComponent(g);

		// Draw each participant in its proper place
		for (Participant p : enhanced_controller) {
			p.draw(g);
		}
		// Sets the font to 80 for the legend
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 80));
		int size = g.getFontMetrics().stringWidth(legend);
		g.drawString(legend, (SIZE - size) / 2, (SIZE / 2) - 50);
		g.setFont(new Font(Font.SERIF, Font.ITALIC, 50));
		g.drawString(finalScoreString, ((SIZE - size) / 2) - 10, (SIZE / 2) + 50);
		// Draw the legend across the middle of the panel

		// Sets the font to 30 for the score, level, and the lives
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		// Creates the score
		String score = enhanced_controller.getScore() + "";
		g.drawString("Score: " + score, 10, 50);

		// Creates the highscore
		String highscore = "High Score is " + enhanced_controller.getfinalScore();
		g.drawString(highscore, 200, 50);

		// Crates the total asteroids destroyed
		String Totasteroid = "Asteroids Destroyed: " + enhanced_controller.recordtotasteroids;
		g.drawString(Totasteroid, 400, 110);

		// Crates the total aliens destroyed
		String Totaliens = "Aliens Destroyed: " + enhanced_controller.recordtotaliens;
		g.drawString(Totaliens, 10, 110);

		// Creates the level
		String level = (int) enhanced_controller.getLevel() + "";
		g.drawString("Current Level: " + level, 500, 50);

		// Creates a ship when lives is equal to 1
		if (enhanced_controller.getLives() == 1) {
			g.setColor(Color.RED);
			g.drawString("\u2665", 50, 80);

		}
		// Creates two ships when lives is equal to 2
		if (enhanced_controller.getLives() == 2) {
			g.setColor(Color.RED);
			g.drawString("\u2665", 40, 80);
			g.drawString("\u2665", 70, 80);
		}
		// Creates three ships when lives is equal to 3
		if (enhanced_controller.getLives() == 3) {

			if (flickerLife == true) {
				g.setColor(Color.RED);
				g.drawString("\u2665", 10, 80);
				g.drawString("\u2665", 40, 80);
				g.drawString("\u2665", 70, 80);
			} else {

				g.setColor(Color.WHITE);
				g.drawString("\u2665", 10, 80);
				g.drawString("\u2665", 40, 80);
				g.drawString("\u2665", 70, 80);
			}
		}
	}

}
