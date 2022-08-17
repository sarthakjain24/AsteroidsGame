package asteroids.participants;

import static asteroids.game.Constants.*;
import sounds.Sound;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import asteroids.destroyers.AlienShipDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Class that represents Bullets
 */
public class Bullets extends Participant implements AsteroidDestroyer, AlienShipDestroyer
{
    /** The outline of the Bullet */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /**
     * Creates the bullets based on the x and y position, and the speed to the constant of the BULLET_SPEED, and made
     * small ellipses, and expires the bullets after a 1000 seconds
     */
    public Bullets (double x, double y, Participant s, Controller controller)
    {
        setPosition(x, y);
        setVelocity(BULLET_SPEED, s.getRotation());
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

    /**
     * When a Bullet collides with an Asteroid, and Ship, it expires.
     */
    @Override
    public void collidedWith (Participant p)
    {
        /**
         * When the Bullet collides with the Asteroid, it expires the Asteroid, and finds the size of the asteroid, and
         * it splits the asteroid based on that, if it is necessary, increments the score depending on the level and
         * plays the sound of a bang
         */
        if (p instanceof Asteroid)
        {
            // Expire the ship from the game and increments the score by 20, and creates a loud bang sound, and splits
            // into 2 medium asteroids
            if (((Asteroid) p).getSize() == 2)
            {
                Participant.expire(this);

                controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
                controller.setScore(controller.getScore() + 20);
                controller.getSound().bangLargeAsteroid();
            }
            // Expire the ship from the game and increments the score by 50, and creates a medium bang sound, and splits
            // into 2 small asteroids
            if (((Asteroid) p).getSize() == 1)
            {
                Participant.expire(this);
                controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
                controller.setScore(controller.getScore() + 50);
                controller.getSound().bangMediumAsteroid();

            }
            // Expire the ship and the asteroid from the game and increments the score by 100, and creates a small bang
            // sound
            if (((Asteroid) p).getSize() == 0)
            {
                Participant.expire(this);

                controller.asteroidDestroyed();
                controller.getSound().bangSmallAsteroid();
                controller.setScore(controller.getScore() + 100);

            }
        }
        /**
         * When the Bullet collides with the AlienShip, it expires the AlienShip, and increments the score depending on
         * the level
         */
        if (p instanceof AlienShip)
        {
            // Expires the AlienShip if shot by a bullet
            Participant.expire(this);
            // If the level is 2, then it adds the score by 200 and creates the sound
            if (controller.getLevel() == 2)
            {
                controller.setScore(controller.getScore() + 200);
                controller.getSound().bangAlienShip();
            }
            // If the level is greater than equal to 3, then it adds the score by 1000 and creates the sound
            if (controller.getLevel() >= 3)
            {
                controller.setScore(controller.getScore() + 1000);
                controller.getSound().bangAlienShip();
            }

        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Destroys the bullets after a 1000 seconds, when the ParticipantCountdownTimer with destroy is called
        if (payload.equals("destroy"))
        {
            Participant.expire(this);
        }
    }

}
