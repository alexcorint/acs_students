package basenostates.doorstate;

import basenostates.Clock;
import basenostates.Door;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Concrete state representing a door temporarily unlocked for a user to pass through.
// It implements the Observer pattern, subscribing to the singleton Clock.
// Each door tracks its own 10-second window. When the timer expires:
// - If the door is closed, it automatically returns to Locked.
// - If the door is left open, it transitions to Propped (alarm state).
@SuppressWarnings("deprecation")
public class UnlockedShortly extends DoorState implements Observer {
  private int remainingSeconds = 10;
  private static final Logger log = LoggerFactory.getLogger(UnlockedShortly.class);

  public UnlockedShortly(Door door) {
    super(door);
    Clock clock = Clock.getInstance();
    clock.addObserver(this);
    clock.start();
  }

  // Periodic callback from Clock (every second).
  // Decrements this door's remaining seconds and handles automatic transition upon expiry.
  @Override
  public void update(Observable o, Object arg) {
    remainingSeconds--;

    if (remainingSeconds <= 0) {
      Clock c = Clock.getInstance();
      c.deleteObserver(this);
      c.stop();

      if (!door.isClosed()) {
        log.warn("[UNLOCK SHORTLY] Door '{}' still open after 10s. Transitioning to Propped alarm", door.getId());
        door.setState(new Propped(door));
        return;
      }

      log.info("[UNLOCK SHORTLY] 10s expired and door '{}' is closed. Relocking door automatically", door.getId());
      door.setState(new Locked(door));
    }
  }

  @Override
  public String toString() {
    return "unlocked_shortly";
  }

  @Override
  public void unlock() {
    Clock clock = Clock.getInstance();
    clock.deleteObserver(this);
    clock.stop();

    door.setState(new Unlocked(door));
  }

  @Override
  public void open() {
    if (!door.isClosed()) {
      log.warn("[UNLOCK SHORTLY] The door is already open");
      return;
    }
    door.setClosed(false);
  }

  @Override
  public void lock() {
    if (!door.isClosed()) {
      log.warn("[UNLOCK SHORTLY] Cannot lock while open. Door must be closed first");
      return;
    }

    Clock clock = Clock.getInstance();
    clock.deleteObserver(this);
    clock.stop();

    door.setState(new Locked(door));
  }

  @Override
  public void close() {
    if (door.isClosed()) {
      log.warn("[UNLOCK SHORTLY] The door is already closed");
      return;
    }
    // According to the state machine, closing physically does not lock immediately;
    // it waits for the 10-second timer to expire before returning to Locked.
    door.setClosed(true);
  }
}
