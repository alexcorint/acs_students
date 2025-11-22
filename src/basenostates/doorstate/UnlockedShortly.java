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
   * @param o     the observable object.
   * @param arg   an argument passed to the {@code notifyObservers}
   *                 method.
   */
  @Override
  public void update(Observable o, Object arg) {
    log.debug("Trying to unlock the door");

    if (!door.isClosed()) {
      log.info("Door is still open after the unlocked period. Door is now propped.");
      door.setState(new Propped(door));
      return;
    }

    log.info("Door has been locked automatically");
    door.setState(new Locked(door));
  }

  /**
   * Converts the state into a string of characters.
   * @return the 'Unlocked shortly' string state.
   */
  @Override
  public String toString() {
    return "Unlocked shortly";
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

    log.info("Door has been unlocked permanently");
    door.setState(new Unlocked(door));
  }

  /**
   * 'Open' the Unlocked Shortly door. Checks if the door
   * is closed before.
   */
  @Override
  public void open() {
    if (!door.isClosed()) {
      log.warn("The door is already open");
      return;
    }

    door.setClosed(false);
    log.info("The door has been opened");
  }

  /**
   * 'Lock' the Unlocked Shortly door and stops the timer.
   */
  @Override
  public void lock() {
    if (!door.isClosed()) {
      log.warn("The door is open. Unable to lock the door");
      return;
    }

    Clock clock = Clock.getInstance();
    clock.stop();
    clock.deleteObserver(this);

    door.setState(new Locked(door));
    log.info("The door has been locked");
  }


  @Override
  public void close() {
    if (door.isClosed()) {
      log.warn("The door is already closed");
      return;
    }

    door.setClosed(true);
    log.info("The door has been closed and locked");
    lock();
  }
}
