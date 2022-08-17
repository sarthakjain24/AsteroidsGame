package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Random;
import asteroids.destroyers.AlienShipDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import static asteroids.game.Constants.*;
import asteroids.game.ParticipantCountdownTimer;
import sounds.Sound;

/**
 * Class that Represents AlienShips
 */
public class AlienShip extends Participant implements AsteroidDestroyer, BulletDestroyer, ShipDestroyer
{

    /** The outline of the AlienShip */
    private Shape outline;
    /** Game controller */
    private Controller controller;
    /** Double to find the alienScale to make it either a big alienShip or a small alienShip */
    private double alienScale;
    /** Double to find the angle degree to know which angle the alienShip should shoot at */
    private double angleDeg;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction, with the velocity and
     * sound depending on the scale of the alienShip.
     */
    public AlienShip (int x, int y, Controller controller, double alienScale)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(135);
        this.alienScale = alienScale;
        // Condition when alienScale is 0.5, so it draws a small alien ship that moves faster and plays the sound of the
        // small alienShip
        if (alienScale == 0.5)
        {
            controller.getSound().smallSaucer();
            setVelocity(8, RANDOM.nextDouble() * 2 * Math.PI);
        }
        // Condition when alienScale is 1, so it draws a big alien ship that moves slower and plays the sound of the big
        // alienShip
        if (alienScale == 1.0)
        {
            controller.getSound().bigSaucer();
            setVelocity(5, RANDOM.nextDouble() * 2 * Math.PI);
        }
        alienShip();
        new ParticipantCountdownTimer(this, "move", 1000);
        new ParticipantCountdownTimer(this, "shoot", 50);
        angleDeg = 0;
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the y-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    /**
     * Returns the outline of the AlienShip
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    /**
     * Creates the alienShip
     */
    private void alienShip ()
    {
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(0, 0);
        poly.lineTo(10, 10);
        poly.lineTo(10, 20);
        poly.lineTo(20, 20);
        poly.lineTo(20, 10);
        poly.lineTo(30, 0);
        poly.lineTo(20, -10);
        poly.lineTo(10, -10);
        poly.lineTo(0, 0);
        poly.lineTo(30, 0);
        poly.lineTo(20, 10);
        poly.lineTo(10, 10);
        poly.closePath();
        poly.transform(AffineTransform.getScaleInstance(alienScale, alienScale));
        outline = poly;
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        super.move();
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(5);
    }

