package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Demonstrates how to put sound files into a project so that they will be included when the project is exported, and
 * demonstrates how to play sounds.
 * 
 * @author Joe Zachary, Sarthak Jain, and Vijai Siddharth Vachaspathi Tunukoju Venkatapuram
 */
@SuppressWarnings("serial")
public class Sound extends JFrame
{
    /** A Clip that, when played, sounds like a weapon being fired */
    private Clip fire;
    /** A Clip that, when played, sounds like a small saucer flying to represent the small AlienShip */
    private Clip smallSaucer;
    /** A Clip that, when played, sounds like a big saucer flying to represent the big AlienShip */
    private Clip bigSaucer;
    /** A Clip that, when played, sounds like a AlienShip crashing with a bang */
    private Clip bangAlienShip;
    /** A Clip that, when played, sounds like a Ship crashing with a bang */
    private Clip bangShip;
    /** A Clip that, when played, sounds like a plane engine to represent the ship moving */
    private Clip thrust;
    /** A Clip that, when played, sounds like a large asteroid crashing with a bang */
    private Clip bangLarge;
    /** A Clip that, when played, sounds like a medium asteroid crashing with a bang */
    private Clip bangMed;
    /** A Clip that, when played, sounds like a small asteroid crashing with a bang */
    private Clip bangSmall;
    /** A Clip that works as a background music of the game and alternates with beat2 */
    private Clip beat1;
    /** A Clip that works as a background music of the game and alternates with beat1 */
    private Clip beat2;

    /**
     * Creates the demo.
     */
    public Sound ()
    {
        // We create the clips in advance so that there will be no delay
        // when we need to play them back. Note that the actual wav
        // files are stored in the "sounds" project.
        fire = createClip("/sounds/fire.wav");
        smallSaucer = createClip("/sounds/saucerSmall.wav");
        bangLarge = createClip("/sounds/bangLarge.wav");
        bangMed = createClip("/sounds/bangMedium.wav");
        bangShip = createClip("/sounds/bangShip.wav");
        bangSmall = createClip("/sounds/bangSmall.wav");
        thrust = createClip("/sounds/thrust.wav");
        bigSaucer = createClip("/sounds/saucerBig.wav");
        bangAlienShip = createClip("/sounds/bangAlienShip.wav");
        beat1 = createClip("/sounds/beat1.wav");
        beat2 = createClip("/sounds/beat2.wav");
    }

    /**
     * Creates an audio clip from a sound file.
     */
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }

    /**
     * If the fire is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void fire ()
    {
        if (fire.isRunning())
        {
            fire.stop();
        }
        fire.setFramePosition(0);
        fire.start();
    }

    /**
     * If the thrust is not running, then it loops the sounds continuously
     */
    public void thrust ()
    {
        thrust.loop(Clip.LOOP_CONTINUOUSLY);
    }
/**
 * If the thrust is running, then it stops the thrust
 */
    public void stopThrust ()
    {
        if (thrust.isRunning())
        {
            thrust.stop();
        }
    }

    /**
     * If the bangLarge sound is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void bangLargeAsteroid ()
    {
        if (bangLarge.isRunning())
        {
            bangLarge.stop();
        }
        bangLarge.setFramePosition(0);
        bangLarge.start();

    }
    /**
     * If the bangMed sound is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void bangMediumAsteroid ()
    {
        if (bangMed.isRunning())
        {
            bangMed.stop();
        }
        bangMed.setFramePosition(0);
        bangMed.start();

    }
    /**
     * If the bangSmall sound is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void bangSmallAsteroid ()
    {
        if (bangSmall.isRunning())
        {
            bangSmall.stop();
        }
        bangSmall.setFramePosition(0);
        bangSmall.start();

    }
    /**
     * If the bangShip sound is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void bangShip ()
    {
        if (bangShip.isRunning())
        {
            bangShip.stop();
        }
        bangShip.setFramePosition(0);
        bangShip.start();

    }

    public void bangAlienShip ()
    {
        if (bangAlienShip.isRunning())
        {
            bangAlienShip.stop();
        }
        bangAlienShip.start();

    }
    /**
     * If the bigSaucer is not running, then it loops the sounds continuously
     */
    public void bigSaucer ()
    {

        if (bigSaucer.isRunning() == false)
        {
            bigSaucer.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }

    /**
     * If the big saucer is running, then it stops it
     */
    public void stopBigSaucer ()
    {
        if (bigSaucer.isRunning())
        {
            bigSaucer.stop();
        }
    }

    /**
     * If the smallSaucer is not running, then it loops the sounds continuously
     */
    public void smallSaucer ()
    {
        if (smallSaucer.isRunning() == false)
        {
            smallSaucer.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }

    /**
     * If the small saucer is running, then it stops it
     */
    public void stopSmallSaucer ()
    {
        if (smallSaucer.isRunning())
        {
            smallSaucer.stop();
        }
    }

    /**
     * If the beat1 sound is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void beat1 ()
    {
        if (beat1.isRunning())
        {
            beat1.stop();
        }
        beat1.setFramePosition(0);
        beat1.start();
        

    }

    /**
     * If the beat2 sound is running, then it stops the clip, and then resets the sound and starts it back again
     */
    public void beat2 ()
    {
        if (beat2.isRunning())
        {
            beat2.stop();
        }
        beat2.setFramePosition(0);
        beat2.start();
        

    }
}
