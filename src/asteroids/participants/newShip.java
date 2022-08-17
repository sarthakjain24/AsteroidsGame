package asteroids.participants;

import static asteroids.game.Constants.*;
import sounds.Sound;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Enhanced_controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Class that Represents ships
 */
public class newShip extends Participant implements AsteroidDestroyer {
	/** The outline of the ship */
	private Shape outline;
	/**
	 * Boolean to check if the ship is moving or not
	 */
	public boolean isMoving = false;
	/**
	 * Boolean to check if the ship shows the flame or not
	 */
	private boolean showFlame = false;

	/** Game enhanced_controller */
	private Enhanced_controller enhanced_controller;

	/**
	 * Constructs a ship at the specified coordinates that is pointed in the given
	 * direction, and with a specific velocity, and a specific rotation.
	 */
	public newShip(int x, int y, double direction, Enhanced_controller enhanced_controller) {
		// TODO Auto-generated constructor stub

		this.enhanced_controller = enhanced_controller;
		setPosition(x, y);
		setRotation(direction);
		Path2D.Double poly = new Path2D.Double();
		poly.moveTo(20,0);
		poly.lineTo(16, -4);
		poly.lineTo(12, -4);
		poly.lineTo(8, -8);
		poly.lineTo(8 , -12);
		poly.lineTo(4 , -12);
		poly.lineTo(-4 , -20);
		poly.lineTo(-12, -20);
		poly.lineTo(-8, -16);
		poly.lineTo(-8, -8);
		poly.lineTo(-20, -8);
		poly.lineTo(-20, 8);
		poly.lineTo(-8 , 8);
		poly.lineTo(-8, 16);
		poly.lineTo(-12, 20);
		poly.lineTo(-4,20 );
		poly.lineTo(4, 12);
		poly.lineTo(8, 12);
		poly.lineTo(8, 8);
		poly.lineTo(12, 4);
		poly.lineTo(16, 4);
		poly.lineTo(20, 0);
		poly.closePath();
		outline = poly;
	}

	/**
	 * Returns the x-coordinate of the point on the screen where the ship's nose is
	 * located.
	 */
	public double getXNose() {
		Point2D.Double point = new Point2D.Double(20, 0);
		transformPoint(point);
		return point.getX();
	}

	/**
	 * Returns the y-coordinate of the point on the screen where the ship's nose is
	 * located.
	 */
	public double getYNose() {
		Point2D.Double point = new Point2D.Double(20, 0);
		transformPoint(point);
		return point.getY();
	}

	/**
	 * A setter method to set the isMoving instance variable to either true or false
	 */
	public void setMoving(boolean moving) {
		isMoving = moving;
	}

	/**
	 * Returns the outline of the newShip
	 */
	@Override
	protected Shape getOutline() {
		// If the ship is moving, then it goes into this condition
		if (isMoving == true) {
			// Alternates the showFlame
			showFlame = !showFlame;
			if (showFlame) {
				// If the ship is moving, and showFlame is true then it draws the flame at the
				// end of it
				Path2D.Double poly = new Path2D.Double();
				poly.moveTo(20,0);
				poly.lineTo(16, -4);
				poly.lineTo(12, -4);
				poly.lineTo(8, -8);
				poly.lineTo(8 , -12);
				poly.lineTo(4 , -12);
				poly.lineTo(-4 , -20);
				poly.lineTo(-12, -20);
				poly.lineTo(-8, -16);
				poly.lineTo(-8, -8);
				poly.lineTo(-20, -8);
				poly.lineTo(-20, 8);
				poly.lineTo(-8 , 8);
				poly.lineTo(-8, 16);
				poly.lineTo(-12, 20);
				poly.lineTo(-4,20 );
				poly.lineTo(4, 12);
				poly.lineTo(8, 12);
				poly.lineTo(8, 8);
				poly.lineTo(12, 4);
				poly.lineTo(16, 4);
				poly.lineTo(20, 0);
				poly.closePath();

				poly.moveTo(-15, 5);
				poly.lineTo(-20, 2.5);
				poly.lineTo(-30, 0);
				poly.lineTo(-20, -2.5);
				poly.lineTo(-15, -5);

				poly.closePath();
				outline = poly;
				return outline;
			} else {
				// If the ship is moving, but showFlame is false then it draws the ship without
				// a flame
				Path2D.Double poly = new Path2D.Double();
				poly.moveTo(20,0);
				poly.lineTo(16, -4);
				poly.lineTo(12, -4);
				poly.lineTo(8, -8);
				poly.lineTo(8 , -12);
				poly.lineTo(4 , -12);
				poly.lineTo(-4 , -20);
				poly.lineTo(-12, -20);
				poly.lineTo(-8, -16);
				poly.lineTo(-8, -8);
				poly.lineTo(-20, -8);
				poly.lineTo(-20, 8);
				poly.lineTo(-8 , 8);
				poly.lineTo(-8, 16);
				poly.lineTo(-12, 20);
				poly.lineTo(-4,20 );
				poly.lineTo(4, 12);
				poly.lineTo(8, 12);
				poly.lineTo(8, 8);
				poly.lineTo(12, 4);
				poly.lineTo(16, 4);
				poly.lineTo(20, 0);
				poly.closePath();
				outline = poly;
				return outline;
			}
		} else {
			// If the ship is not moving, then it draws the ship without the flame at the
			// end
			Path2D.Double poly = new Path2D.Double();
			poly.moveTo(20,0);
			poly.lineTo(16, -4);
			poly.lineTo(12, -4);
			poly.lineTo(8, -8);
			poly.lineTo(8 , -12);
			poly.lineTo(4 , -12);
			poly.lineTo(-4 , -20);
			poly.lineTo(-12, -20);
			poly.lineTo(-8, -16);
			poly.lineTo(-8, -8);
			poly.lineTo(-20, -8);
			poly.lineTo(-20, 8);
			poly.lineTo(-8 , 8);
			poly.lineTo(-8, 16);
			poly.lineTo(-12, 20);
			poly.lineTo(-4,20 );
			poly.lineTo(4, 12);
			poly.lineTo(8, 12);
			poly.lineTo(8, 8);
			poly.lineTo(12, 4);
			poly.lineTo(16, 4);
			poly.lineTo(20, 0);
			poly.closePath();
			outline = poly;
			return outline;
		}
	}