    /**
     * When a Ship collides with a ShipDestroyer, Ship, Asteroid, AlienShipDestroyer, and Bullets, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expires, adds debris, makes a sound representing the alienShip has been destroyed
            Participant.expire(this);
            controller.addParticipant(new Debris(2, p.getX(), p.getY()));
            controller.addParticipant(new Debris(2, p.getX(), p.getY() - 2));
            controller.getSound().bangAlienShip();
            controller.alienShipDestoyed();
        }

        if (p instanceof Ship)
        {
            // Expires, adds debris, makes a sound representing the alienShip has been destroyed
            Participant.expire(this);
            controller.addParticipant(new Debris(2, p.getX(), p.getY()));
            controller.addParticipant(new Debris(2, p.getX(), p.getY() - 2));
            controller.getSound().bangAlienShip();
            controller.alienShipDestoyed();
        }

        if (p instanceof Bullets)
        {
            // Expires, adds debris, makes a sound representing the alienShip has been destroyed, and increments the
            // score based on the level
            Participant.expire(this);
            controller.addParticipant(new Debris(2, p.getX(), p.getY()));
            controller.addParticipant(new Debris(2, p.getX(), p.getY() - 2));
            if (controller.getLevel() == 2)
            {
                controller.setScore(controller.getScore() + 200);
            }
            if (controller.getLevel() >= 3)
            {
                controller.setScore(controller.getScore() + 1000);
            }
            controller.getSound().bangAlienShip();
            controller.alienShipDestoyed();

        }
        if (p instanceof AlienShipDestroyer)
        {
            // Expires, and stops the sound of the alienShip based on the level
            if (controller.getLevel() == 2)
            {
                controller.getSound().stopBigSaucer();
            }
            if (controller.getLevel() >= 3)
            {
                controller.getSound().stopSmallSaucer();
            }

            Participant.expire(this);
            controller.alienShipDestoyed();

        }
        if (p instanceof Asteroid)
        {
            // If asteroid size is 2, then it expires, splits the asteroid and plays the sound
            if (((Asteroid) p).getSize() == 2)
            {
                Participant.expire(this);
                controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
                controller.getSound().bangLargeAsteroid();
                controller.getSound().bangShip();
            }
            // If asteroid size is 1, then it expires, splits the asteroid and plays the sound
            if (((Asteroid) p).getSize() == 1)
            {
                Participant.expire(this);
                controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
                controller.getSound().bangMediumAsteroid();
                controller.getSound().bangShip();
            }
            // If asteroid size is 0, then it expires, and plays the sound
            if (((Asteroid) p).getSize() == 0)
            {
                Participant.expire(this);
                controller.getSound().bangSmallAsteroid();
                controller.getSound().bangShip();
            }
            // Notifies the alienShip has been destroyed
            controller.alienShipDestoyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {

        //Condition that moves the alienShip
        if (payload.equals("move"))
        {
            //Finds a randomNumber and checks if it is between 0 & 3
            Random r = new Random();
            int random = r.nextInt(4);
            //If random is equal to 0, then it moves one radian down, and moves again in 2 seconds
            if (random == 0)
            {
                setDirection(-180 / Math.PI);
                new ParticipantCountdownTimer(this, "move", 2000);
            }
            //If random is equal to 0, then it moves one radian up, and moves again in 2 seconds
            if (random == 1)
            {
                setDirection(180 / Math.PI);
                new ParticipantCountdownTimer(this, "move", 2000);
            }
            //If random is equal to 2, then it moves to the right, and moves again in 2 seconds
            if (random == 2)
            {
                setDirection(-Math.PI);
                new ParticipantCountdownTimer(this, "move", 2000);
            }
          //If random is equal to 3, then it moves to the left, and moves again in 2 seconds
            if (random == 3)
            {
                setDirection(Math.PI);
                new ParticipantCountdownTimer(this, "move", 2000);
            }
        }
        //Condition that makes the alienShip shoot
        if (payload.equals("shoot"))
        {
            //If level is equal to 2, then it goes into this condition
            if (controller.getLevel() == 2)
            {
                int findRandom = RANDOM.nextInt(5);
                //If randomNumber is 0, then goes in this condition and shoots randomly
                if (findRandom == 0)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() - 3, getYNose(), this, controller, RANDOM.nextInt(360)));
                }
                //If randomNumber is 1, then goes in this condition and shoots randomly
                if (findRandom == 1)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() - 3, getYNose() - 3, this, controller, RANDOM.nextInt(360)));
                }
                //If randomNumber is 2, then goes in this condition and shoots randomly
                if (findRandom == 2)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() + 3, getYNose(), this, controller, RANDOM.nextInt(360)));
                }
                //If randomNumber is 3, then goes in this condition and shoots randomly
                if (findRandom == 3)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() + 3, getYNose() + 3, this, controller, RANDOM.nextInt(360)));
                }
                //If randomNumber is 4, then goes in this condition and shoots randomly
                if (findRandom == 4)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() + 3, getYNose() - 3, this, controller, RANDOM.nextInt(360)));
                }
                // Shoots after every 2 seconds
                new ParticipantCountdownTimer(this, "shoot", 2000);

            }
            //Goes into this condition if level is greater than equal to 3
            if (controller.getLevel() >= 3)
            {
                if (controller.getShip() != null)
                {
                    //Finds the angle to always shoot towards the ship
                    angleDeg = Math.atan2(controller.getShip().getY() - this.getY(),
                            controller.getShip().getX() - this.getX());
                }
                int findRandom = RANDOM.nextInt(5);
              //If randomNumber is 0, then goes in this condition and shoots randomly towards the ship
                if (findRandom == 0)
                {
                    controller.addParticipant(new AlienBullet(getXNose() - 3, getYNose(), this, controller, angleDeg));
                }
                //If randomNumber is 1, then goes in this condition and shoots randomly towards the ship
                if (findRandom == 1)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() - 3, getYNose() - 3, this, controller, angleDeg));
                }
                //If randomNumber is 2, then goes in this condition and shoots randomly towards the ship
                if (findRandom == 2)
                {
                    controller.addParticipant(new AlienBullet(getXNose() + 3, getYNose(), this, controller, angleDeg));
                }
                //If randomNumber is 3, then goes in this condition and shoots randomly towards the ship
                if (findRandom == 3)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() + 3, getYNose() + 3, this, controller, angleDeg));
                }
                //If randomNumber is 4, then goes in this condition and shoots randomly towards the ship
                if (findRandom == 4)
                {
                    controller.addParticipant(
                            new AlienBullet(getXNose() + 3, getYNose() - 3, this, controller, angleDeg));
                }
                // Shoots after every 1 second
                new ParticipantCountdownTimer(this, "shoot", 1000);
            }
        }
    }
}
