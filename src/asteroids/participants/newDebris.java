package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import asteroids.game.Enhanced_controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

/**
 * Class that represents newDebris
 */
public class newDebris extends Participant
{
    /** The outline of the newDebris */
    private Shape outline;

    /**
     * Throws an IllegalArgumentException if size or variety is out of range.
     * 
     * Creates an debris of the specified variety (0 through 3) and positions it at the provided coordinates with a
     * random rotation. Its velocity has the given speed but is in a random direction.
     */
    public newDebris (int variety, double x, double y)
    {
        // Make sure size and variety are valid
        if (variety < 0 || variety > 3)
        {
            throw new IllegalArgumentException();
        }
        setPosition(x, y);
        createnewDebrisOutline(variety);
        setVelocity(2, RANDOM.nextDouble() * 2 * Math.PI);
        setRotation(2 * Math.PI * RANDOM.nextDouble());
        
        // Calls ParticipantCountdownTimer which expires the debris after a 1000 seconds
        new ParticipantCountdownTimer(this, "expire", 1000);
    }

    /**
     * Returns the outline of the debris
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Creates the outline of the newDebris based on its variety and size.
     */
    private void createnewDebrisOutline (int variety)
    {
        // This will contain the outline
        Path2D.Double poly = new Path2D.Double();

        // Fill out according to variety
        // If variety is 0, then it creates an ellipse to represent the dust from when the bullets shoot
        if (variety == 0)
        {
            Ellipse2D.Double el = new Ellipse2D.Double(0, 0, 1, 1);
            outline = el;
        }
        // If variety is 1, then it creates a line to represent the debris for the ship or alienShip
        else if (variety == 1)
        {
            poly.moveTo(0, 0);
            poly.lineTo(0 + 20, 0 + 20);
            poly.closePath();
            outline = poly;
        }
        // If variety is 2, then it creates a line to represent the debris for the ship or alienShip
        else if (variety == 2)
        {
            poly.moveTo(0, 0);
            poly.lineTo(0 - 20, 0 - 20);
            poly.closePath();
            outline = poly;
        }

    }

    @Override
    public void collidedWith (Participant p)
    {
    }
    
    /**
     * Expires the debris after the payload equals to "expire"
     */
    @Override
    public void countdownComplete (Object payload)
    {
        if (payload.equals("expire"))
        {
            Participant.expire(this);
        }
    }
}