	/**
	 * Customizes the base move method by imposing friction
	 */
	@Override
	public void move() {
		applyFriction(SHIP_FRICTION);
		super.move();
	}

	/**
	 * Turns right by Pi/16 radians
	 */
	public void turnRight() {
		rotate(Math.PI / 16);

	}

	/**
	 * Turns left by Pi/16 radians
	 */
	public void turnLeft() {
		rotate(-Math.PI / 16);

	}

	/**
	 * Accelerates by SHIP_ACCELERATION
	 */
	public void accelerate() {
		accelerate(SHIP_ACCELERATION);
	}

	/**
	 * When a newShip collides with a newShipDestroyer, AliennewShip, Asteroid, and
	 * AlienBullet, it expires
	 */
	@Override
	public void collidedWith(Participant p) {
		if (p instanceof ShipDestroyer) {
			// Expire the ship from the game
			Participant.expire(this);

			// Adds the debris
			enhanced_controller.addParticipant(new Debris(1, p.getX(), p.getY()));
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY()));

			// Tell the enhanced_controller the ship was destroyed and plays the sound of a
			// ship being destroyed
			enhanced_controller.getSound().bangShip();
			enhanced_controller.shipDestroyed();

		}
		if (p instanceof newAlienship) {
			// Expire the ship from the game
			Participant.expire(this);

			// Adds the debris
			enhanced_controller.addParticipant(new Debris(1, p.getX(), p.getY()));
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY()));

			// Tell the enhanced_controller the ship was destroyed and plays the sound of a
			// ship being destroyed
			enhanced_controller.getSound().bangShip();
			enhanced_controller.shipDestroyed();
		}
		if (p instanceof newAlienbullet) {
			// Expire the ship from the game
			Participant.expire(this);

			// Adds the debris
			enhanced_controller.addParticipant(new Debris(1, p.getX(), p.getY()));
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY()));

			// Tell the enhanced_controller the ship was destroyed and plays the sound of a
			// ship being destroyed
			enhanced_controller.getSound().bangShip();
			enhanced_controller.shipDestroyed();
		}
		if (p instanceof newAsteroid) {

			// If the asteroid size is 2, then it gets in this condition
			if (((newAsteroid) p).getSize() == 2) {
				// Increments the score by 20 and plays the sound, and splits the asteroids
				Participant.expire(this);
				enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
				enhanced_controller.setScore(enhanced_controller.getScore() + 20);
				enhanced_controller.getSound().bangLargeAsteroid();
				enhanced_controller.getSound().bangShip();

			}

			// If the asteroid size is 1, then it gets in this condition
			if (((newAsteroid) p).getSize() == 1) {
				// Increments the score by 50 and plays the sound, and splits the asteroids
				Participant.expire(this);
				enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
				enhanced_controller.setScore(enhanced_controller.getScore() + 50);
				enhanced_controller.getSound().bangMediumAsteroid();
				enhanced_controller.getSound().bangShip();
			}
			// If the asteroid size is 0, then it gets in this condition
			if (((newAsteroid) p).getSize() == 0) {
				// Increments the score by 100 and plays the sound
				Participant.expire(this);
				enhanced_controller.asteroidDestroyed();
				enhanced_controller.setScore(enhanced_controller.getScore() + 100);
				enhanced_controller.getSound().bangSmallAsteroid();
				enhanced_controller.getSound().bangShip();

			}
		}

	}

	/**
	 * This method is invoked when a ParticipantCountdownTimer completes its
	 * countdown.
	 */
	@Override
	public void countdownComplete(Object payload) {
		// Give a burst of acceleration, then schedule another
		// burst for 200 msecs from now.
		if (payload.equals("move")) {
			new ParticipantCountdownTimer(this, "move", 200);
		}
		// Adds the aliennewShip if "create" is called
		if (payload.equals("create")) {
			enhanced_controller.placeAlienShip();
		}
	}
}
