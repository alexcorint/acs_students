package basenostates.doorstate;

import basenostates.Door;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UNLOCKED CLASS.
 * The door state that defines an Unlocked door.
 */
public class Unlocked extends DoorState {
  private static final Logger log = LoggerFactory.getLogger(Unlocked.class);

  /**
   * Constructor for the Unlocked state.
   * @param door the Door instance to be controlled.
   */
  public Unlocked(Door door) {
    super(door);
  }

  /**
   * Unlocked toString conversion.
   * @return the 'Unlocked' string state.
   */
  @Override
  public String toString() {
    return "Unlocked";
  }

  /**
   * Warns that an Unlocked door can't be unlocked again.
   */
  @Override
  public void unlock() {
    log.warn("The door is already unlocked");
  }

  /**
   * 'Open' an Unlocked door. Checks if the door is closed
   * before.
   */
  @Override
  public void open() {
    log.debug("Trying to open the door...");

    if (!door.isClosed()) {
      log.warn("The door is already open");
      return;
    }

    door.setClosed(false);
    log.info("The door has been opened");
  }

  /**
   * 'Lock' an Unlocked door. Checks if the door is closed
   * before locking.
   */
  @Override
  public void lock() {
    log.debug("Trying to lock the door...");

    if (!door.isClosed()) {
      log.warn("The door is open. Unable to lock the door");
      return;
    }

    door.setState(new Locked(door));
    log.info("The door has been locked");
  }

  /**
   * 'Close' an Unlocked door. Checks if the door is not
   * closed before closing.
   */
  @Override
  public void close() {
    log.debug("Trying to close the door...");

    if (door.isClosed()) {
      log.warn("The door is already closed");
      return;
    }

    door.setClosed(true);
    log.info("The door has been closed");
  }
}
