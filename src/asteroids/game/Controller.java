package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import javax.swing.*;
import asteroids.participants.AlienShip;
import asteroids.participants.Asteroid;
import asteroids.participants.Ship;
import asteroids.participants.Bullets;
import asteroids.participants.Debris;
import sounds.Sound;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener, Iterable<Participant>
{
    /** The state of all the Participants */
    private ParticipantState pstate;
    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;
    /** The alienShip (if one is active) or null (otherwise) */
    private AlienShip alienShip;
    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;
    /** Finds a randomNum to be stored in */
    private int randomNum;
    /** Finds the number of bullets shot at any time */
    private int numBullets;
    /** Creates the Sound */
    private Sound sound;
    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;
    /** The game display */
    private Display display;
    /** Keeps track of the score */
    private int score;
    /** Keeps track of the level */
    private int level;
    /** Smoothen out the left movement */
    private boolean left;
    /** Smoothen out the right movement */
    private boolean right;
    /** Smoothen out the acceleration */
    private boolean accelerate;
    /** Smoothes the bullet shooting */
    private boolean bullet;
    /** Creates the bullets */
    private Bullets Bullets;
    /** Finds the randomVariety */
    private int randomVariety;
    /** When this timer goes off, it is time to refresh the beat */
    private Timer beatTimer;
    /** Plays the beat */
    private boolean playBeat;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Intializes the sound
        sound = new Sound();
        // Sets numBullets to 0
        numBullets = 0;
        // Sets lives to 3
        setLives(3);
        // Sets score to 0
        setScore(0);
        // Sets level to 1
        setLevel(1);
        // Sets the left, right, accelerate, bullet to false
        left = false;
        right = false;
        accelerate = false;
        bullet = false;
        // Sets playBeat to true
        playBeat = true;

        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Sets up the beat timer.
        beatTimer = new Timer(INITIAL_BEAT, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * This makes it possible to use an enhanced for loop to iterate through the Participants being managed by a
     * Controller.
     */
    @Override
    public Iterator<Participant> iterator ()
    {
        return pstate.iterator();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        // Creates an alienShip only when the level is greater than 1
        if (level > 1)
        {
            new ParticipantCountdownTimer(ship, "create", ALIEN_DELAY);
        }
        display.setLegend("");
    }

    /**
     * Finds a randomNumber to be set as the speed of the asteroid, which should be non negative and not be zero
     */
    private int findRandomNumber (int speed)
    {
        randomNum = RANDOM.nextInt(speed);
        while (randomNum == 0)
        {
            randomNum = RANDOM.nextInt(speed);
        }
        return randomNum;
    }

    /**
     * Finds a randomVariety to be set as the speed of the asteroid, which should be a random number
     */
    private int findRandomVariety (int variety)
    {
        randomVariety = RANDOM.nextInt(variety);
        return randomVariety;
    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids ()
    {
        // Top left asteroid
        addParticipant(new Asteroid(findRandomVariety(3), 2, EDGE_OFFSET - 20, EDGE_OFFSET - 20,
                findRandomNumber(MAXIMUM_LARGE_ASTEROID_SPEED), this));
        // Top Right Asteroid
        addParticipant(new Asteroid(findRandomVariety(3), 2, EDGE_OFFSET + 550, EDGE_OFFSET + 50,
                (MAXIMUM_LARGE_ASTEROID_SPEED), this));
        // Bottom Left Asteroid
        addParticipant(new Asteroid(findRandomVariety(3), 2, EDGE_OFFSET - 100, EDGE_OFFSET + 550,
                findRandomNumber(MAXIMUM_LARGE_ASTEROID_SPEED), this));
        // Bottom Right Asteroid
        addParticipant(new Asteroid(findRandomVariety(3), 2, EDGE_OFFSET + 550, EDGE_OFFSET + 550,
                findRandomNumber(MAXIMUM_LARGE_ASTEROID_SPEED), this));

        // If level is greater than 1, then it places a new asteroid
        if (level > 1)
        {
            placeNewAsteroid();
        }

    }

    /**
     * A helper method to place new asteroids so that every time the level increments, the number of asteroids go up by
     * one
     */
    private void placeNewAsteroid ()
    {
        if (lives > 0 && ship != null && level > 1)
        {
            for (int i = 1; i < level; i++)
            {
                addParticipant(new Asteroid(findRandomVariety(3), 2, EDGE_OFFSET + findRandomVariety(700),
                        EDGE_OFFSET + findRandomVariety(700), findRandomNumber(MAXIMUM_LARGE_ASTEROID_SPEED), this));
            }
        }
    }

    /**
     * Places the alienShip in the game
     */
    public void placeAlienShip ()
    {

        // If level is equal to 2, then it places the large AlienShip
        if (level == 2 && alienShip == null)
        {
            alienShip = new AlienShip(RANDOM.nextInt(750), RANDOM.nextInt(750), this, 1);
            addParticipant(alienShip);
        }
        // If level is greater than or equal to 3, then it places the small AlienShip
        if (level >= 3 && alienShip == null)
        {

            alienShip = new AlienShip(RANDOM.nextInt(750), RANDOM.nextInt(750), this, 0.5);
            addParticipant(alienShip);
        }
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        // Stops all the sounds
        if (getLevel() == 2)
        {
            sound.stopBigSaucer();
        }
        if (getLevel() >= 3)
        {
            sound.stopSmallSaucer();
        }
        // Nulls the ship and the alienShip
        ship = null;
        alienShip = null;
    }

    /**
     * A helper method to alternate the beat, everytime a new game is clicked or a new level is created
     */
    private void alternateBeat ()
    {
        beatTimer.stop();
        beatTimer.setDelay(INITIAL_BEAT);
        playBeat = true;
        beatTimer.start();
    }

    /**
     * Sets things up and begins a new game.
     */

    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids();

        // Place the ship
        placeShip();

        // Calls the beats
        alternateBeat();

        // Sets lives to 3
        lives = 3;
        display.setLives(lives);

        // Sets score to 0
        score = 0;
        display.setScore(score);

        // Sets level to be 1
        level = 1;
        display.setLevel(level);

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        // Plays the sound of the ship being banged
        sound.bangShip();

        // Null out the ship
        ship = null;

        // Display a legend
        display.setLegend("Ouch!");

        // Decrement lives
        setLives(lives - 1);
        display.setLives(lives);

        // Since the ship was destroyed, schedule a transition and sets all the booleans to false
        scheduleTransition(END_DELAY);
        left = false;
        right = false;
        bullet = false;
        accelerate = false;
    }

    /**
     * The alienShip has been destroyed
     */
    public void alienShipDestoyed ()
    {
        // Nulls the alienShip
        alienShip = null;
        if (alienShip == null)
        {
            // Finds the random transition time for the alienShip showing up the next time should be between 5 and 10
            // seconds
            int newTransition = RANDOM.nextInt(ALIEN_DELAY) + ALIEN_DELAY;
            scheduleTransition(newTransition);
            if (transitionTime % newTransition == 0)
            {
                placeAlienShip();
            }
        }
        // If the alienShip dies then it stops the sound
        if (level == 2)
        {
            sound.stopBigSaucer();
        }
        if (level >= 3)
        {
            sound.stopSmallSaucer();
        }
    }

    /**
     * Splits the asteroid into two smaller asteroids upon collision
     */
    public void splitAsteroid (int variety, double x, double y, int size, int speed)
    {
        addParticipant(new Asteroid(findRandomVariety(variety), size, x, y, findRandomNumber(speed), this));
        addParticipant(new Asteroid(findRandomVariety(variety), size, x, y, findRandomNumber(speed), this));
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed ()
    {
        // If all the asteroids are gone, schedule a transition and stops the beat
        if (countAsteroids() == 0)
        {
            beatTimer.stop();
            scheduleTransition(END_DELAY);
        }
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();

            // If lives is equal to 0, then it ends the game
            if (getLives() == 0)
            {
                display.setLegend("Ouch");
                finalScreen();
                ship = null;
                alienShip = null;
            }
            try
            {
                // Smoothens out the movement and the shooting of the ship
                if (left)
                {
                    ship.turnLeft();
                }
                if (right)
                {
                    ship.turnRight();

                }
                if (accelerate)
                {
                    ship.accelerate();
                    sound.thrust();
                }
                if (bullet)
                {
                    Bullets = new Bullets(ship.getXNose() - 3, ship.getYNose(), ship, this);
                    if (countBullets() < BULLET_LIMIT)
                    {
                        addParticipant(Bullets);
                    }
                    sound.fire();
                }

            }
            catch (Exception exception)
            {

            }
        }
        else if (e.getSource() == beatTimer)
        {
            // Alternates the beat
            if (playBeat == true)
            {
                sound.beat1();
                playBeat = false;
            }
            else
            {
                sound.beat2();
                playBeat = true;
            }
            // The delay at which the beat alternates with
            beatTimer.setDelay(beatSwitch());
        }
    }

    /**
     * A helper method that finds out when to switch the beat
     */
    private int beatSwitch ()
    {
        if (FASTEST_BEAT > beatTimer.getDelay() - BEAT_DELTA)
        {
            return FASTEST_BEAT;
        }
        else
        {
            return beatTimer.getDelay() - BEAT_DELTA;
        }
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (getLives() == 0)
            {
                display.setLegend("Ouch");
                finalScreen();

            }
            // Increments the level, adds new asteroids, places a new ship, and alternates the beat if meets all the
            // conditions
            else if (getLevel() >= 1 && getLives() > 0 && countAsteroids() == 0 && alienShip == null)
            {
                alternateBeat();
                level++;
                placeShip();
                placeAsteroids();

            }
            // Adds a new ship if meets the conditions
            else if (getLives() > 0 && ship == null)
            {
                placeShip();
            }
            // Adds a new alienShip if meets the conditions
            else if (alienShip == null)
            {
                placeAlienShip();
            }
        }
    }

    /**
     * Returns the number of asteroids that are active participants
     */
    private int countAsteroids ()
    {
        int count = 0;
        for (Participant p : this)
        {
            if (p instanceof Asteroid)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns the number of bullets that are active participants
     */
    private int countBullets ()
    {
        numBullets = 0;
        for (Participant p : this)
        {
            if (p instanceof Bullets)
            {
                numBullets++;
            }
        }
        return numBullets;
    }

    /**
     * A bullet has been destroyed
     */
    public void bulletsDestroyed ()
    {
        // Will not let shoot more than the bullet limit at a time
        if (countBullets() == BULLET_LIMIT)
        {
            bullet = false;
        }
    }

    /**
     * If a key of interest is pressed, record that it is down and performs a movement based on the key pressed.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            accelerate = true;
            ship.setMoving(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && ship != null)
        {
            bullet = true;
            sound.fire();
        }
        if (e.getKeyCode() == KeyEvent.VK_D && ship != null)
        {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A && ship != null)
        {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            accelerate = true;
            ship.setMoving(true);
            sound.thrust();
        }
        if (e.getKeyCode() == KeyEvent.VK_S && ship != null)
        {
            bullet = true;
            sound.fire();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {
            bullet = true;
        }
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * Ends the action based on what key is pressed
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null)
        {
            accelerate = false;
            ship.setMoving(false);
            sound.stopThrust();
        }
        if (e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            accelerate = false;
            ship.setMoving(false);
            sound.stopThrust();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null)
        {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null)
        {
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S && ship != null)
        {
            bullet = false;
            numBullets = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE && ship != null)
        {
            bullet = false;
            numBullets = 0;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN && ship != null)
        {
            bullet = false;
            numBullets = 0;
        }
    }

    /**
     * A getter method to get the score
     */
    public int getScore ()
    {
        return score;
    }

    /**
     * A setter method to set the score
     */
    public void setScore (int score)
    {
        this.score = score;
    }

    /**
     * A getter method to get the level
     */
    public int getLevel ()
    {
        return level;
    }

    /**
     * A setter method to set the level
     */
    public void setLevel (int level)
    {
        this.level = level;
    }

    /**
     * A setter method to set the sound
     */
    public void setSound (Sound sound)
    {
        this.sound = sound;
    }

    /**
     * A getter method to get the sound
     */
    public Sound getSound ()
    {
        return sound;
    }

    /**
     * A getter method to get the lives
     */
    public int getLives ()
    {
        return lives;
    }

    /**
     * A setter method to set the lives
     */
    public void setLives (int lives)
    {
        this.lives = lives;
    }
}
