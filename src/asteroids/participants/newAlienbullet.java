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
import asteroids.game.Enhanced_controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Class that represents newAlienbullet
 */
public class newAlienbullet extends Participant implements AsteroidDestroyer
{

    /** The outline of the newAlienbullet */
    private Shape outline;

    /** The game Enhanced_controller */
    private Enhanced_controller enhanced_controller;

    /**
     * Creates the newAlienbullets based on the x and y position, and the speed to the constant of the BULLET_SPEED, and
     * made small ellipses, and expires the bullets after a 1000 seconds with a direction to where the newAlienbullet is
     * shot towards
     */

    public newAlienbullet (double x, double y, Participant s, Enhanced_controller enhanced_controller, double direction)
    {
        setPosition(x, y);
        setVelocity(BULLET_SPEED, direction);
        new ParticipantCountdownTimer(this, "destroy", BULLET_DURATION);
        Ellipse2D poly = new Ellipse2D.Double(1, 1, 1, 1);
        outline = poly;
        this.enhanced_controller = enhanced_controller;
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
         * When the newAlienbullet collides with the Asteroid, it expires the Asteroid, and finds the size of the asteroid,
         * and it splits the asteroid based on that, if it is necessary, increments the score depending on the level and
         * plays the sound of a bang
         */
        if (p instanceof newAsteroid)
        {
            // Expire the ship from the game and creates a loud bang sound, and splits into 2 medium asteroids
            if (((newAsteroid) p).getSize() == 2)
            {
                Participant.expire(this);
                enhanced_controller.asteroidDestroyed();
                enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
                enhanced_controller.getSound().bangLargeAsteroid();
            }
            // Expire the ship from the game and creates a medium bang sound, and splits into 2 small asteroids
            if (((newAsteroid) p).getSize() == 1)
            {
                Participant.expire(this);

                enhanced_controller.asteroidDestroyed();
                enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
                enhanced_controller.getSound().bangMediumAsteroid();
            }
            // Expire the ship and the asteroid from the game, and creates a small bang sound
            if (((newAsteroid) p).getSize() == 0)
            {
                Participant.expire(this);
                enhanced_controller.asteroidDestroyed();
                enhanced_controller.getSound().bangSmallAsteroid();
            }
        }
        /**
         * When the newAlienbullet collides with the Ship, it expires the Ship
         */
        if (p instanceof newShip)
        {
            Participant.expire(this);
            enhanced_controller.getSound().bangShip();
            enhanced_controller.shipDestroyed();
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
