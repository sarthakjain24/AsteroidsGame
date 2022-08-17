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
public class Screen extends JPanel
{
    /** Legend that is displayed across the screen */
    private String legend;
    /** Instance variable to represent the score */
    private int score;
    /** Instance variable to represent the lives */
    private int lives;
    /** Instance variable to represent the level */
    private int level;

    /** Game controller */
    private Controller controller;
    
    

    /**
     * Creates an empty screen
     */
    public Screen (Controller controller)
    {
        this.controller = controller;
        legend = "";
        setPreferredSize(new Dimension(SIZE, SIZE));
        setMinimumSize(new Dimension(SIZE, SIZE));
        setBackground(Color.black);
        setForeground(Color.white);
        setFocusable(true);
    }


	/**
     * Set the legend
     */
    public void setLegend (String legend)
    {
        this.legend = legend;
    }

    /**
     * Sets the score
     */
    public void setScore (int score)
    {
        setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        score = controller.getScore();
        this.score = score;
    }

    /**
     * Sets the lives
     */
    public void setLives (int life)
    {
        this.lives = life;
    }

    /**
     * Sets the levels
     */
    public void setLevel (int level)
    {
        this.level = level;
    }

    /**
     * Paint the participants onto this panel
     */
    @Override
    public void paintComponent (Graphics graphics)
    {
        // Use better resolution
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Do the default painting
        super.paintComponent(g);

        // Draw each participant in its proper place
        for (Participant p : controller)
        {
            p.draw(g);
        }
        // Sets the font to 120 for the legend
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 120));
        int size = g.getFontMetrics().stringWidth(legend);
        g.drawString(legend, (SIZE - size) / 2, SIZE / 2);
        // Draw the legend across the middle of the panel

        // Sets the font to 30 for the score, level, and the lives
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        // Creates the score
        String score = controller.getScore() + "";
        g.drawString(score, 50, 50);
        // Creates the level
        String level = (int) controller.getLevel() + "";
        g.drawString(level, 700, 50);

        // Creates a ship when lives is equal to 1
        if (controller.getLives() == 1)
        {
            g.drawLine(50, 70, 40, 110);
            g.drawLine(50, 70, 60, 110);
            g.drawLine(42, 100, 58, 100);
        }
        // Creates two ships when lives is equal to 2
        if (controller.getLives() == 2)
        {
            g.drawLine(25, 70, 15, 110);
            g.drawLine(25, 70, 35, 110);
            g.drawLine(18, 100, 32, 100);

            g.drawLine(50, 70, 40, 110);
            g.drawLine(50, 70, 60, 110);
            g.drawLine(42, 100, 58, 100);
        }
        // Creates three ships when lives is equal to 3
        if (controller.getLives() == 3)
        {
            g.drawLine(25, 70, 15, 110);
            g.drawLine(25, 70, 35, 110);
            g.drawLine(18, 100, 32, 100);

            g.drawLine(50, 70, 40, 110);
            g.drawLine(50, 70, 60, 110);
            g.drawLine(42, 100, 58, 100);

            g.drawLine(75, 70, 65, 110);
            g.drawLine(75, 70, 85, 110);
            g.drawLine(67, 100, 83, 100);
        }
    }
}
