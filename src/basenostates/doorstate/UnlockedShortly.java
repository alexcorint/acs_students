package basenostates.doorstate;

import basenostates.Clock;
import basenostates.Door;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UNLOCKED SHORTLY CLASS.
 * The door state that defines an Unlocked shortly door.
 */
@SuppressWarnings("deprecation")
public class UnlockedShortly extends DoorState implements Observer {
  private static final Logger log = LoggerFactory.getLogger(UnlockedShortly.class);

  /**
   * Constructor for the Unlocked Shortly state.
   *
   * @param door the Door instance to be controlled.
   */
  public UnlockedShortly(Door door) {
    super(door);
    Clock clock = Clock.getInstance();
    clock.addObserver(this);
    clock.start();
  }

  /**
   * Callback from the Clock.
   * Checks if the door is closed to lock it, or open to prop it.
   *
   * @param o     the observable object.
   * @param arg   an argument passed to the {@code notifyObservers}
   *                 method.
   */
  @Override
  public void update(Observable o, Object arg) {
    log.debug("[UNLOCK SHORTLY] Trying to unlock the door");

    if (!door.isClosed()) {
      log.warn("[UNLOCK SHORTLY] The door is still open after the unlocked period. Door is now propped.");
      door.setState(new Propped(door));
      return;
    }

    door.setState(new Locked(door));
  }

  @Override
  public String toString() {
    return "unlocked_shortly";
  }

  /**
   * 'Unlock' the Unlocked Shortly door and stops the
   * timer.
   */
  @Override
  public void unlock() {
    Clock clock = Clock.getInstance();
    clock.stop();
    clock.deleteObserver(this);

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
      log.warn("[UNLOCK SHORTLY] The door is open. Unable to lock the door");
      return;
    }

    Clock clock = Clock.getInstance();
    clock.stop();
    clock.deleteObserver(this);

    door.setState(new Locked(door));
  }


  @Override
  public void close() {
    if (door.isClosed()) {
      log.warn("[UNLOCK SHORTLY] The door is already closed");
      return;
    }

    door.setClosed(true);
    lock();
  }
}
