package asteroids.participants;

import static asteroids.game.Constants.*;
import asteroids.participants.*;
import sounds.Sound;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Class that represents AlienBullet
 */
public class AlienBullet extends Participant implements AsteroidDestroyer
{

    /** The outline of the AlienBullet */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /**
     * Creates the AlienBullets based on the x and y position, and the speed to the constant of the BULLET_SPEED, and
     * made small ellipses, and expires the bullets after a 1000 seconds with a direction to where the AlienBullet is
     * shot towards
     */

    public AlienBullet (double x, double y, Participant s, Controller controller, double direction)
    {
        setPosition(x, y);
        setVelocity(BULLET_SPEED, direction);
        new ParticipantCountdownTimer(this, "destroy", BULLET_DURATION);
        Ellipse2D poly = new Ellipse2D.Double(1, 1, 1, 1);
        outline = poly;
        this.controller = controller;
        getOutline();
    }

    /**
     * Returns the outline of the ship
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        /**
         * When the AlienBullet collides with the Asteroid, it expires the Asteroid, and finds the size of the asteroid,
         * and it splits the asteroid based on that, if it is necessary, increments the score depending on the level and
         * plays the sound of a bang
         */
        if (p instanceof Asteroid)
        {
            // Expire the ship from the game and creates a loud bang sound, and splits into 2 medium asteroids
            if (((Asteroid) p).getSize() == 2)
            {
                Participant.expire(this);
                controller.asteroidDestroyed();
                controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
                controller.getSound().bangLargeAsteroid();
            }
            // Expire the ship from the game and creates a medium bang sound, and splits into 2 small asteroids
            if (((Asteroid) p).getSize() == 1)
            {
                Participant.expire(this);

                controller.asteroidDestroyed();
                controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
                controller.getSound().bangMediumAsteroid();
            }
            // Expire the ship and the asteroid from the game, and creates a small bang sound
            if (((Asteroid) p).getSize() == 0)
            {
                Participant.expire(this);
                controller.asteroidDestroyed();
                controller.getSound().bangSmallAsteroid();
            }
        }
        /**
         * When the AlienBullet collides with the Ship, it expires the Ship
         */
        if (p instanceof Ship)
        {
            Participant.expire(this);
            controller.getSound().bangShip();
            controller.shipDestroyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Destroys the bullets after a 1000 seconds
        if (payload.equals("destroy"))
        {
            Participant.expire(this);
        }
    }

}
