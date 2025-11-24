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
   *
   * @param door the Door instance to be controlled.
   */
  public Propped(Door door) {
    super(door);
  }

  @Override
  public String toString() {
    return "propped";
  }

  @Override
  public void unlock() {
    log.warn("[PROPPED] Cannot unlock a Propped door");
  }

  @Override
  public void unlockShortly() {
    log.warn("[PROPPED] Cannot unlock shortly a Propped door");
  }

  @Override
  public void open() {
    log.warn("[PROPPED] Cannot open a Propped door");
  }

  @Override
  public void lock() {
    log.warn("[PROPPED] Cannot lock a Propped door");
  }

  @Override
  public void close() {
    log.debug("[PROPPED] Trying to close the propped door...");
    door.setClosed(true);
    door.setState(new Locked(door));
  }
}
