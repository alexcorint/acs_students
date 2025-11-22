package basenostates.doorstate;

import basenostates.Door;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LOCKED CLASS.
 * The door state that defines a Locked door.
 */
public class Locked extends DoorState {
  private static final Logger log = LoggerFactory.getLogger(Locked.class);

  /**
   * Constructor for the Locked state.
   * @param door the Door instance to be controlled.
   */
  public Locked(Door door) {
    super(door);
  }

  /**
   * Locked toString conversion.
   * @return the 'Locked' string state.
   */
  @Override
  public String toString() {
    return "Locked";
  }

  /**
   * Warns that a Locked door can't be locked again.
   */
  @Override
  public void lock() {
    log.warn("The door is already locked");
  }

  /**
   * 'Unlock' a Locked door.
   */
  @Override
  public void unlock() {
    log.info("The door is unlocked");
    door.setState(new Unlocked(door));
  }

  /**
   * 'Unlock shortly' a Locked door.
   */
  @Override
  public void unlockShortly() {
    log.info("The door is unlocked shortly");
    door.setState(new UnlockedShortly(door));
  }

  /**
   * Warns that a Locked door can't be opened.
   */
  @Override
  public void open() {
    log.warn("The door is locked, cannot be opened");
  }

  /**
   * Warns that a Locked door is closed.
   */
  @Override
  public void close() {
    log.warn("The door is locked, it is already closed");
  }
}
