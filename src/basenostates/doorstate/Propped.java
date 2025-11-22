package basenostates.doorstate;

import basenostates.Door;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PROPPED CLASS.
 * The door state that defines a Propped door.
 */
public class Propped extends DoorState {
  private static final Logger log = LoggerFactory.getLogger(Propped.class);

  /**
   * Constructor for the Propped state.
   * @param door the Door instance to be controlled.
   */
  public Propped(Door door) {
    super(door);
  }

  /**
   * Propped toString conversion.
   * @return the 'Propped' string state.
   */
  @Override
  public String toString() {
    return "Propped";
  }

  /**
   * Warns that a Propped door can't be unlocked.
   */
  @Override
  public void unlock() {
    log.warn("Cannot unlock a Propped door");
  }

  /**
   * Warns that a Propped door can't be opened.
   */
  @Override
  public void open() {
    log.warn("Cannot open a Propped door");
  }

  /**
   * Warns that a Propped door can't be locked.
   */
  @Override
  public void lock() {
    log.warn("Cannot lock a Propped door");
  }

  /**
   * 'Close' a Propped door.
   */
  @Override
  public void close() {
    log.debug("Trying to close the propped door...");
    door.setClosed(true);
    door.setState(new Locked(door));
  }
}
