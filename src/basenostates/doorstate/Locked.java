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
   *
   * @param door the Door instance to be controlled.
   */
  public Locked(Door door) {
    super(door);
  }

  @Override
  public String toString() {
    return "locked";
  }

  @Override
  public void lock() {
    log.warn("[LOCKED] The door is already locked");
  }

  @Override
  public void unlock() {
    door.setState(new Unlocked(door));
  }

  @Override
  public void unlockShortly() {
    door.setState(new UnlockedShortly(door));
  }

  @Override
  public void open() {
    log.warn("[LOCKED] The door is locked");
  }

  @Override
  public void close() {
    log.warn("[LOCKED] The door is locked");
  }
}
