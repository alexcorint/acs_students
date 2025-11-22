package basenostates;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * CLOCK CLASS.
 * Defines a timer for the 'Unlocked Shortly' door state
 */
@SuppressWarnings("deprecation")
public class Clock extends Observable {

  private final int seconds;
  private static Clock instance = null;
  private Timer timer;

  private Clock() {
    this.seconds = 10;
  }

  /**
   * Starts the clock to be 10 seconds long whenever
   * it's called.
   */
  public void start() {
    if (timer != null) {
      timer.cancel();
    }

    timer = new Timer();
    TimerTask task = new TimerTask() {
      private int remainingSeconds = seconds;
      @Override
      public void run() {
        if (remainingSeconds > 0) {
          remainingSeconds--;
        } else {
          setChanged();
          notifyObservers();
          timer.cancel();
        }
      }
    };

    timer.scheduleAtFixedRate(task, 1000, 1000);
  }

  /**
   * Stops the actual Timer instance.
   */
  public void stop() {
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }

  /**
   * Returns the Clock instance of the actual timer, or creates
   * a new one if it doesn't exist.
   * @return a Clock instance.
   */
  public static Clock getInstance() {
    if (instance == null) {
      instance = new Clock();
    }
    return instance;
  }
}
