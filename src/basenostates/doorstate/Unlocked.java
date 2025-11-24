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
   *
   * @param door the Door instance to be controlled.
   */
  public Unlocked(Door door) {
    super(door);
  }

  @Override
  public String toString() {
    return "unlocked";
  }

  @Override
  public void unlock() {
    log.warn("[UNLOCKED] The door is already unlocked");
  }

  @Override
  public void unlockShortly() {
    log.warn("[UNLOCKED] The door is already unlocked");
  }

  @Override
  public void open() {
    log.debug("[UNLOCKED] Trying to open the door...");

    if (!door.isClosed()) {
      log.warn("[UNLOCKED] The door is already open");
      return;
    }

    door.setClosed(false);
  }

  @Override
  public void lock() {
    log.debug("[UNLOCKED] Trying to lock the door...");

    if (!door.isClosed()) {
      log.warn("[UNLOCKED] The door is open. Unable to lock the door");
      return;
    }

    door.setState(new Locked(door));
  }

  @Override
  public void close() {
    log.debug("[UNLOCKED] Trying to close the door...");

    if (door.isClosed()) {
      log.warn("[UNLOCKED] The door is already closed");
      return;
    }

    door.setClosed(true);
  }
}
