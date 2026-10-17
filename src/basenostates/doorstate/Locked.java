package basenostates.doorstate;

import basenostates.Door;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Concrete state representing a locked door. In this state, physical opening and
// closing are blocked. Authorized users can transition the door to Unlocked (permanently)
// or to UnlockedShortly (temporary access with automatic relock).
public class Locked extends DoorState {
  private static final Logger log = LoggerFactory.getLogger(Locked.class);

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
    log.warn("[LOCKED] The door is locked. Open action ignored");
  }

  @Override
  public void close() {
    log.warn("[LOCKED] The door is locked and closed. Close action ignored");
  }
}
