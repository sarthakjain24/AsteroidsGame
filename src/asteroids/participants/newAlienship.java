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
import asteroids.game.Enhanced_controller;
import asteroids.game.Participant;
import static asteroids.game.Constants.*;
import asteroids.game.ParticipantCountdownTimer;
import sounds.Sound;

/**
 * Class that Represents newAlienships
 */
public class newAlienship extends Participant implements AsteroidDestroyer, BulletDestroyer, ShipDestroyer {

	/** The outline of the newAlienship */
	private Shape outline;
	/** Game enhanced_controller */
	private Enhanced_controller enhanced_controller;
	/**
	 * Double to find the alienScale to make it either a big newAlienship or a small
	 * newAlienship
	 */
	private double alienScale;
	/**
	 * Double to find the angle degree to know which angle the newAlienship should
	 * shoot at
	 */
	private double angleDeg;

	/**
	 * Constructs a ship at the specified coordinates that is pointed in the given
	 * direction, with the velocity and sound depending on the scale of the
	 * newAlienship.
	 */
	public newAlienship(int x, int y, Enhanced_controller enhanced_controller, double alienScale) {
		this.enhanced_controller = enhanced_controller;
		setPosition(x, y);
		setRotation(135);
		this.alienScale = alienScale;
		// Condition when alienScale is 0.5, so it draws a small alien ship that moves
		// faster and plays the sound of the
		// small newAlienship
		if (alienScale == 0.5) {
			enhanced_controller.getSound().smallSaucer();
			setVelocity(8, RANDOM.nextDouble() * 2 * Math.PI);
		}
		// Condition when alienScale is 1, so it draws a big alien ship that moves
		// slower and plays the sound of the big
		// newAlienship
		if (alienScale == 1.0) {
			enhanced_controller.getSound().bigSaucer();
			setVelocity(5, RANDOM.nextDouble() * 2 * Math.PI);
		}
		newAlienship();
		new ParticipantCountdownTimer(this, "move", 1000);
		new ParticipantCountdownTimer(this, "shoot", 1000);
		angleDeg = 0;
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
	 * Returns the outline of the newAlienship
	 */
	@Override
	protected Shape getOutline() {
		return outline;
	}

	/**
	 * Creates the newAlienship
	 */
	private void newAlienship() {
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
	public void move() {
		super.move();
	}

	/**
	 * Accelerates by SHIP_ACCELERATION
	 */
	public void accelerate() {
		accelerate(5);
	}

	/**
	 * When a Ship collides with a ShipDestroyer, Ship, Asteroid,
	 * newAlienshipDestroyer, and Bullets, it expires
	 */
	@Override
	public void collidedWith(Participant p) {
		if (p instanceof ShipDestroyer) {
			// Expires, adds debris, makes a sound representing the newAlienship has been
			// destroyed
			Participant.expire(this);
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY()));
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY() - 2));
			enhanced_controller.getSound().bangAlienShip();
			enhanced_controller.alienShipDestoyed();
		}

		if (p instanceof newShip) {
			// Expires, adds debris, makes a sound representing the newAlienship has been
			// destroyed
			Participant.expire(this);
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY()));
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY() - 2));
			enhanced_controller.getSound().bangAlienShip();
			enhanced_controller.alienShipDestoyed();
			enhanced_controller.recordtotaliens++;
		}

		if (p instanceof New_Bullet) {
			// Expires, adds debris, makes a sound representing the newAlienship has been
			// destroyed, and increments the
			// score based on the level
			Participant.expire(this);
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY()));
			enhanced_controller.addParticipant(new Debris(2, p.getX(), p.getY() - 2));
			if (enhanced_controller.getLevel() == 2) {
				enhanced_controller.setScore(enhanced_controller.getScore() + 200);
			}
			if (enhanced_controller.getLevel() >= 3) {
				enhanced_controller.setScore(enhanced_controller.getScore() + 1000);
			}
			enhanced_controller.getSound().bangAlienShip();
			enhanced_controller.alienShipDestoyed();
			enhanced_controller.recordtotaliens++;

		}
		if (p instanceof AlienShipDestroyer) {
			// Expires, and stops the sound of the newAlienship based on the level
			if (enhanced_controller.getLevel() == 2) {
				enhanced_controller.getSound().stopBigSaucer();
			}
			if (enhanced_controller.getLevel() >= 3) {
				enhanced_controller.getSound().stopSmallSaucer();
			}

			Participant.expire(this);
			enhanced_controller.alienShipDestoyed();

		}
		if (p instanceof newAsteroid) {
			// If asteroid size is 2, then it expires, splits the asteroid and plays the
			// sound
			if (((newAsteroid) p).getSize() == 2) {
				Participant.expire(this);
				enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 1, MAXIMUM_MEDIUM_ASTEROID_SPEED);
				enhanced_controller.getSound().bangLargeAsteroid();
				enhanced_controller.getSound().bangShip();
			}
			// If asteroid size is 1, then it expires, splits the asteroid and plays the
			// sound
			if (((newAsteroid) p).getSize() == 1) {
				Participant.expire(this);
				enhanced_controller.splitAsteroid(3, p.getX(), p.getY(), 0, MAXIMUM_SMALL_ASTEROID_SPEED);
				enhanced_controller.getSound().bangMediumAsteroid();
				enhanced_controller.getSound().bangShip();
			}
			// If asteroid size is 0, then it expires, and plays the sound
			if (((newAsteroid) p).getSize() == 0) {
				Participant.expire(this);
				enhanced_controller.getSound().bangSmallAsteroid();
				enhanced_controller.getSound().bangShip();
			}
			// Notifies the newAlienship has been destroyed
			enhanced_controller.alienShipDestoyed();
		}
	}

	/**
	 * This method is invoked when a ParticipantCountdownTimer completes its
	 * countdown.
	 */
	@Override
	public void countdownComplete(Object payload) {

		// Condition that moves the newAlienship
		if (payload.equals("move")) {
			// Finds a randomNumber and checks if it is between 0 & 3
			Random r = new Random();
			int random = r.nextInt(4);
			// If random is equal to 0, then it moves one radian down, and moves again in 2
			// seconds
			if (random == 0) {
				setDirection(-180 / Math.PI);

			}
			// If random is equal to 0, then it moves one radian up, and moves again in 2
			// seconds
			if (random == 1) {
				setDirection(180 / Math.PI);

			}
			// If random is equal to 2, then it moves to the right, and moves again in 2
			// seconds
			if (random == 2) {
				setDirection(-Math.PI);

			}
			// If random is equal to 3, then it moves to the left, and moves again in 2
			// seconds
			if (random == 3) {
				setDirection(Math.PI);

			}
			new ParticipantCountdownTimer(this, "move", 2000);
		}
		// Condition that makes the newAlienship shoot
		if (payload.equals("shoot")) {
			// If level is equal to 2, then it goes into this condition
			if (enhanced_controller.getLevel() == 2) {

				int rand = 1;
				if (rand == 1) {

					int findRandom = RANDOM.nextInt(5);
					// If randomNumber is 0, then goes in this condition and shoots randomly
					if (findRandom == 0) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() - 3, getYNose(), this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 1, then goes in this condition and shoots randomly
					if (findRandom == 1) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() - 3, getYNose() - 3, this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 2, then goes in this condition and shoots randomly
					if (findRandom == 2) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose(), this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 3, then goes in this condition and shoots randomly
					if (findRandom == 3) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() + 3, this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 4, then goes in this condition and shoots randomly
					if (findRandom == 4) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() - 3, this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// Shoots after every 2 seconds
					
				}
				if (rand == 1) {

					int findRandom = RANDOM.nextInt(5);
					// If randomNumber is 0, then goes in this condition and shoots randomly
					if (findRandom == 0) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() - 3, getYNose(), this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 1, then goes in this condition and shoots randomly
					if (findRandom == 1) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() - 3, getYNose() - 3, this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 2, then goes in this condition and shoots randomly
					if (findRandom == 2) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose(), this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 3, then goes in this condition and shoots randomly
					if (findRandom == 3) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() + 3, this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// If randomNumber is 4, then goes in this condition and shoots randomly
					if (findRandom == 4) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() - 3, this,
								enhanced_controller, RANDOM.nextInt(360)));
					}
					// Shoots after every 2 seconds
					
				}
				new ParticipantCountdownTimer(this, "shoot", 2000);

			}
			// Goes into this condition if level is greater than equal to 3
			if (enhanced_controller.getLevel() >= 3) {
				if (enhanced_controller.getShip() != null) {
					// Finds the angle to always shoot towards the ship
					angleDeg = Math.atan2(enhanced_controller.getShip().getY() - this.getY(),
							enhanced_controller.getShip().getX() - this.getX());
				}

				int rand = 1;
				if (rand == 1) {

					int findRandom = RANDOM.nextInt(5);
					// If randomNumber is 0, then goes in this condition and shoots randomly
					if (findRandom == 0) {
						enhanced_controller.addParticipant(
								new newAlienbullet(getXNose() - 3, getYNose(), this, enhanced_controller, angleDeg));
					}
					// If randomNumber is 1, then goes in this condition and shoots randomly
					if (findRandom == 1) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() - 3, getYNose() - 3, this,
								enhanced_controller, angleDeg));
					}
					// If randomNumber is 2, then goes in this condition and shoots randomly
					if (findRandom == 2) {
						enhanced_controller.addParticipant(
								new newAlienbullet(getXNose() + 3, getYNose(), this, enhanced_controller, angleDeg));
					}
					// If randomNumber is 3, then goes in this condition and shoots randomly
					if (findRandom == 3) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() + 3, this,
								enhanced_controller, angleDeg));
					}
					// If randomNumber is 4, then goes in this condition and shoots randomly
					if (findRandom == 4) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() - 3, this,
								enhanced_controller, angleDeg));
					}
					// Shoots after every 2 seconds
					
				}
				if (rand == 1) {

					int findRandom = RANDOM.nextInt(5);
					// If randomNumber is 0, then goes in this condition and shoots randomly
					if (findRandom == 0) {
						enhanced_controller.addParticipant(
								new newAlienbullet(getXNose() - 3, getYNose(), this, enhanced_controller, angleDeg));
					}
					// If randomNumber is 1, then goes in this condition and shoots randomly
					if (findRandom == 1) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() - 3, getYNose() - 3, this,
								enhanced_controller, angleDeg));
					}
					// If randomNumber is 2, then goes in this condition and shoots randomly
					if (findRandom == 2) {
						enhanced_controller.addParticipant(
								new newAlienbullet(getXNose() + 3, getYNose(), this, enhanced_controller, angleDeg));
					}
					// If randomNumber is 3, then goes in this condition and shoots randomly
					if (findRandom == 3) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() + 3, this,
								enhanced_controller, angleDeg));
					}
					// If randomNumber is 4, then goes in this condition and shoots randomly
					if (findRandom == 4) {
						enhanced_controller.addParticipant(new newAlienbullet(getXNose() + 3, getYNose() - 3, this,
								enhanced_controller, angleDeg));
					}
					// Shoots after every 2 seconds
					
				}
				new ParticipantCountdownTimer(this, "shoot", 2000);
			}
		}
	}
}
