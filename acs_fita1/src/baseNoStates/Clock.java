package baseNoStates;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

// Clock of the ACU (Observable pattern). Sends the time every second to registered doors.
// It is a thread-safe singleton that only runs while there are active observers.
@SuppressWarnings("deprecation")
public class Clock extends Observable {
  private static Clock instance = null;
  private Timer timer = null;
  private final int period; // seconds

  private Clock(int period) {
    this.period = period;
  }

  public static synchronized Clock getInstance() {
    if (instance == null) {
      instance = new Clock(1);
    }
    return instance;
  }

  public synchronized void start() {
    if (timer != null) {
      return;
    }

    timer = new Timer(true); // true = daemon (always on)
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        LocalDateTime now = LocalDateTime.now();
        setChanged();
        notifyObservers(now);
      }
    };

    timer.scheduleAtFixedRate(task, 1000L * period, 1000L * period);
  }

  public synchronized void stop() {
    if (timer != null && countObservers() == 0) {
      timer.cancel();
      timer = null;
    }
  }

  @Override
  public synchronized void addObserver(Observer o) {
    super.addObserver(o);
    start();
  }

  @Override
  public synchronized void deleteObserver(Observer o) {
    super.deleteObserver(o);
    if (countObservers() == 0) {
      stop();
    }
  }
}
