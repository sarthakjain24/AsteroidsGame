package asteroids.participants;

import static asteroids.game.Constants.*;
import sounds.Sound;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import asteroids.destroyers.AlienShipDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.game.Enhanced_controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Class that represents New_Bullets
 */
public class New_Bullet extends Participant implements AsteroidDestroyer, AlienShipDestroyer
{
    /** The outline of the New_Bullet */
    private Shape outline;

    /** The game enhanced_controller */  
    private Enhanced_controller enhanced_controller;

    /**
     * Creates the New_Bullets based on the x and y position, and the speed to the constant of the New_Bullet_SPEED, and made
     * small ellipses, and expires the New_Bullets after a 1000 seconds
     */
    public New_Bullet (double x, double y, Participant s, Enhanced_controller enhanced_controller)
    {
        setPosition(x, y);
        setVelocity(15, s.getRotation());
        setRotation(s.getRotation());
        new ParticipantCountdownTimer(this, "destroy", 1000);
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0, 0);
        poly.lineTo(0, 10);
        poly.lineTo(2, 10);
        poly.lineTo(2, 0);
        poly.lineTo(0, 0);
        poly.closePath();
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

    /**
     * When a New_Bullet collides with an Asteroid, and Ship, it expires.
     */
    @Override
    public void collidedWith (Participant p)
    {
        /**
         * When the New_Bullet collides with the Asteroid, it expires the Asteroid, and finds the size of the asteroid, and
         * it splits the asteroid based on that, if it is necessary, increments the score depending on the level and
         * plays the sound of a bang
         */
        if (p instanceof newAsteroid)
        {
            // Expire the ship from the game and increments the score by 20, and creates a loud bang sound, and splits
            // into 2 medium asteroids
            if (((newAsteroid) p).getSize() == 2)
            {
                Participant.expire(this);

                enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
                enhanced_controller.setScore(enhanced_controller.getScore() + 20);
                enhanced_controller.getSound().bangLargeAsteroid();
            }
            // Expire the ship from the game and increments the score by 50, and creates a medium bang sound, and splits
            // into 2 small asteroids
            if (((newAsteroid) p).getSize() == 1)
            {
                Participant.expire(this);
                enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
                enhanced_controller.setScore(enhanced_controller.getScore() + 50);
                enhanced_controller.getSound().bangMediumAsteroid();

            }
            // Expire the ship and the asteroid from the game and increments the score by 100, and creates a small bang
            // sound
            if (((newAsteroid) p).getSize() == 0)
            {
                Participant.expire(this);

                enhanced_controller.asteroidDestroyed();
                enhanced_controller.getSound().bangSmallAsteroid();
                enhanced_controller.setScore(enhanced_controller.getScore() + 100);

            }
        }
        /**
         * When the New_Bullet collides with the AlienShip, it expires the AlienShip, and increments the score depending on
         * the level
         */
        if (p instanceof newAlienship)
        {
            // Expires the AlienShip if shot by a New_Bullet
            Participant.expire(this);
            // If the level is 2, then it adds the score by 200 and creates the sound
            if (enhanced_controller.getLevel() == 2)
            {
                enhanced_controller.setScore(enhanced_controller.getScore() + 200);
                enhanced_controller.getSound().bangAlienShip();
            }
            // If the level is greater than equal to 3, then it adds the score by 1000 and creates the sound
            if (enhanced_controller.getLevel() >= 3)
            {
                enhanced_controller.setScore(enhanced_controller.getScore() + 1000);
                enhanced_controller.getSound().bangAlienShip();
            }

        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Destroys the New_Bullets after a 1000 seconds, when the ParticipantCountdownTimer with destroy is called
        if (payload.equals("destroy"))
        {
            Participant.expire(this);
        }
    }

}
