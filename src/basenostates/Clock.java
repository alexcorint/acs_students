package basenostates;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

// Clock implements both the Singleton and Observable patterns.
// It runs a background daemon thread that periodically (every 1 second) ticks and
// notifies all registered Observers (doors currently in the UnlockedShortly state).
// Being a singleton avoids running multiple uncoordinated timer threads.
@SuppressWarnings("deprecation")
public class Clock extends Observable {
  private static Clock instance = null;
  private Timer timer;

  private Clock() {}

  public static Clock getInstance() {
    if (instance == null) {
      instance = new Clock();
    }
    return instance;
  }

  public void start() {
    if (timer != null) {
      return;
    }

    timer = new Timer(true);
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        setChanged();
        notifyObservers();
      }
    };

    timer.scheduleAtFixedRate(task, 1000, 1000);
  }

  public void stop() {
    if (timer != null && countObservers() == 0) {
      timer.cancel();
      timer = null;
    }
  }
}
