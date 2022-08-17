package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Enhanced_controller;
import asteroids.game.Participant;

/**
 * Class that represents asteroids
 */
public class newAsteroid extends Participant implements ShipDestroyer
{
    /** The size of the asteroid (0 = small, 1 = medium, 2 = large) */
    private int size;

    /** The outline of the asteroid */
    private Shape outline;

    /** The game enhanced_controller */
    private Enhanced_controller enhanced_controller;

    /**
     * Throws an IllegalArgumentException if size or variety is out of range.
     * 
     * Creates an asteroid of the specified variety (0 through 3) and size (0 = small, 1 = medium, 2 = large) and
     * positions it at the provided coordinates with a random rotation. Its velocity has the given speed but is in a
     * random direction.
     */
    public newAsteroid (int variety, int size, double x, double y, int speed, Enhanced_controller enhanced_controller)
    {
        // Make sure size and variety are valid
        if (size < 0 || size > 2)
        {
            throw new IllegalArgumentException("Invalid asteroid size: " + size);
        }
        else if (variety < 0 || variety > 3)
        {
            throw new IllegalArgumentException();
        }

        // Create the asteroid and sets the position, velocity and rotation
        this.enhanced_controller = enhanced_controller;
        this.size = size;
        setPosition(x, y);
        setVelocity(speed, RANDOM.nextDouble() * 2 * Math.PI);
        setRotation(2 * Math.PI * RANDOM.nextDouble());
        createAsteroidOutline(variety, size);
    }

	/**
     * Returns the outline of the newAsteroid
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Creates the outline of the asteroid based on its variety and size.
     */
    private void createAsteroidOutline (int variety, int size)
    {
        // This will contain the outline
        Path2D.Double poly = new Path2D.Double();

        // Fill out according to variety
        if (variety == 0)
        {
            poly.moveTo(0, -30);
            poly.lineTo(28, -15);
            poly.lineTo(20, 20);
            poly.lineTo(4, 8);
            poly.lineTo(-1, 30);
            poly.lineTo(-12, 15);
            poly.lineTo(-5, 2);
            poly.lineTo(-25, 7);
            poly.lineTo(-10, -25);
            poly.closePath();
        }
        else if (variety == 1)
        {
            poly.moveTo(10, -28);
            poly.lineTo(7, -16);
            poly.lineTo(30, -9);
            poly.lineTo(30, 9);
            poly.lineTo(10, 13);
            poly.lineTo(5, 30);
            poly.lineTo(-8, 28);
            poly.lineTo(-6, 6);
            poly.lineTo(-27, 12);
            poly.lineTo(-30, -11);
            poly.lineTo(-6, -15);
            poly.lineTo(-6, -28);
            poly.closePath();
        }
        else if (variety == 2)
        {
            poly.moveTo(10, -30);
            poly.lineTo(30, 0);
            poly.lineTo(15, 30);
            poly.lineTo(0, 15);
            poly.lineTo(-15, 30);
            poly.lineTo(-30, 0);
            poly.lineTo(-10, -30);
            poly.closePath();
        }
        else
        {
            poly.moveTo(30, -18);
            poly.lineTo(5, 5);
            poly.lineTo(30, 15);
            poly.lineTo(15, 30);
            poly.lineTo(0, 25);
            poly.lineTo(-15, 30);
            poly.lineTo(-25, 8);
            poly.lineTo(-10, -25);
            poly.lineTo(0, -30);
            poly.lineTo(10, -30);
            poly.closePath();
        }

        // Scale to the desired size
        double scale = ASTEROID_SCALE[size];
        poly.transform(AffineTransform.getScaleInstance(scale, scale));

        // Save the outline
        outline = poly;
    }

    /**
     * Returns the size of the asteroid
     */
    public int getSize ()
    {
        return size;
    }

    /**
     * When an newAsteroid collides with an newAsteroidDestroyer, and BulletDestroyer, it expires.
     */
    @Override
    public void collidedWith (Participant p)
    {
        /**
         * When the newAsteroid collides with the newAsteroidDestroyer, it expires the asteroid and adds the debris
         */
        if (p instanceof AsteroidDestroyer)
        {
            // Expire the asteroid
            Participant.expire(this);

            // Inform the enhanced_controller that the asteroid has been destroyed and adds the debris
            enhanced_controller.addParticipant(new newDebris(0, p.getX(), p.getY()));
            enhanced_controller.addParticipant(new newDebris(0, p.getX() + 1, p.getY() + 1));
            enhanced_controller.addParticipant(new newDebris(0, p.getX() + 0.5, p.getY() + 0.5));
            enhanced_controller.addParticipant(new newDebris(0, p.getX() - 1, p.getY() - 1));
            enhanced_controller.asteroidDestroyed();
            enhanced_controller.recordtotasteroids++;

        }
        if(p instanceof newAsteroid)
        {
        	this.setDirection(-RANDOM.nextDouble() * 2 * Math.PI);
        }
        /**
         * When the newAsteroid collides with the Ship, it expires the ship
         */
        if (p instanceof Ship)
        {
        	enhanced_controller.recordtotasteroids++;
            Participant.expire(this);
        }
    }
}